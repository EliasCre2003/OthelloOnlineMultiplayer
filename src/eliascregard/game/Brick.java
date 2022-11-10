package eliascregard.game;

import java.awt.Color;

public class Brick {

    public int player;

    public Brick(int player) {
        this.player = player;
    }

    public Color getColor() {
        return new Color(255 * player, 255 * player, 255 * player);
    }

}
