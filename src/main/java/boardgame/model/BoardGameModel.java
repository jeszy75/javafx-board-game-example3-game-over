package boardgame.model;

import javafx.beans.property.*;

public class BoardGameModel {

    public static final int BOARD_SIZE = 5;

    private final ReadOnlyObjectWrapper<Square>[][] board;
    private final ReadOnlyIntegerWrapper numberOfCoins;
    private final ReadOnlyBooleanWrapper gameOver;

    public BoardGameModel() {
        board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new ReadOnlyObjectWrapper<>(Square.NONE);
            }
        }
        numberOfCoins = new ReadOnlyIntegerWrapper(0);
        gameOver = new ReadOnlyBooleanWrapper();
        gameOver.bind(numberOfCoins.isEqualTo(BOARD_SIZE * BOARD_SIZE));
    }

    public ReadOnlyObjectProperty<Square> squareProperty(int row, int col) {
        return board[row][col].getReadOnlyProperty();
    }

    public Square getSquare(int row, int col) {
        return board[row][col].get();
    }

    public int getNumberOfCoins() {
        return numberOfCoins.get();
    }

    public ReadOnlyIntegerProperty numberOfCoinsProperty() {
        return numberOfCoins.getReadOnlyProperty();
    }

    public boolean isGameOver() {
        return gameOver.get();
    }

    public ReadOnlyBooleanProperty gameOverProperty() {
        return gameOver.getReadOnlyProperty();
    }

    public void makeMove(int row, int col) {
        int change = 0;
        board[row][col].set(
                switch (board[row][col].get()) {
                    case NONE -> {
                        change = 1;
                        yield Square.HEAD;
                    }
                    case HEAD -> Square.TAIL;
                    case TAIL -> {
                        change = -1;
                        yield Square.NONE;
                    }
                }
        );
        if (change != 0) {
            numberOfCoins.set(numberOfCoins.get() + change);
        }
    }

    public String toString() {
        var sb = new StringBuilder();
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                sb.append(board[i][j].get().ordinal()).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        var model = new BoardGameModel();
        System.out.println(model);
    }

}
