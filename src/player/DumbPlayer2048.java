package player;

import game.Game2048;
import game.Move;
import game.MoveSequence;

import java.util.ArrayList;
import java.util.List;

import com.jeffreymanzione.maps.heaps.BinaryHeap;
import com.jeffreymanzione.maps.heaps.Heap;

public class DumbPlayer2048 extends Player2048 {

	protected int choose(List<Move> map) {
		return map.parallelStream().map(m -> optimal(m, 1)).max((m1, m2) -> Integer.compare(m1.score, m2.score)).get().dir;
	}

	// private class Point {
	// Point(int x, int y) {
	// this.x = x;
	// this.y = y;
	// }
	//
	// int x, y;
	// }

	private Move optimal(Move movz, int iterations) {
		// List<MoveSequence> moves = new ArrayList<MoveSequence>();
		Heap<Integer, MoveSequence> moves = new BinaryHeap<>();

		moves.insert(movz.score, new MoveSequence(movz));

		for (int iters = 0; iters < iterations; iters++) {
			// System.out.println(moves.size());
			Heap<Integer, MoveSequence> movHeap = new BinaryHeap<>();

			while (!moves.isEmpty()) {
				MoveSequence movSeq = moves.delete();
				for (Move mov : Game2048.ifShifts(movSeq.moves.get(0).board)) {
					for (int i = 0; i < mov.board.length; i++) {
						for (int j = 0; j < mov.board[0].length; j++) {
							if (mov.board[i][j] == 0) {

								int[][] b = Game2048.copyBoard(mov.board);
								b[i][j] = 2;

								MoveSequence mov2 = movSeq.clone();
								mov2 = mov2.add(new Move(b, mov.score, mov.numMerges, mov.dir, mov.maxBlock));
								movHeap.insert(mov2.score, mov2);

								b = Game2048.copyBoard(mov.board);
								b[i][j] = 4;

								MoveSequence mov4 = movSeq.clone();
								mov4 = mov4.add(new Move(b, mov.score, mov.numMerges, mov.dir, mov.maxBlock));
								movHeap.insert(mov4.score, mov4);

							}
						}
					}
				}
			}

			moves = movHeap;
		}
		return moves.delete().moves.get(0);
	}

}
