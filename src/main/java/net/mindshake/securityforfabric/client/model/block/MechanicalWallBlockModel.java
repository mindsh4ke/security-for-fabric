package net.mindshake.securityforfabric.client.model.block;

import net.mindshake.securityforfabric.Main;
import net.mindshake.securityforfabric.block.entity.MechanicalWallBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MechanicalWallBlockModel extends AnimatedGeoModel<MechanicalWallBlockEntity> {
    @Override
    public Identifier getModelLocation(MechanicalWallBlockEntity object) {
        return new Identifier(Main.MODID, "geo/mechanical_wall.geo.json");
    }

    @Override
    public Identifier getTextureLocation(MechanicalWallBlockEntity object) {
        return new Identifier(Main.MODID, "textures/block/mechanical_wall.png");
    }

    @Override
    public Identifier getAnimationFileLocation(MechanicalWallBlockEntity animatable) {
        return new Identifier(Main.MODID, "animations/mechanical_wall.animation.json");
    }
}
