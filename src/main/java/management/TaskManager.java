package management;

import java.util.List;
import java.util.TreeSet;

import types.Epic;
import types.Subtask;
import types.Task;

public interface TaskManager {

    void addNewTask(Task task);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    void deleteAllTasks();

    void deleteAllEpics();

    void deletesAllSubtasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    List<Task> getPrioritizedTasks();

    List<Subtask> getSubtasksOfEpic(int id);

    List<Task> history();
}