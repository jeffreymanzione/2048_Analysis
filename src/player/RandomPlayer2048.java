package player;

import game.Move;

import java.util.List;

public class RandomPlayer2048 extends Player2048 {
	
	protected int choose(List<Move> moves) {
		
		int dir;
		
		while(moves.get(dir = game.rand.nextInt(4)) == null);
		
		return dir;
		
	}
}
