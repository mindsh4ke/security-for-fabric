package net.mindshake.securityforfabric.client.renderer.block;

import net.mindshake.securityforfabric.block.entity.SpikesBlockEntity;
import net.mindshake.securityforfabric.client.model.block.SpikesBlockModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class SpikesBlockRenderer extends GeoBlockRenderer<SpikesBlockEntity> {
    public SpikesBlockRenderer() {
        super(new SpikesBlockModel());
    }

    @Override
    public RenderLayer getRenderType(SpikesBlockEntity animatable, float partialTicks, MatrixStack stack,
                                     VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                     Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }

}
