package geekbrains.java_1.noughts_and_crosses;

import javax.swing.*;
import java.awt.*;

class Map extends JPanel {

    static final int GAME_MODE_HUMAN_VS_AI = 0;
    static final int GAME_MODE_HUMAN_VS_HUMAN = 1;
    static private int field_sizex = 3;
    static private int field_sizey = 3;

    Map() {
        setBackground(Color.WHITE);
    }

    void startNewGame(int mode, int field_size_x, int field_size_y, int win_len) {
        System.out.printf("mode = %d field_size = %d win_len = %d%n", mode, field_size_x, win_len);
        field_sizex = field_size_x;
        field_sizey = field_size_y;
        this.repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);

        int width_cell = getWidth()/field_sizex;
        int height_cell = getHeight()/field_sizey;

        for (int i=1; i<field_sizex;i++) g.drawLine(width_cell*i,0,width_cell*i,getHeight());
        for (int i=1; i<field_sizey;i++) g.drawLine(0,height_cell*i,getWidth(),width_cell*i);


    }
}
