package net.mindshake.securityforfabric.item.client;

import net.mindshake.securityforfabric.item.SpikesBlockItem;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class SpikesBlockItemRenderer extends GeoItemRenderer<SpikesBlockItem> {
    public SpikesBlockItemRenderer() {
        super(new SpikesBlockItemModel());
    }
}
