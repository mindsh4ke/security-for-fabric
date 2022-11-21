package net.mindshake.securityforfabric.item.client;

import net.mindshake.securityforfabric.Main;
import net.mindshake.securityforfabric.item.SpikesBlockItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SpikesBlockItemModel extends AnimatedGeoModel<SpikesBlockItem> {
    @Override
    public Identifier getModelLocation(SpikesBlockItem object) {
        return new Identifier(Main.MODID, "geo/spikes.geo.json");
    }

    @Override
    public Identifier getTextureLocation(SpikesBlockItem object) {
        return new Identifier(Main.MODID, "textures/block/spikes.png");
    }

    @Override
    public Identifier getAnimationFileLocation(SpikesBlockItem animatable) {
        return new Identifier(Main.MODID, "animations/spikes.animation.json");
    }
}
