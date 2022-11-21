package net.mindshake.securityforfabric.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.mindshake.securityforfabric.Main;
import net.mindshake.securityforfabric.item.SpikesBlockItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import software.bernie.example.registry.RegistryUtils;

public class ModItems {
    public static final Item SPIKES = registerItem("spikesitem",
            new SpikesBlockItem(ModBlocks.SPIKES, new FabricItemSettings().group(ItemGroup.REDSTONE)));

    public static final Item CPU = registerItem("cpu",
            new Item(new FabricItemSettings().group(ItemGroup.REDSTONE)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Main.MODID, name), item);
    }

    public static void registerModItems() {
        Main.LOGGER.info("Registering Mod Items for " + Main.MODID);
    }
}
