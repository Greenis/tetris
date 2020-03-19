package tetris.enumeration;

/**
 * Game life circle state. Has one of 4 states:
 * INITIALIZED(game just was created),
 * STOPPED(game cannot be resume),
 * RUNNING(game in active state),
 * PAUSE(game was paused, and can be resumed).
 */
public enum GameLifeCircle {

	INITIALIZED,
	STOPPED,
	RUNNING,
	PAUSE;
	
}
