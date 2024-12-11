import java.awt.Color;
import java.awt.Graphics;
public class Particle {
    private double rx, ry;     // position
    private double vx, vy;     // velocity
    private final double radius;  // radius
    private final double mass;  // mass
    private final Color color; // color
    private int count;          // number of collisions

    //Constructor
    public Particle(double rx, double ry, double vx, double vy, double radius, double mass,int r, int g, int b) {
        this.rx = rx;
        this.ry = ry;
        this.vx = vx;
        this.vy = vy;
        this.radius = radius;
        this.mass = mass;
        this.color = new Color(r, g, b);

    }

    // Method to return the time until this particle collides with a vertical wall
    public double collidesX() {
        if (vx > 0) return (1.0 - rx - radius) / vx;
        else if (vx < 0) return (radius - rx) / vx;
        else return Double.POSITIVE_INFINITY; // No collision if velocity in x direction is zero till infinite time
    }

    // Method to return the time until this particle collides with a horizontal wall
    public double collidesY() {
        if (vy > 0) return (1.0 - ry - radius) / vy;
        else if (vy < 0) return (radius - ry) / vy;
        else return Double.POSITIVE_INFINITY; // No collision if velocity in y direction is zero
    }

    // Method to return the time until this particle collides with another particle
    public double collides(Particle that) {
        if (this == that) return Double.POSITIVE_INFINITY;
        double dx = that.rx - this.rx;
        double dy = that.ry - this.ry;
        double dvx = that.vx - this.vx;
        double dvy = that.vy - this.vy;
        double dvdr = dx * dvx + dy * dvy;
        if (dvdr >= 0) return Double.POSITIVE_INFINITY; // They are moving apart
        double dvdv = dvx * dvx + dvy * dvy;
        double drdr = dx * dx + dy * dy;
        double sigma = this.radius + that.radius;
        double d = (dvdr * dvdr) - dvdv * (drdr - sigma * sigma);
        if (d < 0) return Double.POSITIVE_INFINITY; // No solution to the quadratic equation
        return -(dvdr + Math.sqrt(d)) / dvdv;
    }

    // Method to simulate bouncing off a vertical wall
    public void bounceX() {
        vx = -vx;
        count++;
    }

    // Method to simulate bouncing off a horizontal wall
    public void bounceY() {
        vy = -vy;
        count++;
    }

    // Method to simulate bouncing off another particle
    public void bounce(Particle that) {
        if (this == that) return;
        double dx = that.rx - this.rx;
        double dy = that.ry - this.ry;
        double dvx = that.vx - this.vx;
        double dvy = that.vy - this.vy;
        double dvdr = dx * dvx + dy * dvy;
        double dist = this.radius + that.radius;
        double magnitude = 2 * this.mass * that.mass * dvdr / ((this.mass + that.mass) * dist);
        double fx = magnitude * dx / dist;
        double fy = magnitude * dy / dist;
        this.vx += fx / this.mass;
        this.vy += fy / this.mass;
        that.vx -= fx / that.mass;
        that.vy -= fy / that.mass;
        this.count++;
        that.count++;
    }

    // Method to return the total number of collisions involving this particle
    public int getCollisionCount() {
        return count;
    }

    // Move the particle in a straight line for a given amount of time
    public void move(double dt) {
        rx += vx * dt;
        ry += vy * dt;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval((int) ((rx - radius) * 800), (int) ((ry - radius) * 800), (int) (2 * radius * 800), (int) (2 * radius * 800));
    }
}
