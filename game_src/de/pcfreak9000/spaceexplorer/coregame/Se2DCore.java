package de.pcfreak9000.spaceexplorer.coregame;

import de.omnikryptec.old.event.eventV2.EventSubscription;
import de.pcfreak9000.spaceexplorer.coregame.start.PlanetDefinition;
import de.pcfreak9000.spaceexplorer.coregame.start.StartPlanetDef;
import de.pcfreak9000.spaceexplorer.game.core.GameRegistry;
import de.pcfreak9000.spaceexplorer.mod.Instance;
import de.pcfreak9000.spaceexplorer.mod.Mod;
import de.pcfreak9000.spaceexplorer.mod.ModLoader;
import de.pcfreak9000.spaceexplorer.mod.event.Se2DModInitEvent;
import de.pcfreak9000.spaceexplorer.mod.event.Se2DModPostInitEvent;
import de.pcfreak9000.spaceexplorer.mod.event.Se2DModPreInitEvent;

@Mod(id = "SpaceExplorer2D-coregame", name = "SpaceExplorer2D", resourceLocation = "", version = { 0, 0, 1 })
public class Se2DCore {

	@Instance(id = ModLoader.THIS_INSTANCE_ID)
	private static Se2DCore instance;

	@EventSubscription
	public void preInit(Se2DModPreInitEvent pre) {
		GameRegistry.getCelestialBodyRegistry().register("startplanetdef", new StartPlanetDef()).register("planetdef",
				new PlanetDefinition());
	}

	@EventSubscription
	public void init(Se2DModInitEvent init) {

	}

	@EventSubscription
	public void postInit(Se2DModPostInitEvent post) {

	}

}
