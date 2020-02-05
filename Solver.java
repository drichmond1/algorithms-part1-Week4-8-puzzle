import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
	private Board board;
	private SearchNode solution;
	private boolean isSolvable;
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
    	if(initial == null) {
    		throw new IllegalArgumentException();
    	}
    	this.board = initial;
    	solve();
    }

    private void solve() {

    	List<SearchNode> boardsVisited = new LinkedList<>();
    	List<SearchNode> boardsVisitedForTwin = new LinkedList<>();
    	Comparator<SearchNode> comparator = new Comparator<SearchNode>() {

			@Override
			public int compare(SearchNode node1, SearchNode node2) {
				if ((node1.getManhattan() + node1.getMoves()) < (node2.getManhattan() + node2.getMoves())) {
					return -1;
					}
					
					if ((node1.getManhattan() + node1.getMoves()) > (node2.getManhattan() + node2.getMoves())) {
						return 1;
					}
					else
						return 0;
			}			
    		
		};
		
        MinPQ<SearchNode> pq= new MinPQ<>(comparator);
        SearchNode root = new SearchNode(board, null, 0, board.manhattan());
        
        MinPQ<SearchNode> pqTwin= new MinPQ<>(comparator);
        SearchNode twinRoot = new SearchNode(board.twin(), null, 0, board.manhattan());
        
        pq.insert(root);
        pqTwin.insert(twinRoot);

        while(true) {
        	if(pq.isEmpty()) {
        		break;
        	}
          SearchNode currentNode = pq.delMin();
      	  boardsVisited.add(currentNode);
      	  if (currentNode.getBoard().isGoal()) {
      		  solution = currentNode;
      		  isSolvable = true;
      		  break;
      	  }
      	  else {
      		  Iterator<Board> iter = currentNode.getBoard().neighbors().iterator();
      			while (iter.hasNext()) {
      				Board child = iter.next();
      				if(currentNode == root) {
          				pq.insert(new SearchNode(child, currentNode, (currentNode.getMoves() + 1), child.manhattan()));
          			 }
      				else if(!currentNode.getParent().getBoard().equals(child)) {
      				pq.insert(new SearchNode(child, currentNode, (currentNode.getMoves() + 1), child.manhattan()));
      				}
      			}
      	  }
        
        
        ///////////Twin
        if(pqTwin.isEmpty()) {
    		break;
    	}
      SearchNode currentNodeTwin = pqTwin.delMin();
  	  boardsVisitedForTwin.add(currentNodeTwin);
  	  if (currentNodeTwin.getBoard().isGoal()) {
  		  isSolvable = false;
  		  break;
  	  }
  	  else {
  		  Iterator<Board> iter = currentNodeTwin.getBoard().neighbors().iterator();
  			while (iter.hasNext()) {
  				Board child = iter.next();
  				if(currentNodeTwin == twinRoot) {
      				pqTwin.insert(new SearchNode(child, currentNodeTwin, (currentNodeTwin.getMoves() + 1), child.manhattan()));
      			 }
  				else if(!currentNodeTwin.getParent().getBoard().equals(child)) {
  					pqTwin.insert(new SearchNode(child, currentNodeTwin, (currentNodeTwin.getMoves() + 1), child.manhattan()));
  				}
  			}
  	  }
    }

               		
	}


	// is the initial board solvable? (see below)
    public boolean isSolvable() {
    	return isSolvable;
    }

    // min number of moves to solve initial board
    public int moves() {
    	if(solution == null) {
    		return -1;
    	}
    	return solution.getMoves();
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution(){
    	List<Board> list = new ArrayList<>();
    	if(solution != null) {
    		SearchNode current = solution;
    	
    	do {
    		list.add(0, current.getBoard());
    		current = current.getParent();
    	}
    	while(current != null);
    	return new Iterable<Board>() {
			
			@Override
			public Iterator<Board> iterator() {
				return list.iterator();
			}
		};
    	}
    	return null;
    }

    // test client (see below) 
    public static void main(String[] args) {
    	 // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private class SearchNode{
    	private Board board;
    	private int manhattan;
    	private SearchNode Parent;
    	private int moves;
    	
		public SearchNode(Board board, SearchNode parent, int moves, int manhattan) {
			super();
			this.board = board;
			Parent = parent;
			this.moves = moves;
			this.manhattan = manhattan;
		}
		public int getManhattan() {
			return manhattan;
		}
		public void setManhattan(int manhattan) {
			this.manhattan = manhattan;
		}
		public Board getBoard() {
			return board;
		}
		public void setBoard(Board board) {
			this.board = board;
		}
		public SearchNode getParent() {
			return Parent;
		}
		public void setParent(SearchNode parent) {
			Parent = parent;
		}
		public int getMoves() {
			return moves;
		}
		public void setMoves(int moves) {
			this.moves = moves;
		}
  	   	
    }
}

