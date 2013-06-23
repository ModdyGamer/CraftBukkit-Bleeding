package org.bukkit.craftbukkit.inventory;

import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.IInventory;

public class CraftMultiInventory extends CraftInventory {
    private IInventory[] inventories;
    private final int size;

    public CraftMultiInventory(int primary, IInventory... inventories) {
        super(inventories[primary]);
        this.inventories = inventories;
        int size = 0;

        for (IInventory inventory : getInventories()) {
            size += inventory.getSize();
        }
        this.size = size;
    }

    public CraftMultiInventory(IInventory... inventories) {
        this(0, inventories);
    }

    public IInventory[] getInventories() {
        return inventories;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public ItemStack getItem(int slot) {
        Validate.isTrue(slot >= 0, "Slot must be greater than or equal to 0.");
        Validate.isTrue(slot < getSize(), "Slot must be less than the size of this inventory(" + getSize() + ").");

        for (IInventory inventory : getInventories()) {
            if (slot >= inventory.getSize()) {
                slot -= inventory.getSize();
            } else {
                net.minecraft.server.ItemStack item = inventory.getItem(slot);
                return item == null ? null : CraftItemStack.asCraftMirror(inventory.getItem(slot));
            }
        }
        throw new IllegalStateException("Unable to retrieve an ItemStack for slot " + slot);
    }

    @Override
    public ItemStack[] getContents() {
        ItemStack[] contents = new ItemStack[getSize()];
        int index = 0;

        for (IInventory inventory : getInventories()) {
            for (net.minecraft.server.ItemStack item : inventory.getContents()) {
                contents[index++] = item == null ? null : CraftItemStack.asCraftMirror(item);
            }
        }

        return contents;
    }

    @Override
    public void setItem(int slot, ItemStack item) {
        Validate.isTrue(slot >= 0, "Slot must be greater than or equal to 0.");
        Validate.isTrue(slot < getSize(), "Slot must be less than the size of this inventory(" + getSize() + ").");

        for (IInventory inventory : getInventories()) {
            if (slot >= inventory.getSize()) {
                slot -= inventory.getSize();
            } else {
                inventory.setItem(slot, ((item == null || item.getTypeId() == 0) ? null : CraftItemStack.asNMSCopy(item)));
            }
        }
        throw new IllegalStateException("Unable to retrieve an ItemStack for slot " + slot);
    }
}
