package eliascregard.game;

import java.awt.Color;

public class Brick {

    private int player;

    public Brick(int player) {
        this.player = player;
    }

    public Color getColor() {
        return new Color(255 * player, 255 * player, 255 * player);
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }
    public void flip() {
        if (player == 0) {
            player = 1;
        } else {
            player = 0;
        }
    }

}
