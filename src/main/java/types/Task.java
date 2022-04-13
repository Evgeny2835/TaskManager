package types;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

public class Task {
    protected final String name;
    protected final String description;
    protected TasksStatus status;
    protected Optional<LocalDateTime> startTime;
    protected Optional<Duration> duration;
    protected int id;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy_HH:mm");
    public static final Comparator<Task> comparator = new Comparator<Task>() {
        @Override
        public int compare(Task task1, Task task2) {
            if (task1.getId() == task2.getId()) {
                return 0;
            }
            if (task1.getStartTime().isEmpty()) {
                return -1;
            }
            if (task2.getStartTime().isEmpty()) {
                return 1;
            }
            return task1.getStartTime().get().compareTo(task2.getStartTime().get());
        }
    };

    public Task(String name,
                String description,
                TasksStatus status,
                Optional<LocalDateTime> startTime,
                Optional<Duration> duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TasksStatus getStatus() {
        return status;
    }

    public Optional<LocalDateTime> getStartTime() {
        return startTime;
    }

    public Optional<Duration> getDuration() {
        return duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TasksList getType() {
        return TasksList.TASK;
    }

    public Optional<LocalDateTime> getEndTime() {
        return Optional.of(startTime.get().plus(duration.get()));
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s,%s",
                getId(),
                getType(),
                getName(),
                getStatus(),
                getDescription(),
                getStartTime().isPresent() ? getStartTime().get().format(formatter) : "null",
                getDuration().isPresent() ? getDuration().get().toString() : "null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id &&
                Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                status == task.status && Objects.equals(startTime, task.startTime) &&
                Objects.equals(duration, task.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, startTime, duration, id);
    }
}