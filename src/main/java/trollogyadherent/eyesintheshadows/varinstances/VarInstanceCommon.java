package trollogyadherent.eyesintheshadows.varinstances;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import trollogyadherent.eyesintheshadows.Tags;

import java.lang.reflect.Field;

public class VarInstanceCommon {
    public String disappearSound = Tags.MODID + ":" + "mob.eyes.disappear";
    public String laughSound = Tags.MODID + ":" + "mob.eyes.laugh";
    public String jumpScareSound = Tags.MODID + ":" + "mob.eyes.jumpscare";
    public Field superTargetEntityField = ReflectionHelper.findField(EntityAINearestAttackableTarget.class, "field_75309_a", "targetEntity");

    public VarInstanceCommon() {
        superTargetEntityField.setAccessible(true);
    }
}
