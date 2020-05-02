package world.assets;

public class Cell {
    private int x;
    private int y;
    private boolean collision;

    public Cell (int x, int y) {
        this.x = x;
        this.y = y;
        this.collision = false;
    }



    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }

    public boolean getCollision() {return this.collision;}

    public void setCollision(boolean bool) {
        this.collision = bool;
    }

    public boolean equals(Cell Cell) {
        return this.x == Cell.getX() && this.y == Cell.getY();
    }

    //Move:
    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
