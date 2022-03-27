package id;

public class IDCounter {
    private int idCounter;

    public int getIdCounter() {
        return ++idCounter;
    }

    public void setIdCounter(int idCounter) {
        this.idCounter = idCounter;
    }
}