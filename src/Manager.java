import java.util.HashMap;

public class Manager {
    private HashMap<Integer, Task> tasksHashMap = new HashMap<>();
    private HashMap<Integer, Epic> epicsHashMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtasksHashMap = new HashMap<>();

    IDCounter idCounter = new IDCounter();                    // генератор ID для всех типов задач

    public void printTasks() {
        for (Task tmp : tasksHashMap.values()) {
            System.out.println(tmp);
        }
    }

    public void printEpics() {
        for (Epic tmp : epicsHashMap.values()) {
            System.out.println(tmp);
        }
    }

    public void printSubTasks() {
        for (Subtask tmp : subtasksHashMap.values()) {
            System.out.println(tmp);
        }
    }

    public void printSubTasksOfEpic(int id) {
        System.out.println("Список SubTasks у Epic с id=" + id);
        Epic epic = epicsHashMap.get(id);
        epic.printSubtasksInEpic();
    }

    public void deleteAllTasks() {
        tasksHashMap.clear();
    }

    public void deleteAllEpics() {
        for (Epic tmp : epicsHashMap.values()) {               // удаление подзадач у каждого эпика
            tmp.deleteAllSubtasks();
        }
        subtasksHashMap.clear();                               // очистка хеш таблицы всех подзадач
        epicsHashMap.clear();                                  // очистка хеш таблицы всех эпиков
    }

    public void deletesAllSubtasks() {
        for (Epic tmp : epicsHashMap.values()) {              // удаление подзадач у каждого эпика
            tmp.deleteAllSubtasks();
        }
        subtasksHashMap.clear();                              // очистка хеш таблицы всех подзадач
    }

    public void printTaskById(int id) {
        System.out.println(tasksHashMap.get(id));
    }

    public void printEpicById(int id) {
        System.out.println(epicsHashMap.get(id));
    }

    public void printSubtasksById(int id) {
        System.out.println(subtasksHashMap.get(id));
    }

    public void setTasksHashMap(Task task) {
        task.setId(idCounter.getIdCounter());        // присвоение ID новому объекту (аналогично у других объектов)
        tasksHashMap.put(task.getId(), task);
    }

    public void setEpicHashMap(Epic epic) {
        epic.setId(idCounter.getIdCounter());
        epicsHashMap.put(epic.getId(), epic);
    }

    public void setSubtasksHashMap(Subtask subtask) { // реализовано двойное хранение объектов типа Subtask
        subtask.setId(idCounter.getIdCounter());      // в общей хеш таблице и хеш таблице у каждого объекта типа Epic
        subtasksHashMap.put(subtask.getId(), subtask);
        Epic epic = epicsHashMap.get(subtask.getIdEpic());
        epic.setSubtasksList(subtask);
    }

    public void updateTask(Task task) {
        tasksHashMap.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epicsHashMap.put(epic.getId(), epic);
    }

    public void updateSubtask(Subtask subtask) {
        subtasksHashMap.put(subtask.getId(), subtask);
        Epic epic = epicsHashMap.get(subtask.getIdEpic());
        epic.setSubtasksList(subtask);
        // после обновления объекта типа SubTask производится расчет статуса объекта типа Epic
        int statusNewCounter = 0;
        int statusDoneCounter = 0;
        if (epic.getSubtasksList().isEmpty()) {
            epic.setStatus("NEW");
        } else {
            for (Subtask tmp : epic.getSubtasksList().values()) {
                if (tmp.getStatus().equals("NEW")) {
                    statusNewCounter++;
                } else if (tmp.getStatus().equals("DONE")) {
                    statusDoneCounter++;
                }
            }
            if (statusNewCounter == epic.getSubtasksList().size()) {
                epic.setStatus("NEW");
            } else if (statusDoneCounter == epic.getSubtasksList().size()) {
                epic.setStatus("DONE");
            } else {
                epic.setStatus("IN_PROGRESS");
            }
        }
    }

    public void deleteTaskById(int id) {
        tasksHashMap.remove(id);
    }

    public void deleteEpicById(int id) {
        Epic epic = epicsHashMap.get(id);
        for (Integer tmp : epic.getSubtasksList().keySet()) {  // удаление всех подзадач эпика из общей хеш таблицы
            subtasksHashMap.remove(tmp);
        }
        epic.deleteAllSubtasks();                               // удаление всех подзадач у объекта эпика
        epicsHashMap.remove(id);                                // удаление самого эпика
    }

    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasksHashMap.get(id);
        int idEpic = subtask.getIdEpic();
        Epic epic = epicsHashMap.get(idEpic);
        epic.deleteSubtask(id);                                 // удаление подзадачи у конкретного объекта эпика
        subtasksHashMap.remove(id);                             // удаление подзадачи из общей хеш таблицы
    }
}








