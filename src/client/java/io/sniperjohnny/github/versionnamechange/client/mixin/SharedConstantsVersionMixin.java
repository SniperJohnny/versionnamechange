package io.sniperjohnny.github.versionnamechange.client.mixin;

import net.minecraft.SharedConstants;
import net.minecraft.WorldVersion;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackFormat;
import net.minecraft.world.level.storage.DataVersion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Date;

@Mixin(SharedConstants.class)
public class SharedConstantsVersionMixin {

    @Inject(
            method = "getCurrentVersion",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void modifyVersion(CallbackInfoReturnable<WorldVersion> cir) {
        WorldVersion original = cir.getReturnValue();
        if (original == null) return;

        cir.setReturnValue(new WorldVersion() {
            @Override
            public String id() {
                return original.id().replace("26.1", "1.22");
            }

            @Override
            public String name() {
                return original.name().replace("26.1", "1.22");
            }

            @Override
            public DataVersion dataVersion() {
                return original.dataVersion();
            }

            @Override
            public PackFormat packVersion(PackType type) {
                return original.packVersion(type);
            }

            @Override
            public int protocolVersion() {
                return original.protocolVersion();
            }

            @Override
            public Date buildTime() {
                return original.buildTime();
            }

            @Override
            public boolean stable() {
                return original.stable();
            }
        });
    }
}