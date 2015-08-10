package game;


public class Move implements Comparable<Move> {
	public final int[][] board;
	public final int score, dir, maxBlock;
	public final int numMerges;
	
	public Move(int[][] board, int score, int numMerges, int dir, int maxBlock) {
		this.board = board;
		this.score = score;
		this.numMerges = numMerges;
		this.dir = dir;
		this.maxBlock = maxBlock;
	}
	
	@Override
	public int compareTo(Move move) {
		return Integer.compare(this.score, move.score);
	}
}
