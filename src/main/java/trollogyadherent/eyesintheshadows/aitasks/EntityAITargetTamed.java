package trollogyadherent.eyesintheshadows.aitasks;

import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAITargetTamed extends EntityAINearestAttackableTarget
{
    private final EntityTameable theTameable;

    public EntityAITargetTamed(EntityTameable entityTameable, Class class_, int p_i1666_3_, boolean p_i1666_4_)
    {
        super(entityTameable, class_, p_i1666_3_, p_i1666_4_);
        this.theTameable = entityTameable;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        return this.theTameable.isTamed() && super.shouldExecute();
    }
}