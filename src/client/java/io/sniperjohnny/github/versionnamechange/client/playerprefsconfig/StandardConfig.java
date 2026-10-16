package io.sniperjohnny.github.versionnamechange.client.playerprefsconfig;

import io.sniperjohnny.github.versionnamechange.client.version.VersionNameMapper;

public class StandardConfig {
    /**
     * The name the mod resets to. It follows the version of Minecraft that is running, so the same
     * jar means "1.22.2" on 26.1.2 and "1.22.4" on 26.3.
     */
    public String standardmodversionnumber = VersionNameMapper.defaultVersionName();
}
