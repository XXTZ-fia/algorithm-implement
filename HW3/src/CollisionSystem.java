import javax.swing.JPanel;
import java.util.List;
public class CollisionSystem {
    private static final double HZ = 0.5; // Redraw interval in seconds
    private MinPQ<Event> pq;
    private double t = 0.0; // Simulation clock time
    private Particle[] particles;
    private ParticlePanel panel; // Panel for drawing particles

    public CollisionSystem(Particle[] particles,ParticlePanel panel) {
        this.particles = particles;
        this.panel = panel;
    }

    private void predict(Particle a) {
        if (a == null) return;
        for (Particle b : particles) {
            //if(a == b) continue;
            double dt = a.collides(b);
            if (dt < Double.POSITIVE_INFINITY) pq.insert(new Event(t + dt, a, b));
        }
        double dtX = a.collidesX();
        if (dtX < Double.POSITIVE_INFINITY) pq.insert(new Event(t + dtX, a, null));
        double dtY = a.collidesY();
        if (dtY < Double.POSITIVE_INFINITY) pq.insert(new Event(t + dtY, null, a));
    }

    private void redraw() {
        panel.updateParticles(particles);
        try {
            Thread.sleep((long) (100.0 / HZ)); // Delay in milliseconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pq.insert(new Event(t + 1.0 / HZ, null, null)); //schedule next redraw event
    }

    public void simulate(){
        pq = new MinPQ<>();
        for (Particle particle : particles) {
            predict(particle);
        }
        pq.insert(new Event(0, null, null)); // Initial redraw event
        while (!pq.isEmpty()) {
            Event event = pq.delMin(); // the first one in the queue
            if (event.wasSuperveningEvent()) {
                System.out.println("Event invalid, skipping...");
                continue;
            }
            Particle a = event.getParticle1();
            Particle b = event.getParticle2();
            System.out.println(event.getTime());
            for (Particle particle : particles) {
                particle.move(event.getTime() - t);
            }
            t = event.getTime();

            if (a != null && b != null) a.bounce(b);
            else if (a != null) a.bounceX();
            else if (b != null) b.bounceY();
            else redraw();

            if (a != null) predict(a);
            if (b != null) predict(b);
        }
    }
}

