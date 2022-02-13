package management;

import java.util.ArrayList;
import java.util.HashMap;

import types.Epic;
import types.Subtask;
import types.Task;

public interface TaskManager {
    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtasks();

    HashMap<Integer, Subtask> getSubtasksOfEpic(int id);

    void deleteAllTasks();

    void deleteAllEpics();

    void deletesAllSubtasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);
}








