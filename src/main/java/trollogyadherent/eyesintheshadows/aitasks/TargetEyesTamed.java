package trollogyadherent.eyesintheshadows.aitasks;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import trollogyadherent.eyesintheshadows.Config;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;
import trollogyadherent.eyesintheshadows.entity.entities.EntityEyes;

public class TargetEyesTamed extends EntityAITargetTamed {

    public TargetEyesTamed(EntityTameable entityTameable, Class class_, int p_i1666_3_, boolean p_i1666_4_) {
        super(entityTameable, class_, p_i1666_3_, p_i1666_4_);
    }

    @Override
    public boolean shouldExecute()
    {
        if (!Config.wolvesAttackEyes) {
            return false;
        }

        /* super.shouldExecute() must be called at the beginning because it sets the target entity field */
        boolean superShouldExecute = super.shouldExecute();
        EntityLivingBase entityLivingBase;
        try {
            entityLivingBase = (EntityLivingBase) EyesInTheShadows.varInstanceCommon.superTargetEntityField.get(this);
        } catch (IllegalAccessException e) {
            EyesInTheShadows.error("Failed to reflect targetEntity field (TargetEyesTamed)!");
            e.printStackTrace();
            return false;
        }
        if (!(entityLivingBase instanceof EntityEyes)) {
            return false;
        }
        if (((EntityEyes)entityLivingBase).getBrightness() <= 0 && !Config.mobssAttackDormantEyes) {
            return false;
        }
        return superShouldExecute;
    }
}
