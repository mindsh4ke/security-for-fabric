package net.mindshake.securityforfabric.client.renderer.entity;

import net.mindshake.securityforfabric.block.entity.SpikesBlockEntity;
import net.mindshake.securityforfabric.client.model.block.SpikesBlockModel;
import net.mindshake.securityforfabric.client.model.entity.TurretEntityModel;
import net.mindshake.securityforfabric.entity.TurretEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class TurretEntityRenderer extends GeoEntityRenderer<TurretEntity> {


    public TurretEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new TurretEntityModel());
    }

    @Override
    public RenderLayer getRenderType(TurretEntity animatable, float partialTicks, MatrixStack stack,
                                     VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                     Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }

}
