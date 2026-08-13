package io.sniperjohnny.github.versionnamechange.client;

import io.sniperjohnny.github.versionnamechange.client.playerprefsconfig.PlayerPrefsConfigManager;
import io.sniperjohnny.github.versionnamechange.client.playerprefsconfig.StandardConfigManager;
import net.fabricmc.api.ClientModInitializer;

public class VersionnameChangeClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {

		StandardConfigManager.load();
		PlayerPrefsConfigManager.load();

		ModKeyBinds.register();
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}
}