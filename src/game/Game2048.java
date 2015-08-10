package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import player.*;

public class Game2048 {

	public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;

	public Random rand = new Random();

	private int[][] board = new int[4][4];

	private List<Integer> empties = new ArrayList<>();

	private int score = 0;

	public Game2048() {
		for (int i = 0; i < 16; i++) {
			empties.add(i);
		}

		random();
		random();
	}

	public int[][] getBoard() {
		return copyBoard(this.board);
	}

	public static int[][] copyBoard(int[][] board) {
		int[][] boardCopy = new int[4][4];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				boardCopy[i][j] = board[i][j];
			}
		}

		return boardCopy;
	}

	private void random() {
		int loc = rand.nextInt(empties.size());
		int val = rand.nextInt(2) + 1;

		loc = empties.remove(loc);

		board[loc % 4][loc / 4] = val;

		// System.out.println("Random " + (loc % 4) + "," + (loc / 4));
	}

	public List<Move> ifShifts() {
		return ifShifts(getBoard());
	}

	public static List<Move> ifShifts(int[][] board) {
		List<Move> lst = new ArrayList<>();
		for (int i = UP; i <= RIGHT; i++) {
			Move mov = ifShift(board, i);
			if (mov != null)
				lst.add(mov);
		}

		return lst;
	}

	public static Move ifShift(int[][] board, int dir) {
		int score = 0;
		int numMerges = 0;

		int maxBlock = 1;

		board = copyBoard(board);

		int start;
		int end;
		boolean vert = true, back = false;

		if (dir == UP || dir == DOWN) {
			vert = false;
		}

		if (dir == UP || dir == LEFT) {
			start = 0;
			end = 3;
			back = true;
		} else {
			start = 3;
			end = 0;
		}

		boolean moved = false;

		for (int j = 0; j < 4; j++) {
			boolean combined = false;
			for (int i = start; i != end; i += (end > start ? 1 : -1)) {

				int x = vert ? j : i, y = vert ? i : j, xn = (vert ? j : i + (back ? 1 : -1)), yn = (vert ? i
						+ (back ? 1 : -1) : j);

				// System.out.println(x + "," + y + " " + xn + "," + yn);

				int pos = board[x][y];
				int neb = board[xn][yn];

				boolean found = false;

				int k, posk = pos;
				for (k = i + (back ? 1 : -1); (end > start ? k <= end : k >= end) && posk == 0; k += (end > start ? 1
						: -1)) {
					int xk = vert ? j : k, yk = vert ? k : j;
					posk = board[xk][yk];

					if (posk != 0) {
						found = true;
						board[x][y] = board[xk][yk];
						board[xk][yk] = 0;
						moved = true;
					}
				}

				if (found && !combined && i != start) {
					i -= (end > start ? 2 : -2);

				}

				pos = board[x][y];
				neb = board[xn][yn];

				if (pos != 0 && pos == neb && !combined) {
					board[x][y]++;
					int block = (int) Math.pow(2, board[x][y]);
					if (block > maxBlock) {
						maxBlock = block;
					}
					score += block;
					board[xn][yn] = 0;
					numMerges++;
					combined = true;
					moved = true;
				} else {
					combined = false;
				}

			}
		}

		if (moved) {
			return new Move(board, score, numMerges, dir, maxBlock);
		} else {
			return null;
		}

	}

	public Move ifShift(int dir) {
		return ifShift(getBoard(), dir);
	}

	public void shift(int dir) {

		Move move = ifShift(dir);
		this.board = move.board;
		this.score += move.score;

		empties.clear();
		for (int i = 0; i < 16; i++) {
			if (board[i % 4][i / 4] == 0) {
				empties.add(i);
			}
		}

		random();

		if (empties.size() == 0 && !hasMergables()) {
			lost = true;
		}

	}

	private boolean lost = false;

	private boolean hasMergables() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {

				int pos = board[i][j];

				if (pos != 0) {

					if (i > 0 && pos == board[i - 1][j]) {
						return true;
					}

					if (i < 3 && pos == board[i + 1][j]) {
						return true;
					}

					if (j > 0 && pos == board[i][j - 1]) {
						return true;
					}

					if (j < 3 && pos == board[i][j + 1]) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean hasLost() {
		return lost;
	}

	public int getScore() {
		return score;
	}

	@Override
	public String toString() {
		String result = "";

		for (int[] bo : board) {
			for (int b : bo) {
				result += b + " " + (b > 9 ? "" : " ");
			}
			result += "\n";
		}

		result += "Score: " + score + "\n";

		return result;
	}

	public int getMaxBlock() {
		int maxBlock = 1;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (board[i][j] > maxBlock) {
					maxBlock = board[i][j];
				}
			}
		}
		return maxBlock;
	}

	public static void main(String[] args) {

		// try (PrintWriter out = new PrintWriter(new File("out.csv"))) {
		IntStream.range(0, 10_000).forEach(i -> {
			Player2048 player = new LookAheadScorePlayer2048(3);
			player.play(new Game2048());
			// out.println(player.getScore() + "," + player.getMaxBlock());
				System.out.println(player.getScore() + "," + player.getMaxBlock());
			});
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }

		// GameThreadPool pool = new GameThreadPool(() -> new
		// RandomLookAheadScorePlayer2048(3), Runtime.getRuntime()
		// .availableProcessors(), 11);
		// Game2048 game = pool.get();
		// System.out.println("Largest block: " + (int) Math.pow(2,
		// game.getMaxBlock()) + "\nScore: " + game.getScore()
		// + "\nTries: " + pool.getTries() + "\nTime: " + ((double)
		// pool.getTime()) / 1000 + "s");

	}
}
