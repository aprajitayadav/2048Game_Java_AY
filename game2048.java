package com.codegym.games.game2048;

import com.codegym.engine.cell.*;
import java.util.Arrays;

public class Game2048 extends Game {
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private int score = 0;
    private boolean isGameStopped = false;

    @Override
    public void initialize() {
        // Set the field size to 7 cells x 9 cells
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }

    private void createGame() {
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();

    }

    private void drawScene() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                setCellColoredNumber(i, j, gameField[j][i]);
            }
        }
    }

    private void createNewNumber() {
        int x;
        int y;
        do {
            x = getRandomNumber(SIDE);
            y = getRandomNumber(SIDE);
        } while (gameField[x][y] != 0);
        int chance = getRandomNumber(10);
        if (chance == 9)
            gameField[x][y] = 4;
        else
            gameField[x][y] = 2;
        if (getMaxTileValue() == 2048)
            win();

    }

    private Color getColorByValue(int value) {

        switch (value) {
            case 0:
                return Color.WHITE;
            case 2:
                return Color.BLUE;
            case 4:
                return Color.RED;
            case 8:
                return Color.GREEN;
            case 16:
                return Color.CYAN;
            case 32:
                return Color.GRAY;
            case 64:
                return Color.MAGENTA;
            case 128:
                return Color.ORANGE;
            case 256:
                return Color.PINK;
            case 512:
                return Color.YELLOW;
            case 1024:
                return Color.PURPLE;
            case 2048:
                return Color.BROWN;
            default:
                return Color.WHITE;
        }
    }

    private void setCellColoredNumber(int x, int y, int value) {
        if (value != 0) {
            setCellValueEx(x, y, getColorByValue(value), Integer.toString(value));
        } else {
            setCellValueEx(x, y, getColorByValue(value), "");
        }
    }

    private boolean compressRow(int[] row) {
        int temp = 0;
        int[] rowtemp = row.clone();
        boolean isChanged = false;
        for (int i = 0; i < row.length; i++) {
            for (int j = 0; j < row.length - i - 1; j++) {
                if (row[j] == 0) {
                    temp = row[j];
                    row[j] = row[j + 1];
                    row[j + 1] = temp;
                }
            }
        }
        if (!Arrays.equals(row, rowtemp))
            isChanged = true;
        return isChanged;
    }

    private boolean mergeRow(int[] row) {
        boolean moved = false;
        for (int i = 0; i < row.length - 1; i++)
            if ((row[i] == row[i + 1]) && (row[i] != 0)) {
                score += (row[i] + row[i + 1]);
                setScore(score);
                row[i] = 2 * row[i];
                row[i + 1] = 0;
                moved = true;

            }

        return moved;
    }

    private void moveLeft() {
        boolean compress; // variable to get return from compressRow
        boolean merge; // variable to get return from mergeRow
        boolean compresss; // variable to get return from compressRow
        int move = 0; // to check if compressRow or mergeRow occurs
        for (int i = 0; i < SIDE; i++) {
            compress = compressRow(gameField[i]);
            merge = mergeRow(gameField[i]);
            compresss = compressRow(gameField[i]);
            if (compress || merge || compresss)
                move++;
        }
        if (move != 0) {
            createNewNumber();
        }
    }

    private void moveRight() {
        boolean compressed = false;
        boolean merged = false;
        boolean compressed2 = false;
        int move = 0; // counting integer to check movement on screen
        rotateClockwise(); // As we have already set up the left move, by rotating the array we can
        rotateClockwise(); // implement the "slide-to-the-left" method

        for (int i = 0; i < SIDE; i++) {

            compressed = compressRow(gameField[i]); // makes boolean results true
            merged = mergeRow(gameField[i]);
            compressed2 = compressRow(gameField[i]);

            if (compressed || merged || compressed2)// if either action returned true move + 1
                move++;
        }

        if (move != 0)
            createNewNumber();

        rotateClockwise();
        rotateClockwise();

    }

    private void moveUp() {
        boolean compressed = false;
        boolean merged = false;
        boolean compressed2 = false;
        int move = 0; // counting integer to check movement on screen

        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        for (int i = 0; i < SIDE; i++) {

            compressed = compressRow(gameField[i]); // makes boolean results true
            merged = mergeRow(gameField[i]);
            compressed2 = compressRow(gameField[i]);

            if (compressed || merged || compressed2)// if either action returned true move + 1
                move++;
        }

        if (move != 0)
            createNewNumber();

        rotateClockwise();

    }

    private void moveDown() {
        boolean compressed = false;
        boolean merged = false;
        boolean compressed2 = false;
        int move = 0; // counting integer to check movement on screen
        rotateClockwise();
        for (int i = 0; i < SIDE; i++) {

            compressed = compressRow(gameField[i]); // makes boolean results true
            merged = mergeRow(gameField[i]);
            compressed2 = compressRow(gameField[i]);

            if (compressed || merged || compressed2)// if either action returned true move + 1
                move++;
        }

        if (move != 0)
            createNewNumber();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();

    }

    public void onKeyPress(Key key) {

        if (isGameStopped) {
            if (key == Key.SPACE) {
                restart();
            }
        }

        if (!canUserMove()) {
            gameOver();
            if (key == Key.SPACE) {
                restart();
            }
            return;
        }

        if (!isGameStopped) {
            if (key == Key.LEFT) {
                moveLeft();
                drawScene();
            } else if (key == Key.RIGHT) {
                moveRight();
                drawScene();
            } else if (key == Key.UP) {
                moveUp();
                drawScene();
            } else if (key == Key.DOWN) {
                moveDown();
                drawScene();
            }

        }
    }

    private void rotateClockwise() {

        for (int i = 0; i < SIDE / 2; i++) {
            for (int j = i; j < SIDE - i - 1; j++) {
                // Swap elements of each cycle in clockwise direction
                int temp = gameField[i][j];
                gameField[i][j] = gameField[SIDE - 1 - j][i];
                gameField[SIDE - 1 - j][i] = gameField[SIDE - 1 - i][SIDE - 1 - j];
                gameField[SIDE - 1 - i][SIDE - 1 - j] = gameField[j][SIDE - 1 - i];
                gameField[j][SIDE - 1 - i] = temp;
            }
        }
    }

    private int getMaxTileValue() {
        int max = 0;
        for (int i = 0; i < SIDE; i++)
            for (int j = 0; j < SIDE; j++)
                if (gameField[i][j] > max)
                    max = gameField[i][j];

        return max;
    }

    private boolean canUserMove() {
        for (int r = 0; r < SIDE; r++)
            for (int c = 0; c < SIDE; c++)
                if (gameField[r][c] == 0)
                    return true;

        for (int r = 0; r < SIDE - 1; r++)
            for (int c = 0; c < SIDE; c++)
                if (gameField[r][c] == gameField[r + 1][c])
                    return true;

        for (int r = 0; r < SIDE; r++)
            for (int c = 0; c < SIDE - 1; c++)
                if (gameField[r][c] == gameField[r][c + 1])
                    return true;
        return false;

    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.RED, "You Won", Color.BLACK, 20);
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.RED, "You Lost", Color.BLACK, 20);

    }

    private void restart() {
        isGameStopped = false;
        score = 0;
        setScore(score);
        createGame();
        drawScene();
    }
}
