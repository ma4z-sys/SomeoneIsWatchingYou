package com.ma4z.soiwu;

import net.fabricmc.api.ClientModInitializer;

public class SomeoneIsWatchingYouClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		DahdaWatcher.registerClient();
	}
}