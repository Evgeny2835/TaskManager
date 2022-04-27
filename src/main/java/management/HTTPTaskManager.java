package management;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;

public class HTTPTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvTaskClient;

    public HTTPTaskManager(HistoryManager historyManager, URI url) throws IOException, InterruptedException {
        super(historyManager);
        this.kvTaskClient = new KVTaskClient(url);
    }

    @Override
    protected void save() {
        Gson gson = new Gson();
        String jsonTasks = gson.toJson(tasks);
        kvTaskClient.put("task", jsonTasks);
        String jsonEpics = gson.toJson(epics);
        kvTaskClient.put("epic", jsonEpics);
        String jsonSubtasks = gson.toJson(subtasks);
        kvTaskClient.put("subtask", jsonSubtasks);
        String jsonHistory = gson.toJson(history());
        kvTaskClient.put("history", jsonHistory);
    }
}