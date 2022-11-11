package trollogyadherent.eyesintheshadows.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import trollogyadherent.eyesintheshadows.EyesInTheShadows;

public class EyesRenderer extends RenderLiving {
    public EyesRenderer(ModelBase par1ModelBase, float parShadowSize) {
        super(par1ModelBase, parShadowSize);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return EyesInTheShadows.varInstanceClient.eyesTexture;
    }
}
