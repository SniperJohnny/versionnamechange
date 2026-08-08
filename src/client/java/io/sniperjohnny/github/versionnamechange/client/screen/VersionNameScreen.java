package io.sniperjohnny.github.versionnamechange.client.screen;

import io.sniperjohnny.github.versionnamechange.client.playerprefsconfig.PlayerPrefsConfigManager;
import io.sniperjohnny.github.versionnamechange.client.playerprefsconfig.StandardConfigManager;
import io.sniperjohnny.github.versionnamechange.client.widgets.CustomWidgets;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

/**
 * Screen where the player can edit the version name.
 *
 * Contains an edit box (pre-filled with the currently saved version name),
 * a Save button (persists the text via {@link PlayerPrefsConfigManager} and
 * closes the screen) and a Cancel button (closes without saving).
 * Pressing ESC also triggers the save.
 */
public class VersionNameScreen extends Screen {

    private static final int MAX_VERSION_NAME_LENGTH = 64;

    private static final Component TITLE = Component.translatable("screen.versionnamechange.title");

    private EditBox versionNameBox;

    public VersionNameScreen() {
        super(TITLE);
    }

    @Override
    protected void init() {
        // Edit box, pre-filled with the currently saved version name.
        this.versionNameBox = new EditBox(
                this.font,
                this.width / 2 - 100,
                this.height / 2 - 30,
                200,
                20,
                TITLE);
        this.versionNameBox.setMaxLength(MAX_VERSION_NAME_LENGTH);

        String current = PlayerPrefsConfigManager.getConfig().newVersionName;
        this.versionNameBox.setValue(current == null ? "" : current);
        this.addRenderableWidget(this.versionNameBox);

        // Give the edit box focus so the player can type right away.
        this.setInitialFocus(this.versionNameBox);

        CustomWidgets cancelwidget = new CustomWidgets(this.width /2 +5, this.height / 2 + 10
                , 32, 32, () -> {
            this.onClose();
        }, "/widgets/cancel.png", true);

        this.addRenderableWidget(cancelwidget);
        // Save button — persists the text and closes the screen.
        this.addRenderableWidget(Button.builder(
                        Component.translatable("screen.versionnamechange.save"),
                        button -> this.saveVersionName())
                .bounds(this.width / 2 - 105, this.height / 2 + 10, 100, 20)
                .build());

        // Cancel button — closes the screen without saving.
        /*
        this.addRenderableWidget(Button.builder(
                        Component.translatable("screen.versionnamechange.cancel"),
                        button -> this.onClose())
                .bounds(this.width / 2 + 5, this.height / 2 + 10, 100, 20)
                .build());

         */
    }

    /**
     * Saves the current edit-box text to the config and closes the screen.
     * Shared by the Save button and the ESC key handler.
     */
    private void saveVersionName() {
        String text = this.versionNameBox.getValue().trim();
        if (text.isEmpty()) {
            // Empty input falls back to the standard default version name.
            text = StandardConfigManager.getConfig().standardmodversionnumber;
        }
        PlayerPrefsConfigManager.getConfig().newVersionName = text;
        PlayerPrefsConfigManager.save();
        this.onClose();
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        // ESC triggers the save (instead of the default cancel/close behavior).
        if (event.key() == GLFW.GLFW_KEY_ESCAPE) {
            this.saveVersionName();
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        // Screen title.
        graphics.centeredText(this.font, TITLE, this.width / 2, 60, 0xFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
