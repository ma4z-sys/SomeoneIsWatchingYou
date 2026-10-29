package com.ma4z.soiwu;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SomeoneIsWatchingYouFabric implements ModInitializer {
	private static final Logger LOGGER = LoggerFactory.getLogger(SomeoneIsWatchingYou.MOD_ID);

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
				CommandManager.literal("testdahdawatching").executes(context -> {
					DahdaWatcher.requestTest();
					return 1;
				})));
		LOGGER.info("SomeoneIsWatchingYou initialized.");
	}
}