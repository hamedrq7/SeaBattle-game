package world.units;
import world.GameConstants;

public class Castle extends Unit{

    public Castle(int id) {
        super(id);
        this.price = GameConstants.castlePrice;
        this.loot = GameConstants.castleLoot;
        this.moveLimit = GameConstants.castleMoveLimit;
        this.hp = GameConstants.castleHp;
        this.range = GameConstants.castleRange;
        this.coolDownTime = GameConstants.castleCoolDownTime;
        this.size = GameConstants.castleSize;
        this.type = "Castle";
        this.symbol = "R";
        this.xSize = GameConstants.castleX;
        this.ySize = GameConstants.castleY;
    }



}

