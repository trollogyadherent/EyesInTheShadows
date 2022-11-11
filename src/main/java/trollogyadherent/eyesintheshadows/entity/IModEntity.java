/* Taken from WildAnimalsPlus by jabelar */
/* https://github.com/jabelar/WildAnimalsPlus-1.7.10/blob/master/src/main/java/com/blogspot/jabelarminecraft/wildanimals/entities/IModEntity.java */
/* License: GPLv3 - <http://www.gnu.org/licenses/> */

package trollogyadherent.eyesintheshadows.entity;

import net.minecraft.nbt.NBTTagCompound;

public interface IModEntity {
    // set up AI tasks
    void setupAI();

    // use clear tasks for subclasses then build up their ai task list specifically
    void clearAITasks();

    // initialize the tag compound used for syncing custom entity data
    void initSyncDataCompound();

    NBTTagCompound getSyncDataCompound();

    void setSyncDataCompound(NBTTagCompound parCompound);

    // method to send sync of extended properties from server to clients
    void sendEntitySyncPacket();

    // common encapsulation methods
    void setScaleFactor(float parScaleFactor);

    float getScaleFactor();
}
