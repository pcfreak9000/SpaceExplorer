package de.pcfreak9000.spaceexplorer.universe;

import java.util.Random;

import de.omnikryptec.old.main.OmniKryptecEngine;
import de.omnikryptec.render.Camera;
import de.pcfreak9000.space.core.GameRegistry;
import de.pcfreak9000.spaceexplorer.game.core.Player;
import de.pcfreak9000.spaceexplorer.game.launch.SpaceExplorer2D;
import de.pcfreak9000.spaceexplorer.universe.celestial.CelestialBody;

public class Universe {
    
    private Player currentPlayer = null;
    private Camera planetCamera;
    
    private double gametime = 0;
    
    public void update() {
        // System.out.println(Instance.getFPSCounted());
        this.gametime += OmniKryptecEngine.instance().getDeltaTimef();
    }
    
    public double getUniverseTimeSec() {
        return this.gametime;
    }
    
    public void loadWorld(final long seed) {
        this.planetCamera = new Camera()
                .setOrthographicProjection2D(SpaceExplorer2D.getSpaceExplorer2D().getProjectionData());
        this.currentPlayer = new Player();
        // setPlanetAndPlayer(new Planet(new Random().nextInt()), currentPlayer);
        final Random rand = new Random(seed);
        final CelestialBody p = GameRegistry.getCelestialBodyRegistry().getStartCapables()
                .get(rand.nextInt(GameRegistry.getCelestialBodyRegistry().getStartCapables().size()))
                .generate(seed, new SpaceCoordinates(), null);
        p.getWorld().load(this.currentPlayer);
        System.out.println(p);
    }
    
    public Camera getPlanetCamera() {
        return this.planetCamera;
    }
    
    public void setPlanetAndPlayer(final Player player) {
        // System.out.println(planet.toString());
        // player.getComponent(PhysicsComponent2D.class).getBody().getTransform()
        // .setTranslation(ConverterUtil.convertToPhysics2D(
        // new Vector2f(0, planet.getPlanetData().getFadeRadius() *
        // TileDefinition.TILE_SIZE)));
        // planet.setAsScene(player);
    }
    
    public String getGalaxyName(final double x, final double y) {
        return "P";
    }
}
