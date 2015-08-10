package player;

public class LookAheadMergesPlayer2048 extends LookAheadPlayer2048 {

	public LookAheadMergesPlayer2048(int looks) {
		super(looks, (ms) -> ms.numMerges);
	}

}
