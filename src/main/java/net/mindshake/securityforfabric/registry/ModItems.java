package net.mindshake.securityforfabric.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.mindshake.securityforfabric.Main;
import net.mindshake.securityforfabric.item.WalkingMineSpawner;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final Item CPU = registerItem("cpu",
            new Item(new FabricItemSettings().group(ItemGroup.REDSTONE)));

    public static final Item WALKING_MINE = registerItem("walking_mine_spawner",
            new WalkingMineSpawner(new FabricItemSettings().group(ItemGroup.REDSTONE)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Main.MODID, name), item);
    }

    public static void registerModItems() {
        Main.LOGGER.info("Registering Mod Items for " + Main.MODID);
    }
}
