package net.mindshake.securityforfabric.client.model.entity;

import net.mindshake.securityforfabric.Main;
import net.mindshake.securityforfabric.entity.TurretEntity;
import net.mindshake.securityforfabric.entity.WalkingMineEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class WalkingMineModel extends AnimatedGeoModel<WalkingMineEntity> {
    @Override
    public Identifier getModelLocation(WalkingMineEntity object) {
        return new Identifier(Main.MODID, "geo/walking_mine.geo.json");
    }

    @Override
    public Identifier getTextureLocation(WalkingMineEntity object) {
        return new Identifier(Main.MODID, "textures/entity/walking_mine.png");
    }

    @Override
    public Identifier getAnimationFileLocation(WalkingMineEntity animatable) {
        return new Identifier(Main.MODID, "animations/walking_mine.animation.json");
    }
}
