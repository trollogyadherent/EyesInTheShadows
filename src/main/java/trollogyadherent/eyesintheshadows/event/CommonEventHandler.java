package trollogyadherent.eyesintheshadows.event;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import trollogyadherent.eyesintheshadows.Config;
import trollogyadherent.eyesintheshadows.aitasks.TargetEyesTamed;
import trollogyadherent.eyesintheshadows.entity.entities.EntityEyes;

public class CommonEventHandler {
    @SubscribeEvent
    public void onEntitySpawn(EntityJoinWorldEvent event)
    {
        if (event.entity instanceof EntityLiving) {
            handleAITasks((EntityLiving) event.entity, event);
        }
    }

    private void handleAITasks(EntityLiving e, Event ev)
    {
        if (e instanceof EntityWolf)
        {
            //e.tasks.addTask(2, new EntityAIAttackOnCollide((EntityCreature) e, EntityEyes.class, 1.0D, false));
            //e.targetTasks.addTask(2, new EntityAINearestAttackableTarget((EntityCreature) e, EntityEyes.class, 0, true));
            e.targetTasks.addTask(2, new TargetEyesTamed((EntityTameable) e, EntityEyes.class, 0, true));
        }
    }
}
