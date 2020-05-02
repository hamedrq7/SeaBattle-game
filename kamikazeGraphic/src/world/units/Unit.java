package world.units;

import world.*;
import world.assets.*;

import java.util.ArrayList;

public abstract class Unit {

    protected int xSize;
    protected int ySize;
    public int getXSize() {return xSize;}
    public int getYSize() {return ySize;}

    protected double price;
    public double getPrice() {return this.price;}
    protected double loot;
    public double getLoot() {
        return loot;
    }
    protected int moveLimit;

    public int getMoveLimit() {
        return moveLimit;
    }

    protected int hp;

    public int getHp() {
        return hp;
    }

    protected int range;

    public int getRange() {
        return range;
    }

    protected int coolDownTime;

    public int getCoolDownTime() {
        return coolDownTime;
    }

    protected int id;

    public int getId() {
        return id;
    }

    protected int size;

    public int getSize() {
        return size;
    }

    protected String symbol;

    public String getSymbol() {
        return symbol;
    }

    protected String type;
    public String getType() {
        return this.type;
    }

    //protected boolean isInCoolDown;
    protected int remainingCoolDownTime;
    public int getRemainingCoolDownTime() {
        return remainingCoolDownTime;
    }

    protected ArrayList<UnitCell> unitCells;
    public ArrayList<UnitCell> getUnitCells() {
        return unitCells;
    }

    //constructor:
    public Unit (int id) {
        this.id = id;
        this.remainingCoolDownTime = 0;
        unitCells = new ArrayList<>();
    }

    public void setId(int id) {this.id = id;}

    //attack:
    public boolean isInCoolDown() {
        if(this.remainingCoolDownTime == 0) {
            return false;
        }
        else if(this.remainingCoolDownTime > 0) {
            return true;
        }
        else {
            System.out.println("ERROR IN \"isInCoolDown function in Unit class\"");
            return true;
        }
    }
    public boolean unitIsAlive() {
        for(UnitCell currUnitCell : this.unitCells) {
            if(currUnitCell.getUnTouched()) {
                return true;
            }
        }
        return false;
    }
    public void setToCoolDown() {
        this.remainingCoolDownTime = this.coolDownTime;
    }
    public ArrayList<Cell> attack(int boardSize) {
        System.out.println(this.type + "||Enter Coordinates you wanna attack...\t\t(" + this.range*this.range +"x cells)");

        return Scan.scanTargetCells(this, boardSize);
    }
    public void updateCoolDown() {
        if(this.remainingCoolDownTime > 0) {
            remainingCoolDownTime--;
        }
    }
    public boolean equals(Unit Unit) {
        return this.id == Unit.id;
    }




    //functions about Move:
    public ArrayList<ArrayList<Cell>> getDestinations(Player player) {
        ArrayList<ArrayList<Cell>> result = new ArrayList<>();

        for(int l = 1; l < this.moveLimit; l++) {
            // up (-l, 0)
            boolean validity = true;
            for(UnitCell thisUnitCell : this.unitCells) {
                for(Unit unit : player.getUnits()) {
                    if(unit.equals(this)) continue;
                    for(UnitCell cell : unit.getUnitCells()) {
                        if(thisUnitCell.getX()-l == cell.getX() && thisUnitCell.getY() == cell.getY()) {
                            validity = false;
                        }
                    }
                }
                if(thisUnitCell.getX()-l < 0 || thisUnitCell.getX()-l >= player.getBoardSize() || thisUnitCell.getY() < 0 || thisUnitCell.getY() >= player.getBoardSize()) {
                    validity = false;
                }
                for(Unit currDeadUnit : player.getDeadUnits()) {
                    for(UnitCell currDeadCell : currDeadUnit.getUnitCells()) {
                        if(thisUnitCell.getX()-l == currDeadCell.getX() && thisUnitCell.getY() == currDeadCell.getY()) {
                            validity = false;
                        }
                    }
                }
            }
            if(validity) {
                ArrayList<Cell> res = new ArrayList<>();
                for(UnitCell cell : this.unitCells) {
                    res.add(new Cell(cell.getX()-l, cell.getY()));
                }
                result.add(res);
            }

            validity = true;
            //down(+l, 0)
            for(UnitCell thisUnitCell : this.unitCells) {
                for(Unit unit : player.getUnits()) {
                    if(unit.equals(this)) continue;
                    for(UnitCell cell : unit.getUnitCells()) {
                        if(thisUnitCell.getX()+l == cell.getX() && thisUnitCell.getY() == cell.getY()) {
                            validity = false;
                        }
                    }
                }
                if(thisUnitCell.getX()+l < 0 || thisUnitCell.getX()+l >= player.getBoardSize() || thisUnitCell.getY() < 0 || thisUnitCell.getY() >= player.getBoardSize()) {
                    validity = false;
                }
                for(Unit currDeadUnit : player.getDeadUnits()) {
                    for(UnitCell currDeadCell : currDeadUnit.getUnitCells()) {
                        if(thisUnitCell.getX()+l == currDeadCell.getX() && thisUnitCell.getY() == currDeadCell.getY()) {
                            validity = false;
                        }
                    }
                }

            }
            if(validity) {
                ArrayList<Cell> res = new ArrayList<>();
                for(UnitCell cell : this.unitCells) {
                    res.add(new Cell(cell.getX()+l, cell.getY()));
                }
                result.add(res);
            }

            validity = true;
            //right (0, +l)
            for(UnitCell thisUnitCell : this.unitCells) {
                for(Unit unit : player.getUnits()) {
                    if(unit.equals(this)) continue;
                    for(UnitCell cell : unit.getUnitCells()) {
                        if(thisUnitCell.getX() == cell.getX() && thisUnitCell.getY()+l == cell.getY()) {
                            validity = false;
                        }
                    }
                }
                if(thisUnitCell.getX() < 0 || thisUnitCell.getX() >= player.getBoardSize() || thisUnitCell.getY()+l < 0 || thisUnitCell.getY()+l >= player.getBoardSize()) {
                    validity = false;
                }
                for(Unit currDeadUnit : player.getDeadUnits()) {
                    for(UnitCell currDeadCell : currDeadUnit.getUnitCells()) {
                        if(thisUnitCell.getX() == currDeadCell.getX() && thisUnitCell.getY()+l == currDeadCell.getY()) {
                            validity = false;
                        }
                    }
                }
            }
            if(validity) {
                ArrayList<Cell> res = new ArrayList<>();
                for(UnitCell cell : this.unitCells) {
                    res.add(new Cell(cell.getX(), cell.getY()+l));
                }
                result.add(res);
            }

            validity = true;
            //left (0, -l)
            for(UnitCell thisUnitCell : this.unitCells) {
                for(Unit unit : player.getUnits()) {
                    if(unit.equals(this)) continue;
                    for(UnitCell cell : unit.getUnitCells()) {
                        if(thisUnitCell.getX() == cell.getX() && thisUnitCell.getY()-l == cell.getY()) {
                            validity = false;
                        }
                    }
                }
                if(thisUnitCell.getX() < 0 || thisUnitCell.getX() >= player.getBoardSize() || thisUnitCell.getY()-l < 0 || thisUnitCell.getY()-l >= player.getBoardSize()) {
                    validity = false;
                }
                for(Unit currDeadUnit : player.getDeadUnits()) {
                    for(UnitCell currDeadCell : currDeadUnit.getUnitCells()) {
                        if(thisUnitCell.getX() == currDeadCell.getX() && thisUnitCell.getY()-l == currDeadCell.getY()) {
                            validity = false;
                        }
                    }
                }
            }
            if(validity) {
                ArrayList<Cell> res = new ArrayList<>();
                for(UnitCell cell : this.unitCells) {
                    res.add(new Cell(cell.getX(), cell.getY()-l));
                }
                result.add(res);
            }
        }
        return result;
    }





    public void showAllPossibleMoves(Player Player) {
        System.out.println("Move List:");
        for(Move currMove : this.getValidMoves(Player)) {
            System.out.println(currMove.getId()+") " + "X += " + currMove.getX_axis_scale() + ", Y += " + currMove.getY_axis_scale());
        }
    }
    public ArrayList<Move> getValidMoves(Player Player) {
        ArrayList<Move> results = new ArrayList<>();

        ArrayList<Move> allMoves = this.getAllPossibleMoves();
        //now we check for validation:
        for(Move currMove : allMoves) {
            if(moveIsValid(currMove, Player)) {
                results.add(currMove);
            }
        }
        return results;
    }
    public ArrayList<Move> getAllPossibleMoves() {
        //making All possible moves for this Unit:
        ArrayList<Move> results = new ArrayList<>();
        int id = 0;
        for(int n = 1; n <= this.moveLimit; n++) {
            results.add(new Move(0, +n, id++));
            results.add(new Move(0, -n, id++));
            results.add(new Move(+n, 0, id++));
            results.add(new Move(-n, 0, id++));
        }
        return results;
    }
    public boolean moveIsValid(Move Move, Player Player) {
        //System.out.println();
        //System.out.println("Checking Move("+Move.getX_axis_scale()+", "+Move.getY_axis_scale()+") for "+this.type+" with id----> " + this.id);

        //checking if Move interferes with the BoardSIZE:
        for(UnitCell curCell : this.unitCells) {
            if(curCell.getX() + Move.getX_axis_scale() < 0 || curCell.getX() + Move.getX_axis_scale() >= Player.getBoardSize()) {
                //System.out.println("Out of X boundary!");
                return false;
            }
            if(curCell.getY() + Move.getY_axis_scale() < 0 || curCell.getY() + Move.getY_axis_scale() >= Player.getBoardSize()) {
                //System.out.println("Out of Y boundary!");
                return false;
            }
        }

        ////checking if Move interferes with the otherUnits:
        //here we want to check like this:
        //if X_axis = 0 and Y_axis = -3 : we want to check (0, -1) and (0, -2) and (0, -3)

        ///if Move includes jump over other units, then you dont have to check (0, -1) and (0, -2) for Move(0, -3)
        for(UnitCell currCell : this.unitCells) {

            for(int n = 1; n <= java.lang.Math.abs(Move.getX_axis_scale() + Move.getY_axis_scale()); n++) {
                int x = n, y = n;

                if(Move.getX_axis_scale()<0) { x = n * -1;}
                else if(Move.getX_axis_scale()==0) { x = 0;}

                if(Move.getY_axis_scale()<0) { y = n * -1;}
                else if(Move.getY_axis_scale()==0) { y = 0;}

                for(Unit currUnit : Player.getUnits()) {
                    if(currUnit.id == this.id) {continue;}

                    for(UnitCell currOtherUnitCell : currUnit.unitCells) {
                        if(currOtherUnitCell.getX() == currCell.getX()+x && currOtherUnitCell.getY() == currCell.getY()+y) {
                            //System.out.println("interfere in (" + currOtherUnitCell.getX() + ", " + currOtherUnitCell.getY() + ")");
                            //System.out.println("\t Move is (" + x + ", " + y + ")");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }



    /*public void Move(Move Move) {
        for(UnitCell currUnitCell : this.unitCells) {
            currUnitCell.getCell().setCoordinates(currUnitCell.getX()+Move.getX_axis_scale(), currUnitCell.getY()+Move.getY_axis_scale());
        }
    }*/

    public void Move(ArrayList<UnitCell> l) {
        this.unitCells = l;
    }


    //checks based on "isHitted" attribute in UnitCell class
    public boolean isHitted() {
        for(UnitCell currUnitCell : this.unitCells) {
            if(!currUnitCell.getUnTouched()) {
                return true;
            }
        }
        return false;
    }






    public void show() {
        System.out.println(this.type + " " + this.id);
        System.out.println("Coordinates: ");
        System.out.print("\t");
        for(UnitCell currCell : this.unitCells) {
            System.out.print("(" + currCell.getX() + ", " + currCell.getY() + ")  ");
        }
        System.out.println();
    }
}


