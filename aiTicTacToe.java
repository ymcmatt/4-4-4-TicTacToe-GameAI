import java.util.*;
public class aiTicTacToe {

	public int player; //1 for player 1 and 2 for player 2
	//public arrayList
	private List<List<positionTicTacToe>>  winningLines = initializeWinningLines();
	
	private int getStateOfPositionFromBoard(positionTicTacToe position, List<positionTicTacToe> targetBoard)
	{
		//a helper function to get state of a certain position in the Tic-Tac-Toe board by given position TicTacToe
		int index = position.x*16+position.y*4+position.z;
		return targetBoard.get(index).state;
	}
	
	public static boolean makeMove(positionTicTacToe position, int player, List<positionTicTacToe> targetBoard)
	{
		//make move on Tic-Tac-Toe board, given position and player 
		//player 1 = 1, player 2 = 2
		
		//brute force (obviously not a wise way though)
		for(int i=0;i<targetBoard.size();i++)
		{
			if(targetBoard.get(i).x==position.x && targetBoard.get(i).y==position.y && targetBoard.get(i).z==position.z) //if this is the position
			{
				if(targetBoard.get(i).state==0)
				{
					targetBoard.get(i).state = player;
					return true;
				}
				else
				{
					System.out.println("Error: this is not a valid move.");
				}
			}
			
		}
		return false;
	}
	
	private int isEnded(List<positionTicTacToe> board)
	{
		//test whether the current game is ended
		
		//brute-force
		for(int i=0;i<winningLines.size();i++)
		{
			
			positionTicTacToe p0 = winningLines.get(i).get(0);
			positionTicTacToe p1 = winningLines.get(i).get(1);
			positionTicTacToe p2 = winningLines.get(i).get(2);
			positionTicTacToe p3 = winningLines.get(i).get(3);
			
			int state0 = getStateOfPositionFromBoard(p0,board);
			int state1 = getStateOfPositionFromBoard(p1,board);
			int state2 = getStateOfPositionFromBoard(p2,board);
			int state3 = getStateOfPositionFromBoard(p3,board);
			
			//if they have the same state (marked by same player) and they are not all marked.
			if(state0 == state1 && state1 == state2 && state2 == state3 && state0!=0)
			{
				//someone wins
				p0.state = state0;
				p1.state = state1;
				p2.state = state2;
				p3.state = state3;
				
				return state3;
			}
		}
		for(int i=0;i<board.size();i++)
		{
			if(board.get(i).state==0)
			{
				//game is not ended, continue
				return 0;
			}
		}
		return -1; //call it a draw
	}
	public positionTicTacToe myAIAlgorithm1(List<positionTicTacToe> board, int player)
	{
		//TODO: this is where you are going to implement your AI algorithm to win the game. The default is an AI randomly choose any available move.
		positionTicTacToe myNextMove = new positionTicTacToe(0,0,0);
		
		do
			{
				Random rand = new Random();
				int x = rand.nextInt(4);
				int y = rand.nextInt(4);
				int z = rand.nextInt(4);
				myNextMove = new positionTicTacToe(x,y,z);
			}while(getStateOfPositionFromBoard(myNextMove,board)!=0);
		return myNextMove;
			
		
	}
	
	
	public positionTicTacToe myAIAlgorithm(List<positionTicTacToe> board, int player)
	{
		//TODO: this is where you are going to implement your AI algorithm to win the game. The default is an AI randomly choose any available move.
		positionTicTacToe myNextMove = new positionTicTacToe(0,0,0);		
		do
			{				
				ArrayList<Integer> temp = minimax_pruning(player,3,Integer.MIN_VALUE,Integer.MAX_VALUE,true,board);
				myNextMove= new positionTicTacToe(temp.get(0),temp.get(1),temp.get(2));						
			}while(getStateOfPositionFromBoard(myNextMove,board)!=0);		
		return myNextMove;		
	}
	
	public positionTicTacToe myAIAlgorithm2(List<positionTicTacToe> board, int player)
	{
		//TODO: this is where you are going to implement your AI algorithm to win the game. The default is an AI randomly choose any available move.
		positionTicTacToe myNextMove = new positionTicTacToe(0,0,0);
		
		do
			{
				
				ArrayList<Integer> temp = minimax_pruning(player,2,Integer.MIN_VALUE,Integer.MAX_VALUE,true,board);
				myNextMove= new positionTicTacToe(temp.get(0),temp.get(1),temp.get(2));
				//System.out.println("it moves on");
								
			}while(getStateOfPositionFromBoard(myNextMove,board)!=0);
		return myNextMove;
		
	}
	


	public ArrayList<Integer> minimax_pruning (int player,int depth,int alpha,int beta,boolean maximizingPlayer, List<positionTicTacToe> board)
	{
		
		int opponent = 0;
		if (player == 1)
		{
			opponent = 2;
		}
		else
		{
			opponent = 1;
		}
		int value= 0;
		ArrayList<Integer> placeholder = new ArrayList<Integer>();
		placeholder.add(0);
		placeholder.add(0);
		placeholder.add(0);
		placeholder.add(player);
		
		//System.out.println(depth);
		if (depth == 0 || this.isEnded(board) != 0){
			value = getHeuristic(player,board,maximizingPlayer);
			
			placeholder.add(value);
			return placeholder;
		}
		
		if (maximizingPlayer == true)
		{
			int val = Integer.MIN_VALUE;
			ArrayList<ArrayList<Integer>> children = getChildren(board);
			
				int index = 0;
				for (int i = 0; i < children.size(); i++)
				{
					
					ArrayList<Integer> child = children.get(i);
					List<positionTicTacToe> planning=deepCopyATicTacToeBoard(board);
					positionTicTacToe move = new positionTicTacToe(child.get(0),child.get(1),child.get(2));
					makeMove(move,player,planning);
					int score = minimax_pruning(player,depth-1,alpha,beta,false,planning).get(4);
					if(score > val)
					{
						index = i;
						val = score;
					}
					//System.out.println("index"+index);
					alpha = Math.max(alpha,val);
					if (alpha >= beta)
					{
						break;
					}
				}
				ArrayList<Integer> temp = children.get(index);
				temp.add(val);
				return temp;			
			
		}
		else
		{
			//System.out.println("hits Minimizer");
			int val = Integer.MAX_VALUE;
			ArrayList<ArrayList<Integer>> children = getChildren(board);

				int index = 0;
				for (int i = 0; i < children.size(); i++)	
				{
					
					ArrayList<Integer> child = children.get(i);
					List<positionTicTacToe> planning=deepCopyATicTacToeBoard(board);
					positionTicTacToe move = new positionTicTacToe(child.get(0),child.get(1),child.get(2));
					makeMove(move,opponent,planning);
					int score = minimax_pruning(player,depth-1,alpha,beta,true,planning).get(4);
					if(score <= val)
					{
						index = i;
						val = score;
					}
					//System.out.println("score "+val);
					alpha = Math.min(alpha,val);
					if (alpha >= beta)
					{
						break;
					}
				}
				ArrayList<Integer> temp = children.get(index);
				temp.add(val);
				return temp;			
			
				
		}	

		//return null;			
	}
	

	
	public ArrayList<ArrayList<Integer>> getChildren(List<positionTicTacToe> board)
	{
//		int x = node.get(0);
//		int y = node.get(1);
//		int z = node.get(2);
		ArrayList<ArrayList<Integer>> children= new ArrayList<ArrayList<Integer>>();

		for (int i = 0;i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				for(int k = 0; k < 4; k++)
				{
					positionTicTacToe position = new positionTicTacToe(i,j,k);
					int state = getStateOfPositionFromBoard(position,board);
					positionTicTacToe postate = new positionTicTacToe(i,j,k,state);
					if (state != 1 && state != 2)
					{
						ArrayList<Integer> pos = new ArrayList<Integer>();
						pos.add(i);
						pos.add(j);
						pos.add(k);
						pos.add(state);
						children.add(pos);
						
					}
				}
			}
		}
		return children;
		
	}
	
	public int getHeuristic(int player, List<positionTicTacToe> board, boolean maximizingPlayer)
	{
		int opponent;
		int decision = 0;
		if (player == 1)
		{
			opponent = 2;
		}
		else
		{
			opponent = 1;
		}
		
		//List<positionTicTacToe> planning=deepCopyATicTacToeBoard(board);
		//planning.add(new positionTicTacToe(node.get(0),node.get(1),node.get(3),player));
		if (this.isEnded(board) == player)
		{

			decision = 100000;
			return decision;
		}
		if(this.isEnded(board) == opponent)
		{
			decision = -1000000;
			return decision;
		 }
		else
		{
//			int ourscore=0;
//			int theirscore=0;
			
			for(int i=0;i<winningLines.size();i++)
			{
				positionTicTacToe p0 = winningLines.get(i).get(0);
				positionTicTacToe p1 = winningLines.get(i).get(1);
				positionTicTacToe p2 = winningLines.get(i).get(2);
				positionTicTacToe p3 = winningLines.get(i).get(3);
				
				int state0 = getStateOfPositionFromBoard(p0,board);
				int state1 = getStateOfPositionFromBoard(p1,board);
				int state2 = getStateOfPositionFromBoard(p2,board);
				int state3 = getStateOfPositionFromBoard(p3,board);
				

				ArrayList<Integer> list = new ArrayList<Integer>();
				list.add(state0);
				list.add(state1);
				list.add(state2);
				list.add(state3);
				int counter = 0;
				
				for (int n = 0; i < 4; i++)
				{
					if(list.get(n) == player)
					{
						counter ++;
					}
					else
					{
						counter --;
					}
				}
				if(counter == 3)
				{
					decision += 20;
				}
				if(counter == 2)
				{
					decision += 10;
				}
				if(counter == 1)
				{
					decision += 5;
				}
				if(counter == 0)
				{
					decision += 1;
				}
				if(counter == -1)
				{
					decision += -5;
				}
				if(counter == -2)
				{
					decision += -10;
				}
				if(counter == -3)
				{
					decision += -20;
				}
				
			}
			return decision;
		}
								
		
	}
	

	
	public List<positionTicTacToe> deepCopyATicTacToeBoard(List<positionTicTacToe> board)
	{
		//deep copy of game boards
		List<positionTicTacToe> copiedBoard = new ArrayList<positionTicTacToe>();
		for(int i=0;i<board.size();i++)
		{
			copiedBoard.add(new positionTicTacToe(board.get(i).x,board.get(i).y,board.get(i).z,board.get(i).state));
		}
		return copiedBoard;
	}
	
	
	
	
	
	
	
	
	
	private List<List<positionTicTacToe>> initializeWinningLines()
	{
		//create a list of winning line so that the game will "brute-force" check if a player satisfied any 	winning condition(s).
		List<List<positionTicTacToe>> winningLines = new ArrayList<List<positionTicTacToe>>();
		
		//48 straight winning lines
		//z axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,j,0,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//y axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,j,-1));
				winningLines.add(oneWinCondtion);
			}
		//x axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,j,-1));
				winningLines.add(oneWinCondtion);
			}
		
		//12 main diagonal winning lines
		//xz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,0,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,1,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,2,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//yz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,0,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//xy plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,0,i,-1));
				oneWinCondtion.add(new positionTicTacToe(1,1,i,-1));
				oneWinCondtion.add(new positionTicTacToe(2,2,i,-1));
				oneWinCondtion.add(new positionTicTacToe(3,3,i,-1));
				winningLines.add(oneWinCondtion);
			}
		
		//12 anti diagonal winning lines
		//xz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,3,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,2,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,1,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,0,-1));
				winningLines.add(oneWinCondtion);
			}
		//yz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,3,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,0,-1));
				winningLines.add(oneWinCondtion);
			}
		//xy plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,3,i,-1));
				oneWinCondtion.add(new positionTicTacToe(1,2,i,-1));
				oneWinCondtion.add(new positionTicTacToe(2,1,i,-1));
				oneWinCondtion.add(new positionTicTacToe(3,0,i,-1));
				winningLines.add(oneWinCondtion);
			}
		
		//4 additional diagonal winning lines
		List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,0,0,-1));
		oneWinCondtion.add(new positionTicTacToe(1,1,1,-1));
		oneWinCondtion.add(new positionTicTacToe(2,2,2,-1));
		oneWinCondtion.add(new positionTicTacToe(3,3,3,-1));
		winningLines.add(oneWinCondtion);
		
		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,0,3,-1));
		oneWinCondtion.add(new positionTicTacToe(1,1,2,-1));
		oneWinCondtion.add(new positionTicTacToe(2,2,1,-1));
		oneWinCondtion.add(new positionTicTacToe(3,3,0,-1));
		winningLines.add(oneWinCondtion);
		
		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(3,0,0,-1));
		oneWinCondtion.add(new positionTicTacToe(2,1,1,-1));
		oneWinCondtion.add(new positionTicTacToe(1,2,2,-1));
		oneWinCondtion.add(new positionTicTacToe(0,3,3,-1));
		winningLines.add(oneWinCondtion);
		
		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,3,0,-1));
		oneWinCondtion.add(new positionTicTacToe(1,2,1,-1));
		oneWinCondtion.add(new positionTicTacToe(2,1,2,-1));
		oneWinCondtion.add(new positionTicTacToe(3,0,3,-1));
		winningLines.add(oneWinCondtion);	
		
		return winningLines;
		
	}
	public aiTicTacToe(int setPlayer)
	{
		player = setPlayer;
	}
}