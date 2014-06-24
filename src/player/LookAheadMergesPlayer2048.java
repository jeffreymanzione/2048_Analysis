package player;

public class LookAheadMergesPlayer2048 extends LookAheadPlayer2048 {

	public LookAheadMergesPlayer2048(int looks) {
		super((ms1, ms2) -> Integer.compare(ms1.numMerges, ms2.numMerges), looks);
	}

}
