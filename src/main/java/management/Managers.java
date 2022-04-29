package management;

import java.io.IOException;
import java.net.URI;

public class Managers {

    public static TaskManager getDefault(HistoryManager historyManager, URI url) {
        try {
            return new HTTPTaskManager(historyManager, url);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}