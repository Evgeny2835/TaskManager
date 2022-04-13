package types;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class Subtask extends Task {
    private final int idEpic;

    public Subtask(String name,
                   String description,
                   TasksStatus status,
                   Optional<LocalDateTime> startTime,
                   Optional<Duration> duration,
                   int idEpic) {
        super(name, description, status, startTime, duration);
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public TasksList getType() {
        return TasksList.SUBTASK;
    }

    @Override
    public String toString() {
        return super.toString() + "," + getIdEpic();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return idEpic == subtask.idEpic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpic);
    }
}