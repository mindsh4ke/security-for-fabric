package net.mindshake.securityforfabric.client.renderer.entity;

import net.mindshake.securityforfabric.client.model.entity.TurretEntityModel;
import net.mindshake.securityforfabric.client.model.entity.WalkingMineModel;
import net.mindshake.securityforfabric.entity.TurretEntity;
import net.mindshake.securityforfabric.entity.WalkingMineEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class WalkingMineRenderer extends GeoEntityRenderer<WalkingMineEntity> {


    public WalkingMineRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new WalkingMineModel());
    }

    @Override
    public RenderLayer getRenderType(WalkingMineEntity animatable, float partialTicks, MatrixStack stack,
                                     VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                     Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }

}
