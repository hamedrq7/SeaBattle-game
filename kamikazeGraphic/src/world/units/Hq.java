package world.units;
import world.GameConstants;

public class Hq extends Unit{

    public Hq(int id) {
        super(id);
        this.price = GameConstants.hqPrice;
        this.loot = GameConstants.hqLoot;
        this.moveLimit = GameConstants.hqMoveLimit;
        this.hp = GameConstants.hqHp;
        this.range = GameConstants.hqRange;
        this.coolDownTime = GameConstants.hqCoolDownTime;
        this.size = GameConstants.hqSize;
        this.type = "Hq";
        this.symbol = "H";

        this.xSize = GameConstants.hqX;
        this.ySize = GameConstants.hqY;
    }



}