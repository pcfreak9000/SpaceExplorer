package de.pcfreak9000.se2d.universe;

import de.pcfreak9000.se2d.game.core.Player;
import de.pcfreak9000.se2d.game.launch.SpaceExplorer2D;
import omnikryptec.gameobject.Camera;
import omnikryptec.main.OmniKryptecEngine;

public class Universe {

	private Player currentPlayer = null;
	private Camera planetCamera;

	private double gametime = 0;

	public void update() {
		gametime += OmniKryptecEngine.instance().getDeltaTimef();
	}

	public double getUniverseTimeSec() {
		return gametime;
	}

	public void loadWorld() {
		planetCamera = new Camera()
				.setOrthographicProjection2D(SpaceExplorer2D.getSpaceExplorer2D().getProjectionData());
		//currentPlayer = new Player();
		// setPlanetAndPlayer(new Planet(new Random().nextInt()), currentPlayer);
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
