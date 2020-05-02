package world.assets;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class Tile extends Rectangle {
    private int row, col;
    private Board board;
    public Board getBoard() {return board;}
    public int getRow() {return row;}
    public int getCol() {return col;}
    private int boardSize;

    public Tile(int row, int col, Board board) {
        super((int)(340/board.getSize()), (int)(340/board.getSize()));
        this.boardSize = board.getSize();
        this.board = board;
        this.row = row;
        this.col = col;
        setFill(javafx.scene.paint.Color.LIGHTGRAY);
        setStroke(Color.BLACK);

    }
}
