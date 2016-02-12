package logistics;

class Data {
	int graph_size;
	int start;
	int n_dests;
	int[] dest;
	int n_edges;
	int[] from;
	int[] to;
	int[] cost;
}

class Data1 extends Data {

	public Data1() {
		graph_size = 6;
		start = 1;
		n_dests = 1;
		dest = new int[] { 6 };
		n_edges = 7;
		from = new int[] { 1, 1, 2, 2, 3, 4, 4 };
		to = new int[] { 2, 3, 3, 4, 5, 5, 6 };
		cost = new int[] { 4, 2, 5, 10, 3, 4, 11 };
	}
}

class Data2 extends Data {

	public Data2() {
		graph_size = 6;
		start = 1;
		n_dests = 2;
		dest = new int[] { 5, 6 };
		n_edges = 7;
		from = new int[] { 1, 1, 2, 2, 3, 4, 4 };
		to = new int[] { 2, 3, 3, 4, 5, 5, 6 };
		cost = new int[] { 4, 2, 5, 10, 3, 4, 11 };
	}
}

class Data3 extends Data {

	public Data3() {
		graph_size = 6;
		start = 1;
		n_dests = 2;
		dest = new int[] { 5, 6 };
		n_edges = 9;
		from = new int[] { 1, 1, 1, 2, 2, 3, 3, 3, 4 };
		to = new int[] { 2, 3, 4, 3, 5, 4, 5, 6, 6 };
		cost = new int[] { 6, 1, 5, 5, 3, 5, 6, 4, 2 };
	}
}
