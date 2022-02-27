package start;

import management.Managers;
import management.TaskManager;
import types.Epic;
import types.Subtask;
import types.Task;

import static types.Status.NEW;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault(Managers.getDefaultHistory());
        Task task1 = new Task("Получить посылку", "Посылка от бабушки", NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Написать письмо", "Маме об отпуске", NEW);
        taskManager.addTask(task2);
        Epic epic1 = new Epic("Выезд на рыбалку", "На озеро с Петром", NEW);
        taskManager.addEpic(epic1);
        Subtask subTask1Epic1 = new Subtask("Подготовить удочки",
                "Осмотр удочек, если требуется, ремонт",
                NEW,
                3);
        taskManager.addSubtask(subTask1Epic1);
        Subtask subTask2Epic1 = new Subtask("Подготовить наживку",
                "Без наживки никуда, купить в магазине или добыть в поле",
                NEW,
                3);
        taskManager.addSubtask(subTask2Epic1);
        Subtask subTask3Epic1 = new Subtask("Подготовить одежду",
                "Ожидается дождь, проверить плащ и палатку",
                NEW,
                3);
        taskManager.addSubtask(subTask3Epic1);
        Epic epic2 = new Epic("Выезд в отпуск",
                "Место назначения - Сочи",
                NEW);
        taskManager.addEpic(epic2);

        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(4);
        taskManager.getSubtaskById(5);
        taskManager.getSubtaskById(6);
        taskManager.getEpicById(7);
        for (Task task : taskManager.history()) {
            System.out.println(task);
        }
        System.out.println();
        taskManager.getTaskById(1);
        taskManager.getEpicById(3);
        taskManager.getSubtaskById(6);
        for (Task task : taskManager.history()) {
            System.out.println(task);
        }
        System.out.println();
        taskManager.getSubtaskById(6);
        taskManager.deleteEpicById(3);
        taskManager.deleteTaskById(1);
        taskManager.deleteSubtaskById(4);
        for (Task task : taskManager.history()) {
            System.out.println(task);
        }
    }
}