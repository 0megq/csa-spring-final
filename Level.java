public class Level {
	public static final Level LEVEL1 = new Level(
			new Vector2(40, 320),
			new AABB[] { new AABB(20, 340, 60, 30), new AABB(80, 300, 100, 90), new AABB(180, 240, 80, 110),
					new AABB(260, 280, 180, 80), new AABB(570, 100, 30, 460),
					new AABB(440, 240, 69, 140), new AABB(516, 240, 100, 140), new AABB(509, 247, 7, 133) },
			new AABB(516, 246, 7, 1),
			3);
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