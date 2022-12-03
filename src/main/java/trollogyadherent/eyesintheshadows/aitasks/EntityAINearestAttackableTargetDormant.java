package trollogyadherent.eyesintheshadows.aitasks;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import trollogyadherent.eyesintheshadows.Config;
import trollogyadherent.eyesintheshadows.entity.entities.EntityEyes;

public class EntityAINearestAttackableTargetDormant extends EntityAINearestAttackableTarget {
    EntityCreature entityCreature;

    public EntityAINearestAttackableTargetDormant(EntityCreature entityCreature, int p_i1663_3_, boolean p_i1663_4_) {
        super(entityCreature, EntityEyes.class, p_i1663_3_, p_i1663_4_);
        this.entityCreature = entityCreature;
    }


    @Override
    public boolean shouldExecute() {
        if (true) {
            return super.shouldExecute();
        }
        if (Config.mobssAttackDormantEyes) {
            return super.shouldExecute();
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
        if (true) {
            return super.continueExecuting();
        }
        if (Config.mobssAttackDormantEyes) {
            return super.continueExecuting();
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
