package management;

import java.util.HashMap;

import id.IDCounter;
import types.Epic;
import types.Subtask;
import types.Task;

public class Manager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private IDCounter idCounter = new IDCounter();                           // генератор ID для всех типов задач

    public HashMap<Integer, Task> getTasks() {
        return new HashMap<>(tasks);
    }

    public HashMap<Integer, Epic> getEpics() {
        return new HashMap<>(epics);
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return new HashMap<>(subtasks);
    }

    public HashMap<Integer, Subtask> getSubtasksOfEpic(int id) {
        Epic epic = epics.get(id);
        return epic.getSubtasksOfEpic();
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        for (Epic tmp : epics.values()) {               // удаление подзадач у каждого эпика
            tmp.deleteAllSubtasks();
        }
        subtasks.clear();                               // очистка хеш таблицы всех подзадач
        epics.clear();                                  // очистка хеш таблицы всех эпиков
    }

    public void deletesAllSubtasks() {
        for (Epic tmp : epics.values()) {              // удаление подзадач у каждого эпика
            tmp.deleteAllSubtasks();
        }
        subtasks.clear();                              // очистка хеш таблицы всех подзадач
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public void addTask(Task task) {
        task.setId(idCounter.getIdCounter());        // присвоение ID новому объекту (аналогично у других объектов)
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        if (epic.getSubtasksOfEpic().isEmpty()) {        // метод обрабатывает только новые эпики без подзадач
            epic.setId(idCounter.getIdCounter());        // пользователь не должен предоставлять эпики с подзадачами
            epics.put(epic.getId(), epic);               // ввод эпиков и подзадач раздельный
        }
    }

    public void addSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getIdEpic())) { // если новая подзадача, эпик уже должен существовать
            subtask.setId(idCounter.getIdCounter());  // реализовано двойное хранение объектов типа Subtask
            subtasks.put(subtask.getId(), subtask);   // в общей хеш таблице и хеш таблице у каждого объекта типа Epic
            Epic epic = epics.get(subtask.getIdEpic());
            epic.addSubtasksOfEpic(subtask);
        }
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {                      // подзадачи будут обрабатываться в методах для подзадач
        if (epics.containsKey(epic.getId()) &&               // обновление эпиков только при отсутствии противоречий
                isEqualsSubtasks(epics.get(epic.getId()).getSubtasksOfEpic(),     // по состоянию хеш таблиц подзадач
                                 epic.getSubtasksOfEpic())) {                     // нового и старого эпиков
            epics.put(epic.getId(), epic);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId()) && epics.containsKey(subtask.getIdEpic())) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getIdEpic());
            epic.addSubtasksOfEpic(subtask);
        }
    }

    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Integer tmp : epic.getSubtasksOfEpic().keySet()) { //удаление всех подзадач эпика из общей хеш таблицы
                subtasks.remove(tmp);
            }
            epic.deleteAllSubtasks();                        // удаление всех подзадач у объекта эпика
            epics.remove(id);                                // удаление самого эпика
        }
    }

    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            int idEpic = subtask.getIdEpic();
            Epic epic = epics.get(idEpic);
            epic.deleteSubtask(id);                          // удаление подзадачи у конкретного объекта эпика
            subtasks.remove(id);                             // удаление подзадачи из общей хеш таблицы
        }
    }

    private boolean isEqualsSubtasks(HashMap<Integer, Subtask> map1, HashMap<Integer, Subtask> map2) {
        if (map1.isEmpty() && map2.isEmpty()) {              // метод сравнения содерэимого хеш таблиц подзадач
            return true;
        }
        if (map1.size() != map2.size()) {
            return false;
        }
        for (Integer i : map1.keySet()) {
            if (!map1.get(i).equals(map2.get(i))) {
                return false;
            }
        }
        return true;
    }
}








