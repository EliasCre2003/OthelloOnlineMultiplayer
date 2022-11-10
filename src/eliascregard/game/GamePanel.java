package eliascregard.game;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


public class GamePanel extends JPanel implements Runnable {
    final Dimension SCREEN_SIZE = new Dimension(1000, 1000);
    final Dimension DEFAULT_SCREEN_SIZE = new Dimension(1000, 1000);
    final double SCREEN_SCALE = (double) SCREEN_SIZE.width / DEFAULT_SCREEN_SIZE.width;
    int MAX_FRAME_RATE = 0;
    int MAX_TICKSPEED = 0;
    long timeAtMovement = System.nanoTime();
    public long timeSinceMovement() {
        return System.nanoTime() - timeAtMovement;
    }
    public Thread gameThread;
    GameTime time = new GameTime();
    KeyHandler keyH = new KeyHandler();
    MouseHandler mouse = new MouseHandler();
    double deltaT;
    public int tickSpeed;
    double renderDeltaT = 0;
    public int fps;

    Brick[][] grid = new Brick[8][8];
    double gridOffset = 10 * SCREEN_SCALE;
    int gridSize = (int) (SCREEN_SIZE.width - gridOffset * 2);
    int cellSize = gridSize / grid.length;
    int turn = (int) (Math.random() * 2);
    boolean otherPlayerNoLegalMoves = false;
    boolean gameOver = false;


    public double[] sortArray(double[] array) {
        double[] sortedArray = Arrays.copyOf(array, array.length);
        for (int i = 0; i < sortedArray.length; i++) {
            for (int j = 0; j < sortedArray.length - 1; j++) {
                if (sortedArray[j] > sortedArray[j + 1]) {
                    double temp = sortedArray[j];
                    sortedArray[j] = sortedArray[j + 1];
                    sortedArray[j + 1] = temp;
                }
            }
        }
        return sortedArray;
    }

    public void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isLegal(int x, int y) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length) {
            return false;
        }



        return true;
    }

    public void recalculateGrid(int x, int y) {

    }


    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public GamePanel() {
        this.setPreferredSize(SCREEN_SIZE);
        this.setBackground(new Color(0, 0, 0));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addMouseListener(mouse);
        this.setFocusable(true);
    }

    @Override
    public void run() {

        grid[3][3] = new Brick(0);
        grid[3][4] = new Brick(1);
        grid[4][3] = new Brick(1);
        grid[4][4] = new Brick(0);

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
        if (gameOver) {
            return;
        }

        boolean noLegalMoves = true;
        for (int i = 0; i < grid.length; i++) {
            boolean breakLoop = false;
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == null && isLegal(i, j)) {
                    noLegalMoves = false;
                    breakLoop = true;
                    break;
                }
            }
            if (breakLoop) {
                break;
            }
        }
        if (noLegalMoves) {
            if (otherPlayerNoLegalMoves) {
                gameOver = true;
            }
            else {
                turn = (turn + 1) % 2;
            }
            return;
        }

        otherPlayerNoLegalMoves = false;

        if (!mouse.pressed) {
            return;
        }
        mouse.pressed = false;
        if (mouse.x < gridOffset || mouse.x > gridOffset + gridSize ||
                mouse.y < gridOffset || mouse.y > gridOffset + gridSize) {
            return;
        }
        int x = (int) ((mouse.x - gridOffset) / cellSize);
        int y = (int) ((mouse.y - gridOffset) / cellSize);
        if (grid[x][y] != null) {
            return;
        }
        if (isLegal(x, y)) {
            grid[x][y] = new Brick(turn);
            recalculateGrid(x, y);
            turn = (turn + 1) % 2;
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(new Color(255 * turn,255 * turn,255 * turn));
        g2.fillRect(0, 0, SCREEN_SIZE.width, SCREEN_SIZE.height);

        g2.setColor(new Color(0, 155, 0));
        g2.fillRect((int) gridOffset, (int) gridOffset, gridSize, gridSize);
        g2.setStroke(new BasicStroke(1));
        for (int i = 1; i < grid.length; i++) {
            g2.setColor(new Color(0, 0, 0));
            g2.drawLine((int) gridOffset + i * cellSize, (int) gridOffset,
                    (int) gridOffset + i * cellSize, (int) gridOffset + gridSize);
            g2.drawLine((int) gridOffset, (int) gridOffset + i * cellSize,
                    (int) gridOffset + gridSize, (int) gridOffset + i * cellSize);
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                Brick brick = grid[i][j];
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