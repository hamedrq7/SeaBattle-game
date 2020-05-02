package world.assets;

import world.units.*;

public class UnitCell {
    private Cell Cell;
    private String symbol;
    public String getSymbol() {return this.symbol;}
    private boolean unTouched; // true--> this UnitCell have not got hit
    // false--> this UnitCell have got hit

    public void setUnTouched(boolean bool) {
        this.unTouched = bool;
        this.symbol = "D";
    }
    public boolean getUnTouched() {return this.unTouched;}

    public UnitCell(Cell Cell, Unit Unit) {
        this.Cell = Cell;
        this.symbol = Unit.getSymbol();
        this.unTouched = true;
    }

    public Cell getCell() {
        return this.Cell;
    }

    public boolean hasEqualCell(Cell Cell) {
        return this.Cell.equals(Cell);
    }

    public int getX() {return this.Cell.getX();}
    public int getY() {return this.Cell.getY();}


}

