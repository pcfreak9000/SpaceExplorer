package de.pcfreak9000.renderer;

import java.util.Comparator;

import omnikryptec.gameobject.Sprite;
import omnikryptec.graphics.SpriteBatch;
import omnikryptec.renderer.d2.DefaultRenderer2D;

public class PlanetRenderer extends DefaultRenderer2D{

	private static final Comparator<Sprite> COMP = new Comparator<Sprite>() {

		@Override
		public int compare(Sprite o1, Sprite o2) {
			if(o1.getLayer()!=o2.getLayer()) {
				return (int) Math.signum(o1.getLayer()-o2.getLayer());
			} else {
				return (int) Math.signum(o1.getTransform().getPosition(true).y-o2.getTransform().getPosition(true).y);
			}
		}
	};
	
	public PlanetRenderer() {
		super(new SpriteBatch(100_000));
		clearColor().set(0.1f, 0.1f, 0.1f, 1);
	}
	
	@Override
	public Comparator<Sprite> getLayerComparator() {
		return COMP;
	}
	
}
