package game;

import java.util.List;
import java.util.ArrayList;

public class MoveSequence implements Cloneable {
	public List<Move> moves;
	public int score;
	public int numMerges;
	public Move last;
	
	public MoveSequence(Move mov) {
		moves = new ArrayList<>();
		moves.add(mov);
		score = mov.score;
		numMerges = mov.numMerges;
		last = mov;
	}
	
	private MoveSequence(MoveSequence seq) {
		moves = new ArrayList<>();
		moves.addAll(seq.moves);
		score = seq.score;
		numMerges = seq.numMerges;
	}
	
	public MoveSequence add(Move move) {
		MoveSequence clone = this.clone();
		clone.moves.add(move);
		clone.score += move.score;
		clone.numMerges += move.numMerges;
		clone.last = move;
		return clone;
	}
	
	public MoveSequence clone() {
		MoveSequence clone = new MoveSequence(this);
		return clone;
	}
}
