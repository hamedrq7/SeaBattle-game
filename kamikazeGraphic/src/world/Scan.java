package world;

import world.units.*;
import world.assets.*;

import java.util.ArrayList;
import java.util.Scanner;

public final class Scan {
    public static Scanner scanner = new Scanner(System.in);
/*
    //about game prep:
    public static Soldier scanSoldier(int index, Player Player) {
        boolean isValid = true;
        ArrayList<Cell> cells;
        Soldier tempSoldier = new Soldier(index);
        do {
            cells = new ArrayList<>();
            for(int j = 0; j < GameConstants.soldierSize; j++) {
                System.out.println("Soldier " + j + "....\t(X Y)");
                cells.add(scanCell(tempSoldier, Player));
                isValid = cellsAreBinding(cells, GameConstants.soldierSize);
            }
        } while(!isValid);

        for(Cell Cell : cells) {
            UnitCell tempUnitCell = new UnitCell(Cell, tempSoldier);
            tempSoldier.getUnitCells().add(tempUnitCell);
        }
        return tempSoldier;
    }
    public static Cavalry scanCavalry(int index, Player Player) {
        boolean isValid = true;
        ArrayList<Cell> cells;
        Cavalry tempCavalry = new Cavalry(index);
        do {
            cells = new ArrayList<>();
            for(int j = 0; j < GameConstants.cavalrySize; j++) {
                System.out.println("Cavalry " + j + "....\t(X Y)");
                cells.add(scanCell(tempCavalry, Player));
                isValid = cellsAreBinding(cells, GameConstants.cavalrySize);
            }
        } while(!isValid);

        for(Cell Cell : cells) {
            UnitCell tempUnitCell = new UnitCell(Cell, tempCavalry);
            tempCavalry.getUnitCells().add(tempUnitCell);
        }
        return tempCavalry;
    }
    public static Castle scanCastle(int index, Player Player) {
        boolean isValid = true;
        ArrayList<Cell> cells;
        Castle tempCastle = new Castle(index);
        do {
            cells = new ArrayList<>();
            for(int j = 0; j < GameConstants.castleSize; j++) {
                System.out.println("Castle " + j + "....\t(X Y)");
                cells.add(scanCell(tempCastle, Player));
                isValid = cellsAreBinding(cells, GameConstants.castleSize);
            }
        } while(!isValid);

        for(Cell Cell : cells) {
            UnitCell tempUnitCell = new UnitCell(Cell, tempCastle);
            tempCastle.getUnitCells().add(tempUnitCell);
        }
        return tempCastle;
    }
    public static Hq scanHq(int index, Player Player) {
        boolean isValid = true;
        ArrayList<Cell> cells;
        Hq tempHq = new Hq(index);
        do {
            cells = new ArrayList<>();
            for(int j = 0; j < GameConstants.hqSize; j++) {
                System.out.println("Headquarter " + j + "....\t(X Y)");
                cells.add(scanCell(tempHq, Player));
                isValid = cellsAreBinding(cells, GameConstants.hqSize);
            }
        } while(!isValid);

        for(Cell Cell : cells) {
            UnitCell tempUnitCell = new UnitCell(Cell, tempHq);
            tempHq.getUnitCells().add(tempUnitCell);
        }
        return tempHq;
    }
*/
    //hamaro yeki kon:
    public static void scanUnit(Unit Unit, Player Player) {
        boolean isValid = true;
        ArrayList<Cell> cells;

        do {
            cells = new ArrayList<>();
            for(int j = 0; j < Unit.getSize(); j++) {
                System.out.println(Unit.getType() + j + "....\t(X Y)");
                cells.add(scanCell(Unit, Player));
                isValid = cellsAreBinding(cells, Unit.getSize());
            }
        } while(!isValid);

        for(Cell Cell : cells) {
            UnitCell tempUnitCell = new UnitCell(Cell, Unit);
            Unit.getUnitCells().add(tempUnitCell);
        }
        //return Unit;
    }



    private static Cell scanCell(Unit tempUnit, Player Player) {
        int x, y;
        boolean flag;
        do {
            flag = true;
            x = scanner.nextInt();
            y = scanner.nextInt();
            scanner.nextLine();
            if(!(x >= 0 && x < Player.getBoardSize() && y >= 0 && y < Player.getBoardSize())) {
                System.out.println("Cell coordinates are out of boundary, enter again");
                flag = false;
            }
            if(isRepetitive(x, y, tempUnit, Player)) {
                System.out.println("This Cell is full!, enter again");
                flag = false;
            }
        } while (!flag);
        return new Cell(x, y);
    }
    private static boolean isRepetitive(int x, int y, Unit tempUnit, Player Player) {
        for(Unit currUnit : Player.getUnits()) {
            for(UnitCell currUnitCell : currUnit.getUnitCells()) {
                if(currUnitCell.hasEqualCell(new Cell(x, y))) {
                    return true;
                }
            }
        }
        for(UnitCell currUnitCell : tempUnit.getUnitCells()) {
            if(currUnitCell.hasEqualCell(new Cell(x, y))) {
                return true;
            }
        }
        return false;
    }

    //about start:
    //functions haye marboot be Scan kardan: (to doooooooooooo : bebin shayad shabih ham peyda shod va toonesti hazf kone)
    public static Unit scanUnitId(ArrayList<Unit> units) {
        System.out.println("Enter id of Unit...");
        //Check if input is valid:
        boolean isValid = false; int id;
        do {
            id = scanner.nextInt();
            scanner.nextLine();
            for(Unit currUnit : units) {
                if (currUnit.getId() == id) {
                    isValid = true;
                    break;
                }
            }
            if(!isValid) { System.out.println("inValid id!"); }
        } while(!isValid);
        return getUnitById(units, id);
    }
    public static ArrayList<Cell> scanTargetCells(Unit Unit, int boardSize) {
        boolean isValid;
        ArrayList<Cell> result;
        do {
            isValid = true;
            result = new ArrayList<>();
            for(int i = 0; i < Unit.getRange() * Unit.getRange(); i++) {
                int x, y;
                do {
                    x = scanner.nextInt();
                    y = scanner.nextInt();
                    scanner.nextLine();
                    isValid = checkCellBoundary(x, y, boardSize);
                    //we dont check if we have stroke this x,y before, because of "Move" feature
                } while(!isValid);
                result.add(new Cell(x, y));
            }
            isValid = cellsAreBinding(result, Unit.getRange());
        } while (!isValid);

        return result;
    }

    //scans about Move:
    public static Move scanMoveId(ArrayList<Move> moves) {
        System.out.println("Enter id of Move...");
        //Check if input is valid:
        boolean isValid = false; int id;
        do {
            id = scanner.nextInt();
            scanner.nextLine();
            for(Move currMove : moves) {
                if (currMove.getId() == id) {
                    return currMove;
                }
            }
            System.out.println("inValid id!  enter again");
        } while(true);
    }

    //functions haye marboot be check kardan: (to doooooooooooo : bebin shayad shabih ham peyda shod va toonesti hazf kone)
    private static boolean checkCellBoundary(int x, int y, int boardSize) {
        if(!(x >= 0 && x < boardSize && y >= 0 && y < boardSize)) {
            System.out.println("Cell coordinates are out of boundary, enter again");
            return false;
        }
        return true;
    }
    private static boolean cellsAreBinding(ArrayList<Cell> cells, int Y_axis) {



        cells.sort(new cellComparator());

        /////////
        return true;
        /////////
/*
        Y_axis = (int) Math.sqrt(Y_axis);
        for(Cell Cell : cells) {
            System.out.print(Cell.getX() + ", " + Cell.getY() + " || ");
        }
        System.out.println();

        for(int i = 0; i < cells.size()-1; i++) {
            if(cells.get(i).getX() == cells.get(i+1).getX()) {
                if(!(cells.get(i).getY() == cells.get(i+1).getY() - 1)) {
                    System.out.println("NOT BINDING!!!(0), enter again...");
                    return false;
                }
            }
            else if(cells.get(i).getY() == cells.get(i+1).getY() + Y_axis-1) {
                if(!(cells.get(i).getX() == cells.get(i+1).getX() - 1)) {
                    System.out.println("NOT BINDING!!!(1), enter again...");
                    return false;
                }
            }
            else {
                for(Cell cell : cells) {
                    System.out.print("-" + cell.getX() + "," + cell.getY() + "-");
                }
                System.out.println();
                System.out.println("NOT BINDING!!!(2), enter again...");
                return false;
            }
        }
        return true;*/
    }


    ///function haye kolli:
    private static Unit getUnitById(Player Player, int id) {
        for(Unit currUnit : Player.getUnits()) {
            if(currUnit.getId() == id) {
                return currUnit;
            }
        }
        System.out.println("inValid id Input! (in function getUnitById!");
        return null;
    }
    private static Unit getUnitById(ArrayList<Unit> units, int id) {
        for(Unit currUnit : units) {
            if(currUnit.getId() == id) {
                return currUnit;
            }
        }
        System.out.println("inValid id Input! (in function getUnitById!");
        return null;
    }
    /*public static int stringUnSimilarity(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        int result = 0;
        if(a.length() > b.length()) {
            String temp = b;
            b = a;
            a = temp;
        }
        for(int i = 0; i < a.length(); i++) {
            if(a.charAt(i) != b.charAt(i)) {
                result++;
            }
        }
        result += (b.length() - a.length());

        return result;
    }*/


    //////create a new unit by the name with id -1
    public static Unit createUnitByType(String type) {
        if(type.equals("Soldier")) {
            Soldier result = new Soldier(-1);
            return result;
        }
        else if(type.equals("Cavalry")) {
            Cavalry result = new Cavalry(-1);
            return result;
        }
        else if(type.equals("Castle")) {
            Castle result = new Castle(-1);
            return result;
        }
        else if(type.equals("Hq")) {
            Hq result = new Hq(-1);
            return result;
        }
        System.out.println("Error in getUnitByType in scan Class");
        return null;
    }
}
