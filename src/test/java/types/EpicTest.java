package types;

import management.HTTPTaskManager;
import management.InMemoryTaskManager;
import management.Managers;
import management.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servers.HttpTaskServer;
import servers.KVServer;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static types.TasksStatus.*;

class EpicTest {
    private static TaskManager taskManager;
    private static Epic epic1;

    @BeforeEach
    public void beforeEach() {
        //taskManager = Managers.getDefault(Managers.getDefaultHistory());

        taskManager = new InMemoryTaskManager(Managers.getDefaultHistory());

        epic1 = new Epic("Epic1Name", "Epic1Description", NEW);
        taskManager.addNewTask(epic1);
    }

    @Test
    public void ShouldReturnNewIfSubtasksListIsEmpty() {
        TasksStatus epicStatus = epic1.getStatus();
        assertEquals(NEW, epicStatus);
    }

    @Test
    public void ShouldReturnNewIfAllSubtasksWithTheStatusNew() {
        Subtask subTask1Epic1 = new Subtask("subTask1Epic1Name",
                "subTask1Epic1Description",
                TasksStatus.NEW,
                Optional.of(LocalDateTime.of(2022, 7, 1, 10, 0)),
                Optional.of(Duration.ofMinutes(60 * 24)),
                1);
        taskManager.addNewTask(subTask1Epic1);
        Subtask subTask2Epic1 = new Subtask("subTask2Epic1Name",
                "subTask2Epic1Description",
                TasksStatus.NEW,
                Optional.of(LocalDateTime.of(2022, 7, 3, 10, 0)),
                Optional.of(Duration.ofMinutes(60)),
                1);
        taskManager.addNewTask(subTask2Epic1);
        Subtask subTask3Epic1 = new Subtask("subTask3Epic1Name",
                "subTask3Epic1Description",
                TasksStatus.NEW,
                Optional.of(LocalDateTime.of(2022, 7, 5, 10, 0)),
                Optional.of(Duration.ofMinutes(10)),
                1);
        taskManager.addNewTask(subTask3Epic1);
        TasksStatus epicStatus = epic1.getStatus();
        assertEquals(NEW, epicStatus);
    }

    @Test
    public void ShouldReturnDoneIfAllSubtasksWithTheStatusDone() {
        Subtask subTask1Epic1 = new Subtask("subTask1Epic1Name",
                "subTask1Epic1Description",
                TasksStatus.DONE,
                Optional.of(LocalDateTime.of(2022, 7, 1, 10, 0)),
                Optional.of(Duration.ofMinutes(60 * 24)),
                1);
        taskManager.addNewTask(subTask1Epic1);
        Subtask subTask2Epic1 = new Subtask("subTask2Epic1Name",
                "subTask2Epic1Description",
                TasksStatus.DONE,
                Optional.of(LocalDateTime.of(2022, 7, 3, 10, 0)),
                Optional.of(Duration.ofMinutes(60)),
                1);
        taskManager.addNewTask(subTask2Epic1);
        Subtask subTask3Epic1 = new Subtask("subTask3Epic1Name",
                "subTask3Epic1Description",
                TasksStatus.DONE,
                Optional.of(LocalDateTime.of(2022, 7, 5, 10, 0)),
                Optional.of(Duration.ofMinutes(10)),
                1);
        taskManager.addNewTask(subTask3Epic1);
        TasksStatus epicStatus = epic1.getStatus();
        assertEquals(DONE, epicStatus);
    }

    @Test
    public void ShouldReturnInProgressIfSubtasksWithTheStatusNewAndDone() {
        Subtask subTask1Epic1 = new Subtask("subTask1Epic1Name",
                "subTask1Epic1Description",
                TasksStatus.DONE,
                Optional.of(LocalDateTime.of(2022, 7, 1, 10, 0)),
                Optional.of(Duration.ofMinutes(60 * 24)),
                1);
        taskManager.addNewTask(subTask1Epic1);
        Subtask subTask2Epic1 = new Subtask("subTask2Epic1Name",
                "subTask2Epic1Description",
                TasksStatus.NEW,
                Optional.of(LocalDateTime.of(2022, 7, 3, 10, 0)),
                Optional.of(Duration.ofMinutes(60)),
                1);
        taskManager.addNewTask(subTask2Epic1);
        Subtask subTask3Epic1 = new Subtask("subTask3Epic1Name",
                "subTask3Epic1Description",
                TasksStatus.NEW,
                Optional.of(LocalDateTime.of(2022, 7, 5, 10, 0)),
                Optional.of(Duration.ofMinutes(10)),
                1);
        taskManager.addNewTask(subTask3Epic1);
        TasksStatus epicStatus = epic1.getStatus();
        assertEquals(IN_PROGRESS, epicStatus);
    }

    @Test
    public void ShouldReturnInProgressIfAllSubtasksWithTheStatusInProgress() {
        Subtask subTask1Epic1 = new Subtask("subTask1Epic1Name",
                "subTask1Epic1Description",
                TasksStatus.IN_PROGRESS,
                Optional.of(LocalDateTime.of(2022, 7, 1, 10, 0)),
                Optional.of(Duration.ofMinutes(60 * 24)),
                1);
        taskManager.addNewTask(subTask1Epic1);
        Subtask subTask2Epic1 = new Subtask("subTask2Epic1Name",
                "subTask2Epic1Description",
                TasksStatus.IN_PROGRESS,
                Optional.of(LocalDateTime.of(2022, 7, 3, 10, 0)),
                Optional.of(Duration.ofMinutes(60)),
                1);
        taskManager.addNewTask(subTask2Epic1);
        Subtask subTask3Epic1 = new Subtask("subTask3Epic1Name",
                "subTask3Epic1Description",
                TasksStatus.IN_PROGRESS,
                Optional.of(LocalDateTime.of(2022, 7, 5, 10, 0)),
                Optional.of(Duration.ofMinutes(10)),
                1);
        taskManager.addNewTask(subTask3Epic1);
        TasksStatus epicStatus = epic1.getStatus();
        assertEquals(IN_PROGRESS, epicStatus);
    }
}