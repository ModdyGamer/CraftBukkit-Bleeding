package org.bukkit.craftbukkit.inventory;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SkullItemStackTest extends ItemStackTests {

    @Parameters(name="[{index}]:{" + NAME_PARAMETER + "}")
    public static List<Object[]> data() {
        return StackProvider.compound(operaters(), "%s %s", NAME_PARAMETER, Material.SKULL, Material.SKULL_ITEM);
    }

    static List<Object[]> operaters() {
        return Arrays.asList(
            new Object[] {
                new Operater() {
                    public ItemStack operate(ItemStack cleanStack) {
                        SkullMeta meta = (SkullMeta) cleanStack.getItemMeta();
                        meta.setOwner("Notch");
                        cleanStack.setItemMeta(meta);
                        return cleanStack;
                    }
                },
                new Operater() {
                    public ItemStack operate(ItemStack cleanStack) {
                        SkullMeta meta = (SkullMeta) cleanStack.getItemMeta();
                        meta.setOwner("PlantAssassin");
                        cleanStack.setItemMeta(meta);
                        return cleanStack;
                    }
                },
                "Name 1 vs. Name 2"
            },
            new Object[] {
                new Operater() {
                    public ItemStack operate(ItemStack cleanStack) {
                        SkullMeta meta = (SkullMeta) cleanStack.getItemMeta();
                        meta.setOwner("Notch");
                        cleanStack.setItemMeta(meta);
                        return cleanStack;
                    }
                },
                new Operater() {
                    public ItemStack operate(ItemStack cleanStack) {
                        SkullMeta meta = (SkullMeta) cleanStack.getItemMeta();
                        meta.setOwner(null);
                        cleanStack.setItemMeta(meta);
                        return cleanStack;
                    }
                },
                "Name vs. Null"
            }
        );
    }
}
