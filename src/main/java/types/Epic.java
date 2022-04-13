package types;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Epic extends Task {
    private final List<Subtask> subtasksOfEpic = new ArrayList<>();

    public Epic(String name,
                String description,
                TasksStatus status) {
        super(name, description, status, Optional.empty(), Optional.empty());
    }

    public List<Subtask> getSubtasksOfEpic() {
        return new ArrayList<>(subtasksOfEpic);
    }

    public void addSubtasksOfEpic(Subtask subtask) {
        subtasksOfEpic.add(subtask);
    }

    @Override
    public TasksList getType() {
        return TasksList.EPIC;
    }

    @Override
    public TasksStatus getStatus() {
        int statusNewCounter = 0;
        int statusDoneCounter = 0;
        if (getSubtasksOfEpic().isEmpty()) {
            return TasksStatus.NEW;
        } else {
            for (Subtask tmp : getSubtasksOfEpic()) {
                if (tmp.getStatus() == TasksStatus.NEW) {
                    statusNewCounter++;
                } else if (tmp.getStatus() == TasksStatus.DONE) {
                    statusDoneCounter++;
                }
            }
            if (statusNewCounter == getSubtasksOfEpic().size()) {
                return TasksStatus.NEW;
            } else if (statusDoneCounter == getSubtasksOfEpic().size()) {
                return TasksStatus.DONE;
            } else {
                return TasksStatus.IN_PROGRESS;
            }
        }
    }

    @Override
    public Optional<LocalDateTime> getStartTime() {
        if (subtasksOfEpic.isEmpty()) {
            return Optional.empty();
        }
        Optional<LocalDateTime> startTime = subtasksOfEpic.get(0).getStartTime();
        for (Subtask subtask : getSubtasksOfEpic()) {
            if (subtask.getStartTime().get().isBefore(startTime.get())) {
                startTime = subtask.getStartTime();
            }
        }
        return startTime;
    }

    @Override
    public Optional<LocalDateTime> getEndTime() {
        if (subtasksOfEpic.isEmpty()) {
            return Optional.empty();
        }
        Optional<LocalDateTime> endTime = subtasksOfEpic.get(0).getStartTime();
        for (Subtask subtask : getSubtasksOfEpic()) {
            if (subtask.getEndTime().get().isAfter(endTime.get())) {
                endTime = subtask.getEndTime();
            }
        }
        return endTime;
    }

    @Override
    public Optional<Duration> getDuration() {
        if (subtasksOfEpic.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Duration.between(getEndTime().get(), getStartTime().get()));
    }

    public void deleteSubtask(Subtask subtask) {
        if (!subtasksOfEpic.isEmpty()) {
            subtasksOfEpic.remove(subtask);
        }
    }

    public void deleteAllSubtasks() {
        if (!subtasksOfEpic.isEmpty()) {
            subtasksOfEpic.clear();
        }
    }
}