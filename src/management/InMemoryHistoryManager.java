package management;

import types.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    static class Node {
        private Task data;
        private Node prev;
        private Node next;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    private Node first;                                           // переменная хранит ссылку на первый элемент
    private Node last;                                            // переменная хранит ссылку на последний элемент
    private HashMap<Integer, Node> history = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
    }

    private void linkLast(Task task) {
        final Node lastNode = last;                               // запоминаем ссылку на последнюю ноду
        final Node newNode = new Node(lastNode, task, null); // создаем новую ноду с учетом предыдущей
        last = newNode;                                           // переменная last теперь хранит ссылку на новую ноду
        if (lastNode == null) {                                   // проверяем, не является ли ссылка на последнюю ноду
            first = newNode;                                      // пустой, если, да, то ноды ранее не создавались, ссылка на первую и единственную пока ноду
        } else {                                                  // если же нода не является единственной, то у ноды,
            lastNode.next = newNode;                              // которая ранее была последней, нужно скорректировать ссылку на последюю
        }                                                         // ноду, она теперь не null, а ссылается на новую ноду
        if (history.containsKey(task.getId())) {
            remove(task.getId());
        }
        history.put(task.getId(), newNode);
    }

    private List<Task> getTasks() {
        List<Task> tasksList = new ArrayList<>();
        Node node = this.first;
        while (node != null) {
            tasksList.add(node.data);
            node = node.next;
        }
        return tasksList;
    }

      private void removeNode(Node node) {
        if (node != null) {
            final Task task = node.data;
            final Node next = node.next;
            final Node prev = node.prev;

            if (prev == null) {              // для случая удаления головного элемента
                first = next;                // переменная класса first теперь хранит ссылку на следующий элемент
            } else {
                prev.next = next;            // предыдущий элемент теперь ссылается на следующий элемент
                node.prev = null;            // удаляемый элемент не ссылается на предшествующий
            }

            if (next == null) {              // для случая удаления последнего элемента
                last = prev;                 // переменная класса last теперь хранит ссылку на предыдущий элемент
            } else {
                next.prev = prev;            // следующий элемент теперь ссылается на предыдущий
                node.next = null;            // удаляймый элемент не ссылается на следующий
            }
            node.data = null;                // удаление ссылки на хранимый объект
            history.remove(task.getId());    // удаление задачи из истории
        }
    }
}