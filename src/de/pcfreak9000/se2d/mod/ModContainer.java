package de.pcfreak9000.se2d.mod;

import java.util.Arrays;

public class ModContainer {

	private Mod mod;
	private Class<?> mainclass;
	private Object instance;

	public ModContainer(Class<?> mc, Mod mod, Object instance) {
		this.mainclass = mc;
		this.mod = mod;
		this.instance = instance;
	}

	public Mod getMod() {
		return mod;
	}

	public Object getInstance() {
		return instance;
	}

	public Class<?> getModClass() {
		return mainclass;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ModContainer)) {
			return false;
		}
		ModContainer other = (ModContainer) o;
		if (other.mainclass.getName().equals(this.mainclass.getName())) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return mod.id() + " " + Arrays.toString(mod.version());
	}
}
