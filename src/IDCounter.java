public class IDCounter {
    private int idCounter = 0;

    public int getIdCounter() {
        return ++idCounter;
    }
}
