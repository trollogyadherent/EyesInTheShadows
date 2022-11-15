package trollogyadherent.eyesintheshadows.aitasks;

import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.passive.EntityWolf;
import trollogyadherent.eyesintheshadows.Config;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;
import trollogyadherent.eyesintheshadows.entity.entities.EntityEyes;

public class TargetTamedWolves extends EntityAINearestAttackableTarget {
    EntityEyes eyes;
    public TargetTamedWolves(EntityEyes eyes, Class p_i1663_2_, int p_i1663_3_, boolean p_i1663_4_) {
        super(eyes, p_i1663_2_, p_i1663_3_, p_i1663_4_);
        this.eyes = eyes;
    }

    @Override
    public boolean shouldExecute()
    {
        if (!Config.eyesAttackTamedWolves || eyes.getBrightness() <= 0) {
            return false;
        }
        boolean superShouldExecute = super.shouldExecute();
        Object entityLivingBase;
        try {
            entityLivingBase = EyesInTheShadows.varInstanceCommon.superTargetEntityField.get(this);
        } catch (IllegalAccessException e) {
            EyesInTheShadows.error("Failed to reflect targetEntity field!");
            e.printStackTrace();
            return false;
        }
        if (!(entityLivingBase instanceof EntityWolf)) {
            return false;
        }
        if (!((EntityWolf)entityLivingBase).isTamed()) {
            return false;
        }
        return superShouldExecute;
    }
}
