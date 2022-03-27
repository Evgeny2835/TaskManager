package types;

public class Subtask extends Task {
    private final int idEpic;

    public Subtask(String name, String description, TaskStatus status, int idEpic) {
        super(name, description, status);
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d",
                getId(),
                TypesOfTasks.SUBTASK,
                getName(),
                getStatus(),
                getDescription(),
                getIdEpic());
    }
}