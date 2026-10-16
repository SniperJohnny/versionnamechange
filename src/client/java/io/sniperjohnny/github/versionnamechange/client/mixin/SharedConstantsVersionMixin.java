package io.sniperjohnny.github.versionnamechange.client.mixin;

import io.sniperjohnny.github.versionnamechange.client.playerprefsconfig.PlayerPrefsConfigManager;
import io.sniperjohnny.github.versionnamechange.client.version.VersionNameMapper;
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
                // Derived from whatever version is actually running, so this is correct on 26.1.x,
                // 26.2 and 26.3 alike instead of only on the version it was compiled against.
                return VersionNameMapper.toOldStyle(original.id());
            }

            @Override
            public String name() {
                String configured = PlayerPrefsConfigManager.getConfig().newVersionName;
                if (configured == null || configured.isBlank()) {
                    // Fall back to the version's own name rather than showing an empty title.
                    return VersionNameMapper.toOldStyle(original.name());
                }
                return configured;
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