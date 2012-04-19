package net.minecraft.server;

public class ItemHoe extends Item {

    public ItemHoe(int i, EnumToolMaterial enumtoolmaterial) {
        super(i);
        this.maxStackSize = 1;
        this.setMaxDurability(enumtoolmaterial.a());
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        if (!entityhuman.d(i, j, k)) {
            return false;
        } else {
            int i1 = world.getTypeId(i, j, k);
            int j1 = world.getTypeId(i, j + 1, k);

            if ((l == 0 || j1 != 0 || i1 != Block.GRASS.id) && i1 != Block.DIRT.id) {
                return false;
            } else {
                Block block = Block.SOIL;

                world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), block.stepSound.getName(), (block.stepSound.getVolume1() + 1.0F) / 2.0F, block.stepSound.getVolume2() * 0.8F);
                if (world.isStatic) {
                    return true;
                } else {
                    // CraftBukkit start - Delegate to Event Factory
                    //world.setTypeId(i, j, k, block.id);
                    if (!org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockPlace(world, entityhuman, i, j, k, block.id, 0)) {
                        return false;
                    }
                    // CraftBukkit end

                    itemstack.damage(1, entityhuman);
                    return true;
                }
            }
        }
    }
}
