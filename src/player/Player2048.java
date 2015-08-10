package player;

import java.util.List;
import game.Game2048;
import game.Move;

public abstract class Player2048 {
	protected Game2048 game;
	
	public void play(Game2048 game) {
		this.game = game;
		
		while (!game.hasLost()) {
			game.shift(choose(game.ifShifts()));
			
			//System.out.println(game);
		}
	}

	protected abstract int choose(List<Move> map);
	
	public int getScore() {
		return game.getScore();
	}

	public int getMaxBlock() {
		return game.getMaxBlock();
	}

}
