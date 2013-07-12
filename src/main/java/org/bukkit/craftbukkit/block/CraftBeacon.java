package org.bukkit.craftbukkit.block;

import java.util.Collection;

import net.minecraft.server.TileEntityBeacon;

import org.apache.commons.lang.Validate;
import org.bukkit.block.Block;
import org.bukkit.block.Beacon;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventoryBeacon;
import org.bukkit.craftbukkit.potion.CraftPotionBrewer;
import org.bukkit.inventory.BeaconInventory;
import org.bukkit.potion.PotionEffect;

public class CraftBeacon extends CraftBlockState implements Beacon {
    private final CraftWorld world;
    private final TileEntityBeacon beacon;

    public CraftBeacon(final Block block) {
        super(block);

        world = (CraftWorld) block.getWorld();
        beacon = (TileEntityBeacon) world.getTileEntityAt(getX(), getY(), getZ());
    }

    public BeaconInventory getInventory() {
        return new CraftInventoryBeacon(beacon);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            beacon.update();
        }

        return result;
    }

    @Override
    public Collection<PotionEffect> getEffects() {
        return CraftPotionBrewer.nmsToBukkitEffects(beacon.effects);
    }

    @Override
    public Collection<PotionEffect> getDefaultEffects() {
        return CraftPotionBrewer.nmsToBukkitEffects(beacon.getDefaultEffects());
    }

    @Override
    public void setEffects(Collection<PotionEffect> newEffects) {
        if (newEffects == null) {
            beacon.effects = java.util.Collections.emptyList();
        } else {
            Validate.noNullElements(newEffects, "Cannot set null PotionEffects");
            beacon.effects = CraftPotionBrewer.bukkitToNmsEffects(newEffects);
        }
        beacon.customEffects = true;
    }

    @Override
    public void resetEffects() {
        beacon.customEffects = false;
        beacon.updateEffects();
    }

    @Override
    public boolean isActive() {
        return beacon.isEnabled();
    }

    @Override
    public boolean isActivationOverridden() {
        return beacon.activationOverride != TileEntityBeacon.OVERRIDE_DEFAULT;
    }

    @Override
    public void setActive(boolean active) {
        beacon.activationOverride = active ? TileEntityBeacon.OVERRIDE_ON : TileEntityBeacon.OVERRIDE_OFF;
    }

    @Override
    public void resetActive() {
        beacon.activationOverride = TileEntityBeacon.OVERRIDE_DEFAULT;
    }

    @Override
    public double getRadius() {
        return beacon.getRadius();
    }

    @Override
    public double getDefaultRadius() {
        if (beacon.e >= 4) { // compensate for getPyramidSize()
            return 50D;
        }
        return beacon.e * 10 + 10;
    }

    @Override
    public void setRadius(double radius) {
        Validate.isTrue(radius >= 0, "Radius cannot be less than 0");
        beacon.radiusOverride = radius;
    }

    @Override
    public void resetRadius() {
        beacon.radiusOverride = -1;
    }

    @Override
    public boolean canSeeSky() {
        return beacon.canSeeSky();
    }

    @Override
    public int getPyramidSize() {
        return beacon.e;
    }

    @Override
    public int getPyramidSize(boolean calculate) {
        if (!calculate) {
            return beacon.e;
        }
        return beacon.countPyramid(4);
    }

    @Override
    public int getPyramidSize(boolean calculate, int maximum) {
        Validate.isTrue(maximum > 1, "Maximum number of layers must be at least 1");
        if (!calculate) {
            return beacon.e;
        }
        return beacon.countPyramid(maximum);
    }
}

