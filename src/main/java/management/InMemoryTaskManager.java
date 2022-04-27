package management;

import java.util.*;
import counters.tasksID;
import types.Epic;
import types.Subtask;
import types.Task;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, Epic> epics;
    protected final Map<Integer, Subtask> subtasks;
    protected final HistoryManager historyManager;
    protected final tasksID idCounter;
    protected final TreeSet<Task> tasksSortedByStartTime;

    public InMemoryTaskManager(HistoryManager historyManager) {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        this.historyManager = historyManager;
        idCounter = new tasksID();
        tasksSortedByStartTime = new TreeSet<>(Task.comparator);
    }

    @Override
    public void addNewTask(Task task) {
        switch (task.getType()) {
            case TASK:
                addTask(task);
                break;
            case EPIC:
                addEpic((Epic) task);
                break;
            case SUBTASK:
                addSubtask((Subtask) task);
                break;
        }
   }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            if (checkUniqueTimeOfTask(task)) {
                tasksSortedByStartTime.remove(tasks.get(task.getId()));
                tasks.put(task.getId(), task);
                tasksSortedByStartTime.add(task);
            }
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
            if (checkUniqueTimeOfTask(subtask)) {
                tasksSortedByStartTime.remove(subtasks.get(subtask.getId()));
                subtasks.put(subtask.getId(), subtask);
                tasksSortedByStartTime.add(subtask);
                Epic epic = epics.get(subtask.getIdEpic());
                epic.addSubtasksOfEpic(subtask);
            }
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasksSortedByStartTime.remove(tasks.get(id));
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
            tasksSortedByStartTime.remove(subtasks.get(id));
            Subtask subtask = subtasks.get(id);
            int idEpic = subtask.getIdEpic();
            Epic epic = epics.get(idEpic);
            epic.deleteSubtask(subtask);
            historyManager.remove(id);
            subtasks.remove(id);

        }
    }

    @Override
    public void deleteAllTasks() {
        for (Integer key : tasks.keySet()) {
            tasksSortedByStartTime.remove(tasks.get(key));
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
            tasksSortedByStartTime.remove(subtasks.get(key));
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
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(tasksSortedByStartTime);
    }

    @Override
    public List<Task> history() {
        return historyManager.getHistory();
    }

    private void addTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            return;
        }
        if (task.getId() == 0) {
            task.setId(idCounter.getIdCounter());
        }
        if (checkUniqueTimeOfTask(task)) {
            tasks.put(task.getId(), task);
            tasksSortedByStartTime.add(task);
        }
    }

    private void addEpic(Epic epic) {
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

    private void addSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            return;
        }
        if (epics.containsKey(subtask.getIdEpic())) {
            if (subtask.getId() == 0) {
                subtask.setId(idCounter.getIdCounter());
            }
            if (checkUniqueTimeOfTask(subtask)) {
                subtasks.put(subtask.getId(), subtask);
                tasksSortedByStartTime.add(subtask);
                Epic epic = epics.get(subtask.getIdEpic());
                epic.addSubtasksOfEpic(subtask);
            }
        }
    }

    private boolean isEqualsSubtasks(List<Subtask> list1, List<Subtask> list2) {
        // метод сравнения содержимого списков подзадач
        return (list1.isEmpty() && list2.isEmpty()) ||
                (list1.size() == list2.size());
    }

    private boolean checkUniqueTimeOfTask(Task task) {
        if (tasksSortedByStartTime.isEmpty()) {
            return true;
        }
        Task taskBefore = tasksSortedByStartTime.floor(task);
        Task taskAfter = tasksSortedByStartTime.ceiling(task);
        if (taskBefore == null) {
            return task.getEndTime().get().isBefore(tasksSortedByStartTime.first().getStartTime().get());
        }
        if (taskAfter == null) {
            return task.getStartTime().get().isAfter(tasksSortedByStartTime.last().getEndTime().get());
        }
        if (task.getStartTime().get().isAfter(taskBefore.getEndTime().get()) &&
                task.getEndTime().get().isBefore(taskAfter.getStartTime().get())) {
            return true;
        }
        return false;
    }
}