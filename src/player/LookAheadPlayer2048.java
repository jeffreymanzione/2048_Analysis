package player;

import game.Game2048;
import game.Move;
import game.MoveSequence;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LookAheadPlayer2048 extends Player2048 {

	private Comparator<MoveSequence> msComparator;
	
	private final int looks;

	public LookAheadPlayer2048(Comparator<MoveSequence> msComparator, int looks) {
		this.msComparator = msComparator;
		this.looks = looks;
	}

	protected int choose(List<Move> map) {
		List<MoveSequence> seqs = new ArrayList<>();
		map.forEach(v -> seqs.add(new MoveSequence(v)));
		List<MoveSequence> res = choose(seqs, looks);

		// System.out.println(res.size());

		return res.stream()
				.max((ms1, ms2) -> msComparator.compare(ms1, ms2)).get().moves
				.get(0).dir;
	}

	private List<MoveSequence> choose(List<MoveSequence> seqs, int level) {
		if (level > 0) {
			// System.out.println(seqs.size());
			List<MoveSequence> news = new ArrayList<>();

			seqs.forEach(seq -> Game2048.ifShifts(seq.last.board).stream()
					.forEach(mov -> news.add(seq.add(mov))));
			return choose(news, level - 1);
		} else {
			return seqs;
		}
	}

}
