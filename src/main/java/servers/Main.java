package servers;

import com.google.gson.Gson;
import management.KVTaskClient;
import management.Managers;
import management.TaskManager;
import types.Task;
import types.TasksStatus;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class Main {
    private static KVServer kvServer;
    private static HttpTaskServer httpTaskServer;

    public static void main(String[] args) {
        try {
            kvServer = new KVServer();
            kvServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TaskManager taskManager = Managers.getDefault();
        try {
            httpTaskServer = new HttpTaskServer(taskManager);
            httpTaskServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        KVTaskClient kvTaskClient = new KVTaskClient(URI.create("http://localhost:8078"), "DEBUG");
        Gson gson = new Gson();
        Task task1 = new Task("task1Name",
                "task1Description",
                TasksStatus.NEW,
                Optional.of(LocalDateTime.of(2022, 5, 1, 10, 0)),
                Optional.of(Duration.ofMinutes(60 * 24)));
        taskManager.addNewTask(task1);
        taskManager.getTaskById(task1.getId());
        String json = gson.toJson(task1);
        kvTaskClient.put("task", json);
        if (json.equals(kvTaskClient.load("task"))) {
            System.out.println("Успешный тест");
        } else {
            System.out.println("Тест провален");
        }
        httpTaskServer.stop();
        kvServer.stop();
    }
}