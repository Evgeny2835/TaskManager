package Servers;

import com.sun.net.httpserver.HttpServer;
import management.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final TaskManager taskManager;
    private final HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
    }

    public void start() {
        httpServer.start();
        System.out.println("TaskServer открыт на порту " + PORT);
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("TaskServer остановлен на порту " + PORT);
    }
}