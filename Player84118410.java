// ΚΩΝΣΤΑΝΤΙΝΟΣ ΞΑΝΘΟΠΟΥΛΟΣ, ΑΕΜ 8411, ΤΗΛ 6994157425, konxantho@gmail.com
// ΠΑΡΑΣΚΕΥΟΠΟΥΛΟΣ ΙΑΣΩΝ ,ΑΕΜ 8410, ΤΗΛ 6989280019, iaswnparaskev@gmail.com
package gr.auth.ee.dsproject.crush.node;

import gr.auth.ee.dsproject.crush.board.Board;
import gr.auth.ee.dsproject.crush.board.CrushUtilities;
import gr.auth.ee.dsproject.crush.defplayers.AbstractPlayer;
import gr.auth.ee.dsproject.crush.node.Node84118410;

import java.util.ArrayList;
//δημιουργεί έναν παίκτη που επιλέγει την κίνηση με βάση και τις κινήσεις του αντιπάλου
public class Player84118410 implements AbstractPlayer
{
  // TODO Fill the class code.

  int score;
  int id;
  String name;

  public Player84118410 (Integer pid)
  {
    id = pid; 
    score = 0;
    
  }

  public String getName ()
  {

    return "Drills";

  }

  public int getId ()
  {
    return id;
  }

  public void setScore (int score)
  {
    this.score = score;
  }

  public int getScore ()
  {
    return score;
  }

  public void setId (int id)
  {
    this.id = id;
  }

  public void setName (String name)
  {
    this.name = name;
  }

  public int[] getNextMove (ArrayList<int[]> availableMoves, Board board)
  {
    // TODO Fill the code
      
	  Board cBoard = CrushUtilities.cloneBoard(board,50); //κάνουμε clone τις πρώτες 50 γραμμές του πίνακα για να είναι πιο γρήγορη η κίνηση μας
	    
	  Node84118410 root = new Node84118410(); //δημιουργούμε ρίζα του δέντρου ένα αντικείμενο node 
	  root.setBoard(cBoard); //δίνουμε τον πίνακα όπως είναι πριν γίνει καμία κίνηση
	  createMySubTree(root, 1); //καλούμε την συνάρτηση που θα δημιουργήσει το υποδέντρο με τις κινήσεις μας
		 
	  int indexBest = 0; // η μεταβλητή που θα μας δώσει την επιλογή της καλύτερης κίνησης  
	  indexBest = chooseMove(root);  // περνάμε την θέση αυτής της κίνησης
	  int[] bestMove = availableMoves.get(indexBest);
      return CrushUtilities.calculateNextMove(bestMove);
	    
    
  }
  
  /* η συνάρτηση createMySubTree δημιουργεί το υποδέντρο που έχει ως κόμβους τις κινήσεις μας
    και δέχεται ως ορίσματα ένα αντικείμενο τύπου node που είναι ο πατέρας του υποδέντρου και 
    έναν ακέραιο που δείχνει το βάθος που βρίσκεται ,δεν επιστρέφει κάτι*/ 
  private void createMySubTree (Node84118410 parent, int depth)
  {
    // TODO Fill the code
	    Board pBoard = parent.getBoard();  
	    ArrayList<int[]> avMoves = CrushUtilities.getAvailableMoves(pBoard); //στην μεταβλητή avMoves αποθηκέυουμε τις διαθέσιμες κινήσεις  
	                                                                         //του παίκτη μας 
	    for(int i=0;i<avMoves.size();i++){
	    	//πρωτο παιδι 
	    	if(i==0){
	    		int[] move = avMoves.get(i);    //η κάθε διαθέσιμη κίνηση 
		    	Board chBoard = CrushUtilities.boardAfterFirstMove(pBoard, move); //ο πίνακας μετά την κίνηση   
		    	
		    	Node84118410 child = new Node84118410(chBoard , parent , move , depth); //δημιουργούμε τον κάθε νέο κόμβο που αντιτοιχεί σε κάθε κίνηση
		    	double eval = child.getEvaluation();	
		    	child.setEvaluation(eval);  
		    	
		    	parent.setChildren(child); //ορίζουμε σαν παιδί του πρώτου πίνακα τον κάθε κόμβο	
		        //System.out.println("1   eval "+eval+"    a,b"+parent.getA()+", "+parent.getB());
		    	createOpponentSubTree(child, depth+1); //καλούμε την συνάρτηση που δημιουργει το υποδέντρο για τις κ
	    	}
	    	//ab comp
	    	//change a of root with last child b or not 
	    	else{
	    		int[] move = avMoves.get(i);    //η κάθε διαθέσιμη κίνηση 
		    	Board chBoard = CrushUtilities.boardAfterFirstMove(pBoard, move); //ο πίνακας μετά την κίνηση   
	            //set a for parent
	    		if(parent.getChildren().get(i-1).getB() > parent.getA()){
	    			parent.setA(parent.getChildren().get(i-1).getB());
	    		}
		    	Node84118410 child = new Node84118410(chBoard , parent , move , depth); //δημιουργούμε τον κάθε νέο κόμβο που αντιτοιχεί σε κάθε κίνηση
		    	double eval = child.getEvaluation();	
		    	child.setEvaluation(eval);
		    	parent.setChildren(child); //ορίζουμε σαν παιδί του πρώτου πίνακα τον κάθε κόμβο
		    	createOpponentSubTree(child, depth+1); //καλούμε την συνάρτηση που δημιουργει το υποδέντρο για τις κ
	    	}
	        
	    }
  }
  
  
  
  /* η συνάρτηση createOpponentSubTree δημιουργεί το υποδέντρο που έχει ως κόμβους τις κινήσεις του αντιπάλου
  και δέχεται ως ορίσματα ένα αντικείμενο τύπου node που είναι ο πατέρας του υποδέντρου και 
  έναν ακέραιο που δείχνει το βάθος που βρίσκεται ,δεν επιστρέφει κάτι*/ 
  private void createOpponentSubTree (Node84118410 parent, int depth)
  {
    // TODO Fill the code
	    Board board = CrushUtilities.boardAfterFullMove(parent.getParent().getBoard(), parent.getMove()); 	
	    ArrayList<int[]> moves = CrushUtilities.getAvailableMoves(board);	//οι διαθέσιμες κινήσεις του αντιπάλου
	    
	    for(int i=0;i<moves.size();i++){
	        //ab comp
	    	if(parent.getB() <= parent.getA() ){
	    		break; //bad 
	    	}
	    	else{
	    		int move[] = moves.get(i);	//η κάθε κίνηση
		    	Board boardOfNode = CrushUtilities.boardAfterFirstMove(board, move);	// ο πίνακας μετά την κίνηση
	    		Node84118410 child = new Node84118410(boardOfNode , parent , move , depth);  	//ο κόμβος της κάθε κίνησης
		    	double eval = parent.getEvaluation()-child.getEvaluation(); //adjustments
		    	child.setEvaluation(eval);   //set evaluation for opps
	    		child.setA(eval); //give a at last node
	    		//set b for minimizer
	    		if(parent.getB()>child.getA()){
		    		parent.setB(child.getA());
		    	}
	    		parent.setChildren(child);		//ορίζουμε κάθε κίνηση του αντιπάλου σαν παιδί της αντίστοιχης δική μας κίνησης
		    	
	    	}
	    	
	    	
	    	 /*double oEval = parent.getEvaluation()-oChild.getEvaluation();		// αφαιρούμε την αξιολόγηση του αντιπάλου από την δική μας 
	    	oChild.setEvaluation(oEval);		//ορίζουμε την αξιολόγηση του αντιπάλου
	    	
	    	parent.setChildren(oChild);		//ορίζουμε κάθε κίνηση του αντιπάλου σαν παιδί της αντίστοιχης δική μας κίνησης
	    	*/
	    	 
	    }
  }
  /*create my player's next sub tree
  private void createMyNextSubTree(Node84118410 parent,int depth){
     //System.out.println("3");
      Board middle = CrushUtilities.boardAfterFullMove(parent.getParent().getParent().getBoard(), parent.getParent().getMove()); //o pinakas prin tin kinish tou antipalou
      Board myBoard = CrushUtilities.boardAfterFullMove(middle, parent.getMove());  //o pinakas meta tin kinisi toy antipalou
      ArrayList<int []> posMoves=CrushUtilities.getAvailableMoves(myBoard); //possible moves
     
      for(int i=0; i< posMoves.size();i++){
          int[] move = posMoves.get(i); //h kinish
          Board nodeBoard = CrushUtilities.boardAfterFirstMove(myBoard, move); // o pinakas meta thn kinish
          Node84118410 child = new Node84118410(nodeBoard, parent, move, depth);
          double eval = child.getEvaluation() + parent.getEvaluation(); // score after move;
          child.setEvaluation(eval);
          parent.setChildren(child);
          createOpponentNextSubTree(child,depth + 1);
      }     
  }
 
  //create opps next sub tree
  private void createOpponentNextSubTree(Node84118410 parent,int depth){
      //System.out.println("4");
      Board middle = CrushUtilities.boardAfterFullMove(parent.getParent().getParent().getParent().getBoard(),parent.getParent().getParent().getMove() ); //o pinakas pou vlepei o antipalos arxika
      Board secMidlle = CrushUtilities.boardAfterFullMove(middle, parent.getParent().getMove()); //board before my second move
      Board last = CrushUtilities.boardAfterFullMove(secMidlle, parent.getMove()); // final board
      ArrayList<int []> posMoves = CrushUtilities.getAvailableMoves(last); //possible moves
      for(int i=0 ; i < posMoves.size();i++){
          int[] move = posMoves.get(i); //h kinish
          Board nodeBoard = CrushUtilities.boardAfterFirstMove(last, move); // o pinakas meta thn kinish
          Node84118410 child = new Node84118410(nodeBoard, parent, move, depth);
          double eval = parent.getEvaluation() - child.getEvaluation(); // score after move;
          child.setEvaluation(eval);
          parent.setChildren(child);
      }
  }
  */
  
  /*η συνάρτηση chooseMove ελέγχει ποια κίνηση μας συμφέρει περισσότερο
    δέχεται ως όρισμα την ρίζα του δέντρου που έχει τον αρχικό πίνακα
    και επιστρέφει έναν ακέραιο που μας δίνει την θέση της κίνησης */
  private int chooseMove (Node84118410 root)
  {

     //TODO Fill the code
	  ArrayList<Node84118410> myMoves=root.getChildren();  //μία λίστα που έχει ως περιεχόμενο τους κόμβους με τις κινήσεις μας
      ArrayList<Node84118410> opps=new ArrayList<Node84118410>(); //μία λίστα που θα έχει ως περιεχόμενο τα παιδιά κάθε κίνησης μας
      
      double max=-300;  //η μεγαλύτερη κίνηση από τις πιθανές του αντιπάλου
      int pos=0; // η θέση της κίνησής μας
      
      for(int i=0;i<myMoves.size();i++){
          opps=myMoves.get(i).getChildren(); //η κινήσεις του αντιπάλου για την κάθε κίνηση μας
          double min=300; // το ελάχιστο από τις διαθέσιμες κινήσεις του αντιπάλου
          for(int j=0; j<opps.size(); j++){
        	  if(opps.get(j).getEvaluation()<min) min = opps.get(j).getEvaluation();
          }
          
          if(min > max){
        	  max = min;
        	  pos = i;
          }
      }
      
      return pos;
	 
  }

}
