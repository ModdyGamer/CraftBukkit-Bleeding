package net.minecraft.server;

public class ItemBed extends Item {

    public ItemBed(int i) {
        super(i);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        if (l != 1) {
            return false;
        } else {
            ++j;
            BlockBed blockbed = (BlockBed) Block.BED;
            int i1 = MathHelper.floor((double) (entityhuman.yaw * 4.0F / 360.0F) + 0.5D) & 3;
            byte b0 = 0;
            byte b1 = 0;

            if (i1 == 0) {
                b1 = 1;
            }

            if (i1 == 1) {
                b0 = -1;
            }

            if (i1 == 2) {
                b1 = -1;
            }

            if (i1 == 3) {
                b0 = 1;
            }

            if (entityhuman.d(i, j, k) && entityhuman.d(i + b0, j, k + b1)) {
                if (world.isEmpty(i, j, k) && world.isEmpty(i + b0, j, k + b1) && world.e(i, j - 1, k) && world.e(i + b0, j - 1, k + b1)) {
                    // CraftBukkit start - Delegate to Event Factory
                    //world.setTypeIdAndData(i, j, k, blockbed.id, i1);
                    org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockPlace(world, entityhuman, i, j, k, blockbed.id, i1);
                    // CraftBukkit end

                    if (world.getTypeId(i, j, k) == blockbed.id) {
                        world.setTypeIdAndData(i + b0, j, k + b1, blockbed.id, i1 + 8);
                    }

                    --itemstack.count;
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }
}
