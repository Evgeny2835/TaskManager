package management;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void start() {
        taskManager = (InMemoryTaskManager) Managers.getDefault(Managers.getDefaultHistory());
    }
}