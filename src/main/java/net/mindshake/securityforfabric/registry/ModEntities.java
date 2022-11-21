package net.mindshake.securityforfabric.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.mindshake.securityforfabric.Main;
import net.mindshake.securityforfabric.entity.TurretEntity;
import net.mindshake.securityforfabric.entity.projectile.TurretBulletEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntities {
    public static final EntityType<TurretEntity> TURRET = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(Main.MODID, "turret"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, TurretEntity::new).dimensions(EntityDimensions.fixed(1f,0.9f)).build()
    );

    public static final EntityType<TurretBulletEntity> TURET_BULLET = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(Main.MODID, "turret_bullet"),
            FabricEntityTypeBuilder.<TurretBulletEntity>create(SpawnGroup.MISC, TurretBulletEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F)) // dimensions in Minecraft units of the projectile
                    .trackRangeBlocks(4).trackedUpdateRate(10) // necessary for all thrown projectiles (as it prevents it from breaking, lol)
                    .build()// VERY IMPORTANT DONT DELETE FOR THE LOVE OF GOD PSLSSSSSS
    );

    public static void registerAttributes () {
        FabricDefaultAttributeRegistry.register(ModEntities.TURRET, TurretEntity.setAttributes());
    }
}
