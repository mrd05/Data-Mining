package assignment;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AprioriAlgorithm2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			List<String> lines = Files
					.readAllLines(
							new File(
									"C:\\Users\\Mohit\\Desktop\\workspace\\Data Mining\\dataminingdatasets\\retail.dat")
									.toPath(), StandardCharsets.UTF_8);
			Map<Integer, Integer> map1item = new ConcurrentHashMap<Integer, Integer>();
			Map<Integer, List<Integer>> mapItemTran = new HashMap<Integer, List<Integer>>();
			List<List<List<Integer>>> C = new ArrayList<>();
			float minSupportPercent = 90 / 100; // change as per need
			int minSupport = (int) (lines.size() * minSupportPercent);
			int lineNo = 1;
			for (String str : lines) {
				if (!str.isEmpty()) {
					String arr[] = str.split(" ");
					for (int i = 0; i < arr.length; i++) {
						if (arr[i] != " ") {
							int x = Integer.parseInt(arr[i]);
							if (map1item.containsKey(x))
								map1item.put(x, map1item.get(x) + 1);
							else
								map1item.put(x, 1);

							if (mapItemTran.containsKey(x))
								mapItemTran.get(x).add(lineNo);
							else {
								mapItemTran.put(x, new ArrayList<Integer>());
								mapItemTran.get(x).add(lineNo);
							}
						}
					}
					lineNo++;
				}
			}

			List<List<Integer>> l1 = new ArrayList<List<Integer>>();
			List<Integer> keys = new ArrayList<Integer>(map1item.keySet());
			Collections.sort(keys);
			for (Integer i : keys) {
				if (map1item.get(i) >= minSupport) {
					List<Integer> ls = new ArrayList<Integer>();
					ls.add(i);
					l1.add(ls);
					System.out.println(i + " --> " + map1item.get(i));
				}
			}
			C.add(l1);

			for (int i = 1; !C.get(i - 1).isEmpty(); i++) {
				List<List<Integer>> list = aprioriGen(C.get(i - 1));
				List<List<Integer>> fl = new ArrayList<>();
				for (List<Integer> li : list) {
					int count = 0;
					for (int no = 1; no <= lines.size(); no++) {
						boolean flag = true;
						for (Integer x : li) {
							if (mapItemTran.get(x).contains(no))
								continue;
							else {
								flag = false;
								break;
							}
						}
						if (flag)
							count++;
					}
					if (count >= minSupport) {
						fl.add(li);
						for (Integer x : li)
							System.out.print(x + " ");
						System.out.print("--> " + count);
					}
					System.out.println();
				}
				C.add(fl);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static List<List<Integer>> aprioriGen(List<List<Integer>> list) {
		List<List<Integer>> C = new ArrayList<>();
		Set<List<Integer>> set = new HashSet<>();
		for (int i = 0; i < list.size(); i++) {
			for (int j = i + 1; j < list.size(); j++) {
				if (compare(list.get(i), list.get(j))) {
					List<Integer> c = new ArrayList<>();
					c.addAll(list.get(i));
					c.add(list.get(j).get(list.get(j).size() - 1));
					Collections.sort(c);
					if (!set.contains(c)) {
						set.add(c);
						if (!hasInfrequentSubset(c, list))
							C.add(c);
					}

				}
			}
		}
		return C;
	}

	private static boolean hasInfrequentSubset(List<Integer> c,
			List<List<Integer>> list) {
		Set<List<Integer>> set = new HashSet<>();
		for (List<Integer> l : list) {
			Collections.sort(l);
			set.add(l);
		}
		List<Integer> dup = new ArrayList<>(c);
		for (int i = 0; i < c.size(); i++) {
			dup = new ArrayList<>(c);
			dup.remove(i);
			if (!set.contains(dup))
				return true;
		}
		return false;
	}

	private static boolean compare(List<Integer> list, List<Integer> list2) {

		for (int i = 0; i < list.size() - 1; i++) {
			if (!(list.get(i).intValue() == list2.get(i).intValue()))
				return false;
		}
		return true;
	}
}
