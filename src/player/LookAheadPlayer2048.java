package player;

import game.Game2048;
import game.Move;
import game.MoveSequence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class LookAheadPlayer2048 extends Player2048 {

	//private static final Object lockable = new Object();

	private Function<MoveSequence, Integer> fun;

	private final int looks;

	public LookAheadPlayer2048(int looks, Function<MoveSequence, Integer> fun) {
		this.looks = looks;
		this.fun = fun;
	}

	@Override
	protected int choose(List<Move> map) {
		List<MoveSequence> seqs = new ArrayList<>();
		map.forEach(v -> seqs.add(new MoveSequence(v)));
		List<MoveSequence> res = choose(seqs, looks);

		// System.out.println(res.size());

		Function<List<MoveSequence>, Integer> toInt = mov1 -> mov1.stream().mapToInt(mov -> fun.apply(mov)).sum();
		//synchronized (lockable) {
			return categorize(res).stream()
					.reduce((mov1, mov2) -> Integer.compare(toInt.apply(mov1), toInt.apply(mov2)) >= 0 ? mov1 : mov2)
					.filter(msl -> !msl.isEmpty()).get().get(0).moves.get(0).dir;
		//}

	}

	private static Collection<List<MoveSequence>> categorize(Collection<MoveSequence> seqs) {

		Collection<List<MoveSequence>> movSeqs = new ArrayList<>();

		//System.out.println("SEQS: " + seqs.size());
		IntStream.range(Game2048.UP, Game2048.RIGHT + 1).forEach(i -> {
			List<MoveSequence> movs = new ArrayList<>();
			seqs.stream().filter(seq -> seq.moves.get(0).dir == i).forEach(e -> movs.add(e));

			//System.out.println("DIR: " + i + "\nSIZE: " + movs.size());
			//System.out.flush();
			if (movs.size() > 0) {
				movSeqs.add(movs);
			}
		});
		//System.out.println("MVSQ: " + movSeqs.size() + "\n");

		return movSeqs;
	}

	private List<MoveSequence> choose(List<MoveSequence> seqs, int level) {
		if (level > 0) {
			// System.out.println(seqs.size());
			List<MoveSequence> news = new ArrayList<>();

			seqs.forEach(seq -> Game2048.ifShifts(seq.last.board).stream().forEach(mov -> news.add(seq.add(mov))));
			return choose(news, level - 1);
		} else {
			return seqs;
		}
	}

}
