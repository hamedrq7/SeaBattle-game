package world;

import java.util.ArrayList;
import world.units.*;

public final class GameConstants {

    public static final int nSoldier = 5; //1
    public static final int nCavalry = 3; //1
    public static final int nCastle = 2;
    public static final int nHq = 1;

    public static final int soldierMoveLimit = 3;
    public static final int cavalryMoveLimit = 2;
    public static final int castleMoveLimit = 0;
    public static final int hqMoveLimit = 0;
    /*
        public area soldierArea = new area(1, 1);
        public area cavalryArea = new area(2, 1);
        public area castleArea = new area(2, 2);
        public area hqArea = new area(3, 3);
    */
    public static final int soldierHp = 1;
    public static final int cavalryHp = 2;
    public static final int castleHp = 4;
    public static final int hqHp = 9;

    //to do -> set following attributes in a way that game would be playable:
    public static final int soldierRange = 1;
    public static final int cavalryRange = 2;
    public static final int castleRange = 2;
    public static final int hqRange = 3;

    public static final double soldierLoot = 1/4.0;
    public static final double cavalryLoot = 1/3.0;
    public static final double castleLoot = 1/2.0;
    public static final double hqLoot = 1;

    public static final double soldierPrice = 1/4.0;
    public static final double cavalryPrice = 1/3.0;
    public static final double castlePrice = 2;
    public static final double hqPrice = 3;

    public static final int soldierCoolDownTime = 1;
    public static final int cavalryCoolDownTime = 2;
    public static final int castleCoolDownTime = 3;
    public static final int hqCoolDownTime = 4;

    public static final int soldierSize = 1;
    public static final int cavalrySize = 2;
    public static final int castleSize = 4;
    public static final int hqSize = 9;

    public static final int soldierX = 1;
    public static final int cavalryX = 1;
    public static final int castleX = 2;
    public static final int hqX = 3;
    public static final int soldierY = 1;
    public static final int cavalryY = 2;
    public static final int castleY = 2;
    public static final int hqY = 3;

    public static ArrayList<Unit> allUnits = new ArrayList<Unit>() {
        {
            add(new Soldier(-1));
            add(new Cavalry(-1));
            add(new Castle(-1));
            add(new Hq(-1));
        }
    };

}
