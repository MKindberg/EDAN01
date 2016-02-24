package lab5;

import org.jacop.constraints.Element;
import org.jacop.constraints.LexOrder;
import org.jacop.constraints.SumInt;
import org.jacop.constraints.XplusYeqC;
import org.jacop.constraints.XplusYeqZ;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.PrintOutListener;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleSelect;

public class CityPlanning {
	public static void main(String[] args) {
		long T1, T2, T;
		T1 = System.currentTimeMillis();
		plan(new Data3());
		T2 = System.currentTimeMillis();
		T = T2 - T1;
		System.out.println("\n\t***	Execution time = " + T + " ms");
	}

	public static void plan(Data data) {
		Store store = new Store();

		int n = data.n;
		int n_residential = data.n_residential;
		int[] point_distribution = data.point_distribution;

		IntVar[][] city = new IntVar[n][n];
		IntVar[][] city2 = new IntVar[n][n];
		IntVar[] flat = new IntVar[n * n];

		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++) {
				city[i][j] = new IntVar(store, "city", 0, 1);
				city2[j][i] = city[i][j];
				flat[j + i * n] = city[i][j];
			}

		IntVar[] rowSum = new IntVar[n];
		IntVar[] colSum = new IntVar[n];

		for (int i = 0; i < n; i++) {
			colSum[i] = new IntVar(store, 0, n);
			rowSum[i] = new IntVar(store, 0, n);
			store.impose(new SumInt(store, city[i], "==", rowSum[i]));
			store.impose(new SumInt(store, city2[i], "==", colSum[i]));
		}
		store.impose(new SumInt(store, flat, "==", new IntVar(store, n_residential, n_residential)));

		IntVar[] rowScore = new IntVar[n];
		IntVar[] colScore = new IntVar[n];

		for (int i = 0; i < n; i++) {
			rowScore[i] = new IntVar(store, -100, 100);
			colScore[i] = new IntVar(store, -100, 100);
			store.impose(new Element(rowSum[i], point_distribution, rowScore[i], -1));
			store.impose(new Element(colSum[i], point_distribution, colScore[i], -1));
		}

		IntVar totalRowScore = new IntVar(store, -100, 100);
		IntVar totalColScore = new IntVar(store, -100, 100);

		store.impose(new SumInt(store, rowScore, "==", totalRowScore));
		store.impose(new SumInt(store, colScore, "==", totalColScore));

		IntVar score = new IntVar(store, -100, 100);
		store.impose(new XplusYeqZ(totalColScore, totalRowScore, score));

		for (int i = 0; i < n - 1; i++) {
			store.impose(new LexOrder(city[i], city[i + 1], true));
			store.impose(new LexOrder(city2[i], city2[i + 1], true));
		}

		IntVar minusScore = new IntVar(store, -100, 100);
		store.impose(new XplusYeqC(score, minusScore, 0));

		Search<IntVar> search = new DepthFirstSearch<IntVar>();
		SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(flat, null, new IndomainMin<IntVar>());

		search.setSolutionListener(new PrintOutListener<IntVar>());
		boolean Result = search.labeling(store, select, minusScore);
		System.out.println(store);
		if (Result) {
			System.out.println("\n***Yes");
			System.out.println("Score: " + score.value());

			for (int i = 0; i < n; i++)
				System.out.print(colScore[i].value() + " ");
			System.out.println();
			for (int i = 0; i < n; i++)
				System.out.print(colSum[i].value() + " ");
			System.out.println();
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++)
					System.out.print(city[i][j].value() + " ");
				System.out.print(rowSum[i].value() + " ");
				System.out.println(rowScore[i].value());
			}
		}
	}
}
