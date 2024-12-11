import javax.swing.JFrame;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
public class MDSimulation {
    public static void main(String[] args) {
        // Example usage
        Scanner scanner = null;
        try {
            if (args.length > 0) {
                scanner = new Scanner(new File(args[0])); // Read from file if filename provided
            } else {
                scanner = new Scanner(System.in); // Otherwise read from standard input
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found.");
            System.exit(1);
        }

        int N = scanner.nextInt(); // Number of particles
        Particle[] particles = new Particle[N];

        for (int i = 0; i < N; i++) {
            double rx = scanner.nextDouble();
            double ry = scanner.nextDouble();
            double vx = scanner.nextDouble();
            double vy = scanner.nextDouble();
            //double mass = scanner.nextDouble();
            double radius = scanner.nextDouble();
            double mass = scanner.nextDouble();
            int r = scanner.nextInt();
            int g = scanner.nextInt();
            int b = scanner.nextInt();

            particles[i] = new Particle(rx, ry, vx, vy, radius, mass, r, g, b);
        }


        JFrame frame = new JFrame("Particle Simulation");
        ParticlePanel panel = new ParticlePanel(particles);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.add(panel);
        frame.setVisible(true);

        CollisionSystem system = new CollisionSystem(particles,panel);
        system.simulate();
    }
}
