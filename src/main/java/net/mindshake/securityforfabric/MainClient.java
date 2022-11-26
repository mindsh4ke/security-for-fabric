package net.mindshake.securityforfabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.mindshake.securityforfabric.client.renderer.block.FanBlockRenderer;
import net.mindshake.securityforfabric.client.renderer.block.SpikesBlockRenderer;
import net.mindshake.securityforfabric.client.renderer.entity.TurretBulletEntityRenderer;
import net.mindshake.securityforfabric.client.renderer.entity.TurretEntityRenderer;
import net.mindshake.securityforfabric.client.renderer.entity.WalkingMineRenderer;
import net.mindshake.securityforfabric.item.client.SpikesBlockItemRenderer;
import net.mindshake.securityforfabric.registry.*;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class MainClient implements ClientModInitializer {

    public static final EntityModelLayer TURRET_BULLET_MODEL_LAYER =
            new EntityModelLayer(new Identifier(Main.MODID, "turret_bullet"), "main");

    @Override
    public void onInitializeClient() {

        ClientSidePacketRegistry.INSTANCE.register(Main.SET_PASSWORD_PACKET_ID,
                (packetContext, attachedData) -> packetContext.getTaskQueue().execute(() -> {
                    
                }));

        BlockEntityRendererRegistry.register(ModBlockEntities.SPIKES_BLOCKENTITY,
                (BlockEntityRendererFactory.Context rendererDispatcherIn) -> new SpikesBlockRenderer());

        BlockEntityRendererRegistry.register(ModBlockEntities.FAN_BLOCKENTITY,
                (BlockEntityRendererFactory.Context rendererDispatcherIn) -> new FanBlockRenderer());

        EntityRendererRegistry.register(ModEntities.TURRET, TurretEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.TURET_BULLET, TurretBulletEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.WALKING_MINE, WalkingMineRenderer::new);


        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BLAST_MINE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMART_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GRASS_MINE, RenderLayer.getCutout());


        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> 0x5C9854, ModBlocks.FAKE_FLOOR, ModBlocks.GRASS_MINE);
        ColorProviderRegistry.BLOCK.register(((state, world, pos, tintIndex) -> world.getColor(pos, BiomeColors.GRASS_COLOR)), ModBlocks.FAKE_FLOOR, ModBlocks.GRASS_MINE);
    }
}
