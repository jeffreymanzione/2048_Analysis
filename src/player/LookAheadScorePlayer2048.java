package player;

public class LookAheadScorePlayer2048 extends LookAheadPlayer2048 {

	public LookAheadScorePlayer2048(int looks) {
		super(looks, (ms) -> ms.score);
	}

}
