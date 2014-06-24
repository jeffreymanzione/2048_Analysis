package player;

public class LookAheadScorePlayer2048 extends LookAheadPlayer2048 {

	public LookAheadScorePlayer2048(int looks) {
		super((ms1, ms2) -> Integer.compare(ms1.score, ms2.score), looks);
	}

}
