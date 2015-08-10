package player;

import game.Move;

import java.util.List;
import java.util.Scanner;

public class HumanPlayer2048 extends Player2048 {
	Scanner scan = new Scanner(System.in);

	@Override
	protected int choose(List<Move> moves) {
		System.out.println(game);
		moves.forEach(k -> { System.out.print(getLetter(k.dir) + " "); } );
		System.out.println();
		switch(scan.next()) {
		case "U":
			return moves.get(0) != null ? 0 : choose(moves);
		case "D":
			return moves.get(1) != null ? 1 : choose(moves);
		case "L":
			return moves.get(2) != null ? 2 : choose(moves);
		case "R":
			return moves.get(3) != null ? 3 : choose(moves);
		default:
			System.out.println("Invalid input.");
			return choose(moves);
		}
	}

	private String getLetter(int val) {
		switch (val) {
		case 0:
			return "U";
		case 1:
			return "D";
		case 2:
			return "L";
		default:
			return "R";
		}
	}
}