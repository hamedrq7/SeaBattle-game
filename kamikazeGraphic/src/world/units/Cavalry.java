package world.units;
import world.GameConstants;

public class Cavalry extends Unit{

    public Cavalry(int id) {
        super(id);
        this.price = GameConstants.cavalryPrice;
        this.loot = GameConstants.cavalryLoot;
        this.moveLimit = GameConstants.cavalryMoveLimit;
        this.hp = GameConstants.cavalryHp;
        this.range = GameConstants.cavalryRange;
        this.coolDownTime = GameConstants.cavalryCoolDownTime;
        this.size = GameConstants.cavalrySize;
        this.type = "Cavalry";
        this.symbol = "C";
        this.xSize = GameConstants.cavalryX;
        this.ySize = GameConstants.cavalryY;
    }



}
