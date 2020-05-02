package world.assets;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.event.Event;

import java.util.ArrayList;

public class Board extends Pane {
    private int size;
    public int getSize() {return this.size;}
    private Player player;
    public Player getPlayer() {return this.player;}
    private ArrayList<Tile> tiles = new ArrayList<>();
    public ArrayList<Tile> getTiles() {return tiles;}

    private VBox rows = new VBox();

    public Board(int size, Player player, EventHandler<? super MouseEvent> handler) {
        this.size = size;
        this.player = player;
        this.makeGrid();
        this.setEventOnTiles(handler);

    }
    private void makeGrid() {
        for(int i = 0; i < size; i++) {
            HBox row = new HBox();
            for(int j = 0; j < size; j++) {
                Tile tile = new Tile(i, j, this);
                this.tiles.add(tile);
                row.getChildren().add(tile);
            }
            this.rows.getChildren().add(row);
        }
        this.getChildren().add(rows);
    }

    public void setEventOnTiles(EventHandler<? super MouseEvent> handler) {
        for(Tile tile : this.tiles) {
            //maybe we need to set conditions for dead tiles
            tile.setOnMouseClicked(handler);
        }
    }

    public void removeEventsOfTiles() {
        for(Tile tile : this.tiles) {
            tile.setOnMouseClicked(event -> {
                System.out.println("NO EVENT");
            });
        }
    }

    public Tile getTileByRowCol(int row, int col) {
        for(Tile tile : this.tiles) {
            if(tile.getRow() == row && tile.getCol() == col) {
                return tile;
            }
        }
        System.out.println("ERROR in function getTileByRowCol in Board class");
        return null;
    }

}
