package eliascregard.game;

import java.awt.Color;

public class Brick {

    public int type = 0;

    public Brick(int type) {
        this.type = type;
    }

    public Color getColor() {
        return new Color(255 * type, 255 * type, 255 * type);
    }

}
