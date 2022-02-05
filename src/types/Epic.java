package types;

import java.util.HashMap;

public class Epic extends Task {
    private HashMap<Integer, Subtask> subtasksOfEpic = new HashMap<>();

    public Epic(String title, String description, String status) {
        super(title, description, status);
    }

    public HashMap<Integer, Subtask> getSubtasksOfEpic() {
        return new HashMap<>(subtasksOfEpic);
    }

    public void addSubtasksOfEpic(Subtask subtask) {
        subtasksOfEpic.put(subtask.getId(), subtask);
    }

    @Override
    public String getStatus() {                            // расчет статусов эпиков при обращении к его полю статус
        int statusNewCounter = 0;
        int statusDoneCounter = 0;
        if (getSubtasksOfEpic().isEmpty()) {
            return "NEW";
        } else {
            for (Subtask tmp : getSubtasksOfEpic().values()) {
                if (tmp.getStatus().equals("NEW")) {
                    statusNewCounter++;
                } else if (tmp.getStatus().equals("DONE")) {
                    statusDoneCounter++;
                }
            }
            if (statusNewCounter == getSubtasksOfEpic().size()) {
                return "NEW";
            } else if (statusDoneCounter == getSubtasksOfEpic().size()) {
                return "DONE";
            } else {
                return "IN_PROGRESS";
            }
        }
    }

    public void deleteSubtask(int id) {
        if (subtasksOfEpic.containsKey(id)) {
            subtasksOfEpic.remove(id);
        }
    }

    public void deleteAllSubtasks() {
        subtasksOfEpic.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", id=" + getId() +
                ", subtasksListSize=" + subtasksOfEpic.size() +
                '}';
    }

    @Override
    public void setStatus(String status) {}            // пользователь не может корректировать статус эпика
}

