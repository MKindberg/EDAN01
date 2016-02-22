package lab5;

import org.jacop.constraints.Element;
import org.jacop.constraints.Sum;
import org.jacop.constraints.XeqY;
import org.jacop.constraints.XplusYeqC;
import org.jacop.constraints.XplusYeqZ;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMax;
import org.jacop.search.PrintOutListener;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleSelect;

public class CityPlanning {
	public static void main(String[] args) {
		long T1, T2, T;
		T1 = System.currentTimeMillis();
		plan(new Data1());
		T2 = System.currentTimeMillis();
		T = T2 - T1;
		System.out.println("\n\t***	Execution time = " + T + " ms");
	}

	@SuppressWarnings("deprecation")
	public static void plan(Data data) {
		Store store = new Store();

		int n = data.n;
		int n_commercial = data.n_commercial;
		int n_residential = data.n_residential;
		int[] point_distribution = data.point_distribution;

		IntVar[][] city = new IntVar[n][n];
		IntVar[][] city2 = new IntVar[n][n];

		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++) {
				city[i][j] = new IntVar(store, 0, 1);
				city2[j][i] = new IntVar(store, 0, 1);
				store.impose(new XeqY(city[i][j], city2[j][i]));
			}

		IntVar[] colSum = new IntVar[n];
		IntVar[] rowSum = new IntVar[n];

		for (int i = 0; i < n; i++) {
			colSum[i] = new IntVar(store, 0, n);
			rowSum[i] = new IntVar(store, 0, n);
			store.impose(new Sum(city[i], colSum[i]));
			store.impose(new Sum(city2[i], rowSum[i]));
		}

		store.impose(new Sum(colSum, new IntVar(store, n_residential, n_residential)));
		store.impose(new Sum(rowSum, new IntVar(store, n_residential, n_residential)));

		IntVar[] colScore = new IntVar[n];
		IntVar[] rowScore = new IntVar[n];

		for (int i = 0; i < n; i++) {
			colScore[i] = new IntVar(store, 0, 100);
			rowScore[i] = new IntVar(store, 0, 100);
			store.impose(new Element(colSum[i], point_distribution, colScore[i]));
			store.impose(new Element(rowSum[i], point_distribution, rowScore[i]));
		}

		IntVar totalColScore = new IntVar(store, 0, 100);
		IntVar totalRowScore = new IntVar(store, 0, 100);

		store.impose(new Sum(colScore, totalColScore));
		store.impose(new Sum(rowScore, totalRowScore));

		IntVar score = new IntVar(store, 0, 100);
		store.impose(new XplusYeqZ(totalRowScore, totalColScore, score));

		IntVar[] sum = new IntVar[2 * n];
		for (int i = 0; i < n; i++) {
			sum[i] = new IntVar(store, 0, 100);
			sum[i + n] = new IntVar(store, 0, 100);
			store.impose(new XeqY(colScore[i], sum[i]));
			store.impose(new XeqY(rowScore[i], sum[i + n]));
		}

		IntVar minusScore = new IntVar(store, -100, 0);
		store.impose(new XplusYeqC(score, minusScore, 0));

		Search<IntVar> search = new DepthFirstSearch<IntVar>();
		SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(sum, null, new IndomainMax<IntVar>());

		search.setSolutionListener(new PrintOutListener<IntVar>());
		boolean Result = search.labeling(store, select, minusScore);
		if (Result) {
			System.out.println("\n***Yes");
			System.out.println("Score: " + score.value());

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++)
					System.out.print(city[i][j].value() + " ");
				System.out.println();
			}
		}
	}
}
