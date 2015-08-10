package player;

import game.Move;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class EdgeMiniMaxPlayer2048 extends Player2048 {

	@Override
	protected int choose(List<Move> map) {
		double[] scores = new double[4];
		double[] numMerges = new double[4];
		double[] score = { 1, 1, 1, 1 };
		System.out.println("SIZ: " + map.size());

		for (Move mov : map) {
			int[][] board = mov.board;

			scores[mov.dir] = mov.score;
			numMerges[mov.dir] = mov.numMerges;

			int cost = 0;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 4; j++) {
					cost += Math.abs(Math.pow(2, board[j][i]) - Math.pow(2, board[j][i + 1]));
					cost += Math.abs(Math.pow(2, board[i][j]) - Math.pow(2, board[i + 1][j]));
				}
			}
			score[mov.dir] = cost;
		}
		
		add(new double[]{1,1,1,1}, scores);
		
		System.out.println(scores[0] + " " + scores[1] + " " + scores[2] + " " + scores[3]);
		System.out.println(score[0] + " " + score[1] + " " + score[2] + " " + score[3]);
		div(score, scores);
		mult(numMerges, scores);

		System.out.println(scores[0] + " " + scores[1] + " " + scores[2] + " " + scores[3]);
		System.out.println("\t" + min(scores));
		return max(scores);
	}

	public static void mult(double[] mod, double[] source) {
		for (int i = 0; i < source.length; i++) {
			source[i] *= mod[i];
		}
	}

	public static void div(double[] mod, double[] source) {
		for (int i = 0; i < source.length; i++) {
			source[i] /= mod[i];
		}
	}

	public static void add(double[] mod, double[] source) {
		for (int i = 0; i < source.length; i++) {
			source[i] += mod[i];
		}
	}

	public static void normalize(double[] arr) {
		normalize(arr, false);
	}

	public int max(double[] arr) {
		double max = Double.MIN_VALUE;
		int dir = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > max) {
				max = arr[i];
				dir = i;
			}
		}
		return dir;
	}

	public int min(double[] arr) {
		double min = Double.MAX_VALUE;
		int dir = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] < min) {
				min = arr[i];
				dir = i;
			}
		}
		return dir;
	}

	public static void normalize(double[] arr, boolean reverse) {
		double max = Double.MIN_VALUE;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > max) {
				max = arr[i];
			}
		}

		for (int i = 0; i < arr.length; i++) {
			arr[i] = reverse ? 1 - arr[i] / max : arr[i] / max;
		}

	}

}
