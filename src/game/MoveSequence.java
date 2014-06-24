package game;

import java.util.List;
import java.util.ArrayList;

public class MoveSequence implements Comparable<MoveSequence>, Cloneable {
	public List<Move> moves;
	public int score;
	public Move last;
	
	public MoveSequence(Move mov) {
		moves = new ArrayList<>();
		moves.add(mov);
		score = mov.score;
		last = mov;
	}
	
	private MoveSequence(MoveSequence seq) {
		moves = new ArrayList<>();
		moves.addAll(seq.moves);
		score = seq.score;
	}
	
	public MoveSequence add(Move move) {
		MoveSequence clone = this.clone();
		clone.moves.add(move);
		clone.score += move.score;
		clone.last = move;
		return clone;
	}

	public int compareTo(MoveSequence ms) {
		return Integer.compare(score, ms.score);
	}
	
	public MoveSequence clone() {
		MoveSequence clone = new MoveSequence(this);
		return clone;
	}
}
