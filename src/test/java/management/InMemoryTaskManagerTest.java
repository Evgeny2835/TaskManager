package management;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    void setManager() {
        taskManager = (InMemoryTaskManager) Managers.getDefault(Managers.getDefaultHistory());
    }
}