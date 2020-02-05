import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

public class Board {
	private int[][] tiles;

	// create a board from an n-by-n array of tiles,
	// where tiles[row][col] = tile at (row, col)
	public Board(int[][] tiles) {
		this.tiles = cloneTiles(tiles);
	}

	// string representation of this board
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(tiles.length);
		sb.append("\n");
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles.length; j++) {
				sb.append(" ");
				sb.append(tiles[i][j]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	// board dimension n
	public int dimension() {
		return tiles.length;
	}

	// number of tiles out of place
	public int hamming() {
		int total = 0;
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles.length; j++) {

				if ((j == tiles.length - 1) && (i == tiles.length - 1)) {
					if (tiles[i][j] != 0) {
						total++;
					}
				} else {
					if(tiles[i][j] == 0) {
						continue;
					}
					if (tiles[i][j] != getCellNumber(i, j)) {
						total++;
					}
				}
			}
		}

		return total;
	}

	// sum of Manhattan distances between tiles and goal
	public int manhattan() {
		int total = 0;
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles.length; j++) {

				if ((j == tiles.length - 1) && (i == tiles.length - 1)) {
					if (tiles[i][j] != 0) {
						total += calManhattan(new Point(i + 1, j + 1), getCoordinates(i, j));
					}
				} else {
					if (tiles[i][j] != getCellNumber(i, j)) {
						if(tiles[i][j] == 0) {
							continue;
						}
						total += calManhattan(new Point(i + 1, j + 1), getCoordinates(i, j));
					}
				}
			}
		}

		return total;
	}

	private Point getCoordinates(int i, int j) {
		int number = tiles[i][j];

		if (number == 0) {
			number = tiles.length * tiles.length;
		}
		int row = number / tiles.length;

		if (number % tiles.length != 0) {
			row++;
		}

		int col = number % tiles.length;

		Point point = new Point(row, (col != 0 ? col : col + tiles.length));
		return point;
	}

	private int getCellNumber(int i, int j) {
		return (i * tiles.length) + (j + 1);
	}

	private int calManhattan(Point point, Point point2) {
		int result = (Math.abs(point.getX() - point2.getX()) + Math.abs(point.getY() - point2.getY()));
		return result;
	}

	// is this board the goal board?
	public boolean isGoal() {
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles.length; j++) {

				if ((j == tiles.length - 1) && (i == tiles.length - 1)) {
					if (tiles[i][j] != 0) {
						return false;
					}
				} else {
					if (tiles[i][j] != getCellNumber(i, j)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	// does this board equal y?
	public boolean equals(Object y) {
		if (y == null) {
			return false;
		}
		if (this.getClass() != y.getClass()) {
			return false;
		}
		Board thatBoard = (Board) y;
		int[][] temp = thatBoard.tiles;
		if (temp.length != this.tiles.length) {
			return false;
		}
		for (int x = 0; x < tiles.length; x++) {
			if (!Arrays.equals(tiles[x], temp[x])) {
				return false;
			}
		}
		return true;
	}

	// all neighboring boards
	public Iterable<Board> neighbors() {
		LinkedList<Board> neighbors;
		neighbors = getNeighbors();
		return new Iterable<Board>() {

			@Override
			public Iterator<Board> iterator() {
				// TODO Auto-generated method stub
				return neighbors.iterator();
			}

		};

	}

	private LinkedList<Board> getNeighbors() {

		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles.length; j++) {
				if (tiles[i][j] == 0) {
					return findPossibleMoves(i, j);
				}
			}
		}
		return null;
	}

	private LinkedList<Board> findPossibleMoves(int i, int j) {

		LinkedList<Board> neighbors = new LinkedList<Board>();
		// first Row
		if (i == 0) {
			// first column
			if (j == 0) {
				// move 1
				int[][] clone = cloneTiles(this.tiles);
				swapDown(clone,i, j);
				Board move = new Board(clone);
				neighbors.add(move);

				// move 2
				clone = cloneTiles(this.tiles);
				swapRight(clone, i, j);
				move = new Board(clone);
				neighbors.add(move);

			}
			// last column
			else if (j == tiles.length - 1) {
				// move 1
				int[][] clone = cloneTiles(this.tiles);
				swapDown(clone, i, j);
				Board move = new Board(clone);
				neighbors.add(move);

				// move 2
				clone = cloneTiles(this.tiles);
				swapLeft(clone, i, j);
				move = new Board(clone);
				neighbors.add(move);

			} else {
				// move 1
				int[][] clone = cloneTiles(this.tiles);
				swapLeft(clone, i, j);
				Board move = new Board(clone);
				neighbors.add(move);

				// move 2
				clone = cloneTiles(this.tiles);
				swapRight(clone, i, j);
				move = new Board(clone);
				neighbors.add(move);

				// move 3
				clone = cloneTiles(this.tiles);
				swapDown(clone, i, j);
				move = new Board(clone);
				neighbors.add(move);
			}

		}

		// last Row
		else if (i == tiles.length - 1) {
			// first column
			if (j == 0) {
				// move 1
				int[][] clone = cloneTiles(this.tiles);
				// right cell
				swapRight(clone, i, j);
				Board move = new Board(clone);
				neighbors.add(move);

				// move 2 top cell
				clone = cloneTiles(this.tiles);
				swapTop(clone, i, j);
				move = new Board(clone);
				neighbors.add(move);

			}
			// last column
			else if (j == tiles.length - 1) {
				// move 1 left cell
				int[][] clone = cloneTiles(this.tiles);
				swapLeft(clone, i, j);
				Board move = new Board(clone);
				neighbors.add(move);

				// move 2 top cell
				clone = cloneTiles(this.tiles);
				swapTop(clone, i, j);
				move = new Board(clone);
				neighbors.add(move);

			} else {
				// move 1 left
				int[][] clone = cloneTiles(this.tiles);
				swapLeft(clone, i, j);
				Board move = new Board(clone);
				neighbors.add(move);

				// move 2 right
				clone = cloneTiles(this.tiles);
				swapRight(clone, i, j);
				move = new Board(clone);
				neighbors.add(move);

				// move 3 top
				clone = cloneTiles(this.tiles);
				swapTop(clone, i, j);
				move = new Board(clone);
				neighbors.add(move);
			}

		} else {

			// first column
			if (j == 0) {
				// move 1 top
				int[][] clone = cloneTiles(this.tiles);
				swapTop(clone, i, j);
				Board move = new Board(clone);
				neighbors.add(move);
				
				//move 2
				clone = cloneTiles(this.tiles);
				swapRight(clone, i, j);
				move = new Board(clone);
				neighbors.add(move);
				
				//move 2
				clone = cloneTiles(this.tiles);
				swapDown(clone, i, j);
				move = new Board(clone);
				neighbors.add(move);
			}
			// last column
			else if (j == tiles.length - 1) {
				int[][] clone = cloneTiles(this.tiles);
				swapTop(clone, i, j);
				Board move = new Board(clone);
				neighbors.add(move);
				
				//move 2
				clone = cloneTiles(this.tiles);
				swapLeft(clone, i, j);
				move = new Board(clone);
				neighbors.add(move);
				
				//move 2
				clone = cloneTiles(this.tiles);
				swapDown(clone, i, j);
				move = new Board(clone);
				neighbors.add(move);
			}

			else {
				int[][] clone = cloneTiles(this.tiles);
				swapTop(clone, i, j);
				Board move = new Board(clone);
				neighbors.add(move);
				
				//move 2
				clone = cloneTiles(this.tiles);
				swapLeft(clone, i, j);
				move = new Board(clone);
				neighbors.add(move);
				
				//move 3
				clone = cloneTiles(this.tiles);
				swapRight(clone, i, j);
				move = new Board(clone);
				neighbors.add(move);
				
				//move 4
				clone = cloneTiles(this.tiles);
				swapDown(clone, i, j);
				move = new Board(clone);
				neighbors.add(move);
			}
		}
		return neighbors;
	}
private void swapTop(int[][] clone, int i, int j) {
	clone[i][j] = clone[i -1][j];
	clone[i - 1][j] = 0;
}

private void swapLeft(int[][] clone, int i, int j) {
	clone[i][j] = clone[i][j - 1];
	clone[i][j - 1] = 0;
}
private void swapRight(int[][] clone, int i, int j) {
	clone[i][j] = clone[i][j +1];
	clone[i][j + 1] = 0;
}
private void swapDown(int[][] clone, int i, int j) {
	clone[i][j] = clone[ i + 1][j];
	clone[i + 1][j] = 0;
	
}
	
	private int[][] cloneTiles(int [][] tiles) {
		int[][] temp = new int[tiles.length][tiles.length];
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles.length; j++) {
				temp[i][j] = tiles[i][j];
			}
		}
		return temp;
	}

	// a board that is obtained by exchanging any pair of tiles
		public Board twin() {
			int col  = tiles.length / 2;
			int row = 0;
			int [][] clone = cloneTiles(this.tiles);
			if(col == tiles.length -1) {
				col = col -1;
			}
			if(clone[row][col] == 0 || clone[row][col + 1] == 0) {
				row ++;
			}
			
			int temp = clone[row][col];
			clone[row][col] = clone[row][col + 1];
			clone[row][col + 1] = temp;
			return new Board(clone);

		
	}



	// unit testing (not graded)
	public static void main(String[] args) {
//		int [][] arr = {
//				{0,1,3},
//				{4,2,5},
//				{7,8,6}
//		};
//		int [][] arr = {
//				{0,3},
//				{1,2}
//		};
//		Board board = new Board(arr);
//		System.out.println(board.twin());
//		System.out.println(new Board(arr).twin().equals(new Board(arr).twin()));

	}

	private class Point {
		private int x;
		private int y;

		public Point(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}


		public int getY() {
			return y;
		}

	}
}