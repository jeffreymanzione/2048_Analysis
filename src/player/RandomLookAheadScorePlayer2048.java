package player;

public class RandomLookAheadScorePlayer2048 extends RandomLookAheadPlayer2048 {
	public RandomLookAheadScorePlayer2048(int looks) {
		super(looks, (ms) -> ms.score);
	}
}
