package neuralnets;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NeuralNet {
	private Random rand = new Random();

	private double[][] weights;

	private int[] starts;

	private final int inputBits, outputBits;

	private final int outputStartNum;

	public NeuralNet(int inputBits, int outputBits, int levels) {
		this.inputBits = inputBits;
		this.outputBits = outputBits;

		weights = new double[(levels+1) * inputBits + outputBits][(levels+1) * inputBits + outputBits];
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[i].length; j++) {
				weights[i][j] = -1;
			}
		}

		List<Integer> level = new ArrayList<>();


		starts = new int[levels + 3];

		int oracle = 0;
		int start = 0;

		for (int i = 0; i <= levels; i++) {
			starts[start++] = oracle;
			List<Integer> newLevels = new ArrayList<>();
			for (int j = 0; j < inputBits; j++) {
				int node = oracle++;
				newLevels.add(node);
				level.forEach(n -> weights[n][node] = rand.nextDouble() / 10);
			}
			level = newLevels;
		}

		outputStartNum = oracle;

		starts[start++] = oracle;
		for (int i = 0; i < outputBits; i++) {
			int node = oracle++;
			level.forEach(n -> weights[n][node] = rand.nextDouble() / 10);

		}

		starts[start] = oracle;

	}

	@Override
	public String toString() {
		String result = "";
		result += "Input bits: " + inputBits;
		result += "\nOutput bits: " + outputBits;
		result += "\nLevel starts: ";

		for (int start : starts) {
			result += start + " ";
		}

		result += "\n\n";

		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[i].length; j++) {
				result += String.format("%.3f ", weights[i][j]);
			}
			result += "\n";
		}

		return result;
	}

	public byte[] feed(byte[] in) {
		int start;
		int level = 1;

		byte[] storage = in;

		if (in.length == inputBits) {

			double[] nextStorage = new double[inputBits];



			while (level < starts.length - 1) {
				start = starts[level - 1];
				for (int i = start, storage_i = 0; i < starts[level]; i++, storage_i++) {
					for (int j = starts[level], storage_j = 0; j < starts[level + 1]; j++, storage_j++) {
						nextStorage[storage_j] += storage[storage_i] * weights[i][j];
					}
				}

				storage = new byte[starts[level+1] - starts[level]];

				for (int j = starts[level], storage_j = 0; j < starts[level + 1]; j++, storage_j++) {
					storage[storage_j] = (byte) ((nextStorage[storage_j] >= 1) ? 1 : 0);
				}

				level++;
			}

		} else {
			System.out.println("No enough input!");
		}

		return storage;
	}

	private static final double ETA = 0.1;

	public void train(byte[] input, byte[] output) {
		//byte[] answer = this.feed(inputs[w]);
		byte[] storage = output;

		/*System.out.print("Net: {");

		for (int q = 0; q < answer.length; q++) {
			System.out.print(answer[q]);
		}

		System.out.print("}, Actual {");

		for (int q = 0; q < storage.length; q++) {
			System.out.print(storage[q]);
		}

		System.out.println("}");*/

		int start;
		int level = starts.length - 2;

		//System.out.println(starts[level] - starts[level - 1]);

		double[] nextStorage = new double[starts[level] - starts[level - 1]];

		while (level > 0) {
			//System.out.println("Level: " + level);

			start = starts[level];
			for (int i = start, storage_i = 0; i < starts[level + 1]; i++, storage_i++) {

				for (int j = starts[level - 1], storage_j = 0; j < starts[level]; j++, storage_j++) {
					//System.out.println(storage[storage_i] + " " + weights[j][i]);
					weights[j][i] += ETA * (storage[storage_i] - weights[j][i]);
					nextStorage[storage_j] = storage[storage_i] - weights[j][i];
				}
			}


			storage = new byte[starts[level] - starts[level - 1]];

			for (int j = starts[level - 1], storage_j = 0; j < starts[level]; j++, storage_j++) {
				storage[storage_j] = (byte) ((nextStorage[storage_j] >= 1) ? 1 : 0);
			}


			level--;
		}
	}

	public void train(byte[][] inputs, byte[][] outputs) {
		for (int w = 0; w < inputs.length; w++) {
			train(inputs[w], outputs[w]);
		}
	}

	public static void main(String[] args) throws IOException {
		NeuralNet net = new NeuralNet(4, 1, 1);
		System.out.println(net);

		/*byte[][] train_inputs = new byte[128][4];
		byte[][] train_outputs = new byte[128][1];

		for (int i = 0; i < 128; i++) {
			byte[] in = train_inputs[i];
			in[0] = (byte) (i & 0x1);
			in[1] = (byte) ((i & 0x2) >> 1);
			in[2] = (byte) ((i & 0x4) >> 2);
			in[3] = (byte) ((i & 0x8) >> 3);
			train_inputs[i] = in;
			train_outputs[i][0] = (byte) (i % 2);
		}*/

		//net.train(train_inputs, train_outputs);

		for (int i = 0; i < 5; i ++) {
			net.train(new byte[]{0x1, 0x1, 0x0, 0x1}, new byte[]{0x1});
			System.out.println(net);
		}

		ByteBuffer buff = ByteBuffer.allocate(4);
		buff.put((byte) 1);
		buff.put((byte) 1);
		buff.put((byte) 0);
		buff.put((byte) 1);
		byte[] answer = net.feed(buff.array());
		
		for (int i = 0; i < answer.length; i++) {
			System.out.println(answer[i]);
		}
	}
}
