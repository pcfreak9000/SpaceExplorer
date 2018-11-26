package de.pcfreak9000.spaceexplorer.universe;

import java.util.Random;

import de.omnikryptec.old.gameobject.Camera;
import de.omnikryptec.old.main.OmniKryptecEngine;
import de.pcfreak9000.spaceexplorer.game.core.GameRegistry;
import de.pcfreak9000.spaceexplorer.game.core.Player;
import de.pcfreak9000.spaceexplorer.game.launch.SpaceExplorer2D;
import de.pcfreak9000.spaceexplorer.universe.celestial.CelestialBody;

public class Universe {

	private Player currentPlayer = null;
	private Camera planetCamera;

	private double gametime = 0;

	public void update() {
		// System.out.println(Instance.getFPSCounted());
		gametime += OmniKryptecEngine.instance().getDeltaTimef();
	}

	public double getUniverseTimeSec() {
		return gametime;
	}

	public void loadWorld(long seed) {
		planetCamera = new Camera()
				.setOrthographicProjection2D(SpaceExplorer2D.getSpaceExplorer2D().getProjectionData());
		currentPlayer = new Player();
		// setPlanetAndPlayer(new Planet(new Random().nextInt()), currentPlayer);
		Random rand = new Random(seed);
		CelestialBody p = GameRegistry.getCelestialBodyRegistry().getStartCapables()
				.get(rand.nextInt(GameRegistry.getCelestialBodyRegistry().getStartCapables().size()))
				.generate(seed, new SpaceCoordinates(), null);
		p.getWorld().load(currentPlayer);
		System.out.println(p);
	}

	public Camera getPlanetCamera() {
		return planetCamera;
	}

	public void setPlanetAndPlayer(Player player) {
		// System.out.println(planet.toString());
		// player.getComponent(PhysicsComponent2D.class).getBody().getTransform()
		// .setTranslation(ConverterUtil.convertToPhysics2D(
		// new Vector2f(0, planet.getPlanetData().getFadeRadius() *
		// TileDefinition.TILE_SIZE)));
		// planet.setAsScene(player);
	}

	public String getGalaxyName(double x, double y) {
		return "P";
	}
}