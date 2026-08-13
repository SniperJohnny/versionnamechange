package io.sniperjohnny.github.versionnamechange.client.mixin;

import io.sniperjohnny.github.versionnamechange.client.screen.VersionNameScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init()V", at = @At("TAIL"))
    private void versionnamechange$addMenuButton(CallbackInfo ci) {
        // Create the button that opens our screen
        Button menuButton = Button.builder(
                        Component.translatable("screen.versionnamechange.menu"),
                        button -> {
                            if (this.minecraft != null) {
                                this.minecraft.setScreenAndShow(new VersionNameScreen());
                            }
                        })
                .bounds(20, 10, 70, 20)
                .build();

        this.addRenderableWidget(menuButton);
    }
}
