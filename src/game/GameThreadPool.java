package game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import player.Player2048;

public class GameThreadPool {
	private Game2048 winner;

	private Lock lock = new ReentrantLock();
	private Condition cond = lock.newCondition();
	private boolean shouldReturn = false;
	private List<GameThread> threads;
	private Supplier<Player2048> getPlayer;

	private final int maxBlock;

	public GameThreadPool(Supplier<Player2048> player, int numThreads, int maxBlock) {
		this.maxBlock = maxBlock;
		this.getPlayer = player;
		threads = new ArrayList<GameThread>();
		IntStream.range(0, numThreads).forEach(i -> threads.add(new GameThread(i)));
	}

	private synchronized void setWinner(Game2048 game) {
		lock.lock();
		if (!shouldReturn) {

			winner = game;
			shouldReturn = true;
			cond.signalAll();
		}
		lock.unlock();
	}

	private long time;
	
	public Game2048 get() {
		time = System.currentTimeMillis();
		threads.parallelStream().forEach(Runnable::run);
		try {
			lock.lock();
			while (!shouldReturn) {
				cond.await();
			}
			time = System.currentTimeMillis() - time;
			return winner;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		} finally {
			lock.unlock();
		}
	}

	private volatile int tries = 0;

	public class GameThread implements Runnable {
		private int num;

		public GameThread(int num) {
			this.num = num;
		}

		public void run() {
			int maxBlock;
			Game2048 game;
			do {
				tries++;
				// System.out.println("New Game: " + num);
				Player2048 p1 = getPlayer.get();
				game = new Game2048();
				p1.play(game);
				maxBlock = game.getMaxBlock();

				// Player2048 p2 = new DumbPlayer2048();
				// p2.play(new Game2048());

				// out.println(p1.getScore() + ","
				// + Math.pow(2, p1.getMaxBlock()));
			} while (maxBlock < GameThreadPool.this.maxBlock && !shouldReturn);

			GameThreadPool.this.setWinner(game);
		}

		public int getNum() {
			return num;
		}
	}

	public int getTries() {
		return tries;
	}

	public long getTime() {
		return time;
	}
}
