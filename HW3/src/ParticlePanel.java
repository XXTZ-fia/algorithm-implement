import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.util.List;
public class ParticlePanel extends JPanel{
    private Particle[] particles;

    public ParticlePanel(Particle[] particles) {
        this.particles = particles;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Particle particle : particles) {
            particle.draw(g);
        }
    }

    public void updateParticles(Particle[] particles) {
        this.particles = particles;
        repaint();
    }
}
