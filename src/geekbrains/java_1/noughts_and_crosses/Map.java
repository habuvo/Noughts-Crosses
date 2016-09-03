package geekbrains.java_1.noughts_and_crosses;

import javax.swing.*;
import java.awt.*;

class Map extends JPanel {

    static final int GAME_MODE_HUMAN_VS_AI = 0;
    static final int GAME_MODE_HUMAN_VS_HUMAN = 1;

    Map() {
        setBackground(Color.WHITE);
    }

    void startNewGame(int mode, int field_size_x, int field_size_y, int win_len) {
        System.out.println("mode = " + mode + " field_size = " + field_size_x + " win_len = " + win_len);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.drawLine(10, 10, 100, 100);
    }
}
