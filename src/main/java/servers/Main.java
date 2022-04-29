package servers;

import management.HTTPTaskManager;
import management.Managers;

import java.io.IOException;
import java.net.URI;

public class Main {

    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        try {
            new HttpTaskServer(new HTTPTaskManager(Managers.getDefaultHistory(),
                    URI.create("http://localhost:8078"))).start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}