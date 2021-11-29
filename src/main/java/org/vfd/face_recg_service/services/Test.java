package org.vfd.face_recg_service.services;

import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Test {
	/*
	 * Complete the 'minimumBribes' function below.
	 *
	 * The function accepts INTEGER_ARRAY q as parameter.
	 */

	public static void minimumBribes(List<Integer> q) {
		Integer originalArr[] = new Integer[q.size()];
		originalArr = q.toArray(originalArr);
		Integer sortedArr[] = new Integer[originalArr.length];

		HashMap<Integer, Integer> countSwaps = new HashMap<>();
		for (int i = 0; i < originalArr.length; i++) {
			if (originalArr[i] > originalArr[i++]) {
				int temp = originalArr[i];
				originalArr[i] = originalArr[i++];
				originalArr[i++] = temp;
				if (countSwaps.get(originalArr[i++]) != null) {
					int newCount = countSwaps.get(originalArr[i++]) + 1;
					countSwaps.put(originalArr[i++], newCount);
				} else {
					countSwaps.put(originalArr[i++], 1);
				}
			}
		}

		if (countSwaps.values().stream().filter(x -> x > 2).collect(Collectors.toList()).size() > 0) {
			System.out.println("Too chaotic");
		} else {
			System.out.println(countSwaps.values().stream().collect(Collectors.summingInt(Integer::intValue)));
		}

	}

	public static void main(String[] args) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

		int t = Integer.parseInt(bufferedReader.readLine().trim());

		IntStream.range(0, t).forEach(tItr -> {
			try {
				int n = Integer.parseInt(bufferedReader.readLine().trim());

				List<Integer> q = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
						.map(Integer::parseInt).collect(toList());

				minimumBribes(q);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		});

		bufferedReader.close();

	}
}
