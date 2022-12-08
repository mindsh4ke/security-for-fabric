package net.mindshake.securityforfabric.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.mindshake.securityforfabric.Main;
import net.mindshake.securityforfabric.block.entity.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockEntities {
    public static BlockEntityType<SpikesBlockEntity> SPIKES_BLOCKENTITY;

    public static BlockEntityType<BlastMineBlockEntity> BLAST_MINE_BLOCKENTITY;

    public static BlockEntityType<SmartDoorBlockEntity> SMART_DOOR_BLOCKENTITY;
    public static BlockEntityType<SmartChestBlockEntity> SMART_CHEST_BLOCKENTITY;

    public static BlockEntityType<FanBlockEntity> FAN_BLOCKENTITY;

    public static BlockEntityType<SensorBlockEntity> SENSOR_BLOCKENTITY;

    public static void registerAllBlockEntities () {

        SPIKES_BLOCKENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MODID, "spikes_blockentity"),
                FabricBlockEntityTypeBuilder.create(SpikesBlockEntity::new,
                        ModBlocks.SPIKES).build(null));

        BLAST_MINE_BLOCKENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MODID, "blast_mine_blockentity"),
                FabricBlockEntityTypeBuilder.create(BlastMineBlockEntity::new,
                        ModBlocks.BLAST_MINE, ModBlocks.GRASS_MINE, ModBlocks.COBBLESTONE_MINE, ModBlocks.DIRT_MINE, ModBlocks.STONE_MINE, ModBlocks.CRAFTING_TABLE_MINE, ModBlocks.FURNACE_MINE).build(null));

        SMART_DOOR_BLOCKENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MODID, "smart_door_blockentity"),
                FabricBlockEntityTypeBuilder.create(SmartDoorBlockEntity::new,
                        ModBlocks.SMART_DOOR).build(null));

        SMART_CHEST_BLOCKENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MODID, "smart_chest_blockentity"),
                FabricBlockEntityTypeBuilder.create(SmartChestBlockEntity::new,
                        ModBlocks.SMART_CHEST).build(null));

        FAN_BLOCKENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MODID, "fan_blockentity"),
                FabricBlockEntityTypeBuilder.create(FanBlockEntity::new,
                        ModBlocks.FAN).build(null));

        SENSOR_BLOCKENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Main.MODID, "sensor_blockentity"),
                FabricBlockEntityTypeBuilder.create(SensorBlockEntity::new,
                        ModBlocks.SENSOR).build(null));
    }
}
