package io.sniperjohnny.github.versionnamechange.client.widgets;

import io.sniperjohnny.github.versionnamechange.VersionnameChange;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class CustomWidgets extends AbstractWidget {
    private final Runnable onPress;
    private final String iconFilepath;
    public boolean iswanted; // FIXED: Removed 'static' keyword

    private static final Identifier BUTTON_SPRITE = Identifier.withDefaultNamespace("widget/button");
    private static final Identifier BUTTON_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("widget/button_highlighted");

    public CustomWidgets(int x, int y, int width, int height, Runnable onPress, String iconFilepath, boolean iswanted) {
        super(x, y, width, height, Component.empty());
        this.onPress = onPress;
        this.iconFilepath = iconFilepath;
        this.iswanted = iswanted;
    }

    @Override
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        super.onClick(event, doubleClick);
        if (this.onPress != null) {
            this.onPress.run();
        }
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        // 1. Render button background
        Identifier backgroundSprite = this.isHovered() ? BUTTON_HIGHLIGHTED_SPRITE : BUTTON_SPRITE;

        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                backgroundSprite,
                this.getX(),
                this.getY(),
                this.width,
                this.height
        );

        // 2. Render Icon or Item inside button bounds
        switch (this.iconFilepath) {
            case "ec":
                renderScaledItem(graphics, new ItemStack(Blocks.ENDER_CHEST));
                break;
            case "ah":
                renderScaledItem(graphics, new ItemStack(Blocks.CHEST));
                break;
            case "market":
                renderScaledItem(graphics, new ItemStack(Blocks.RAW_GOLD_BLOCK));
                break;
            case "shop":
                renderScaledItem(graphics, new ItemStack(Blocks.GOLD_BLOCK));
                break;
            default:
                // Custom texture rendering
                Identifier iconTexture = Identifier.fromNamespaceAndPath(VersionnameChange.MOD_ID, this.iconFilepath);

                // Add padding inside the button (e.g., 4px inset)
                int padding = 4;
                graphics.blit(
                        RenderPipelines.GUI_TEXTURED,
                        iconTexture,
                        this.getX() + padding,
                        this.getY() + padding,
                        0,
                        0,
                        this.width - (padding * 2),
                        this.height - (padding * 2),
                        this.width - (padding * 2),
                        this.height - (padding * 2)
                );
                break;
        }
    }

    /**
     * Helper method to render an item centered and scaled inside the widget bounds.
     */
    private void renderScaledItem(GuiGraphicsExtractor graphics, ItemStack itemStack) {
        float scaleX = (float) this.width / 16.0f;
        float scaleY = (float) this.height / 16.0f;

        graphics.pose().pushMatrix();
        // Translate matrix to widget top-left position
        graphics.pose().translate(this.getX(), this.getY());
        // Scale matrix down/up based on widget dimensions vs standard 16x16 item size
        graphics.pose().scale(scaleX, scaleY);

        // Render item at local (0,0) of the matrix transformation
        graphics.item(itemStack, 0, 0);
        graphics.pose().popMatrix();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput builder) {
        this.defaultButtonNarrationText(builder);
    }

    public boolean isWanted() {
        return this.iswanted;
    }
}