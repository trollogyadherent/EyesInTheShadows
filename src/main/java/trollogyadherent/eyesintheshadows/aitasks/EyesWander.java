package trollogyadherent.eyesintheshadows.aitasks;

import net.minecraft.entity.ai.EntityAIWander;
import trollogyadherent.eyesintheshadows.Config;
import trollogyadherent.eyesintheshadows.entity.entities.EntityEyes;

public class EyesWander extends EntityAIWander {
    EntityEyes eyes;

    public EyesWander(EntityEyes entityEyes, double p_i1648_2_) {
        super(entityEyes, p_i1648_2_);
        eyes = entityEyes;
    }

    @Override
    public boolean continueExecuting() {
        return eyes.getBrightness() > 0 && !eyes.isPlayerLookingInMyGeneralDirection() && super.continueExecuting();
    }

    @Override
    public boolean shouldExecute() {
        return Config.eyesWander && eyes.getBrightness() > 0 && !eyes.isPlayerLookingInMyGeneralDirection() && super.shouldExecute();
    }
}
