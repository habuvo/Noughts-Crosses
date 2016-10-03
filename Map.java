package geekbrains.java_1.noughts_and_crosses;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Random;


class Map extends JPanel {

    static final int GAME_MODE_HUMAN_VS_AI = 0;
    static final int GAME_MODE_HUMAN_VS_HUMAN = 1;

    private static final int EMPTY_DOT = 0;
    private static final int PLAYER1_DOT = 1;
    private static final int PLAYER2_DOT = 2;

    private static final int DRAW = 0;
    private static final int PLAYER1_WIN = 1;
    private static final int PLAYER2_WIN = 2;

    private static final int DOTS_MARGIN = 4;

    private static final String DRAW_MSG = "Draw!";
    private static final String HUMAN_MSG = "Human win!";
    private static final String AI_MSG = "AI win!";

    private static final String PLAYER1_WIN_MSG = "Player1 win!";
    private static final String PLAYER2_WIN_MSG = "Player2 win!";
    private final Random rnd = new Random();
    private final Font font = new Font("Times new roman", Font.BOLD, 48);
    private int gameMode;
    private int currentTurn = PLAYER1_DOT;
    private boolean initialized;
    private int[][] field;
    private int field_size_x;
    private int field_size_y;
    private int win_len;
    private boolean game_over;
    private int game_over_state;
    private int cell_width;
    private int cell_height;

    private Toolkit toolkit = Toolkit.getDefaultToolkit();
    private Cursor noughtCur;
    private Cursor crossCur;

    private Image cross_cur;
    private Image nought_cur;
    private Image cross_im;
    private Image nought_im;

    Map() {
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                update(e);
            }
        });
        try {
            cross_cur = ImageIO.read(Map.class.getResourceAsStream("crossS.png"));
            nought_cur = ImageIO.read(Map.class.getResourceAsStream("noughtS.png"));
            cross_im = ImageIO.read(Map.class.getResourceAsStream("crossB.png"));
            nought_im = ImageIO.read(Map.class.getResourceAsStream("noughtB.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        noughtCur = toolkit.createCustomCursor(nought_cur, new Point(0, 0), "NOUGHT_CUR");
        crossCur = toolkit.createCustomCursor(cross_cur, new Point(0, 0), "CROSS_CUR");
    }

    private void update(MouseEvent e) {
        if (game_over || !initialized) return;
        int cell_x = e.getX() / cell_width;            //get selected sell coords
        int cell_y = e.getY() / cell_height;

        if (!isValidCell(cell_x, cell_y) || !isEmptyCell(cell_x, cell_y)) return;

        field[cell_y][cell_x] = currentTurn;
        repaint();

        if (checkWin(currentTurn)) {
            game_over_state = (currentTurn == PLAYER1_DOT) ? PLAYER1_WIN : PLAYER2_WIN;
            game_over = true;
            return;
        }
        if (isMapFull()) {
            game_over_state = DRAW;
            game_over = true;
            return;
        }

        switch (currentTurn) {
            case PLAYER1_DOT:
                this.setCursor(noughtCur);
                currentTurn = PLAYER2_DOT;
                break;
            case PLAYER2_DOT:
                this.setCursor(crossCur);
                currentTurn = PLAYER1_DOT;
                break;
        }


        if (gameMode == GAME_MODE_HUMAN_VS_HUMAN) return;

        aiTurn();

        repaint();
        if (checkWin(PLAYER2_DOT)) {
            game_over_state = PLAYER2_WIN;
            game_over = true;
            return;
        }
        if (isMapFull()) {
            game_over_state = DRAW;
            game_over = true;
        }
        this.setCursor(crossCur);
        currentTurn = PLAYER1_DOT;

    }

    void startNewGame(int mode, int field_size_x, int field_size_y, int win_len) {
        this.field_size_x = field_size_x;
        this.field_size_y = field_size_y;
        this.win_len = win_len;
        int panel_width = getWidth();
        int panel_height = getHeight();
        this.cell_width = panel_width / field_size_x;
        this.cell_height = panel_height / field_size_y;
        field = new int[field_size_y][field_size_x];
        game_over = false;
        initialized = true;
        this.gameMode = mode;
        //this.setCursor(crossCur);
        this.setCursor(crossCur);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    private void render(Graphics g) {
        if (!initialized) return;

        for (int i = 0; i <= field_size_y; i++) {
            int y = i * cell_height;
            g.drawLine(0, y, getHeight(), y);
        }
        for (int i = 0; i <= field_size_x; i++) {
            int x = i * cell_width;
            g.drawLine(x, 0, x, getWidth());
        }
        for (int i = 0; i < field_size_y; i++) {
            for (int j = 0; j < field_size_x; j++) {
                if (isEmptyCell(j, i)) continue;
                if (field[i][j] == PLAYER1_DOT)
                    g.drawImage(cross_im, j * cell_height, i * cell_width, cell_height, cell_width, null);
                    //  g.setColor(Color.BLUE);
                else if (field[i][j] == PLAYER2_DOT)
                    g.drawImage(nought_im, j * cell_height, i * cell_width, cell_height, cell_width, null);
                
                // g.setColor(Color.RED);

                // g.fillOval(j * cell_width + DOTS_MARGIN, i * cell_height + DOTS_MARGIN,
                // cell_width - 2 * DOTS_MARGIN, cell_height - 2 * DOTS_MARGIN);
            }
        }
        if (game_over) showGameOver(g);
    }

    private void showGameOver(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 200, getWidth(), 70);
        g.setColor(Color.YELLOW);
        g.setFont(font);
        switch (game_over_state) {
            case DRAW:
                g.drawString(DRAW_MSG, 180, getHeight() / 2);
                break;
            case PLAYER1_WIN:
                if (gameMode == GAME_MODE_HUMAN_VS_AI) g.drawString(HUMAN_MSG, 110, getHeight() / 2);
                else if (gameMode == GAME_MODE_HUMAN_VS_HUMAN) g.drawString(PLAYER1_WIN_MSG, 20, getHeight() / 2);
                break;
            case PLAYER2_WIN:
                if (gameMode == GAME_MODE_HUMAN_VS_AI) g.drawString(AI_MSG, 20, getHeight() / 2);
                else if (gameMode == GAME_MODE_HUMAN_VS_HUMAN) g.drawString(PLAYER2_WIN_MSG, 20, getHeight() / 2);
                break;
            default:
                throw new RuntimeException("Неизвестный game_over_state = " + game_over_state);
        }
    }

    private void aiTurn() {
        if (turnAIWinCell()) return;
        if (turnHumanWinCell()) return;
        int x, y;
        do {
            x = rnd.nextInt(field_size_x);
            y = rnd.nextInt(field_size_y);
        } while (!isEmptyCell(x, y));
        field[y][x] = PLAYER2_DOT;
    }

    private boolean turnAIWinCell() {
        for (int i = 0; i < field_size_y; i++) {
            for (int j = 0; j < field_size_x; j++) {
                if (isEmptyCell(j, i)) {
                    field[i][j] = PLAYER2_DOT;
                    if (checkWin(PLAYER2_DOT)) return true;
                    field[i][j] = EMPTY_DOT;
                }
            }
        }
        return false;
    }

    private boolean turnHumanWinCell() {
        for (int i = 0; i < field_size_y; i++) {
            for (int j = 0; j < field_size_x; j++) {
                if (isEmptyCell(j, i)) {
                    field[i][j] = PLAYER1_DOT;
                    if (checkWin(PLAYER1_DOT)) {
                        field[i][j] = PLAYER2_DOT;
                        return true;
                    }
                    field[i][j] = EMPTY_DOT;
                }
            }
        }
        return false;
    }

    private boolean checkWin(int dot) {
        for (int i = 0; i < field_size_x; i++) {
            for (int j = 0; j < field_size_y; j++) {
                if (checkLine(i, j, 1, 0, win_len, dot)) return true;
                if (checkLine(i, j, 1, 1, win_len, dot)) return true;
                if (checkLine(i, j, 0, 1, win_len, dot)) return true;
                if (checkLine(i, j, 1, -1, win_len, dot)) return true;
            }
        }
        return false;
    }

    private boolean checkLine(int x, int y, int vx, int vy, int len, int dot) {
        final int far_x = x + (len - 1) * vx;
        final int far_y = y + (len - 1) * vy;
        if (!isValidCell(far_x, far_y)) return false;
        for (int i = 0; i < len; i++) {
            if (field[y + i * vy][x + i * vx] != dot) return false;
        }
        return true;
    }

    private boolean isMapFull() {
        for (int i = 0; i < field_size_y; i++) {
            for (int j = 0; j < field_size_x; j++) {
                if (field[i][j] == EMPTY_DOT) return false;
            }
        }
        return true;
    }

    private boolean isValidCell(int x, int y) {
        return x >= 0 && x < field_size_x && y >= 0 && y < field_size_y;
    }

    private boolean isEmptyCell(int x, int y) {
        return field[y][x] == EMPTY_DOT;
    }
}