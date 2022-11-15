package trollogyadherent.eyesintheshadows.aitasks;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import trollogyadherent.eyesintheshadows.entity.entities.EntityEyes;

public class CreepTowardPlayer extends EntityAIAttackOnCollide {
    private final EntityEyes eyes;

    public CreepTowardPlayer(EntityCreature creature, double p_i1636_2_, boolean p_i1636_4_) {
        super(creature, p_i1636_2_, p_i1636_4_);
        eyes = (EntityEyes) creature;
    }

    @Override
    public boolean continueExecuting()
    {
        if (eyes.isPlayerLookingInMyGeneralDirection() || eyes.getBrightness() <= 0) {
            return false;
        }
        return super.continueExecuting();
    }

    @Override
    public boolean shouldExecute()
    {
        if (eyes.isPlayerLookingInMyGeneralDirection() || eyes.getBrightness() <= 0) {
            return false;
        }
        return super.shouldExecute();
    }
}