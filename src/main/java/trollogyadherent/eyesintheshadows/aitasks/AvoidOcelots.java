package trollogyadherent.eyesintheshadows.aitasks;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import trollogyadherent.eyesintheshadows.Config;
import trollogyadherent.eyesintheshadows.entity.entities.EntityEyes;

public class AvoidOcelots extends EntityAIAvoidEntity {
    EntityEyes eyes;
    public AvoidOcelots(EntityEyes entityEyes, Class p_i1616_2_, float p_i1616_3_, double p_i1616_4_, double p_i1616_6_) {
        super(entityEyes, p_i1616_2_, p_i1616_3_, p_i1616_4_, p_i1616_6_);
        eyes = entityEyes;
    }

    @Override
    public boolean shouldExecute() {
        return false;//Config.eyesFleeOcelots && eyes.getBrightness() > 0 && super.shouldExecute();
    }
}
