package management;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import types.TasksStatus;
import types.Epic;
import types.Subtask;
import types.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static types.TasksStatus.NEW;

abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;
    Task task1 = new Task("task1Name",
            "task1Description",
            TasksStatus.NEW,
            Optional.of(LocalDateTime.of(2022, 5, 1, 10, 0)),
            Optional.of(Duration.ofMinutes(60 * 24)));
    Task task2 = new Task("task2Name",
            "task2Description",
            TasksStatus.NEW,
            Optional.of(LocalDateTime.of(2022, 5, 3, 10, 0)),
            Optional.of(Duration.ofMinutes(60)));
    Epic epic1 = new Epic("Epic1Name", "Epic1Description", NEW);
    Epic epic2 = new Epic("Epic2Name", "Epic2Description", NEW);

    abstract void setManager();

    @BeforeEach
    public void beforeEach() {
        setManager();
    }

    @Test
    public void addNewTask() {
        taskManager.addNewTask(task1);
        final int task1Id = task1.getId();
        final Task savedTask = taskManager.getTaskById(task1Id);
        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task1, savedTask, "Задачи не совпадают");
        final List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают");
        assertEquals(1, savedTask.getId(), "Неверный идентификатор задачи");
    }

    @Test
    public void addNewEpic() {
        taskManager.addNewTask(epic1);
        final int epic1Id = epic1.getId();
        final Epic savedEpic1 = taskManager.getEpicById(epic1Id);
        assertNotNull(savedEpic1, "Задача не найдена");
        assertEquals(epic1, savedEpic1, "Задачи не совпадают");
        final List<Epic> epics = taskManager.getEpics();
        assertNotNull(epics, "Задачи не возвращаются");
        assertEquals(1, epics.size(), "Неверное количество задач");
        assertEquals(epic1, epics.get(0), "Задачи не совпадают");
        assertEquals(1, savedEpic1.getId(), "Неверный идентификатор задачи");
    }

    @Test
    public void addNewSubTask() {
        taskManager.addNewTask(epic1);
        Subtask subTask1Epic1 = new Subtask("subTask1Epic1Name",
                "subTask1Epic1Description",
                TasksStatus.NEW,
                Optional.of(LocalDateTime.of(2022, 7, 1, 10, 0)),
                Optional.of(Duration.ofMinutes(60 * 24)),
                epic1.getId());
        taskManager.addNewTask(subTask1Epic1);
        final int subTask1Epic1Id = subTask1Epic1.getId();
        final Subtask savedSubTask1Epic1 = taskManager.getSubtaskById(subTask1Epic1Id);
        assertNotNull(savedSubTask1Epic1, "Задача не найдена");
        assertEquals(subTask1Epic1, savedSubTask1Epic1, "Задачи не совпадают");
        final List<Subtask> subTasks = taskManager.getSubtasks();
        assertNotNull(subTasks, "Задачи не возвращаются");
        assertEquals(1, subTasks.size(), "Неверное количество задач");
        assertEquals(subTask1Epic1, subTasks.get(0), "Задачи не совпадают");
        assertEquals(2, savedSubTask1Epic1.getId(), "Неверный идентификатор задачи");
    }

    @Test
    public void updateTask() {
        taskManager.addNewTask(task1);
        final int task1Id = task1.getId();
        task1 = new Task("task1Name",
                "task1Description",
                TasksStatus.DONE,
                Optional.of(LocalDateTime.of(2022, 5, 1, 10, 0)),
                Optional.of(Duration.ofMinutes(60 * 24)));
        task1.setId(task1Id);
        taskManager.updateTask(task1);
        assertEquals(TasksStatus.DONE, task1.getStatus(), "Неверный статус задачи");
    }

    @Test
    public void updateEpic() {
        taskManager.addNewTask(epic1);
        Subtask subTask1Epic1 = new Subtask("subTask1Epic1Name",
                "subTask1Epic1Description",
                TasksStatus.DONE,
                Optional.of(LocalDateTime.of(2022, 7, 1, 10, 0)),
                Optional.of(Duration.ofMinutes(60 * 24)),
                epic1.getId());
        taskManager.addNewTask(subTask1Epic1);
        taskManager.updateEpic(epic1);
        assertEquals(TasksStatus.DONE, epic1.getStatus(), "Неверный статус задачи");
    }

    @Test
    public void updateSubTask() {
        taskManager.addNewTask(epic1);
        Subtask subTask1Epic1 = new Subtask("subTask1Epic1Name",
                "subTask1Epic1Description",
                TasksStatus.NEW,
                Optional.of(LocalDateTime.of(2022, 7, 1, 10, 0)),
                Optional.of(Duration.ofMinutes(60 * 24)),
                epic1.getId());
        taskManager.addNewTask(subTask1Epic1);
        final int subTask1Epic1Id = subTask1Epic1.getId();
        subTask1Epic1 = new Subtask("subTask1Epic1Name",
                "subTask1Epic1Description",
                TasksStatus.DONE,
                Optional.of(LocalDateTime.of(2022, 7, 1, 10, 0)),
                Optional.of(Duration.ofMinutes(60 * 24)),
                epic1.getId());
        subTask1Epic1.setId(subTask1Epic1Id);
        taskManager.updateSubtask(subTask1Epic1);
        assertEquals(TasksStatus.DONE, subTask1Epic1.getStatus(), "Неверный статус задачи");
    }

    @Test
    public void deleteTaskById() {
        taskManager.addNewTask(task1);
        final int task1Id = task1.getId();
        taskManager.deleteTaskById(task1Id);
        taskManager.getTasks();
        assertEquals(0, taskManager.getTasks().size(), "Задача не удалена");
    }

    @Test
    public void deleteEpicById() {
        taskManager.addNewTask(epic1);
        final int epic1Id = epic1.getId();
        taskManager.deleteEpicById(epic1Id);
        taskManager.getEpics();
        assertEquals(0, taskManager.getEpics().size(), "Задача не удалена");
    }

    @Test
    public void deleteSubTaskById() {
        taskManager.addNewTask(epic1);
        Subtask subTask1Epic1 = new Subtask("subTask1Epic1Name",
                "subTask1Epic1Description",
                TasksStatus.NEW,
                Optional.of(LocalDateTime.of(2022, 7, 1, 10, 0)),
                Optional.of(Duration.ofMinutes(60 * 24)),
                epic1.getId());
        taskManager.addNewTask(subTask1Epic1);
        final int subTask1Epic1Id = subTask1Epic1.getId();
        taskManager.deleteSubtaskById(subTask1Epic1Id);
        taskManager.getSubtasks();
        assertEquals(0, taskManager.getSubtasks().size(), "Задача не удалена");
    }

    @Test
    public void deleteAllTasks() {
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getTasks().size(), "Задачи не удалены");
    }

    @Test
    public void deleteAllEpic() {
        taskManager.addNewTask(epic1);
        taskManager.deleteAllEpics();
        assertEquals(0, taskManager.getEpics().size(), "Задачи не удалены");
    }

    @Test
    public void deleteAllSubTasks() {
        taskManager.addNewTask(epic1);
        Subtask subTask1Epic1 = new Subtask("subTask1Epic1Name",
                "subTask1Epic1Description",
                TasksStatus.NEW,
                Optional.of(LocalDateTime.of(2022, 7, 1, 10, 0)),
                Optional.of(Duration.ofMinutes(60 * 24)),
                epic1.getId());
        taskManager.addNewTask(subTask1Epic1);
        taskManager.deletesAllSubtasks();
        assertEquals(0, taskManager.getSubtasks().size(), "Задачи не удалены");
    }

    @Test
    public void getTaskById() {
        taskManager.addNewTask(task1);
        final int task1Id = task1.getId();
        final Task savedTask = taskManager.getTaskById(task1Id);
        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task1, savedTask, "Задачи не совпадают");
    }

    @Test
    public void getEpicById() {
        taskManager.addNewTask(epic1);
        final int epic1Id = epic1.getId();
        final Epic savedEpic = taskManager.getEpicById(epic1Id);
        assertNotNull(savedEpic, "Задача не найдена");
        assertEquals(epic1, savedEpic, "Задачи не совпадают");
    }

    @Test
    public void getSubTaskById() {
        taskManager.addNewTask(epic1);
        Subtask subTask1Epic1 = new Subtask("subTask1Epic1Name",
                "subTask1Epic1Description",
                TasksStatus.NEW,
                Optional.of(LocalDateTime.of(2022, 7, 1, 10, 0)),
                Optional.of(Duration.ofMinutes(60 * 24)),
                epic1.getId());
        taskManager.addNewTask(subTask1Epic1);
        final int subTask1Epic1Id = subTask1Epic1.getId();
        final Subtask savedSubtask = taskManager.getSubtaskById(subTask1Epic1Id);
        assertNotNull(savedSubtask, "Задача не найдена");
        assertEquals(subTask1Epic1, savedSubtask, "Задачи не совпадают");
    }

    @Test
    public void ShouldReturn1IfTwoTasksInListOfTasks() {
        taskManager.addNewTask(task1);
        assertEquals(1, taskManager.getTasks().size());
    }

    @Test
    public void ShouldReturn0IfListOfTasksIsEmpty() {
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    public void getPrioritizedTasks() {
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.addNewTask(epic1);
        Subtask subTask1Epic1 = new Subtask("subTask1Epic1Name",
                "subTask1Epic1Description",
                TasksStatus.NEW,
                Optional.of(LocalDateTime.of(2022, 7, 1, 10, 0)),
                Optional.of(Duration.ofMinutes(60 * 24)),
                epic1.getId());
        taskManager.addNewTask(subTask1Epic1);
        Subtask subTask2Epic1 = new Subtask("subTask2Epic1Name",
                "subTask2Epic1Description",
                TasksStatus.NEW,
                Optional.of(LocalDateTime.of(2022, 7, 3, 10, 0)),
                Optional.of(Duration.ofMinutes(60)),
                epic1.getId());
        taskManager.addNewTask(subTask2Epic1);
        Subtask subTask3Epic1 = new Subtask("subTask3Epic1Name",
                "subTask3Epic1Description",
                TasksStatus.NEW,
                Optional.of(LocalDateTime.of(2022, 7, 5, 10, 0)),
                Optional.of(Duration.ofMinutes(10)),
                epic1.getId());
        taskManager.addNewTask(subTask3Epic1);
        taskManager.addNewTask(epic2);
        assertEquals(5, taskManager.getPrioritizedTasks().size());
    }

    @Test
    public void getSubtasksOfEpic() {
        taskManager.addNewTask(epic1);
        final int epic1Id = epic1.getId();
        Subtask subTask1Epic1 = new Subtask("subTask1Epic1Name",
                "subTask1Epic1Description",
                TasksStatus.NEW,
                Optional.of(LocalDateTime.of(2022, 7, 1, 10, 0)),
                Optional.of(Duration.ofMinutes(60 * 24)),
                epic1.getId());
        taskManager.addNewTask(subTask1Epic1);
        final List<Subtask> subtasks = taskManager.getSubtasksOfEpic(epic1Id);
        assertEquals(1, subtasks.size(), "Неверное количество задач");
        assertEquals(subTask1Epic1, subtasks.get(0), "Задачи не совпадают");
    }

    @Test
    public void getHistory() {
        taskManager.addNewTask(task1);
        taskManager.getTaskById(task1.getId());
        assertEquals(1, taskManager.history().size());
    }
}