package management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import types.TasksStatus;
import types.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    HistoryManager historyManager;
    Task task1;
    Task task2;
    Task task3;
    Task task4;
    Task task5;

    @BeforeEach
    public void beforeEach(){
        historyManager = Managers.getDefaultHistory();
        task1 = new Task("task1Name",
                "task1Description",
                TasksStatus.NEW,
                Optional.of(LocalDateTime.of(2022, 5, 1, 10, 0)),
                Optional.of(Duration.ofMinutes(60 * 24)));
        task1.setId(1);
        task2 = new Task("task2Name",
                "task2Description",
                TasksStatus.NEW,
                Optional.of(LocalDateTime.of(2022, 5, 3, 10, 0)),
                Optional.of(Duration.ofMinutes(60)));
        task2.setId(2);
        task3 = new Task("task3Name",
                "task3Description",
                TasksStatus.NEW,
                Optional.of(LocalDateTime.of(2022, 5, 5, 10, 0)),
                Optional.of(Duration.ofMinutes(10)));
        task3.setId(3);
        task4 = new Task("task4Name",
                "task4Description",
                TasksStatus.NEW,
                Optional.of(LocalDateTime.of(2022, 5, 7, 10, 0)),
                Optional.of(Duration.ofMinutes(60 * 24 + 60 + 10)));
        task4.setId(4);
        task5 = new Task("task5name",
                "task5Description",
                TasksStatus.NEW,
                Optional.of(LocalDateTime.of(2022, 5, 9, 10, 0)),
                Optional.of(Duration.ofMinutes(5)));
        task5.setId(5);
    }

    @Test
    public void ShouldReturn0IfHistoryIsEmpty() {
        assertNotNull(historyManager.getHistory());
        assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    public void ShouldReturn2IfHistoryIsNotEmpty() {
        historyManager.add(task1);
        historyManager.add(task2);
        assertNotNull(historyManager.getHistory());
        assertEquals(2, historyManager.getHistory().size());
    }

    @Test
    public void ShouldReturnListOfTwoTaskIfHistoryIsNotDuplicated() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);
        assertNotNull(historyManager.getHistory());
        assertEquals(List.of(task2,task1), historyManager.getHistory());
    }

    @Test
    public void ShouldReturn2IfDeletingFromHistoryAtBeginning() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(1);
        assertNotNull(historyManager.getHistory());
        assertEquals(2, historyManager.getHistory().size());
    }

    @Test
    public void ShouldReturn4IfDeletingFromHistoryInMiddle() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        historyManager.add(task5);
        historyManager.remove(3);
        assertNotNull(historyManager.getHistory());
        assertEquals(4, historyManager.getHistory().size());
    }

    @Test
    public void ShouldReturn2IfDeletingFromHistoryAtEnd() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(3);
        assertNotNull(historyManager.getHistory());
        assertEquals(2, historyManager.getHistory().size());
    }
}