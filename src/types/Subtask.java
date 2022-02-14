package types;

public class Subtask extends Task {
    private final int idEpic;

    public Subtask(String title, String description, Status status, int idEpic) {
        super(title, description, status);
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", id=" + getId() +
                ", idEpic=" + idEpic +
                '}';
    }
}