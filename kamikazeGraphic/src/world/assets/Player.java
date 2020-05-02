package world.assets;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.paint.Color;

import world.*;
import world.units.*;

public class Player {

    private boolean turn;
    public boolean getTurn() {return this.turn;}
    public void setTurn(boolean bool) {this.turn = bool;}
    private boolean playerAttacked;
    public void setPlayerAttacked(boolean bool) {this.playerAttacked = bool;}
    private boolean playerMoved;
    public void setPlayerMoved(boolean bool) {this.playerMoved = bool;}
    private boolean playerBought;
    public void setPlayerBought(boolean bool) {this.playerBought = bool;}
    private boolean allUnitsPlaced;

    private Player enemy;
    public void setEnemy(Player enemy) {this.enemy = enemy;}

    private int id;
    private String name;
    private double money;
    private ArrayList<Unit> units = new ArrayList<>();                      //contains current Unit of Player in each turn
    private ArrayList<Cell> myTargetCells = new ArrayList<>();              //contains strikes of Player
    private ArrayList<Cell> enemyTargetCells = new ArrayList<>();           //contains strikes that Player took
    private ArrayList<Cell> myLastTargetCells = new ArrayList<>();
    private ArrayList<Cell> enemyLastTargetCells = new ArrayList<>();
    private int boardSize;
    public int getBoardSize() {return this.boardSize; }

    public ArrayList<Unit> getUnits() { return units; }
    public void addToMyTargetCells(ArrayList<Cell> cells) {this.myTargetCells.addAll(cells);}
    public void addToEnemyTargetCells(ArrayList<Cell> cells) {this.enemyTargetCells.addAll(cells);}
    public void setEnemyLastTargetCells(ArrayList<Cell> cells) {this.enemyLastTargetCells = cells;}

    private Stage playerStage;
    private Scene playerScene;
    private BorderPane playerBoarderPane = new BorderPane();
    private Board playerBoard;
    private Board enemyBoard;

    private Button soldierRemainingButton;
    private Button cavalryRemainingButton;
    private Button castleRemainingButton;
    private Button hqRemainingButton;

    private Button attackButton = new Button("Attack");
    private Button turnButton = new Button("next Turn");
    private Button moveButton = new Button("Move");
    private ArrayList<Button> shopButtons = new ArrayList<>();

    public int remainingSoldier = GameConstants.nSoldier;
    public int remainingCavalry = GameConstants.nCavalry;
    public int remainingCastle = GameConstants.nCastle;
    public int remainingHq = GameConstants.nHq;

    private int selectedAttackingUnitId = -1;
    private int selectedMovingUnitId = -1;
    private Unit selectedPlacingUnit = null;
    private Unit selectedShopUnit = null;

    private ArrayList<ArrayList<Cell>> destinations = new ArrayList<>();
    private ArrayList<Unit> deadUnits = new ArrayList<>();
    public ArrayList<Unit> getDeadUnits() {return this.deadUnits;}
    private Label moneyLabel = new Label("Current money: " + 0);

    public Player(int id, int boardSize) {
        this.id = id;
        this.name = "Player"+this.id;
        this.boardSize = boardSize;
        this.money = 0;

        if(this.id == 1) {
            this.turn = true;
        }
        else {
            this.turn = false;
        }
        this.playerAttacked = false;
        this.playerMoved = false;
        this.playerBought = false;

        initializePlayerBoard();
        initializeEnemyBoard();

        VBox vBox1 = new VBox(10, this.playerBoard, this.enemyBoard);
        vBox1.setAlignment(Pos.CENTER);
        this.playerBoarderPane.setCenter(vBox1);

        this.playerBoarderPane.setTop(getHBoxOfRemainingButton());
        this.playerBoarderPane.setPrefSize(600, 800);



        VBox rightVBox = new VBox(10, this.attackButton, this.moveButton);
        this.playerBoarderPane.setRight(rightVBox);
        this.playerBoarderPane.setLeft(this.turnButton);

        this.playerBoarderPane.setBottom(getHBoxOfShopButtons());
        this.setMainButtonEvents();

        updateShopButtons();
    }

    private void initializePlayerBoard() {
        System.out.println(boardSize);
        this.playerBoard = new Board(boardSize, this, event -> {
            Tile clickedTile = (Tile) event.getSource();
            if(selectedPlacingUnit!=null) {
                int xSize, ySize;
                if(event.getButton() == MouseButton.PRIMARY) {
                    // C  C
                    xSize = selectedPlacingUnit.getXSize();
                    ySize = selectedPlacingUnit.getYSize();
                }
                else {
                    // C
                    // C
                    xSize = selectedPlacingUnit.getYSize();
                    ySize = selectedPlacingUnit.getXSize();
                }
                if(clickedTile.getRow() + xSize-1 < boardSize && clickedTile.getCol() + ySize-1 < boardSize) {
                    //checking for interfere with other units:
                    boolean valid = true;
                    for(int i = clickedTile.getRow(); i < clickedTile.getRow()+xSize; i++) {
                        for(int j = clickedTile.getCol(); j < clickedTile.getCol()+ySize; j++) {
                            for(Unit unit : this.getUnits()) {
                                for(UnitCell cell : unit.getUnitCells()) {
                                    if(cell.getX() == i && cell.getY() == j) {
                                        valid = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if(valid) {
                        //now we add the unit:
                        Unit newUnit = Scan.createUnitByType(selectedPlacingUnit.getType());

                        newUnit.setId(this.getUnits().size());

                        for(int i = clickedTile.getRow(); i < clickedTile.getRow()+xSize; i++) {
                            for(int j = clickedTile.getCol(); j < clickedTile.getCol()+ySize; j++) {
                                Cell newCell = new Cell(i, j);
                                UnitCell newUnitCell = new UnitCell(newCell, newUnit);
                                newUnit.getUnitCells().add(newUnitCell);
                                clickedTile.getBoard().getTileByRowCol(i, j).setFill(Color.BLUE);
                            }
                        }
                        this.getUnits().add(newUnit);
                        //////work with buttons:
                        updateRemainingButtons(newUnit);
                    }
                    else {
                        //nothing
                        System.out.println("Interfere");
                    }
                }
            }
            this.showUnits();
        });
    }
    private void initializeEnemyBoard() {
        this.enemyBoard = new Board(boardSize, this, event -> {
            Tile clickedTile = (Tile) event.getSource();

        });
    }

    private HBox getHBoxOfRemainingButton() {

        ArrayList<Button> remainingButtons = new ArrayList<>();

        soldierRemainingButton = new Button("Soldier - " + remainingSoldier);
        remainingButtons.add(soldierRemainingButton);
        cavalryRemainingButton = new Button("Cavalry - " + remainingCavalry);
        remainingButtons.add(cavalryRemainingButton);
        castleRemainingButton = new Button("Castle - " + remainingCastle);
        remainingButtons.add(castleRemainingButton);
        hqRemainingButton = new Button("Hq - " + remainingHq);
        remainingButtons.add(hqRemainingButton);

        for(Button currButton : remainingButtons) {
            currButton.setOnMouseClicked(mouseEvent -> {
                String type;
                Button button = (Button) mouseEvent.getSource();
                Scanner scan = new Scanner(button.getText());
                type = scan.next();
                scan.close();

                if(enemy.getAllUnitsPlaced() || this.turn) {
                    selectedPlacingUnit = Scan.createUnitByType(type);
                }
                else
                {
                    System.out.println("Not your turn + enemy hasn't placed all units");
                }
            });
        }
        HBox hBox1 = new HBox(10, soldierRemainingButton, cavalryRemainingButton);
        HBox hBox2 = new HBox(10, castleRemainingButton, hqRemainingButton);

        return new HBox(10, hBox1, hBox2);
    }
    private HBox getHBoxOfShopButtons() {
        Button soldierShop = new Button("Soldier - " + GameConstants.soldierPrice);
        Button cavalryShop = new Button("Cavalry - " + GameConstants.cavalryPrice);
        Button castleShop = new Button("Castle - " + GameConstants.castlePrice);
        Button hqShop = new Button("Hq - " + GameConstants.hqPrice);

        this.shopButtons.add(soldierShop);
        this.shopButtons.add(cavalryShop);
        this.shopButtons.add(castleShop);
        this.shopButtons.add(hqShop);

        HBox temp1 = new HBox(10, soldierShop, cavalryShop);
        HBox temp2 = new HBox(10, castleShop, hqShop);
        HBox result = new HBox(10, temp1, temp2);
        result.getChildren().add(this.moneyLabel);
        return result;
    }

    public void updateShopButtons() {

        ///check if there are any castle
        boolean thereIsCastle = false;
        for(Unit unit : this.units) {
            if(unit.getType().equals("Castle")) {
                thereIsCastle = true;
            }
        }
        if(thereIsCastle) {
            for(Button button : this.shopButtons) {
                Scanner scan = new Scanner(button.getText());
                String type = scan.next();
                scan.close();

                if(type.equals("Soldier")) {
                    if(this.money >= GameConstants.soldierPrice) {
                        button.setDisable(false);
                    }
                    else button.setDisable(true);
                }
                else if(type.equals("Cavalry")) {
                    if(this.money >= GameConstants.cavalryPrice) {
                        button.setDisable(false);
                    }
                    else button.setDisable(true);
                }
                else if(type.equals("Castle")) {
                    if(this.money >= GameConstants.castlePrice) {
                        button.setDisable(false);
                    }
                    else button.setDisable(true);
                }
                else if(type.equals("Hq")) {
                    if(this.money >= GameConstants.hqPrice) {
                        button.setDisable(false);
                    }
                    else button.setDisable(true);
                }
                else
                {
                    System.out.println("ERROR in 66.69.55");
                }



            }
        }
        else
        {
            System.out.println("Not Castle ---> no buy");
            for(Button button : this.shopButtons) {
                button.setDisable(true);
            }
        }
        this.moneyLabel.setText("Current Money: " + this.money);
    }
    public boolean getAllUnitsPlaced() {
        return (this.remainingSoldier < 1 && this.remainingCavalry < 1 && this.remainingCastle < 1 && this.remainingHq < 1);
    }
    public void updateRemainingButtons(Unit newAddedUnit) {
        switch (newAddedUnit.getType()) {
            case "Soldier":
                this.remainingSoldier--;
                this.soldierRemainingButton.setText("Soldier - " + remainingSoldier);
                if (this.remainingSoldier <= 0) {
                    soldierRemainingButton.setDisable(true);
                    selectedPlacingUnit = null;
                }
                break;
            case "Cavalry":
                this.remainingCavalry--;
                this.cavalryRemainingButton.setText("Cavalry - " + this.remainingCavalry);
                if (this.remainingCavalry <= 0) {
                    cavalryRemainingButton.setDisable(true);
                    selectedPlacingUnit = null;
                }
                break;
            case "Castle":
                this.remainingCastle--;
                this.castleRemainingButton.setText("Castle - " + this.remainingCastle);
                if (this.remainingCastle <= 0) {
                    castleRemainingButton.setDisable(true);
                    selectedPlacingUnit = null;
                }
                break;
            case "Hq":
                this.remainingHq--;
                this.hqRemainingButton.setText("Hq - " + this.remainingHq);
                if (this.remainingHq <= 0) {
                    hqRemainingButton.setDisable(true);
                    selectedPlacingUnit = null;
                }
                break;
            default:
                System.out.println("ERROR in 1.69");
                break;
        }
    }

    private void setMainButtonEvents() {

        // nextTurn Button:
        this.turnButton.setOnMouseClicked(event -> {
            if(this.turn)
            {
                //**
                clearDestinations();

                if(this.getAllUnitsPlaced()) {

                    this.turn = false;
                    this.playerAttacked = true;
                    this.playerMoved = true;
                    this.playerBought = true;

                    for(Button button : this.shopButtons) {
                        button.setDisable(true);
                    }

                    //set this player main buttons setDisable to true
                    this.turnButton.setDisable(true);
                    this.attackButton.setDisable(true);
                    this.moveButton.setDisable(true);

                    //removing all events for this player:
                    this.playerBoard.removeEventsOfTiles();
                    this.enemyBoard.removeEventsOfTiles();

                    //updating coolDowns for currentPlayer
                    this.updateCoolDown();

                    /////checking for end of the game:
                    if(enemy.unitIsAlive()) {
                        enemy.updateShopButtons();

                        enemy.setTurn(true);
                        enemy.setPlayerAttacked(false);
                        enemy.setPlayerMoved(false);
                        enemy.setPlayerBought(false);

                        ////set enemy main buttons setDisable to false
                        enemy.turnButton.setDisable(false);
                        enemy.attackButton.setDisable(false);
                        enemy.moveButton.setDisable(false);

                        System.out.println("Its Player" + this.enemy.id + " turn...");
                    }
                    else
                    {
                        displayResult();
                    }
                }
                else
                {
                    System.out.println("There is unit remaining to put");
                }
            }
            else
            {
                System.out.println("Not player turn, clicking nextTurn button");
            }
        });

        // Attack Button
        this.attackButton.setOnMouseClicked(event -> {

            this.playerBoard.removeEventsOfTiles();
            this.enemyBoard.removeEventsOfTiles();
            clearDestinations();
            this.playerBoard.setEventOnTiles(playerBoardEvent -> {
                if(this.turn)
                {
                    ///****

                    if(this.getAllUnitsPlaced() && enemy.getAllUnitsPlaced()) {
                        if(!playerAttacked) {
                            //not sure:
                            this.selectedAttackingUnitId = -1;

                            Tile clickedTile = (Tile) playerBoardEvent.getSource();
                            int x = clickedTile.getRow(), y = clickedTile.getCol();

                            for(Unit unit : this.units) {
                                for(UnitCell cell : unit.getUnitCells()) {
                                    if(cell.getX() == x && cell.getY() == y) {
                                        if(!unit.isInCoolDown()) {
                                            this.selectedAttackingUnitId = unit.getId();
                                            System.out.println("Current attacking unit id: " + this.selectedAttackingUnitId);
                                        }
                                        else {
                                            System.out.println("unit is in cool down, RemainingTurn-> " + unit.getRemainingCoolDownTime());
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            System.out.println("You have already attacked in this turn");
                        }
                    }
                }
                else
                {
                    System.out.println("(you cant choose selectedAttackingUnit) Not your turn, clicking player board tiles");
                }
            });

            this.enemyBoard.setEventOnTiles(enemyBoardEvent -> {
                if(this.selectedAttackingUnitId == -1) {
                    System.out.println("No valid unit selected");
                }
                else
                {
                    if((!playerAttacked) && this.turn) {
                        Tile clickedTile = (Tile) enemyBoardEvent.getSource();
                        int x = clickedTile.getRow(), y = clickedTile.getCol();
                        int range = this.getUnitById(this.selectedAttackingUnitId).getRange();

                        if ((x + range - 1 < this.boardSize) && (y + range - 1 < this.boardSize)) {
                            //now attack is  valid!!

                            //***
                            this.playerAttacked = true;

                            //disabling attack button and removing events of both boards:
                            this.attackButton.setDisable(true);
                            this.playerBoard.removeEventsOfTiles();
                            this.enemyBoard.removeEventsOfTiles();


                            //now collecting cells:
                            ArrayList<Cell> selectedTargetCells = new ArrayList<>();
                            for(int i = x; i < x+range; i++) {
                                for(int j = y; j < y+range; j++) {
                                    //(i, j) is target
                                    Cell targetCell = new Cell(i, j);
                                    selectedTargetCells.add(targetCell);
                                }
                            }


                            //setting unit to coolDown:
                            this.getUnitById(this.selectedAttackingUnitId).setToCoolDown();


                            //////you have to send the targetCells to enemy, and then we know if target hit any units or not , by using the collision boolean in cell (selectedTargetCells)
                            //note that we dont remove units from enemy now, we remove them after we mapped deadUnits in playerBoard
                            ArrayList<Unit> killedUnits = enemy.getAttack(selectedTargetCells);


                            //now we paint older targets if they are not "A HIT"
                            if(!this.myLastTargetCells.isEmpty()) {
                                for(Cell cell : myLastTargetCells) {

                                    //we wont change color of dead units
                                    if(this.enemyBoard.getTileByRowCol(cell.getX(), cell.getY()).getFill() != Color.DARKGRAY) {
                                        if(!cell.getCollision()) {
                                            //this cell is an older target that was not a hit
                                            if(this.enemyBoard.getTileByRowCol(cell.getX(), cell.getY()).getFill() != Color.RED)
                                            {
                                                this.enemyBoard.getTileByRowCol(cell.getX(), cell.getY()).setFill(Color.YELLOW);
                                            }
                                        }
                                        else
                                        {
                                            //we keep the color of that red, because that hit point wouldn't change (Enemy cant move it and if its dead , Enemy cant move other units in that spot)
                                        }
                                    }
                                    else
                                    {
                                        //we keep the color of dead units
                                    }
                                }
                            }

                            this.myLastTargetCells = selectedTargetCells;
                            this.addToMyTargetCells(selectedTargetCells);

                            //now we paint new Targets:
                            for(Cell cell : this.myLastTargetCells) {

                                //if there is a dead unit there dont paint
                                if(this.enemyBoard.getTileByRowCol(cell.getX(), cell.getY()).getFill() != Color.DARKGRAY) {
                                    if(cell.getCollision()) {
                                        this.enemyBoard.getTileByRowCol(cell.getX(), cell.getY()).setFill(Color.RED);
                                    }
                                    else
                                    {
                                        if(this.enemyBoard.getTileByRowCol(cell.getX(), cell.getY()).getFill() != Color.RED)
                                        {
                                            this.enemyBoard.getTileByRowCol(cell.getX(), cell.getY()).setFill(Color.BLACK);

                                        }
                                    }
                                }
                            }

                            //now map killing units:
                            for(Unit killedUnit : killedUnits) {
                                System.out.println("WE KILLED " + killedUnit.getType() + killedUnit.getId() + " FROM ENEMY");
                                for(UnitCell cell : killedUnit.getUnitCells()) {
                                    this.enemyBoard.getTileByRowCol(cell.getX(), cell.getY()).setFill(Color.DARKGRAY);
                                }
                            }

                            //adding money of enemy dead units:
                            for(Unit killedUnit : killedUnits) {
                                this.money += killedUnit.getLoot();
                            }


                            //now we remove deadUnits from enemy:
                            enemy.removeDeadUnits();


                            //now update shop buttons:
                            this.updateShopButtons();

                        }
                        else
                        {
                            System.out.println("Your targets are not in range");
                        }
                    }
                    else
                    {
                        if(playerAttacked) {
                            System.out.println("you already attacked66666");
                        }
                        else
                        {
                            System.out.println("Not your turn66666");
                        }
                    }
                }
            });

        });

        // Move button
        this.moveButton.setOnMouseClicked(event -> {

            this.playerBoard.removeEventsOfTiles();
            this.enemyBoard.removeEventsOfTiles();

            clearDestinations();

            //player board:
            this.playerBoard.setEventOnTiles(playerBoardEvent -> {
                if(this.turn) {
                    if(!this.playerMoved) {
                        if(this.selectedMovingUnitId == -1) {
                            //sadra
                            //it means no unit has selected for move:
                            //so we have to set playerBoard tiles on an event to choose a unit for move
                            Tile clickedTile = (Tile) playerBoardEvent.getSource();
                            for(Unit unit : this.units) {
                                for(UnitCell cell : unit.getUnitCells()) {
                                    if(cell.getX() == clickedTile.getRow() && cell.getY() == clickedTile.getCol()) {
                                        if(!unit.isHitted()) {
                                            this.selectedMovingUnitId = unit.getId();

                                            //add to destination array list and paint destinations
                                            this.destinations = this.getUnitById(this.selectedMovingUnitId).getDestinations(this);

                                            for(ArrayList<Cell> list : this.destinations) {
                                                for(Cell cell2 : list) {
                                                    boolean isInUnit = false;
                                                    for(UnitCell unitCell : getUnitById(this.selectedMovingUnitId).getUnitCells()) {
                                                        if(unitCell.getX() == cell2.getX() && unitCell.getY() == cell2.getY()) {
                                                            isInUnit = true;
                                                        }
                                                    }

                                                    if(!isInUnit) {
                                                        //now we paint it
                                                        this.playerBoard.getTileByRowCol(cell2.getX(), cell2.getY()).setFill(Color.DARKGREEN);
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            System.out.println("This unit has got hit before, you cant move it anymore");
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            Tile clickedTile = (Tile) playerBoardEvent.getSource();
                            //it means a unit has already selected, now destinations:

                            //  five possible thing user can do :
                            //  1- selecting a blank tile --> we assume this act means canceling the move and we have to clear extra paint
                            //  2- selecting other unit than the selectedMovingUnit --> it means we set the chosen unit to selectedMovingUnit
                            //  3- selecting the current selectedMovingUnit again --> we assume this act means canceling the move and we have to clear extra paint
                            //  4- clicking the nextTurn or attackButton or shopButton --> we assume this act means canceling the move and we have to clear extra paint
                            //  5- clicking the destination --> we make the move
                            //  -so we have to make a function for clearing extra paint, probably you have to make a "ArrayList<Cell> destinations" for this.player

                            boolean validDest = false;
                            for(ArrayList<Cell> list : this.destinations) {
                                for(Cell cellDest : list) {
                                    if(cellDest.getX() == clickedTile.getRow() && cellDest.getY() == clickedTile.getCol()) {
                                        validDest = true;
                                    }
                                }
                            }

                            if(validDest) {
                                //we make the move

                                //**
                                this.playerMoved = true;

                                for(ArrayList<Cell> list : this.destinations) {
                                    for(Cell cellDest : list) {
                                        if(cellDest.getX() == clickedTile.getRow() && cellDest.getY() == clickedTile.getCol()) {

                                            //finally a fucking move:
                                            this.moveButton.setDisable(true);
                                            // ArrayList "list" is the new destination for the whole unit

                                            //getUnitById(selectedMovingUnitId)
                                            ArrayList<UnitCell> newLocation = new ArrayList<>();
                                            for(Cell newCell : list) {
                                                newLocation.add(new UnitCell(newCell, getUnitById(selectedMovingUnitId)));
                                            }

                                            //deleting current location from board
                                            for(UnitCell selectedUnitCells : getUnitById(selectedMovingUnitId).getUnitCells()) {
                                                this.playerBoard.getTileByRowCol(selectedUnitCells.getX(), selectedUnitCells.getY()).setFill(Color.LIGHTGRAY);
                                            }

                                            getUnitById(selectedMovingUnitId).Move(newLocation);

                                            //painting new Location
                                            for(UnitCell selectedUnitCells : getUnitById(selectedMovingUnitId).getUnitCells()) {
                                                this.playerBoard.getTileByRowCol(selectedUnitCells.getX(), selectedUnitCells.getY()).setFill(Color.BLUE);
                                            }

                                            System.out.println("Moved unit: " + getUnitById(selectedMovingUnitId).getType() + getUnitById(selectedMovingUnitId).getId());
                                            System.out.println("New Location: ");
                                            getUnitById(selectedMovingUnitId).show();
                                            System.out.println();
                                        }
                                    }
                                }


                                clearDestinations();
                                this.selectedMovingUnitId = -1;
                            }
                            else
                            {
                                clearDestinations();
                                boolean newSelection = false;
                                for(Unit unit : this.units) {
                                    if(unit.equals(getUnitById(this.selectedMovingUnitId)) && getUnitById(this.selectedMovingUnitId).getMoveLimit() <= 1) continue;
                                    for(UnitCell cell : unit.getUnitCells()) {
                                        if(cell.getX() == clickedTile.getRow() && cell.getY() == clickedTile.getCol()) {
                                            newSelection = true;
                                        }
                                    }
                                }
                                if(newSelection) {
                                    for(Unit unit : this.units) {
                                        for(UnitCell cell : unit.getUnitCells()) {
                                            if(cell.getX() == clickedTile.getRow() && cell.getY() == clickedTile.getCol()) {
                                                if(!unit.isHitted()) {
                                                    this.selectedMovingUnitId = unit.getId();

                                                    //add to destination array list and paint destinations
                                                    this.destinations = this.getUnitById(this.selectedMovingUnitId).getDestinations(this);

                                                    for(ArrayList<Cell> list : this.destinations) {
                                                        for(Cell cell2 : list) {
                                                            boolean isInUnit = false;
                                                            for(UnitCell unitCell : getUnitById(this.selectedMovingUnitId).getUnitCells()) {
                                                                if(unitCell.getX() == cell2.getX() && unitCell.getY() == cell2.getY()) {
                                                                    isInUnit = true;
                                                                }
                                                            }

                                                            if(!isInUnit) {
                                                                //now we paint it
                                                                this.playerBoard.getTileByRowCol(cell2.getX(), cell2.getY()).setFill(Color.DARKGREEN);
                                                            }
                                                        }
                                                    }

                                                }
                                                else
                                                {
                                                    System.out.println("This unit has got hit before, you cant move it anymore");
                                                }
                                            }
                                        }
                                    }
                                }
                            }


                        }
                    }
                    else {
                        System.out.println("You have already moved unit in this turn");
                    }
                }
                else {
                    System.out.println("Not your turn(In Move)56");
                }
            });


        });

        //shop buttons:
        for(Button button : this.shopButtons) {
            button.setOnMouseClicked(event -> {
                //if its able, then you can place
                if(this.turn) {
                    System.out.println("Button clicked");
                    String type;
                    Scanner scan = new Scanner(button.getText());
                    type = scan.next();
                    scan.close();

                    this.selectedShopUnit = Scan.createUnitByType(type);

                    this.playerBoard.setEventOnTiles(shopEvent -> {
                        Tile clickedTile = (Tile) shopEvent.getSource();
                        if(selectedShopUnit!=null) {
                            int xSize, ySize;
                            if(shopEvent.getButton() == MouseButton.PRIMARY) {
                                // C  C
                                xSize = selectedShopUnit.getXSize();
                                ySize = selectedShopUnit.getYSize();
                            }
                            else {
                                // C
                                // C
                                xSize = selectedShopUnit.getYSize();
                                ySize = selectedShopUnit.getXSize();
                            }
                            if(clickedTile.getRow() + xSize-1 < boardSize && clickedTile.getCol() + ySize-1 < boardSize) {
                                //checking for interfere with other units:
                                boolean valid = true;
                                for(int i = clickedTile.getRow(); i < clickedTile.getRow()+xSize; i++) {
                                    for(int j = clickedTile.getCol(); j < clickedTile.getCol()+ySize; j++) {
                                        for(Unit unit : this.getUnits()) {
                                            for(UnitCell cell : unit.getUnitCells()) {
                                                if(cell.getX() == i && cell.getY() == j) {
                                                    valid = false;
                                                    break;
                                                }
                                            }
                                        }
                                        //also check for not placing in dead unit:
                                        for(Unit deadUnit : this.deadUnits) {
                                            for(UnitCell deadCell : deadUnit.getUnitCells()) {
                                                if(deadCell.getX() == i && deadCell.getY() == j) {
                                                    valid = false;
                                                }
                                            }
                                        }
                                    }
                                }
                                if(valid) {
                                    //now we add the unit:
                                    Unit newUnit = Scan.createUnitByType(selectedShopUnit.getType());

                                    newUnit.setId(this.getUnits().size());

                                    System.out.println("----------------------------------------------");
                                    System.out.println("new unit added: " + newUnit.getType() + newUnit.getId());
                                    System.out.println("----------------------------------------------");

                                    for(int i = clickedTile.getRow(); i < clickedTile.getRow()+xSize; i++) {
                                        for(int j = clickedTile.getCol(); j < clickedTile.getCol()+ySize; j++) {
                                            Cell newCell = new Cell(i, j);
                                            UnitCell newUnitCell = new UnitCell(newCell, newUnit);
                                            newUnit.getUnitCells().add(newUnitCell);
                                            clickedTile.getBoard().getTileByRowCol(i, j).setFill(Color.BLUE);
                                        }
                                    }

                                    //decrease  the money:
                                    this.money -= newUnit.getPrice();

                                    this.getUnits().add(newUnit);


                                    //////work with buttons:
                                    updateShopButtons();
                                }
                                else {
                                    //nothing
                                    System.out.println("Interfere in placing the placing bought unit");
                                }
                            }
                        }
                        this.showUnits();
                    });
                }
            });
        }

    }

    private void clearDestinations() {
        for(ArrayList<Cell> list : this.destinations) {
            for(Cell cell : list) {

                boolean wasTargetBefore = false;
                for(Cell enemyTarget : this.enemyLastTargetCells) {
                    if(cell.getX() == enemyTarget.getX() && cell.getY() == enemyTarget.getY()) {
                        wasTargetBefore = true;
                    }
                }

                boolean isPartOfUnit = false;
                for(UnitCell currCell : getUnitById(this.selectedMovingUnitId).getUnitCells()) {
                    if(currCell.hasEqualCell(cell)) {
                        isPartOfUnit = true;
                    }
                }

                if(!wasTargetBefore && !isPartOfUnit) {
                    this.playerBoard.getTileByRowCol(cell.getX(), cell.getY()).setFill(Color.LIGHTGRAY);
                }
            }
        }
        ////deleting destinations:
        this.destinations = new ArrayList<ArrayList<Cell>>();
    }

    public void display() {
        this.playerScene = new Scene(this.playerBoarderPane);
        this.playerStage = new Stage();

        this.playerStage.setScene(playerScene);
        this.playerStage.setTitle("PLayer" + this.id);
        this.playerStage.show();
    }

    //receive target cells and execute them on thisPlayer
    public ArrayList<Unit> getAttack(ArrayList<Cell> targetCells) {

        for(Cell currTarget : targetCells) {

            boolean hit = false;
            for(Unit currUnit: this.units) {
                //not sure:
                if(!currUnit.unitIsAlive()) {continue;}
                for(UnitCell currCell: currUnit.getUnitCells()) {
                    //not sure:
                    if(!currCell.getUnTouched()) {continue;}
                    if(currCell.hasEqualCell(currTarget)) {
                        //we have a collision
                        //stuff about UnitCell:
                        currCell.setUnTouched(false);

                        currTarget.setCollision(true);

                        hit = true;
                    }
                }
            }

            if(hit) {
                //we will make playerBoard red if it got hit:
                this.playerBoard.getTileByRowCol(currTarget.getX(), currTarget.getY()).setFill(Color.RED);
            }
            else
            {
                //if it used to be hit we wont change the color
                if(this.playerBoard.getTileByRowCol(currTarget.getX(), currTarget.getY()).getFill() != Color.RED)
                {
                    //just black, we will also delete the color in next attack:
                    this.playerBoard.getTileByRowCol(currTarget.getX(), currTarget.getY()).setFill(Color.BLACK);
                }
            }

        }

        //we will remove color of old targets, if they were not "a hit"(if they are not RED)
        if(!this.enemyLastTargetCells.isEmpty()) {
            for(Cell cell : this.enemyLastTargetCells) {
                //if tile is black:
                if(this.playerBoard.getTileByRowCol(cell.getX(), cell.getY()).getFill() == Color.BLACK) {
                    //if it doesnt exist in new targets we removae it:
                    boolean bool = true;
                    for(Cell newTarget : targetCells) {
                        if(newTarget.getX() == cell.getX() && newTarget.getY() == cell.getY()) {
                            bool = false;
                        }
                    }

                    if(bool) {
                        this.playerBoard.getTileByRowCol(cell.getX(), cell.getY()).setFill(Color.LIGHTGRAY);
                    }
                }
            }
        }

        this.setEnemyLastTargetCells(targetCells);
        this.addToEnemyTargetCells(targetCells);

        //now Checking for dead players:
        ArrayList<Unit> deadUnits = new ArrayList<>();

        for(Unit currUnit: this.units) {
            if(!currUnit.unitIsAlive()) {
                System.out.println("Catch dead in enemyClass: " + currUnit.getType());
                deadUnits.add(currUnit);
            }
        }

        return deadUnits;

    }

    public void removeDeadUnits() {
        ArrayList<Unit> toRemove = new ArrayList<>();

        for(Unit currUnit: this.units) {
            if(!currUnit.unitIsAlive()) {
                this.deadUnits.add(currUnit);
                toRemove.add(currUnit);
            }
        }

        //removing:
        for(Unit unit : toRemove) {
            this.units.remove(unit);
        }
    }

    //updateCoolDow:
    public void updateCoolDown() {
        for(Unit currUnit : this.units) {
            currUnit.updateCoolDown();
        }
    }

    public void showUnits() {
        System.out.println("Player " + this.id + ": ");

        for(Unit a : this.units) a.show();

        System.out.println("---------------------------------");
    }

    public Unit getUnitById(int id) {
        for(Unit unit : this.units) {
            if(unit.getId() == id) {
                return unit;
            }
        }
        System.out.println("ERROR in getUnitById in class Player");
        return null;
    }

    public boolean unitIsAlive() {
        boolean result = false;
        for(Unit unit : this.units) {
            if(unit.unitIsAlive()) {
                result = true;
            }
        }
        return result;
    }
    public void displayResult() {
        Stage resultStage = new Stage();
        Scene resultScene = new Scene(new Label("Player" + this.id + " Won!"));
        resultStage.setScene(resultScene);
        resultStage.setTitle("Game is Over");
        resultStage.setHeight(450);
        resultStage.setWidth(450);

        resultStage.show();
    }
}
