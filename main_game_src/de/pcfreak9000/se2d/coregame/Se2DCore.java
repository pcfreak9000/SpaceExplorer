package de.pcfreak9000.se2d.coregame;

import de.pcfreak9000.se2d.coregame.start.PlanetDefinition;
import de.pcfreak9000.se2d.coregame.start.StartPlanetDef;
import de.pcfreak9000.se2d.game.core.GameRegistry;
import de.pcfreak9000.se2d.mod.Instance;
import de.pcfreak9000.se2d.mod.Mod;
import de.pcfreak9000.se2d.mod.event.Se2DModInitEvent;
import de.pcfreak9000.se2d.mod.event.Se2DModPostInitEvent;
import de.pcfreak9000.se2d.mod.event.Se2DModPreInitEvent;
import omnikryptec.event.eventV2.EventSubscription;

@Mod(id = "SpaceExplorer2D-coregame", name = "SpaceExplorer2D", resourceLocation = "", version = { 0, 0, 1 })
public class Se2DCore {

	@Instance(id = "SpaceExplorer2D-coregame")
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
