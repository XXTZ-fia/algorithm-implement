import java.util.PriorityQueue;
public class MinPQ<Event>{
    private PriorityQueue<Event> pq;

    public MinPQ() {
        pq = new PriorityQueue<>();
    }

    public void insert(Event event) {
        pq.add(event);
    }

    public Event delMin() {
        return pq.poll();
    }

    public boolean isEmpty() {
        return pq.isEmpty();
    }
}


