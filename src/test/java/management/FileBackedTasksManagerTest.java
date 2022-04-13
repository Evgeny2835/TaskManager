package management;

import org.junit.jupiter.api.Test;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static management.FileBackedTasksManager.loadFromFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
   String filePath = "history.csv";

    @Override
    void setManager() {
        taskManager = new FileBackedTasksManager(Managers.getDefaultHistory(), new File(filePath));
    }

    @Test
    public void loadFromFileAndSaveTestTask() {
        // для тестирования - очистка содержимого файла
        try {
            BufferedWriter bw = Files.newBufferedWriter(Path.of(filePath), StandardOpenOption.TRUNCATE_EXISTING);
            bw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        taskManager.addNewTask(task1);
        taskManager.getTaskById(task1.getId());
        TaskManager taskManagerNew = loadFromFile(new File(filePath));
        assertEquals(1, taskManagerNew.getTasks().size(), "Список не восстановился");
        assertEquals(task1, taskManagerNew.getTaskById(task1.getId()), "Задачи не совпадают");
        assertEquals(1, taskManagerNew.history().size(), "История не восстановилась");
        taskManager.deleteTaskById(task1.getId());
        assertNotNull(taskManagerNew.history(), "История не пустая");
    }

    @Test
    public void loadFromFileAndSaveTestEpic() {
        // для тестирования - очистка содержимого файла
        try {
            BufferedWriter bw = Files.newBufferedWriter(Path.of(filePath), StandardOpenOption.TRUNCATE_EXISTING);
            bw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        taskManager.addNewTask(epic1);
        taskManager.getTaskById(epic1.getId());
        TaskManager taskManagerNew = loadFromFile(new File(filePath));
        assertEquals(1, taskManagerNew.getEpics().size(), "Список не восстановился");
        assertEquals(epic1, taskManagerNew.getEpicById(epic1.getId()), "Задачи не совпадают");
        assertEquals(1, taskManagerNew.history().size(), "История не восстановилась");
        taskManager.deleteEpicById(task1.getId());
        assertNotNull(taskManagerNew.history(), "История не пустая");
    }
}