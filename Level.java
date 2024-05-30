public class Level {
	public static final Level LEVEL1 = new Level(
			new Vector2(60, 280),
			new AABB[] { new AABB(20, 260, 120, 100), new AABB(140, 300, 100, 80), new AABB(240, 240, 120, 110),
					new AABB(360, 200, 100, 110), new AABB(550, 100, 50, 290),
					new AABB(460, 240, 49, 140), new AABB(516, 240, 34, 140), new AABB(509, 247, 7, 133) },
			new AABB(509, 245, 7, 2),
			3);
	public static final Level LEVEL2 = new Level(
			new Vector2(40, 320),
			new AABB[] { new AABB(20, 340, 60, 30), new AABB(80, 300, 100, 90), new AABB(180, 240, 80, 110),
					new AABB(260, 280, 180, 80), new AABB(570, 100, 30, 290),
					new AABB(440, 240, 69, 140), new AABB(516, 240, 84, 140), new AABB(509, 247, 7, 133) },
			new AABB(509, 245, 7, 2),
			3);
	public static final Level LEVEL3 = new Level(
			new Vector2(190, 300),
			new AABB[] { new AABB(460, 80, 100, 310), new AABB(240, 120, 80, 140),
					new AABB(320, 160, 80, 80), new AABB(100, 150, 60, 230), new AABB(160, 320, 300, 50),
					new AABB(160, 180, 50, 60), new AABB(220, 180, 20, 60), new AABB(0, 20, 40, 300),
					new AABB(40, 220, 27, 60), new AABB(74, 220, 26, 60), new AABB(67, 227, 7, 53) },
			new AABB(67, 225, 7, 2), 4);
	public static final Level LEVEL4 = new Level(
			new Vector2(160, 300),
			new AABB[] {new AABB(20, 120, 80, 260), new AABB(120, 280, 240, 80), new AABB(320, 200, 120, 100), new AABB(440, 120, 120, 200), new AABB(560, 40, 40, 320),
				new AABB( 150, 160, 30, 80), new AABB( 220, 160, 80, 80), new AABB( 170, -120, 60, 220), new AABB(280, 60, 47, 60),
				new AABB(334, 60, 66, 60), new AABB(327, 67, 7, 53), new AABB(100, 200, 25, 180),
				new AABB(180, 190, 40, 40), new AABB(255, 155, 5, 10)},
			new AABB(327, 65, 7, 2), 5);
	public static final Level[] LEVELS = { LEVEL1, LEVEL2, LEVEL3, LEVEL4 };
	public final Vector2 BALL_START;
	public final AABB[] TERRAIN;
	public final AABB HOLE;
	public final int PAR;

	public Level(Vector2 ballStart, AABB[] terrain, AABB hole, int par) {
		BALL_START = ballStart;
		TERRAIN = terrain;
		HOLE = hole;
		PAR = par;
	}
}