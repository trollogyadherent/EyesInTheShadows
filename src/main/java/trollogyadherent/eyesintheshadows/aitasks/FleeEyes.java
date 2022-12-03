package trollogyadherent.eyesintheshadows.aitasks;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import trollogyadherent.eyesintheshadows.Config;
import trollogyadherent.eyesintheshadows.entity.entities.EntityEyes;

public class FleeEyes extends EntityAIAvoidEntity {
    EntityCreature entityCreature;

    public FleeEyes(EntityCreature entityCreature, Class p_i1616_2_, float p_i1616_3_, double p_i1616_4_, double p_i1616_6_) {
        super(entityCreature, p_i1616_2_, p_i1616_3_, p_i1616_4_, p_i1616_6_);
        this.entityCreature = entityCreature;
    }

    @Override
    public boolean shouldExecute() {
        if (Config.mobsFleeDormantEyes) {
            return true;
        }
        EntityEyes eyes;
        if (this.entityCreature.getAttackTarget() instanceof EntityEyes) {
            eyes = (EntityEyes) this.entityCreature.getAttackTarget();
        } else {
            return false;
        }
        if (eyes.getBrightness() <= 0) {
            return false;
        }

        return super.shouldExecute();
    }

    @Override
    public boolean continueExecuting() {
        if (Config.mobsFleeDormantEyes) {
            return true;
        }
        EntityEyes eyes;
        if (this.entityCreature.getAttackTarget() instanceof EntityEyes) {
            eyes = (EntityEyes) this.entityCreature.getAttackTarget();
        } else {
            return false;
        }
        if (eyes.getBrightness() <= 0) {
            return false;
        }

        return super.continueExecuting();
    }
}
