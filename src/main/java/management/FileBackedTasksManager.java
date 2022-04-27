package management;

import types.*;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTasksManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    public FileBackedTasksManager(HistoryManager historyManager) {
        super(historyManager);
    }

    public static FileBackedTasksManager loadFromFile(File file) {
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
                if (TasksList.TASK.toString().equals(split[1])) {
                    Task task = new Task(split[2],
                            split[4],
                            TasksStatus.valueOf(split[3]),
                            Optional.of(LocalDateTime.parse(split[5], Task.formatter)),
                            Optional.of(Duration.parse(split[6])));
                    task.setId(Integer.parseInt(split[0]));
                    fileBackedTasksManager.addNewTask(task);
                    if (task.getId() > maxIdTaskInFile) {
                        maxIdTaskInFile = task.getId();
                    }
                } else if ((TasksList.EPIC.toString().equals(split[1]))) {
                    Epic epic = new Epic(split[2],
                            split[4],
                            TasksStatus.valueOf(split[3]));
                    epic.setId(Integer.parseInt(split[0]));
                    fileBackedTasksManager.addNewTask(epic);
                    if (epic.getId() > maxIdTaskInFile) {
                        maxIdTaskInFile = epic.getId();
                    }
                } else if ((TasksList.SUBTASK.toString().equals(split[1]))) {
                    Subtask subtask = new Subtask(split[2],
                            split[4],
                            TasksStatus.valueOf(split[3]),
                            Optional.of(LocalDateTime.parse(split[5], Task.formatter)),
                            Optional.of(Duration.parse(split[6])),
                            Integer.parseInt(split[7]));
                    subtask.setId(Integer.parseInt(split[0]));
                    fileBackedTasksManager.addNewTask(subtask);
                    if (subtask.getId() > maxIdTaskInFile) {
                        maxIdTaskInFile = subtask.getId();
                    }
                }
            }
        } catch (IOException ex) {
            throw new ManagerSaveException(ex.getMessage());
        }
        fileBackedTasksManager.idCounter.setIdCounter(maxIdTaskInFile);
        return fileBackedTasksManager;
    }

    protected void save() {
        try (Writer fileWriter = new FileWriter(file.toString())) {
            fileWriter.write("id,type,name,status,description,startTime,duration,epic");
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
            throw new ManagerSaveException(ex.getMessage());
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
    public void addNewTask(Task task) {
        super.addNewTask(task);
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
}