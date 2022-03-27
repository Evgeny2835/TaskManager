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
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager;
    protected final IDCounter idCounter = new IDCounter();

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
        for (Integer key : tasks.keySet()) {
            historyManager.remove(key);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        deletesAllSubtasks();
        for (Integer key : epics.keySet()) {
            historyManager.remove(key);
        }
        epics.clear();
    }

    @Override
    public void deletesAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.deleteAllSubtasks();
        }
        for (Integer key : subtasks.keySet()) {
            historyManager.remove(key);
        }
        subtasks.clear();
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
        if (tasks.containsKey(task.getId())) {
            return;
        }
        if (task.getId() == 0) {
            task.setId(idCounter.getIdCounter());
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            return;
        }
        if (epic.getSubtasksOfEpic().isEmpty()) {
            if (epic.getId() == 0) {
                epic.setId(idCounter.getIdCounter());
            }
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            return;
        }
        if (epics.containsKey(subtask.getIdEpic())) {
            if (subtask.getId() == 0) {
                subtask.setId(idCounter.getIdCounter());
            }
            subtasks.put(subtask.getId(), subtask);
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
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId()) &&
                isEqualsSubtasks(epics.get(epic.getId()).getSubtasksOfEpic(),
                        epic.getSubtasksOfEpic())) {
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
            historyManager.remove(id);
            tasks.remove(id);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Subtask subtask : epic.getSubtasksOfEpic()) {
                historyManager.remove(subtask.getId());
                subtasks.remove(subtask.getId());
            }
            historyManager.remove(id);
            epics.remove(id);
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            int idEpic = subtask.getIdEpic();
            Epic epic = epics.get(idEpic);
            epic.deleteSubtask(subtask);
            historyManager.remove(id);
            subtasks.remove(id);

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