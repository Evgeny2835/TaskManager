package management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.IDCounter;
import types.Epic;
import types.Subtask;
import types.Task;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final IDCounter idCounter = new IDCounter();                      // генератор ID для всех типов задач
    private final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Subtask> getSubtasksOfEpic(int id) {
        Epic epic = epics.get(id);
        return epic.getSubtasksOfEpic();
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Epic epic : epics.values()) {               // удаление подзадач у каждого эпика
            epic.deleteAllSubtasks();
        }
        subtasks.clear();                               // очистка хеш таблицы всех подзадач
        epics.clear();                                  // очистка хеш таблицы всех эпиков
    }

    @Override
    public void deletesAllSubtasks() {
        for (Epic epic : epics.values()) {              // удаление подзадач у каждого эпика
            epic.deleteAllSubtasks();
        }
        subtasks.clear();                              // очистка хеш таблицы всех подзадач
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void addTask(Task task) {
        task.setId(idCounter.getIdCounter());        // присвоение ID новому объекту (аналогично у других объектов)
        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic.getSubtasksOfEpic().isEmpty()) {        // метод обрабатывает только новые эпики без подзадач
            epic.setId(idCounter.getIdCounter());        // пользователь не должен предоставлять эпики с подзадачами
            epics.put(epic.getId(), epic);               // ввод эпиков и подзадач раздельный
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getIdEpic())) { // если новая подзадача, эпик уже должен существовать
            subtask.setId(idCounter.getIdCounter());  // реализовано двойное хранение объектов типа Subtask
            subtasks.put(subtask.getId(), subtask);   // в общей хеш таблице и хеш таблице у каждого объекта типа Epic
            Epic epic = epics.get(subtask.getIdEpic());
            epic.addSubtasksOfEpic(subtask);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {                      // подзадачи будут обрабатываться в методах для подзадач
        if (epics.containsKey(epic.getId()) &&               // обновление эпиков только при отсутствии противоречий
                isEqualsSubtasks(epics.get(epic.getId()).getSubtasksOfEpic(),     // по состоянию списков подзадач
                        epic.getSubtasksOfEpic())) {                     // нового и старого эпиков
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId()) && epics.containsKey(subtask.getIdEpic())) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getIdEpic());
            epic.addSubtasksOfEpic(subtask);
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Subtask subtask : epic.getSubtasksOfEpic()) { //удаление всех подзадач эпика из общего списка
                subtasks.remove(subtask.getId());
            }
            epic.deleteAllSubtasks();                        // удаление всех подзадач у объекта эпика
            epics.remove(id);                                // удаление самого эпика
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            int idEpic = subtask.getIdEpic();
            Epic epic = epics.get(idEpic);
            epic.deleteSubtask(id);                          // удаление подзадачи у конкретного объекта эпика
            subtasks.remove(id);                             // удаление подзадачи из общей хеш таблицы
        }
    }

    @Override
    public List<Task> history() {
        return historyManager.getHistory();
    }

    private boolean isEqualsSubtasks(List<Subtask> list1, List<Subtask> list2) {
        // метод сравнения содержимого списков подзадач
        return (list1.isEmpty() && list2.isEmpty()) ||
                (list1.size() == list2.size());
    }
}