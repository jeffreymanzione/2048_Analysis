package player;

import game.Game2048;
import game.Move;
import game.MoveSequence;

import java.util.ArrayList;
import java.util.List;

public class ImprovedGreedyPlayer2048 extends Player2048 {

	private final int looks;
	
	public ImprovedGreedyPlayer2048(int looks) {
		this.looks = looks;
	}
	
	protected int choose(List<Move> map) {
		List<MoveSequence> seqs = new ArrayList<>();
		map.forEach(v -> seqs.add(new MoveSequence(v)) );
		List<MoveSequence> res = choose(seqs, looks);
		
		//System.out.println(res.size());
		
		
		return res.stream().max(MoveSequence::compareTo).get().moves.get(0).dir;
	}
	
	private List<MoveSequence> choose(List<MoveSequence> seqs, int level) {
		if (level > 0) {
			//System.out.println(seqs.size());
			List<MoveSequence> news = new ArrayList<>();
			
			seqs.forEach(seq -> Game2048.ifShifts(seq.last.board).stream().forEach(mov -> news.add(seq.add(mov))));
			return choose(news, level - 1);
		} else {
			return seqs;
		}
	}

}
