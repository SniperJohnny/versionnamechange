package io.sniperjohnny.github.versionnamechange.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.sniperjohnny.github.versionnamechange.VersionnameChange;
import io.sniperjohnny.github.versionnamechange.client.screen.VersionNameScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class ModKeyBinds {
    private static KeyMapping openVersionNameScreen;

    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(VersionnameChange.MOD_ID, "version_name_changer")
    );

    public static void register() {
        openVersionNameScreen = KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                        "key.versionnamechange.changeversionname", // The translation key for the key mapping.
                        InputConstants.Type.KEYSYM, // The type of the keybinding; KEYSYM for keyboard, MOUSE for mouse.
                        InputConstants.KEY_COMMA, // The keycode of the key.
                        CATEGORY // The category of the mapping.
                ));

        // Register the tick handler that polls the key and opens the screen.
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // `if` (not `while`) so holding the key can't rebuild the screen
            // and yank focus away while the player is typing in the edit box.
            if (openVersionNameScreen.consumeClick()) {
                // Opens from anywhere, including the title screen (no player needed).
                client.setScreenAndShow(new VersionNameScreen());
            }
        });
    }
}
