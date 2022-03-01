package management;

import types.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private Node first;                                               // для хранения ссылки на головной элемент узла
    private Node last;                                                // для хранения ссылки на последний элемент узла
    private final HashMap<Integer, Node> history = new HashMap<>();   // для хранения ссылки на историю просмотра задач

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (history.containsKey(task.getId())) {
            remove(task.getId());                           // история должна хранить только последний просмотр задачи
        }                                                   // при наличии истории, удаляем старую запись о просмотре и
        history.put(task.getId(), linkLast(task));          // добавляем новую
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {                                  // удаление истории просмотра задачи из 2 этапов
        removeNode(history.get(id));                              // удаление узла
        history.remove(id);                                       // удаление сведений из хеш-таблицы
    }

    private Node linkLast(Task task) {
        final Node lastNode = last;
        final Node newNode = new Node(lastNode, task, null);
        last = newNode;
        if (lastNode == null) {
            first = newNode;
        } else {
            lastNode.next = newNode;
        }
        return newNode;
    }

    private List<Task> getTasks() {
        List<Task> tasksList = new ArrayList<>(20);
        Node node = first;
        while (node != null) {                                // в цикле перебираем по цепочке узлы
            tasksList.add(node.task);                         // для добавления очередной задачи по порядку
            node = node.next;
        }
        return tasksList;
    }

    private void removeNode(Node node) {
        if (node != null) {
            final Node next = node.next;
            final Node prev = node.prev;
            if (prev == null) {
                first = next;
            } else {
                prev.next = next;
                node.prev = null;
            }
            if (next == null) {
                last = prev;
            } else {
                next.prev = prev;
                node.next = null;
            }
            node.task = null;
        }
    }

    static class Node {
        private Task task;
        private Node prev;
        private Node next;

        public Node(Node prev, Task task, Node next) {
            this.task = task;
            this.next = next;
            this.prev = prev;
        }
    }
}