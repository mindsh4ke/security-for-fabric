package net.mindshake.securityforfabric.client.model.entity;

import net.mindshake.securityforfabric.Main;
import net.mindshake.securityforfabric.block.entity.SpikesBlockEntity;
import net.mindshake.securityforfabric.entity.TurretEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class TurretEntityModel extends AnimatedGeoModel<TurretEntity> {
    @Override
    public Identifier getModelResource(TurretEntity object) {
        return new Identifier(Main.MODID, "geo/turret.geo.json");
    }

    @Override
    public Identifier getTextureResource(TurretEntity object) {
        return new Identifier(Main.MODID, "textures/entity/turrets/turret_0.png");
    }

    @Override
    public Identifier getAnimationResource(TurretEntity animatable) {
        return new Identifier(Main.MODID, "animations/turret.animation.json");
    }

    @Override
    public void setCustomAnimations(TurretEntity animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);
        IBone head = this.getAnimationProcessor().getBone("head");
        IBone canyon = this.getAnimationProcessor().getBone("canyon");

        EntityModelData extraData = (EntityModelData)  animationEvent.getExtraDataOfType(EntityModelData.class).get(0);

        if (head != null && canyon != null && animatable.isActive()) {
            head.setRotationY(extraData.headPitch * ((float) Math.PI / 180f));
        }
    }
}
