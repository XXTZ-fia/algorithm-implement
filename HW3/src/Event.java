public class Event implements Comparable<Event>{
    private final double time; // Time of event
    private final Particle a, b; // Particles involved in event
    private final int countA, countB; // Collision counts at event creation

    // Constructor for a new event
    public Event(double t, Particle a, Particle b) {
        this.time = t;
        this.a = a;
        this.b = b;
        this.countA = (a != null) ? a.getCollisionCount() : -1;
        this.countB = (b != null) ? b.getCollisionCount() : -1;
    }


    // Return the time associated with this event
    public double getTime() {
        return time;
    }

    // Return the first particle involved in the event, possibly null
    public Particle getParticle1() {
        return a;
    }

    // Return the second particle involved in the event, possibly null
    public Particle getParticle2() {
        return b;
    }

    // Compare the time associated with this event and another event
    @Override
    public int compareTo(Event that) {
        return Double.compare(this.time, that.time);
    }

    // Check if the event has been invalidated(return true)
    // If the "Collision Count" has changed, it implies that the event has been invalidated
    // due to other collisions that happened after this event was scheduled
    public boolean wasSuperveningEvent() {
        if (a != null && a.getCollisionCount() != countA) return true;
        if (b != null && b.getCollisionCount() != countB) return true;
        return false;
    }
}
