package de.pcfreak9000.se2d.universe.worlds;

import de.pcfreak9000.se2d.util.Private;

/**
 * Thrown in the {@link Chunk}
 * @author pcfreak9000
 *
 */
@Private
public class ChunkCompilationStatusException extends RuntimeException {

	/**
	 * whatever
	 */
	private static final long serialVersionUID = 5392148752195289831L;

	public ChunkCompilationStatusException(String msg) {
		super(msg);
	}

}
