package eliascregard.game;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Main {
    public static GamePanel gamePanel;
    public static JFrame window;
    public static Dimension WINDOW_SIZE;

    public static void main(String[] args) {
        boolean maximized = false;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        try {
            if (args.length > 2) {
                maximized = Boolean.parseBoolean(args[2]);
                if (maximized) {
                    WINDOW_SIZE = screenSize;
                } else {
                    WINDOW_SIZE = new Dimension(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
                }
            } else {
                WINDOW_SIZE = new Dimension(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            }
        } catch (Exception e) {
            WINDOW_SIZE = new Dimension(1600, 900);
        }
        System.out.println("Window size: " + WINDOW_SIZE.width + " x " + WINDOW_SIZE.height);

        window = new JFrame("Othello");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setUndecorated(maximized);

        gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();
        window.setLocation(
                (int) (screenSize.getWidth() / 2 - window.getWidth() / 2),
                (int) ((screenSize.getHeight() / 2 - window.getHeight() / 2) / 2)
        );
        window.setVisible(true);
        gamePanel.startGameThread();
        System.out.println("Game started");
    }
}