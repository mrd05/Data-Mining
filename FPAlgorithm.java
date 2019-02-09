package assignment;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FPAlgorithm {

	public static void main(String[] args) {
		try {
			List<String> lines = Files
					.readAllLines(
							new File(
									"C:\\Users\\Mohit\\Desktop\\workspace\\Data Mining\\dataminingdatasets\\test.txt")
									.toPath(), StandardCharsets.UTF_8);

			Map<Integer, Integer> map1item = new ConcurrentHashMap<Integer, Integer>();
			Map<Integer, List<Integer>> mapItemTran = new HashMap<Integer, List<Integer>>();
			float minSupportPercent = (float) 30 / 100; // change as per need

			int lineNo = 1;
			for (String str : lines) {
				if (!str.isEmpty()) {
					String arr[] = str.split(" ");
					for (int i = 0; i < arr.length; i++) {
						int x = Integer.parseInt(arr[i]);
						if (map1item.containsKey(x))
							map1item.put(x, map1item.get(x) + 1);
						else
							map1item.put(x, 1);

						if (mapItemTran.containsKey(lineNo))
							mapItemTran.get(lineNo).add(x);
						else {
							mapItemTran.put(lineNo, new ArrayList<Integer>());
							mapItemTran.get(lineNo).add(x);
						}
					}
					lineNo++;
				}

			}
			int minSupport = (int) ((lineNo - 1) * minSupportPercent);

			List<ListNode> l1 = new ArrayList<ListNode>();
			List<Integer> keys = new ArrayList<Integer>(map1item.keySet());
			Collections.sort(keys);
			for (Integer i : keys) {
				if (map1item.get(i) >= minSupport) {
					ListNode node = new ListNode(i, map1item.get(i));
					l1.add(node);
				}
			}

			Collections.sort(l1);
			for (ListNode l : l1)
				System.out.println(l.d + " --> " + l.freq);

			TreeNode root = new TreeNode(-1, null);
			for (List<Integer> list : mapItemTran.values()) {
				TreeNode node = root;
				for (ListNode l : l1) {
					if (list.contains(l.d)) {
						TreeNode c = new TreeNode(l.d, node);

						if (node.childs.contains(c)) {
							node.childs.get(node.childs.lastIndexOf(c)).freq++;
						} else {
							node.childs.add(c);
							l.nodes.add(c);
						}
						node = node.childs.get(node.childs.lastIndexOf(c));
					}
				}
			}

			for (ListNode l : l1) {
				System.out.print(l.d + "-->" + l.freq + " ");
				for (TreeNode node : l.nodes) {
					System.out.print(node.d + "-" + node.freq + "   ");
				}
				System.out.println();
			}

			for (int x = l1.size() - 1; x >= 0; x--) {
				ListNode l = l1.get(x);
				TreeNode head = new TreeNode(-1, null);
				for (TreeNode t : l.nodes) {
					List<Integer> in = new ArrayList<>();
					int f = t.freq;
					t = t.parent;
					while (t.d != -1) {
						in.add(t.d);
						t = t.parent;
					}
					TreeNode node = head;
					for (int y = in.size() - 1; y >= 0; y--) {
						int a = in.get(y);
						TreeNode c = new TreeNode(a, f, node);
						if (node.childs.contains(c)) {
							node.childs.get(node.childs.lastIndexOf(c)).freq += f;
						} else {
							node.childs.add(c);
						}
						node = node.childs.get(node.childs.lastIndexOf(c));
					}
				}
				Map<String, Integer> map = new HashMap<>();
				traverseTree(l.d, head, map, "");
				List<String> sl = new ArrayList<>(map.keySet());
				Collections.sort(sl);
				for (String str : sl) {
					if (map.get(str) >= minSupport)
						System.out.println(str + "--> " + map.get(str));

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void traverseTree(int d, TreeNode head,
			Map<String, Integer> map, String s) {

		if (head.d != -1) {
			if (!s.isEmpty()) {
				String[] str = s.split(" ");
				List<Integer> l = new ArrayList<>();
				for (String sh : str)
					l.add(Integer.parseInt(sh));
				l.add(head.d);
				Collections.sort(l);
				String st = "";
				for (int x : l)
					st = st + x + " ";
				if (map.containsKey(st))
					map.put(st, map.get(st) + head.freq);
				else
					map.put(st, head.freq);
			}
			List<Integer> l = new ArrayList<>();
			l.add(head.d);
			l.add(d);
			Collections.sort(l);
			String str = "";
			for (int x : l)
				str = str + x + " ";
			if (map.containsKey(str))
				map.put(str, map.get(str) + head.freq);
			else
				map.put(str, head.freq);
			for (TreeNode t : head.childs)
				traverseTree(d, t, map, str);

		} else {
			for (TreeNode t : head.childs)
				traverseTree(d, t, map, "");
		}
	}

}

class ListNode implements Comparable<ListNode> {
	int d;
	int freq;
	List<TreeNode> nodes = new ArrayList<>();

	public ListNode(int d, int freq) {
		super();
		this.d = d;
		this.freq = freq;
	}

	@Override
	public int compareTo(ListNode arg0) {
		return arg0.freq - this.freq;
	}
}

class TreeNode {
	int d;
	int freq;
	TreeNode parent = null;
	List<TreeNode> childs = new ArrayList<>();

	public TreeNode(int d, TreeNode parent) {
		this.d = d;
		this.freq = 1;
		this.parent = parent;
	}

	public TreeNode(int d, int f, TreeNode parent) {
		this.d = d;
		this.freq = f;
		this.parent = parent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + d;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TreeNode other = (TreeNode) obj;
		if (d != other.d)
			return false;
		return true;
	}

}
