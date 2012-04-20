package net.minecraft.server;

import java.util.Random;

import org.bukkit.event.block.BlockIgniteEvent.IgniteCause; // CraftBukkit

public class BlockStationary extends BlockFluids {

    protected BlockStationary(int i, Material material) {
        super(i, material);
        this.a(false);
        if (material == Material.LAVA) {
            this.a(true);
        }
    }

    public boolean b(IBlockAccess iblockaccess, int i, int j, int k) {
        return this.material != Material.LAVA;
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        super.doPhysics(world, i, j, k, l);
        if (world.getTypeId(i, j, k) == this.id) {
            this.i(world, i, j, k);
        }
    }

    private void i(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);

        world.suppressPhysics = true;
        world.setRawTypeIdAndData(i, j, k, this.id - 1, l);
        world.b(i, j, k, i, j, k);
        world.c(i, j, k, this.id - 1, this.d());
        world.suppressPhysics = false;
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (this.material == Material.LAVA) {
            int l = random.nextInt(3);

            int i1;
            int j1;

            for (i1 = 0; i1 < l; ++i1) {
                i += random.nextInt(3) - 1;
                ++j;
                k += random.nextInt(3) - 1;
                j1 = world.getTypeId(i, j, k);
                if (j1 == 0) {
                    if (this.j(world, i - 1, j, k) || this.j(world, i + 1, j, k) || this.j(world, i, j, k - 1) || this.j(world, i, j, k + 1) || this.j(world, i, j - 1, k) || this.j(world, i, j + 1, k)) {
                        // CraftBukkit start - prevent lava putting something on fire.
                        if (world.getTypeId(i, j, k) != Block.FIRE.id) {
                            if (org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockIgnite(world, null, IgniteCause.LAVA, i, j, k)) {
                                continue;
                            }
                        }
                        // CraftBukkit end

                        world.setTypeId(i, j, k, Block.FIRE.id);
                        return;
                    }
                } else if (Block.byId[j1].material.isSolid()) {
                    return;
                }
            }

            if (l == 0) {
                i1 = i;
                j1 = k;

                for (int k1 = 0; k1 < 3; ++k1) {
                    i = i1 + random.nextInt(3) - 1;
                    k = j1 + random.nextInt(3) - 1;
                    if (world.isEmpty(i, j + 1, k) && this.j(world, i, j, k)) {
                        // CraftBukkit start - prevent lava putting something on fire.
                        if (world.getTypeId(i, j + 1, k) != Block.FIRE.id) {
                            if (org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockIgnite(world, null, IgniteCause.LAVA, i, j + 1, k)) {
                                continue;
                            }
                        }
                        // CraftBukkit end

                        world.setTypeId(i, j + 1, k, Block.FIRE.id);
                    }
                }
            }
        }
    }

    private boolean j(World world, int i, int j, int k) {
        return world.getMaterial(i, j, k).isBurnable();
    }
}
