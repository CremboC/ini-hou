package seprini.data;

import com.badlogic.gdx.math.Vector2;

public final class Config {

	// Used for debugging all around
	public final static boolean DEBUG_UI = false;
	public final static boolean DEBUG_TEXT = false;

	// General, graphics related settings
	public final static String GAME_TITLE = "Controller Concern";
	public final static int SCREEN_WIDTH = 1280;
	public final static int SCREEN_HEIGHT = 720;
	public final static boolean VSYNC = true;
	public final static boolean RESIZABLE = true;

	// Art related config
	public final static Vector2 AIRSPACE_SIZE = new Vector2(1080, SCREEN_HEIGHT);
	public final static Vector2 WAYPOINT_SIZE = new Vector2(20, 20);
	public final static Vector2 EXIT_WAYPOINT_SIZE = new Vector2(50, 50);
	public final static Vector2 AIRPORT_SIZE = new Vector2(164, 125);

	// Multiplayer config
	public final static Vector2 MULTIPLAYER_SIZE = new Vector2(SCREEN_WIDTH,
			SCREEN_HEIGHT);
	public final static int[] NO_MAN_LAND = { 540, 640, 740 };

	// UI related
	public final static Vector2 SIDEBAR_SIZE = new Vector2(200, SCREEN_HEIGHT);

	// Game related
	public final static float AIRCRAFT_SPEED_MULTIPLIER = 40f / 3f;
	// Minimum vertical altitude between aircraft that must be maintained when
	// passing over one another to avoid a crash.
	public final static int MIN_ALTITUDE_DIFFERENCE = 500;
	// The altitude 'levels'
	public static final int[] ALTITUDES = { 0, 1000, 2000, 5000, 10000, 15000 };
	// Time that must elapse before an aircraft can take off after landing
	public static final int AIRCRAFT_TAKEOFF_AND_LANDING_DELAY = 10;
	// Number of points held by an aircraft when it enters the airspace
	public static final int AIRCRAFT_POINTS = 20;
	// lump sum given to player who didn't crash, when a crash occurs.
	public static final int MULTIPLAYER_CRASH_BONUS = 0;
	public static final int MIN_DIST_BETWEEN_ENTRY_EXIT_WAYPOINTS = 300;

	// other
	public final static String COPYRIGHT_NOTICE = "Copyright Disclaimer Under Section 107 of the Copyright Act 1976, allowance is made "
			+ "for 'fair use' for purposes such as criticism, comment, news reporting, teaching, "
			+ "scholarship, and research. Fair use is a use permitted by copyright statute that "
			+ "might otherwise be infringing. Non-profit, educational or personal use tips the "
			+ "balance in favor of fair use.";

}
