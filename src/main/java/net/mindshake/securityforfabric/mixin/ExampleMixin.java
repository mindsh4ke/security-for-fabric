package net.mindshake.securityforfabric.mixin;

import net.mindshake.securityforfabric.Main;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class ExampleMixin {
	/*@Inject(at = @At("HEAD"), method = "init()V") WORK IN PROGRESS
	private void init(CallbackInfo info) {
		Main.LOGGER.info("This line is printed by an example mod mixin!");
	}*/
}
