package net.mindshake.securityforfabric.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.mindshake.securityforfabric.Main;
import net.mindshake.securityforfabric.block.*;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    public static Block BLAST_MINE = registerBlock("blast_mine",
            new BlastMineBlock(FabricBlockSettings.of(Material.METAL).strength(0.1f).nonOpaque(), false), ItemGroup.REDSTONE);

    public static Block GRASS_MINE = registerBlock("grass_mine",
            new BlastMineBlock(FabricBlockSettings.of(Material.SOLID_ORGANIC).strength(0.1f).sounds(BlockSoundGroup.GRASS).nonOpaque(), true), ItemGroup.REDSTONE);

    public static Block DIRT_MINE = registerBlock("dirt_mine",
            new BlastMineBlock(FabricBlockSettings.of(Material.STONE).strength(0.1f).sounds(BlockSoundGroup.GRAVEL), true), ItemGroup.REDSTONE);
    public static Block COBBLESTONE_MINE = registerBlock("cobblestone_mine",
            new BlastMineBlock(FabricBlockSettings.of(Material.STONE).strength(0.1f).sounds(BlockSoundGroup.STONE), true), ItemGroup.REDSTONE);

    public static Block STONE_MINE = registerBlock("stone_mine",
            new BlastMineBlock(FabricBlockSettings.of(Material.STONE).strength(0.1f).sounds(BlockSoundGroup.STONE), true), ItemGroup.REDSTONE);

    public static Block CRAFTING_TABLE_MINE = registerBlock("crafting_table_mine",
            new BlastMineBlock(FabricBlockSettings.of(Material.STONE).strength(0.1f).sounds(BlockSoundGroup.WOOD), true), ItemGroup.REDSTONE);

    public static Block FURNACE_MINE = registerBlock("furnace_mine",
            new OrientableBlastMineBlock(FabricBlockSettings.of(Material.STONE).strength(0.1f).sounds(BlockSoundGroup.STONE), true), ItemGroup.REDSTONE);


    public static Block FAKE_FLOOR = registerBlock("fake_floor",
            new FakeFloorBlock(FabricBlockSettings.of(Material.SOLID_ORGANIC).strength(0.6f).nonOpaque().sounds(BlockSoundGroup.GRASS)), ItemGroup.REDSTONE);

    public static Block SPIKES = registerBlock("spikes",
            new SpikesBlock(FabricBlockSettings.of(Material.METAL).strength(1f).requiresTool().nonOpaque()), ItemGroup.REDSTONE);

    public static Block SMART_DOOR = registerBlock("smart_door",
            new SmartDoorBlock(FabricBlockSettings.of(Material.METAL).strength(-1f).nonOpaque().sounds(BlockSoundGroup.METAL).resistance(3600000.0f)), ItemGroup.REDSTONE);
    public static Block SMART_CHEST = registerBlock("smart_chest",
            new SmartChestBlock(FabricBlockSettings.of(Material.METAL).strength(-1f).nonOpaque().sounds(BlockSoundGroup.METAL).resistance(3600000.0f)), ItemGroup.REDSTONE);

    public static Block TURRET_BLOCK = registerBlock("turret_block",
            new TurretBlock(FabricBlockSettings.of(Material.METAL).strength(-1f).nonOpaque().sounds(BlockSoundGroup.METAL).resistance(3600000.0f)), ItemGroup.REDSTONE);

    public static Block FAN = registerBlock("fan",
            new FanBlock(FabricBlockSettings.of(Material.METAL).strength(0.4f).nonOpaque().sounds(BlockSoundGroup.METAL)), ItemGroup.REDSTONE);

    public static Block SENSOR = registerBlock("sensor",
            new SensorBlock(FabricBlockSettings.of(Material.METAL).strength(0.5f).requiresTool().nonOpaque().sounds(BlockSoundGroup.METAL)), ItemGroup.REDSTONE);

    //public static Block MECHANICAL_WALL = registerBlock("mechanical_wall",
    //        new MechanicalWallBlock(FabricBlockSettings.of(Material.METAL).strength(0.5f).requiresTool().nonOpaque().sounds(BlockSoundGroup.METAL)), ItemGroup.REDSTONE);

    private static Block registerBlock(String name, Block block, ItemGroup group) {
        registerBlockItem(name, block, group);
        return Registry.register(Registry.BLOCK, new Identifier(Main.MODID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup group) {
        return Registry.register(Registry.ITEM, new Identifier(Main.MODID, name),
                new BlockItem(block, new FabricItemSettings().group(group)));
    }

    public static void registerModBlocks() {
        Main.LOGGER.info("Registering ModBlocks for " + Main.MODID);
    }
}
