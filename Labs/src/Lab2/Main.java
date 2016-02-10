package Lab2;

import java.util.ArrayList;

import org.jacop.constraints.Constraint;
import org.jacop.constraints.Element;
import org.jacop.constraints.IfThen;
import org.jacop.constraints.Or;
import org.jacop.constraints.PrimitiveConstraint;
import org.jacop.constraints.Subcircuit;
import org.jacop.constraints.Sum;
import org.jacop.constraints.XeqY;
import org.jacop.constraints.XneqC;
import org.jacop.constraints.XneqY;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.PrintOutListener;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleMatrixSelect;

public class Main {

	public static void main(String[] args) {
		long T1, T2, T;
		T1 = System.currentTimeMillis();
		logistics(new Data2());
		T2 = System.currentTimeMillis();
		T = T2 - T1;
		System.out.println("\n\t***	Execution time = " + T + " ms");
	}

	public static void logistics(Data data) {
		Store store = new Store();

		int n_edges = data.n_edges + data.graph_size;
		int graph_size = data.graph_size + 1;
		int[] from = data.from;
		int[] to = data.to;
		int[] cost = data.cost;
		int start = data.start;
		int n_dests = data.n_dests;
		int dest[] = data.dest;

		int[][] distances = new int[graph_size][graph_size];
		for (int i = 1; i < graph_size; i++)
			for (int j = 1; j < graph_size; j++) {
				distances[i][j] = 100;
				if (i == j)
					distances[i][j] = 0;

			}

		for (int i = 0; i < from.length; i++) {
			distances[from[i]][to[i]] = cost[i];
			distances[to[i]][from[i]] = cost[i];
		}

		IntVar[][] x = new IntVar[n_dests][graph_size];
		for (int i = 0; i < n_dests; i++)
			for (int j = 0; j < graph_size; j++)
				x[i][j] = new IntVar(store, "x" + i + " " + j, 0, graph_size);

		store.impose(new Subcircuit(x[0]));
		store.impose(new XneqC(x[0][start], start + 1));
		store.impose(new XneqC(x[0][dest[0]], dest[0] + 1));
		for (int i = 1; i < x.length; i++) {
			store.impose(new Subcircuit(x[i]));
			store.impose(new XneqC(x[i][dest[i]], dest[i] + 1));
		}
		ArrayList<PrimitiveConstraint> c = new ArrayList<PrimitiveConstraint>();
		IntVar[] vs = new IntVar[x[0].length];
		for (int i = 0; i < x[0].length; i++) {
			vs[i] = new IntVar(store, 0, 100);
			store.impose(new Element(x[0][i], x[1], vs[i]));
			c.add(new IfThen(new XneqC(vs[i], 0), new XneqY(x[0][i], vs[i])));
		}
		store.impose(new Or(c));
		// store.impose(new XneqC(x[1][5],6));

		IntVar[][] d = new IntVar[x.length][graph_size];
		for (int j = 0; j < d.length; j++)
			for (int i = 0; i < graph_size; i++) {
				d[j][i] = new IntVar(store, "dist" + i, 0, 1000);
				IntVar v = new IntVar(store, 0, 100);
				store.impose(new Element(x[j][i], distances[i], v));
				store.impose(new XeqY(d[j][i], v));
			}

		IntVar distance[] = new IntVar[d.length];
		for (int i = 0; i < distance.length; i++) {
			distance[i] = new IntVar(store, 0, 1000);
			Constraint distanceCost = new Sum(d[i], distance[i]);
			store.impose(distanceCost);
		}
		IntVar distance2 = new IntVar(store, 0, 1000);
		store.impose(new Sum(distance, distance2));
		Search<IntVar> search = new DepthFirstSearch<IntVar>();
		SelectChoicePoint<IntVar> select = new SimpleMatrixSelect<IntVar>(x, new IndomainMin<IntVar>());
		// SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(distance,
		// null, new IndomainMin<IntVar>());

		search.setSolutionListener(new PrintOutListener<IntVar>());
		boolean Result = search.labeling(store, select, distance2);
		if (Result) {
			System.out.println(store);
			System.out.println("\n***Yes");
			System.out.println("Price: " + distance2.value());
			System.out.println("\nFrom \tTo");
			for (int i = 0; i < graph_size; i++)
				System.out.println(i + "\t" + (x[0][i].value() - 1));
			System.out.println();
			for (int i = 0; i < graph_size; i++)
				System.out.println(i + "\t" + (x[1][i].value() - 1));
		}
		// for(int i=0;i<distances.length;i++){
		// for(int j=0;j<distances[0].length;j++)
		// System.out.print(distances[i][j]+ "\t");
		// System.out.println();
		// }
	}
}

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
