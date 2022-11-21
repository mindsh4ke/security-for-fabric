package net.mindshake.securityforfabric;

import net.fabricmc.api.ModInitializer;
import net.mindshake.securityforfabric.registry.*;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.example.GeckoLibMod;

public class Main implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Identifier SET_PASSWORD_PACKET_ID = new Identifier(Main.MODID, "password");
	public static final String MODID = "securityforfabric";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		GeckoLibMod.DISABLE_IN_DEV = true;
		LOGGER.info("Hello Fabric world!");
		ModEntities.registerAttributes();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerAllBlockEntities();
		ModItems.registerModItems();
		ModScreenHandlers.registerScreenHandlers();
	}
}
