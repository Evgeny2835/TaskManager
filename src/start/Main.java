package start;

import management.Manager;
import types.Epic;
import types.Subtask;
import types.Task;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task1 = new Task("Получить посылку", "Посылка от бабушки", "NEW");
        manager.addTask(task1);
        Task task2 = new Task("Написать письмо", "Маме об отпуске", "NEW");
        manager.addTask(task2);
        Epic epic1 = new Epic("Выезд на рыбалку", "На озеро с Петром", "NEW");
        manager.addEpic(epic1);
        Subtask subTask1Epic1 = new Subtask("Подготовить удочки",
                "Осмотр удочек, если требуется, ремонт",
                "NEW",
                3);
        manager.addSubtask(subTask1Epic1);
        Subtask subTask2Epic1 = new Subtask("Подготовить наживку",
                "Без наживки никуда, купить в магазине или добыть в поле",
                "NEW",
                3);
        manager.addSubtask(subTask2Epic1);
        Epic epic2 = new Epic("Выезд в отпуск",
                "Место назначения - Сочи",
                "NEW");
        manager.addEpic(epic2);
        Subtask subTask1Epic2 = new Subtask("Подготовить машину",
                "Осмотр машины, устранение неисправностей",
                "NEW",
                6);
        manager.addSubtask(subTask1Epic2);
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
        System.out.println();
        task1.setStatus("DONE");
        manager.updateTask(task1);
        epic1.setStatus("DONE");
        manager.updateEpic(epic1);
        subTask1Epic1.setStatus("DONE");
        manager.updateSubtask(subTask1Epic1);
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
        System.out.println();
        manager.deleteTaskById(2);
        manager.deleteEpicById(3);
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
    }
}


