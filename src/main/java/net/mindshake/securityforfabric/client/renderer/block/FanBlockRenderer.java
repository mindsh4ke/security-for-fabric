package net.mindshake.securityforfabric.client.renderer.block;

import net.mindshake.securityforfabric.block.entity.FanBlockEntity;
import net.mindshake.securityforfabric.block.entity.SpikesBlockEntity;
import net.mindshake.securityforfabric.client.model.block.FanBlockModel;
import net.mindshake.securityforfabric.client.model.block.SpikesBlockModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class FanBlockRenderer extends GeoBlockRenderer<FanBlockEntity> {
    public FanBlockRenderer() {
        super(new FanBlockModel());
    }

    @Override
    public RenderLayer getRenderType(FanBlockEntity animatable, float partialTicks, MatrixStack stack,
                                     VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                     Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }

}
