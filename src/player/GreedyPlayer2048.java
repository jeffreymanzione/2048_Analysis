package player;

import java.util.ArrayList;
import java.util.List;

import game.Move;

public class GreedyPlayer2048 extends Player2048 {

	@Override
	protected int choose(List<Move> moves) {
		
		List<Integer> list = new ArrayList<Integer>();
		moves.forEach(k -> list.add(k.dir));
		
		return list.stream().max((x, y)-> Integer.compare(moves.get(x).score, moves.get(y).score)).get();
	}

}
