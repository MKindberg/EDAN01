import org.jacop.constraints.Alldiff;
import org.jacop.constraints.XeqC;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.PrintOutListener;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleMatrixSelect;

public class Sudoku {
	public static void main(String[] args) {
		long T1, T2, T;
		T1 = System.currentTimeMillis();
		int[][] sudoku = new int[9][9];
		sudoku[0] = new int[] { 0, 0, 0, 0, 0, 2, 0, 4, 5 };
		sudoku[1] = new int[] { 0, 9, 6, 0, 1, 0, 0, 0, 0 };
		sudoku[2] = new int[] { 2, 3, 0, 0, 0, 0, 0, 0, 0 };
		sudoku[3] = new int[] { 0, 1, 0, 5, 0, 0, 2, 0, 0 };
		sudoku[4] = new int[] { 0, 0, 7, 0, 0, 6, 0, 0, 8 };
		sudoku[5] = new int[] { 5, 0, 9, 4, 0, 0, 0, 0, 3 };
		sudoku[6] = new int[] { 0, 0, 0, 0, 0, 1, 0, 2, 0 };
		sudoku[7] = new int[] { 0, 0, 4, 0, 0, 0, 0, 6, 0 };
		sudoku[8] = new int[] { 0, 0, 0, 0, 9, 3, 4, 0, 1 };

		solve(new int[9][9]);
		T2 = System.currentTimeMillis();
		T = T2 - T1;
		System.out.println("\n\t*** Execution time = " + T + " ms");
	}

	public static void solve(int[][] sudoku) {
		Store store = new Store();
		int len = sudoku.length;
		IntVar[][] sudokuVars = new IntVar[len][len];
		for (int i = 0; i < len; i++)
			for (int j = 0; j < len; j++)
				sudokuVars[i][j] = new IntVar(store, i + "," + j, 1, len);

		for (int i = 0; i < len; i++)
			for (int j = 0; j < len; j++)
				if (sudoku[i][j] != 0)
					store.impose(new XeqC(sudokuVars[i][j], sudoku[i][j]));

		for (int i = 0; i < len; i++) {
			IntVar[] col = new IntVar[len];
			store.impose(new Alldiff(sudokuVars[i]));
			for (int j = 0; j < len; j++)
				col[j] = sudokuVars[i][j];
			store.impose(new Alldiff(col));
		}

		Search<IntVar> search = new DepthFirstSearch<IntVar>();
		SelectChoicePoint<IntVar> select = new SimpleMatrixSelect<IntVar>(sudokuVars, new IndomainMin<IntVar>());

		search.setSolutionListener(new PrintOutListener<IntVar>());

		boolean Result = search.labeling(store, select);

		if (Result)
			System.out.println("\n*** Yes");
		// System.out.println("Solution : " + java.util.Arrays.asList(weights));
		else
			System.out.println("\n*** No");
	}

}
