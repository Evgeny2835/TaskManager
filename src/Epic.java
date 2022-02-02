import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasksList = new HashMap<>();

    public Epic(String title, String description, String status) {
        super(title, description, status);
    }

    public HashMap<Integer, Subtask> getSubtasksList() {
        return subtasksList;
    }

    public void setSubtasksList(Subtask subtask) {
        subtasksList.put(subtask.getId(), subtask);
    }

    public void printSubtasksInEpic() {
        for (Subtask tmp : subtasksList.values()) {
            System.out.println(tmp);
        }
    }

    public void deleteSubtask(int id) {
        subtasksList.remove(id);
    }

    public void deleteAllSubtasks() {
        subtasksList.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", id=" + getId() +
                ", subtasksListSize=" + subtasksList.size() +
                '}';
    }
}

