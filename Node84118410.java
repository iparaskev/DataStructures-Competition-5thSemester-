// ΚΩΝΣΤΑΝΤΙΝΟΣ ΞΑΝΘΟΠΟΥΛΟΣ, ΑΕΜ 8411, ΤΗΛ 6994157425, konxantho@gmail.com
// ΠΑΡΑΣΚΕΥΟΠΟΥΛΟΣ ΙΑΣΩΝ ,ΑΕΜ 8410, ΤΗΛ 6989280019, iaswnparaskev@gmail.com
package gr.auth.ee.dsproject.crush.node;

import java.util.ArrayList;

import gr.auth.ee.dsproject.crush.board.Board;
import gr.auth.ee.dsproject.crush.board.CrushUtilities;

public class Node84118410
{
	Node84118410 parent;
	ArrayList<Node84118410> children;
	int nodeDepth;
	int[] nodeMove;
	Board nodeBoard;
	double nodeEvaluation;
	double a; //maximizer worst case 
	double b; //minimizer worst case
	
	//constructor
	public Node84118410(Node84118410 par, ArrayList<Node84118410> child, int depth,int[] move,Board boardM){
		parent = par;
		children = child;
		nodeDepth = depth;
		nodeMove = move;
		nodeBoard = boardM;
		a = par.getA(); //child get parents ab
		b = par.getB(); 
		nodeEvaluation= moveEvaluation(nodeMove, nodeBoard);
	}
	
	public Node84118410(){
		children=new ArrayList<Node84118410>();
		nodeDepth=0;			
		nodeEvaluation=0;
		a=Integer.MIN_VALUE; //value for a
		b=Integer.MAX_VALUE; //value for b
		
	}
	
	public Node84118410(Board board, Node84118410 par , int[] m , int d){
		parent = par;
		nodeBoard=board;
		nodeDepth = d ;
		nodeMove = m;
		children = new ArrayList<Node84118410>();
		a = par.getA(); //child get parents ab
		b = par.getB(); 
		nodeEvaluation= moveEvaluation(nodeMove, nodeBoard);
	}
	
	//setters
	public void setParent(Node84118410 par){
		parent=par;
	}
	
	public void setChildren(Node84118410 child){
		children.add(child);
	}
	
	public void setDepth(int depth){
		nodeDepth=depth;
	}
	
	public void setMove(int[] move){
		nodeMove=move;
	}
	
	public void setBoard(Board board){
		nodeBoard=board;
	}

	public void setEvaluation(double eval){
		nodeEvaluation=eval;
	}
	
	public void setA(double alpha){
		a = alpha ;
	}
	
	public void setB(double beta){
		b = beta ;
	}
	//getters
	public Node84118410 getParent(){
		return parent;
	}

	public ArrayList<Node84118410> getChildren(){
		return children;
	}
	
	public int getDepth(){
		return nodeDepth;
	}
	
	public int[] getMove(){
		return nodeMove;
	}

	public Board getBoard(){
		return nodeBoard;
	}

	public double getEvaluation(){
		return nodeEvaluation;
	}
	
	public double getA(){
		return a;
	}
	
	public double getB(){
		return b;
	}

	
	//υπολογίζει την αξιολόγηση της κάθε κίνησης	
	double moveEvaluation (int[] move, Board board2)
	{
			  
		double evaluation=0;
			  
			  int x=move[0],y=move[1] ; //οι συντεταγμένες του πλακιδίου μετά την κίνηση
			  switch (move[2]){
			  case CrushUtilities.LEFT : x=move[0]-1;
			  		  break;
			  case CrushUtilities.DOWN :
			  		  y = move[1]-1;
			  		  break;
			  case CrushUtilities.RIGHT :x = move[0]+1;
			  		  break;
			  case CrushUtilities.UP :
			  		  y = move[1]+1;
			  		 break;
			  }
			  
			  int color=board2.giveTileAt(x,y).getColor(); //το χρώμα του πλακιδίου που θα μετακινηθεί
			  
			  int ver = sameUp(x,y,color,board2) + sameDown(x, y, color,board2) +1; //το σύνολο των όμοιων πλακιδίως κάθετα
			  int hor = sameLeft(x,y,color,board2) + sameRight(x,y,color,board2	) +1;  //το σύνολο των όμοιων πλακιδίων οριζόντια
			  	
			  int points[] = evaluationPoints(hor,ver,x,y,color,board2);//πίνακας ακεραίων με τους πόντους,τον αριθμό των πλακιδιών που κουνιούνται
                                                                        //και τον προσανατολισμό της διαγραφής	   
			  int chain = checkForChain( board2);//οι πόντοι από την αλυσιδωτή κίνηση αν υπάρχει 
			  //int proxy[] = sameColorInProximity(3, 3, board2, x, y); //ο πίνακας με τον αριθμό των πλακιδιών που έχουν ίδιο χρώμα μεταξύ τους
			  //int min = 1;
			  //for(int k=0;k<proxy.length;k++){
			//	  if(proxy[k]>2 && k!=color){
				//	  min=0;
				  //}
			 // }
			  int pointsTotal = points[0]+chain; //οι συνολικοί πόντοι μαζί με αυτούς της αλυσιδοτής κίνησης
			  //if(pointsTotal > 5){
			//	  evaluation = 80 + pointsTotal*0.2  + points[2] + min + points[1]/10;
			  //}
			  //else{
				//  evaluation = (pointsTotal*points[1])/100 + (points[2]+min);
			  //}
			
			  return pointsTotal;
			  

		}

	    //επιστρέφει τον αριθμό των πλακιδίων ίδου χρώματος αριστερά από το πλακίδιο της κίνησης
		int sameLeft(int x, int y, int col, Board board){
			  
			  if(x>0){
				  if(board.giveTileAt(x-1,y).getColor() == col){
					  return 1+sameLeft(x-1,y , col , board);
				  }
			  }
			  return 0;
		}
		//επιστρέφει τον αριθμό των πλακιδίων ίδου χρώματος δεξιά από το πλακίδιο της κίνησης
		int sameRight(int x,int y, int col , Board board){
			  
			  if(x+1<board.getCols()){
				  if(board.giveTileAt(x+1,y).getColor() == col){
					  return 1+sameRight(x+1,y , col , board);
				  }
			  }
			  return 0;
		}
		//επιστρέφει τον αριθμό των πλακιδίων ίδου χρώματος πάνω από το πλακίδιο της κίνησης
		int sameUp(int x,int y, int col , Board board){
			  
			  if(y+1<CrushUtilities.NUMBER_OF_PLAYABLE_ROWS){
				  if(board.giveTileAt(x,y+1).getColor() == col){
					  return 1+sameUp(x,y+1 , col , board);
			  	  }
			  }
			  return 0;
		}
		//επιστρέφει τον αριθμό των πλακιδίων ίδου χρώματος κάτω από το πλακίδιο της κίνησης
		int sameDown(int x,int y, int col , Board board){
			  
			  if(y>0){
				  if(board.giveTileAt(x,y-1).getColor() == col){
					  return 1+sameDown(x,y-1 , col , board);
				  }
			  }
			  return 0;
		}
		//επιστρέφει τον αριθμό των πλακιδίων που θα διαγραφούν,που θα μετακινηθούν και τον προσανατολισμό της διαγραφής 
		int[] evaluationPoints(int hor,int ver,int x,int y,int color, Board board){
			  int belongs = 0; //ο αριθμός των πλακιδιών που θα διαγραφούν 
			  int moving = 0; //ο αριθμός των πλακιδιών που θα κουνηθούν 
			  int orientation = 0;//προσανατολισμός της διαγραφής ,0 κάθετη, 1 οριζόντια και 3 και τα δύο
			    
			  //κάθετη κίνηση
			  if(hor<3){
				  belongs=ver ;
				  moving=10-(y-(sameDown(x,y,color,board)) );  //10-κατώτερο σημείο 
				  orientation = 1;
			  }
			  
			  //ïñéæüíôéá êßíçóç
			  else if(ver<3){
				  
				  belongs=hor;
				  moving=hor*(CrushUtilities.NUMBER_OF_PLAYABLE_ROWS-y) ; //οριζόντια*(απόσταση από κορυφή) 
				  orientation = 2;
				 
			  }
			  
			  //οριζόντια και κάθετη 
			  else if((ver>2) && (hor>2)){
				  belongs=ver + hor - 1; 
				  moving=hor*(10-y) + sameDown(x,y,color,board) ;
				  orientation= 3;
			  }
			  int[] points = {belongs , moving , orientation};
			  return points;
			  
		}

		//ο αριθμός των πλακιδιών που έχουν ίδιο χρώμα μεταξύ τους στο τετράγωνο width*height 
		int[] sameColorInProximity(int width,int height,Board board,int x,int y){
			  int[] send = new int[7]; // o πίνακας που επιστρέφεται 
			  //τα όρια της αναζήτησης
			  int sx=x-width/2;
			  int fx=x+width/2+1;
			  int sy=y-height/2;
			  int fy=y+height/2+1;
			  if(sx < 0){
				 sx=0;
			  }
			  if(x+width/2 > 9){
				 fx=board.getCols();
			  }
			  if(sy < 0){
				  sy=0;
			  }
			  if(y+height/2 > 9){
				  fy=10;
			  }
			  //η αναζήτηση
			  for(int i=sx ; i<fx ; i++){
				  for(int j=sy ; j<fy ; j++){
					  int color=board.giveTileAt(i,j).getColor();
					  switch(color){
					  case CrushUtilities.RED : send[0]++;
					  							break;
					  case CrushUtilities.GREEN : send[1]++;
					  							  break;
					  case CrushUtilities.BLUE : send[2]++;
												 break;
					  case CrushUtilities.YELLOW : send[3]++;
												   break;
					  case CrushUtilities.BLACK : send[4]++;
												  break;
					  case CrushUtilities.PURPLE : send[5]++;
												   break;
					  case CrushUtilities.CYAN : send[6]++;
												 break;
					  }
				  }
			  }
			  return send;
		}



		//ελέγχει αν υπάρχουν chainmoves μετά την κίνηση μας,δέχεται ως όρισμα μία μεταβλητή τύπου Board
		//και επιστρέφει έναν ακέραιο
		int checkForChain(Board boardAfterMov){
			int chainCounter=0; //οι συνολικοί πόντοι αλυσίδας
			int newHor=0; // αποθηκεύει τα νέα κάθετα
			int newVer=0; // αποθηκεύει τα νέα οριζόντια
			int newColor=-1;
			ArrayList<int []>  borders=new ArrayList<int []>();  // λιστα για ορια κάθετης ν-αδας,δηλαδή πιο είναι το μεγαλύτερο y της κίνησης
			Board boardAfterCr=CrushUtilities.boardAfterDeletingNples(boardAfterMov); //ο πίνακας μετά την διαγραφή των πλακιδίων της κίνησης
			//προσπέλαση όλου του πίνακα
			for(int y=0;y<CrushUtilities.NUMBER_OF_PLAYABLE_ROWS;y++){ 
				for(int x=0;x<CrushUtilities.NUMBER_OF_COLUMNS;x++){
					newColor=boardAfterCr.giveTileAt(x, y).getColor(); //το χρώμα του πλακιδίου που βρισκόμαστε
					newHor=sameRight(x,y,newColor,boardAfterCr)+1; //παντα θα βρισκει τα δεξια λογω προσπελασης
					newVer=sameUp(x,y,newColor,boardAfterCr)+1; //παντα θα βρισκει τα πανω λογω προσπελασης
					//αν βρει ν-άδα μόνο οριζόντια
					if(newHor>2 && newVer<3){
						//αν δεν έχει προηγηθεί κάθετη κίνηση
						if(borders.isEmpty()){
							chainCounter+=newHor;
							for(int xboard=x+1;xboard<x+newHor;xboard++){
								newVer=sameUp(xboard,y,newColor,boardAfterCr)+1;
								if(newVer>2){
									chainCounter+=newVer-1;
									borders.add(fillTheList(xboard,y,sameUp(xboard,y,newColor,boardAfterCr),newColor));
								}
							}
						}
						// αν δεν είναι άδεια η λίστα borders
						else{
							int numberOfShorterBorders=0;
							int checkLeft=0;
							int flag= 0;
							for(int i=0; i<borders.size();i++){
								if(y>borders.get(i)[1]) numberOfShorterBorders+=1; // ελέγχουμε αν η διαγραφή μας βρίσκεται πιο ψηλά από όλες τις κάθετες διαγραφές
								if((x+(newHor-1))<borders.get(i)[0]){
									checkLeft+=1;
								}
								//αν η διαγραφή είναι σε εύρος της κάθετης και το χ >= της στηλης της κάθετης
								else if(((x+(newHor-1))>=borders.get(i)[0]) && y <= borders.get(i)[1]){
									// αν η διαγραφή έχει σχέση με την κάθετη και σχηματίζουν μία διαγραφή
									if((x<=borders.get(i)[0]) && newColor==borders.get(i)[2]) flag=1; 
								}
							}
							//αν είναι πιο ψηλά από όλες τις κάθετες διαγραφές
							if(numberOfShorterBorders == borders.size()){
								chainCounter+=newHor;  
								for(int xboard=x+1;xboard<x+newHor;xboard++){
									newVer=sameUp(xboard,y,newColor,boardAfterCr)+1;
									if(newVer>2){
										chainCounter+=newVer-1;
										borders.add(fillTheList(xboard,y,sameUp(xboard,y,newColor,boardAfterCr),newColor));
									}
								}
							}
						    // αν δεν είναι πιο ψηλά από όλες τις διαγραφές
							else{
								//αν είναι πιο αριστερά από όλες τις κάθετες
								if(checkLeft == borders.size()){
									chainCounter+=newHor; 
									for(int xboard=x+1;xboard<x+newHor;xboard++){
										newVer=sameUp(xboard,y,newColor,boardAfterCr)+1;
										if(newVer>2){
											chainCounter+=newVer-1;
											borders.add(fillTheList(xboard,y,sameUp(xboard,y,newColor,boardAfterCr),newColor));		
										}
									}
								}
								else {
									//αν δεν υπάρχει κάθετη στο ευρος που είναι και σε ύψος μεγαλύτερη ιση του y 
									if(flag == 0){
										chainCounter+=newHor; 
										for(int xboard=x+1;xboard<x+newHor;xboard++){
											newVer=sameUp(xboard,y,newColor,boardAfterCr)+1;
											if(newVer>2){
												chainCounter+=newVer-1;
												borders.add(fillTheList(xboard,y,sameUp(xboard,y,newColor,boardAfterCr),newColor));		
											}
										}
									}
									else{
									    chainCounter+=newHor -1;
									}
								}
							}
						}
						x+=sameRight(x,y,newColor,boardAfterCr);	
					}
					//αν είναι κάθετη η διαγραφή
					else if(newHor<3 && newVer>2){
						//αν είναι άδεια η λίστα borders
						if(borders.isEmpty()){
							chainCounter+=newVer;
							borders.add(fillTheList(x,y,sameUp(x,y,newColor,boardAfterCr),newColor));
						}
						else{
							int yMax=-1;
							for(int i=0 ; i<borders.size();i++){
								if(x == borders.get(i)[0]){
									if(borders.get(i)[1] > yMax) yMax = borders.get(i)[1]; //αν υπάρχει προηγούμενη κάθετη με το ίδιο χ
								}
							}
							//αν το υ της διαγραφής είναι μεγαλύτερο του μέγιστου κάθετου y στο ίδιο x τότε προσθέτω στο counter
							if(y > yMax ){
								chainCounter+=newVer;
								borders.add(fillTheList(x,y,sameUp(x,y,newColor,boardAfterCr),newColor));
							}
						}	
					}
					else if(newVer>2 && newHor>2){
						chainCounter+=newHor+newVer-1;
						borders.add(fillTheList(x,y,sameUp(x,y,newColor,boardAfterCr),newColor));
						x+=sameRight(x,y,newColor,boardAfterCr);
					}
				}
			}
		    
			if(chainCounter>0){
				return chainCounter+=checkForChain(boardAfterCr);
			}
			else return chainCounter;
			
		}


		//fill the arraylist πινακας με χ,υ και χρωμα για να αποφευγω διπλά 
		int [] fillTheList(int x,int y,int sameUp,int color){
			int specs[]=new int[3];
			specs[0]=x;
			specs[1]=y+sameUp;
			specs[2]=color;

			return specs;	
		}		
}
