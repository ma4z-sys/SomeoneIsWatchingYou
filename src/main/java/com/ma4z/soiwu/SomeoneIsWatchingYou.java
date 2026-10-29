package com.ma4z.soiwu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;

public class SomeoneIsWatchingYou implements ModInitializer {
	public static final String MOD_ID = "someoneiswatchingyou";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("SomeoneIsWatchingYou initialized.");
	}
}
