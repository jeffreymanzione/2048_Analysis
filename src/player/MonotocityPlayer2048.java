package player;

import game.Game2048;
import game.Move;

import java.util.ArrayList;
import java.util.List;

public class MonotocityPlayer2048 extends Player2048 {

	@Override
	protected int choose(List<Move> map) {

		// Map<MaxPos, Integer> dirs = MaxPos.getCorners(game.getBoard());
		//
		// List<Integer> avlDirs = new ArrayList<>();
		// map.stream().map(m -> m.dir).forEach(dir -> avlDirs.add(dir));
		//
		// for (Move mov : map) {
		// // mov.dir
		// }

		Corner most = Corner.getCorner(game.getBoard());
		List<Integer> dirs = most.getDirs();
		Move move = map.stream().filter(s -> dirs.stream().anyMatch(i -> i == s.dir))
				.max((m1, m2) -> Integer.compare(m1.score, m2.score)).orElse(null);

		if (move == null) {
			move = map.get(0);
		}

		return move.dir;
	}

	private enum Corner {
		UpperLeft(0, 0), LowerLeft(0, 1), UpperRight(1, 0), LowerRight(1, 1);

		public final int x, y;

		Corner(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public List<Integer> getDirs() {
			List<Integer> dirs = new ArrayList<Integer>();
			if (this.x == 0) {
				dirs.add(Game2048.LEFT);
			} else {
				dirs.add(Game2048.RIGHT);
			}

			if (this.y == 0) {
				dirs.add(Game2048.UP);
			} else {
				dirs.add(Game2048.DOWN);
			}

			return dirs;
		}

		private static Corner getCorner(int[][] board) {

			int maxScore = 0;
			Corner maxCorner = UpperLeft;

			int i, j;

			for (Corner corner : Corner.values()) {
				i = corner.x;
				j = corner.y;
				int score = board[i * 2][j * 2] + board[i * 2 + 1][j * 2] + board[i * 2][j * 2 + 1]
						+ board[i * 2 + 1][j * 2 + 1];
				if (score > maxScore) {
					maxScore = score;
					maxCorner = LowerLeft;
				}
			}

			return maxCorner;

		}
	}

}
