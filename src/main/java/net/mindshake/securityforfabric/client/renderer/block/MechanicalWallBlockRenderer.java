package net.mindshake.securityforfabric.client.renderer.block;

import net.mindshake.securityforfabric.block.entity.MechanicalWallBlockEntity;
import net.mindshake.securityforfabric.client.model.block.MechanicalWallBlockModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class MechanicalWallBlockRenderer extends GeoBlockRenderer<MechanicalWallBlockEntity> {
    public MechanicalWallBlockRenderer() {
        super(new MechanicalWallBlockModel());
    }

    @Override
    public RenderLayer getRenderType(MechanicalWallBlockEntity animatable, float partialTicks, MatrixStack stack,
                                     VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                     Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }

}
