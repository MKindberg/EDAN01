package lab4;

public class ar_2_4 extends Data {
	public ar_2_4() {

		del_add = 1;
		del_mul = 2;

		number_add = 2;
		number_mul = 4;
		n = 28;

		last = new int[] { 26, 27 };

		add = new int[] { 9, 10, 11, 12, 13, 14, 19, 20, 25, 26, 27, 28 };

		mul = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 15, 16, 17, 18, 21, 22, 23, 24 };

		dependencies = new int[][] { {}, {}, {}, {}, {}, {}, {}, {}, { 0, 1 }, { 2, 3 }, { 4, 5 }, { 6, 7 }, { 9 },
				{ 10 }, { 12 }, { 13 }, { 12 }, { 13 }, { 14, 15 }, { 16, 17 }, { 18 }, { 19 }, { 18 }, { 19 },
				{ 20, 21 }, { 22, 23 }, { 8, 24 }, { 11, 25 }, };
	}
}
