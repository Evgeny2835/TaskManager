package types;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Subtask> subtasksOfEpic = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public List<Subtask> getSubtasksOfEpic() {
        return new ArrayList<>(subtasksOfEpic);
    }

    public void addSubtasksOfEpic(Subtask subtask) {
        subtasksOfEpic.add(subtask);
    }

    @Override
    public Status getStatus() {                            // расчет статусов эпиков при обращении к его полю статус
        int statusNewCounter = 0;
        int statusDoneCounter = 0;
        if (getSubtasksOfEpic().isEmpty()) {
            return Status.NEW;
        } else {
            for (Subtask tmp : getSubtasksOfEpic()) {
                if (tmp.getStatus() == Status.NEW) {
                    statusNewCounter++;
                } else if (tmp.getStatus() == Status.DONE) {
                    statusDoneCounter++;
                }
            }
            if (statusNewCounter == getSubtasksOfEpic().size()) {
                return Status.NEW;
            } else if (statusDoneCounter == getSubtasksOfEpic().size()) {
                return Status.DONE;
            } else {
                return Status.IN_PROGRESS;
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
    public void setStatus(Status status) {
    }
}