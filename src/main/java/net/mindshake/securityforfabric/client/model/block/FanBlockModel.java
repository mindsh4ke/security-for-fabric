package net.mindshake.securityforfabric.client.model.block;

import net.mindshake.securityforfabric.Main;
import net.mindshake.securityforfabric.block.entity.FanBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FanBlockModel extends AnimatedGeoModel<FanBlockEntity> {
    @Override
    public Identifier getModelResource(FanBlockEntity object) {
        return new Identifier(Main.MODID, "geo/fan.geo.json");
    }

    @Override
    public Identifier getTextureResource(FanBlockEntity object) {
        return new Identifier(Main.MODID, "textures/block/fan.png");
    }

    @Override
    public Identifier getAnimationResource(FanBlockEntity animatable) {
        return new Identifier(Main.MODID, "animations/fan.animation.json");
    }
}
