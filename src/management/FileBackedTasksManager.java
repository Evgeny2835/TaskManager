package management;

import types.*;

import java.io.*;
import java.util.ArrayList;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    static FileBackedTasksManager loadFromFile(File file) {
        int maxIdTaskInFile = 0;
        HistoryManager historyManager = Managers.getDefaultHistory();
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(historyManager, file);
        try (FileReader reader = new FileReader(file.toString());
             BufferedReader br = new BufferedReader(reader)) {
            while (br.ready()) {
                String line = br.readLine();
                if (line.isEmpty()) {
                    line = br.readLine();
                    if (line == null) {
                        continue;
                    }
                    String[] split = line.split(",");
                    for (String str : split) {
                        Integer key = Integer.valueOf(str);
                        if (fileBackedTasksManager.tasks.containsKey(key)) {
                            fileBackedTasksManager.getTaskById(key);
                        } else if (fileBackedTasksManager.epics.containsKey(key)) {
                            fileBackedTasksManager.getEpicById(key);
                        } else if (fileBackedTasksManager.subtasks.containsKey(key)) {
                            fileBackedTasksManager.getSubtaskById(key);
                        }
                    }
                    break;
                }
                String[] split = line.split(",");
                if (TypesOfTasks.TASK.toString().equals(split[1])) {
                    Task task = new Task(split[2],
                            split[4],
                            TaskStatus.valueOf(split[3]));
                    task.setId(Integer.parseInt(split[0]));
                    fileBackedTasksManager.addTask(task);
                    if (task.getId() > maxIdTaskInFile) {
                        maxIdTaskInFile = task.getId();
                    }
                } else if ((TypesOfTasks.EPIC.toString().equals(split[1]))) {
                    Epic epic = new Epic(split[2],
                            split[4],
                            TaskStatus.valueOf(split[3]));
                    epic.setId(Integer.parseInt(split[0]));
                    fileBackedTasksManager.addEpic(epic);
                    if (epic.getId() > maxIdTaskInFile) {
                        maxIdTaskInFile = epic.getId();
                    }
                } else if ((TypesOfTasks.SUBTASK.toString().equals(split[1]))) {
                    Subtask subtask = new Subtask(split[2],
                            split[4],
                            TaskStatus.valueOf(split[3]),
                            Integer.parseInt(split[5]));
                    subtask.setId(Integer.parseInt(split[0]));
                    fileBackedTasksManager.addSubtask(subtask);
                    if (subtask.getId() > maxIdTaskInFile) {
                        maxIdTaskInFile = subtask.getId();
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        fileBackedTasksManager.idCounter.setIdCounter(maxIdTaskInFile);
        return fileBackedTasksManager;
    }

    public void save() {
        try (Writer fileWriter = new FileWriter(file.toString())) {
            fileWriter.write("id,type,name,status,description,epic");
            if (!tasks.isEmpty()) {
                for (Task task : tasks.values()) {
                    fileWriter.write("\n" + task.toString());
                }
            }
            if (!epics.isEmpty()) {
                for (Epic epic : epics.values()) {
                    fileWriter.write("\n" + epic.toString());
                }
            }
            if (!subtasks.isEmpty()) {
                for (Subtask subtask : subtasks.values()) {
                    fileWriter.write("\n" + subtask.toString());
                }
            }
            fileWriter.write("\n\n");
            if (!history().isEmpty()) {
                ArrayList<String> idList = new ArrayList();
                for (Task task : history()) {
                    idList.add(String.valueOf(task.getId()));
                }
                fileWriter.write(String.join(",", idList));
            }
        } catch (IOException ex) {
            throw new ManagerSaveException();
        }
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deletesAllSubtasks() {
        super.deletesAllSubtasks();
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    public static void main(String[] args) {
        String filePath = "/home/user/Yandex.Disk/STORAGE/LEARNING/IT/Yandex/SPRINTS/5/storageForFinalSprints/history.csv";
        TaskManager taskManager = loadFromFile(new File(filePath));

        Task task1 = new Task("Получить посылку", "Посылка от бабушки", TaskStatus.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Написать письмо", "Маме об отпуске", TaskStatus.NEW);
        taskManager.addTask(task2);
        Epic epic1 = new Epic("Выезд на рыбалку", "На озеро с Петром", TaskStatus.NEW);
        taskManager.addEpic(epic1);
        Subtask subTask1Epic1 = new Subtask("Подготовить удочки",
                "Осмотр и ремонт удочек",
                TaskStatus.NEW,
                3);
        taskManager.addSubtask(subTask1Epic1);
        Subtask subTask2Epic1 = new Subtask("Подготовить наживку",
                "Купить наживку в магазине или добыть в поле",
                TaskStatus.NEW,
                3);
        taskManager.addSubtask(subTask2Epic1);
        Subtask subTask3Epic1 = new Subtask("Подготовить одежду",
                "Проверить плащ и палатку",
                TaskStatus.NEW,
                3);
        taskManager.addSubtask(subTask3Epic1);
        Epic epic2 = new Epic("Выезд в отпуск",
                "Место назначения - Сочи",
                TaskStatus.NEW);
        taskManager.addEpic(epic2);

        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(6);

        for (Task task : taskManager.history()) {
            System.out.println(task);
        }
    }
}