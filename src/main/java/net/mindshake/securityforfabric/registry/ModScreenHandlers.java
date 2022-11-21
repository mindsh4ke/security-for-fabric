package net.mindshake.securityforfabric.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.mindshake.securityforfabric.Main;
import net.mindshake.securityforfabric.screen.TurretScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {

    public static ScreenHandlerType<TurretScreenHandler> TURRET_SCREEN_HANDLER;
    public static void registerScreenHandlers () {
        TURRET_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(Main.MODID, "turret_screen_handler"),
                TurretScreenHandler::new);
    }
}
