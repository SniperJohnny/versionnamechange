package io.sniperjohnny.github.versionnamechange.client.version;

import net.fabricmc.loader.api.FabricLoader;

import java.util.List;

/**
 * Translates Minecraft's calendar version numbers into the "old style" numbers this mod shows.
 *
 * <pre>
 *   26.1   -&gt; 1.22
 *   26.1.1 -&gt; 1.22.1
 *   26.1.2 -&gt; 1.22.2
 *   26.2   -&gt; 1.22.3
 *   26.3   -&gt; 1.22.4
 *   27.1   -&gt; 1.23
 * </pre>
 *
 * <p>The scheme bumps the old-style number by one for every Minecraft release that actually
 * shipped, in release order, so it cannot be derived from the calendar number alone: 26.1, 26.1.1
 * and 26.1.2 all share a calendar minor but consume three old-style numbers. The mapping is
 * therefore an explicit table of known releases. Everything is looked up at runtime, so a single
 * jar stays correct on every 26.x release instead of having a hardcoded version string each time.
 */
public final class VersionNameMapper {

	/** Every old-style name this mod produces starts with this major version. */
	private static final int OLD_MAJOR = 1;

	/** 26.x maps onto 1.22.x, so 26.1 is the first calendar release this mod knows about. */
	private static final int FIRST_OLD_MINOR = 22;

	/**
	 * The Minecraft releases this mod knows how to rename, in the order they shipped. A release's
	 * position in this list is its old-style patch number: the first entry is "1.22", the second
	 * "1.22.1", and so on. Add new releases to the end as they come out.
	 */
	private static final List<String> KNOWN_RELEASES = List.of(
			"26.1",   // 1.22
			"26.1.1", // 1.22.1
			"26.1.2", // 1.22.2
			"26.2",   // 1.22.3
			"26.3"    // 1.22.4
	);

	private VersionNameMapper() {
	}

	/**
	 * The Minecraft version the game is actually running, e.g. {@code "26.1.2"} or {@code "26.3"}.
	 *
	 * <p>Read from the loader rather than {@code SharedConstants#getCurrentVersion()} so this stays
	 * the <em>real</em> version even while this mod is replacing the name the game reports.
	 *
	 * @return the running version, or an empty string if it cannot be determined
	 */
	public static String runningMinecraftVersion() {
		return FabricLoader.getInstance()
				.getModContainer("minecraft")
				.map(container -> container.getMetadata().getVersion().getFriendlyString())
				.orElse("");
	}

	/**
	 * The name the mod falls back to when the player has not chosen one yet (first launch, or the
	 * reset button) for the version of Minecraft that is currently running.
	 */
	public static String defaultVersionName() {
		return toOldStyle(runningMinecraftVersion());
	}

	/**
	 * Converts a calendar version such as {@code "26.3"} into its old-style name ({@code "1.22.4"}).
	 *
	 * <p>Only releases listed in {@link #KNOWN_RELEASES} are converted. Snapshots, pre-releases and
	 * versions this jar predates are returned untouched rather than guessed at, since the scheme
	 * cannot be extrapolated from a calendar number alone.
	 */
	public static String toOldStyle(String minecraftVersion) {
		if (minecraftVersion == null || minecraftVersion.isBlank()) {
			return minecraftVersion;
		}

		int index = KNOWN_RELEASES.indexOf(minecraftVersion.trim());
		if (index < 0) {
			return minecraftVersion;
		}

		return index == 0
				? OLD_MAJOR + "." + FIRST_OLD_MINOR
				: OLD_MAJOR + "." + FIRST_OLD_MINOR + "." + index;
	}
}
