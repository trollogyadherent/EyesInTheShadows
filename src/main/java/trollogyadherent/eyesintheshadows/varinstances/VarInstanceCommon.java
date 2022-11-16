package trollogyadherent.eyesintheshadows.varinstances;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import trollogyadherent.eyesintheshadows.Tags;
import trollogyadherent.eyesintheshadows.util.XSTR;

import java.lang.reflect.Field;

public class VarInstanceCommon {
    /* A faster random. http://demesos.blogspot.com/2011/09/pseudo-random-number-generators.html */
    public XSTR rand = new XSTR();
    public String disappearSound = Tags.MODID + ":" + "mob.eyes.disappear";
    public String laughSound = Tags.MODID + ":" + "mob.eyes.laugh";
    public String jumpScareSound = Tags.MODID + ":" + "mob.eyes.jumpscare";
    public Field superTargetEntityField = ReflectionHelper.findField(EntityAINearestAttackableTarget.class, "field_75309_a", "targetEntity");
    public Field speedTowardsTargetField = ReflectionHelper.findField(EntityAIAttackOnCollide.class, "field_75440_e", "speedTowardsTarget");

    public VarInstanceCommon() {
        superTargetEntityField.setAccessible(true);
        speedTowardsTargetField.setAccessible(true);
    }
}
