package types;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Subtask> subtasksOfEpic = new ArrayList<>();

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
    }

    public List<Subtask> getSubtasksOfEpic() {
        return new ArrayList<>(subtasksOfEpic);
    }

    public void addSubtasksOfEpic(Subtask subtask) {
        subtasksOfEpic.add(subtask);
    }

    @Override
    public TaskStatus getStatus() {                            // расчет статусов эпиков при обращении к его полю статус
        int statusNewCounter = 0;
        int statusDoneCounter = 0;
        if (getSubtasksOfEpic().isEmpty()) {
            return TaskStatus.NEW;
        } else {
            for (Subtask tmp : getSubtasksOfEpic()) {
                if (tmp.getStatus() == TaskStatus.NEW) {
                    statusNewCounter++;
                } else if (tmp.getStatus() == TaskStatus.DONE) {
                    statusDoneCounter++;
                }
            }
            if (statusNewCounter == getSubtasksOfEpic().size()) {
                return TaskStatus.NEW;
            } else if (statusDoneCounter == getSubtasksOfEpic().size()) {
                return TaskStatus.DONE;
            } else {
                return TaskStatus.IN_PROGRESS;
            }
        }
    }

    public void deleteSubtask(Subtask subtask) {
        subtasksOfEpic.remove(subtask);
    }

    public void deleteAllSubtasks() {
        subtasksOfEpic.clear();
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s",
                getId(),
                TypesOfTasks.EPIC,
                getName(),
                getStatus(),
                getDescription());
    }

    @Override
    public void setStatus(TaskStatus status) {
    }
}