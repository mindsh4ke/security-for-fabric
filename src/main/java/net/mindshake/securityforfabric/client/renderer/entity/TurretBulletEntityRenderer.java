package net.mindshake.securityforfabric.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mindshake.securityforfabric.Main;
import net.mindshake.securityforfabric.entity.projectile.TurretBulletEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(value = EnvType.CLIENT)
public class TurretBulletEntityRenderer extends ProjectileEntityRenderer<TurretBulletEntity> {

    public static final Identifier TEXTURE = new Identifier(Main.MODID,"textures/entity/projectiles/turret_bullet.png");


    public TurretBulletEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(TurretBulletEntity persistentProjectileEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(persistentProjectileEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(TurretBulletEntity entity) {
        return TEXTURE;
    }
}
