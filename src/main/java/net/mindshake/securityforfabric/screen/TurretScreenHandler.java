package net.mindshake.securityforfabric.screen;

import net.mindshake.securityforfabric.registry.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;

public class TurretScreenHandler extends ScreenHandler {
    public TurretScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModScreenHandlers.TURRET_SCREEN_HANDLER, syncId);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
