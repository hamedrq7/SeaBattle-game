package world.units;
import world.GameConstants;

public class Soldier extends Unit{
    //constructor:
    public Soldier (int id) {
        super(id);
        this.price = GameConstants.soldierPrice;
        this.loot = GameConstants.soldierLoot;
        this.moveLimit = GameConstants.soldierMoveLimit;
        this.hp = GameConstants.soldierHp;
        this.range = GameConstants.soldierRange;
        this.coolDownTime = GameConstants.soldierCoolDownTime;
        this.size = GameConstants.soldierSize;
        this.type = "Soldier";
        this.symbol = "S";
        this.xSize = GameConstants.soldierX;
        this.ySize = GameConstants.soldierY;
    }


}

