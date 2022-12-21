package eliascregard.game;

import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel implements Runnable {
    private Dimension WINDOW_SIZE = Main.WINDOW_SIZE;
    private final Dimension DEFAULT_WINDOW_SIZE = new Dimension(1000, 1000);
    private double WINDOW_SCALE = (double) WINDOW_SIZE.width / DEFAULT_WINDOW_SIZE.width;
    private int MAX_FRAME_RATE = 0;
    private Thread gameThread;
    private GameTime time = new GameTime();
    private KeyHandler keyH = new KeyHandler();
    private MouseHandler mouse = new MouseHandler(WINDOW_SCALE);
    private double deltaT;
    private int tickSpeed;
    private double renderDeltaT = 0;
    private int fps;
    private double gridOffset = 10 * WINDOW_SCALE;
    private int gridSize = (int) (WINDOW_SIZE.width - gridOffset * 2);
    private int cellSize = gridSize / 8;
    private OthelloGame game = new OthelloGame();

    public void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public GamePanel() {
        this.setPreferredSize(WINDOW_SIZE);
        this.setBackground(new Color(0, 0, 0));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addMouseListener(mouse);
        this.setFocusable(true);
    }

    @Override
    public void run() {

        while (gameThread != null) {
            deltaT = time.getDeltaTime();
            tickSpeed = time.getFPS(deltaT);
            renderDeltaT += deltaT;
            fps = tickSpeed;
            if (fps > MAX_FRAME_RATE && MAX_FRAME_RATE > 0) {
                fps = MAX_FRAME_RATE;
            }

            if (keyH.escapePressed) {
                System.exit(0);
            }

            update();
            if (MAX_FRAME_RATE > 0) {
                if (renderDeltaT >= 1.0 / MAX_FRAME_RATE) {
                    repaint();
                    renderDeltaT -= 1.0 / MAX_FRAME_RATE;
                }
            }
            else {
                repaint();
            }
        }
    }

    public void update() {

        game.update();

        if (!mouse.leftIsPressed()) {
            return;
        }
        mouse.setLeftIsPressed(false);
        if (mouse.getX() < gridOffset || mouse.getX() > gridOffset + gridSize ||
            mouse.getY() < gridOffset || mouse.getY() > gridOffset + gridSize) return;
        int x = (int) ((mouse.getX() - gridOffset) / cellSize);
        int y = (int) ((mouse.getY() - gridOffset) / cellSize);
        if (game.getBrick(x, y) != null) return;
        game.placeBrick(x, y);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(new Color(255 * game.getTurn(),255 * game.getTurn(),255 * game.getTurn()));
        g2.fillRect(0, 0, WINDOW_SIZE.width, WINDOW_SIZE.height);

        g2.setColor(new Color(0, 155, 0));
        g2.fillRect((int) gridOffset, (int) gridOffset, gridSize, gridSize);
        g2.setStroke(new BasicStroke(1));
        for (int i = 1; i < 8; i++) {
            g2.setColor(new Color(0, 0, 0));
            g2.drawLine((int) gridOffset + i * cellSize, (int) gridOffset,
                    (int) gridOffset + i * cellSize, (int) gridOffset + gridSize);
            g2.drawLine((int) gridOffset, (int) gridOffset + i * cellSize,
                    (int) gridOffset + gridSize, (int) gridOffset + i * cellSize);
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Brick brick = game.getBrick(i, j);
                if (brick != null) {
                    g2.setColor(brick.getColor());
                    g2.fillOval((int) gridOffset + i * cellSize + 1, (int) gridOffset + j * cellSize + 1,
                            cellSize - 2, cellSize - 2);
                }
            }
        }

        g2.setColor(new Color(255, 0, 0));
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.drawString("FPS: " + fps, 10, 20);

        g2.dispose();
    }
}