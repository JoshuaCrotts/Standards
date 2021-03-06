package com.revivedstandards.platform;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * This is essentially a wrapper for a Swing JFrame and a Thread
 * starter, so the user doesn't have to mess with it. StandardDraw
 * methods such as circle, rect, etc. cannot be used with this class.
 * Those are only usable with StandardGraphicalApplication and StandardGame.
 */
public abstract class StandardSwingApplication {

  /* JFrame to add content to. */
  private final JFrame FRAME;

  /* Timer for updating the JFrame. */
  private Timer timer;

  /* Frames per second to run the timer at. */
  private int fps;

  /* Milliseconds to specify the speed of our timer. */
  private int ms;

  /* Number of milliseconds per second. */
  private static final int SECONDS_TO_MS = 1000;

  /* Boolean to set the timer to run or not. */
  private boolean isRunning = false;

  public StandardSwingApplication(int width, int height, int fps, String title) {
    this.FRAME = new JFrame(title);
    this.FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.FRAME.setSize(width, height);
    this.FRAME.setResizable(false);

    this.setFPS(fps);

    // Starts the timer.
    SwingUtilities.invokeLater(() -> {
      this.start();
    });
  }

  /**
   * 
   * @param layout
   */
  public void setFrameLayout(LayoutManager layout) {
    this.FRAME.setLayout(layout);
  }

  /**
   * 
   * @param component
   */
  public void addComponent(Component component) {
    this.FRAME.getContentPane().add(component);
  }

  /**
   * 
   * @param component
   */
  public void addComponent(Component component, int index) {
    this.FRAME.getContentPane().add(component, index);
  }

  /**
   * 
   * @param component
   */
  public void addComponent(Component component, Object constraints) {
    this.FRAME.getContentPane().add(component, constraints);
  }

  /**
   * 
   * @param component
   */
  public void addComponent(Component component, Object constraints, int index) {
    this.FRAME.getContentPane().add(component, constraints, index);
  }

  /**
   * Sets the location of the frame to the center of the screen, and packs all
   * components together, updating their dimensions.
   */
  public void packComponents() {
    this.FRAME.pack();
    this.FRAME.setLocationRelativeTo(null);
  }

  /**
   * 
   */
  public abstract void run();

  /**
   * 
   */
  private synchronized void start() {
    if (this.isRunning) {
      return;
    }

    this.isRunning = true;
    this.update();
  }

  /**
   * 
   */
  private synchronized void stop() {
    this.timer.stop();
    this.FRAME.dispose();
    System.exit(0);
  }

  /**
   * 
   */
  private void update() {
    this.timer = new Timer(this.ms, timerListener -> {
      if (!this.isRunning) {
        this.stop();
      }

      this.run();

      // Since the paintComponent method for the JFrame
      // is never called, we have to explicitly tell it that
      // we want drawing to occur.
      this.FRAME.repaint();
    });

    this.timer.start();
  }

// =================== GETTERS AND SETTERS ====================== //

  public void setFPS(int fps) {
    this.fps = fps;
    this.ms = SECONDS_TO_MS / fps;
  }

  public int getFPS() {
    return this.fps;
  }

  public boolean isRunning() {
    return this.isRunning;
  }

  public void setRunning(boolean running) {
    this.isRunning = running;
  }

  public void isVisible() {
    this.FRAME.isVisible();
  }

  public void setVisible(boolean visible) {
    this.FRAME.setVisible(true);
  }

  public JFrame getFrame() {
    return this.FRAME;
  }
}
