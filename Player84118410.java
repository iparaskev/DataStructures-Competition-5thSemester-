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
	    Board pBoard = parent.getBoard();  
	    if(depth>1){ //αν εχει μπει σε κινηση μετα την 1η
	    	pBoard = CrushUtilities.boardAfterFullMove(parent.getOriginalBoard(), parent.getMove());
	    }
	    ArrayList<int[]> avMoves = CrushUtilities.getAvailableMoves(pBoard); //στην μεταβλητή avMoves αποθηκέυουμε τις διαθέσιμες κινήσεις  
	                                                                         //του παίκτη μας 
	    for(int i=0;i<avMoves.size();i++){
	    	
	    	int[] move = avMoves.get(i);    //η κάθε διαθέσιμη κίνηση 
	    	Board chBoard = CrushUtilities.boardAfterFirstMove(pBoard, move); //ο πίνακας μετά την κίνηση   
	    	Node84118410 child = new Node84118410(chBoard , parent , move , depth); //δημιουργούμε τον κάθε νέο κόμβο που αντιτοιχεί σε κάθε κίνηση
	    	child.setOriginalBoard(pBoard);  // πινακας πριν την κινηση
	    	double eval = child.getEvaluation();
	    	
	    	if(depth>1){ //αν μπηκε σε επομενη κινηση επειδη την καει δικια μας κινηση θα ειναι αθροισμα
	    		eval+=parent.getEvaluation();
	    	}
	    	
	    	child.setEvaluation(eval);  
	    	parent.setChildren(child); //ορίζουμε σαν παιδί του πρώτου πίνακα τον κάθε κόμβο
	    	
	    	if(child.bonus && depth<3){ //αν υπαρχει 5αδα και ειμαστε κατω απο 4 επιπεδα καλει τον εαυτο της
	    		createMySubTree(child,depth+1);
	    	}
	    	
	    	createOpponentSubTree(child, depth+1); //καλούμε την συνάρτηση που δημιουργει το υποδέντρο για τις κινησεις του αντιπαλου
	    	
	    	 if(parent.bonus){
	              if(child.getB()> parent.getB()) parent.setB(child.getB());
	         }
	         else{
	        	 if(child.getB() > parent.getA()){
	                  parent.setA(child.getB());
	             }
	         }        
	    	
    	    if(child.getB() > parent.getA()){
    	    	parent.setA(child.getB());
    	   	}      
	    }
  }
  
  
  /* η συνάρτηση createOpponentSubTree δημιουργεί το υποδέντρο που έχει ως κόμβους τις κινήσεις του αντιπάλου
  και δέχεται ως ορίσματα ένα αντικείμενο τύπου node που είναι ο πατέρας του υποδέντρου και 
  έναν ακέραιο που δείχνει το βάθος που βρίσκεται ,δεν επιστρέφει κάτι*/ 
  private void createOpponentSubTree (Node84118410 parent, int depth)
  {
	    Board board = CrushUtilities.boardAfterFullMove(parent.getOriginalBoard(), parent.getMove()); 	
	    ArrayList<int[]> moves = CrushUtilities.getAvailableMoves(board);	//οι διαθέσιμες κινήσεις του αντιπάλου
	    
	    for(int i=0;i<moves.size();i++){
	    	
	    	//check for prune
    		if(parent.getA() >= parent.getB() ){
    			break ;
    		}
    		else{
    			int move[] = moves.get(i);	//η κάθε κίνηση
    	    	Board boardOfNode = CrushUtilities.boardAfterFirstMove(board, move);	// ο πίνακας μετά την κίνηση
        		Node84118410 child = new Node84118410(boardOfNode , parent , move , depth);  	//ο κόμβος της κάθε κίνησης
    	    	double eval = parent.getEvaluation()-child.getEvaluation(); //adjustments
    	    	//if oppenent's score becomes more than 500 
    	    	if((child.getEvaluation() + CrushUtilities.getOpponentsScore(id)) >= 500){
              	  parent.setB(Double.MIN_VALUE);
                }
    	    	child.setEvaluation(eval);   //set evaluation for opps
    	    	child.setOriginalBoard(board); //πινακας πριν την κινηση
        		parent.setChildren(child);//ορίζουμε κάθε κίνηση του αντιπάλου σαν παιδί της αντίστοιχης δική μας κίνησης 	
        		
        		if(child.bonus && depth<3){	//αν υπαρχει 5αδα και ειμαστε πανω απο το 4ο επιπεδο καλει τον εαυτο της
            		createOpponentNextSubTree(child, depth+1);
        		}
        		
        		if(depth<4){
        			createMyNextSubTree(child,depth+1); //call opp next sub tree
        		}
        		else{
        			child.setA(eval);
        		}
        		
        		if(parent.bonus){
                     if(child.getA() < parent.getA()) parent.setA(child.getA());
                 }
                 else{
                     //set b for depth 2
                     if(child.getA() < parent.getB()){
                         parent.setB(child.getA());
                     }
                 }   
            	

        		//set b for depth 2
        		if(child.getA() < parent.getB()){
        			parent.setB(child.getA());
        		}
    		}			 
	    }
  }
  //create my player's next sub tree
  private void createMyNextSubTree(Node84118410 parent,int depth){
     
      Board myBoard = CrushUtilities.boardAfterFullMove(parent.getOriginalBoard(), parent.getMove());  //o pinakas meta tin kinisi toy antipalou
      ArrayList<int []> posMoves=CrushUtilities.getAvailableMoves(myBoard); //possible moves
     
      for(int i=0; i< posMoves.size();i++){
    	//check for prune
          if(parent.getA() >= parent.getB()){
        	  break;
          }
          else{
              int[] move = posMoves.get(i); //h kinish
              Board nodeBoard = CrushUtilities.boardAfterFirstMove(myBoard, move); // o pinakas meta thn kinish   
              Node84118410 child = new Node84118410(nodeBoard, parent, move, depth);
              double eval = child.getEvaluation() + parent.getEvaluation(); // score after move;
              child.setEvaluation(eval);
              child.setOriginalBoard(myBoard);
              parent.setChildren(child);
              
              createOpponentNextSubTree(child,depth + 1);
              
              //set a for maximizer at depth 3
              if(child.getB() > parent.getA() ){
    			  parent.setA(child.getB());
    		  }
              else if((i==posMoves.size()-1) && (parent.getA()==parent.getParent().getA())){
            	  //if a doesn't change value
            	  //b for depth 2
            	  parent.getParent().setB(child.getB());
              }
          }    
      }     
  }
 
  //create opps next sub tree
  private void createOpponentNextSubTree(Node84118410 parent,int depth){
     
      Board last = CrushUtilities.boardAfterFullMove(parent.getOriginalBoard(), parent.getMove()); // final board
      ArrayList<int []> posMoves = CrushUtilities.getAvailableMoves(last); //possible moves
      for(int i=0 ; i < posMoves.size();i++){
    	  //prune for minimizer
          if(parent.getA() >= parent.getB()){
        	  break;
          }
          else{
        	  int[] move = posMoves.get(i); //h kinish
              Board nodeBoard = CrushUtilities.boardAfterFirstMove(last, move); // o pinakas meta thn kinish
              Node84118410 child = new Node84118410(nodeBoard, parent, move, depth);
              double eval = parent.getEvaluation() - child.getEvaluation(); // score after move;
              //if oppenent's score becomes more than 500 
              if((child.getEvaluation() + CrushUtilities.getOpponentsScore(id)) >= 500){
            	  parent.setB(Double.MIN_VALUE);
              }
              child.setEvaluation(eval);
              child.setOriginalBoard(last);
            
              //set a for node
              child.setA(eval);
              //set b for minimizer
              if(parent.getB() > child.getA()){
            	  parent.setB(child.getA());
            	  
              }
              else if((i== (posMoves.size()-1)) && (parent.getB() == parent.getParent().getB())){
            	  //if b doesn't change value and there arent more nodes
            	  //a for depth 3
            	  parent.getParent().setA(child.getA());
              }
              parent.setChildren(child);
          }
      }
  }
  
  
  
  /*η συνάρτηση chooseMove ελέγχει ποια κίνηση μας συμφέρει περισσότερο
    δέχεται ως όρισμα την ρίζα του δέντρου που έχει τον αρχικό πίνακα
    και επιστρέφει έναν ακέραιο που μας δίνει την θέση της κίνησης */
  private int chooseMove (Node84118410 root)
  {
	  ArrayList<Node84118410> myMoves=root.getChildren();  //μία λίστα που έχει ως περιεχόμενο τους κόμβους με τις κινήσεις μας
      double max=-500;
      int pos=-1;
	  for(int i=0; i<myMoves.size(); i++){
    	  if(myMoves.get(i).getB()>max){
    		  max=myMoves.get(i).getB();
    		  pos=i;
    	  }
      }
      return pos;
	 
  }
  private void reArrangeTree(Node84118410 root){
	  ArrayList<Node84118410> listNodes= new ArrayList<Node84118410>();
	  ArrayList<Node84118410> myMoves= root.getChildren();
	  for(int i=0; i< myMoves.size();i++){
		  ArrayList<Node84118410> oppsMoves=myMoves.get(i).getChildren();
		  
		  for(int j=0;j<oppsMoves.size();j++){
			  
		  }
	  }
  }
  
  private ArrayList<Node84118410> quicksort(ArrayList<Node84118410> child,int low,int high){
	  int pivot;
	  if(high > low){
		  pivot= partition(child,low,high);
		  quicksort(child,low,pivot-1);
		  quicksort(child,pivot+1,high);
		  
	  }
	  return child;
  }
  
  private int partition(ArrayList<Node84118410> child,int low,int high){
	  int left,right;
	  double pivot_item= child.get(low).getEvaluation();
	  Node84118410 piv=child.get(low);
	  left= low;
	  right = high;
	  while ( left < right){
		  while(child.get(left).getEvaluation() <= pivot_item) left++;
		  while( child.get(right).getEvaluation() >= pivot_item) right--;
		  if(left < right) Swap(left,right,child);
	  }
	  child.set(low, child.get(right));
	  child.set(right, piv);
	  return right;
  }
  
  private void Swap(int loc1,int loc2,ArrayList<Node84118410> child){
	  Node84118410 flag= child.get(loc1);
	  child.set(loc1, child.get(loc2));
	  child.set(loc2, flag);
  }
  
    int partitionDes (ArrayList<Node84118410> a, int low,int high){
	  int left,right;
	  Node84118410 pivot_item=a.get(low).getChildren().get(0);
	  int pivot=left=low;
	  right=high;
	  while(left<right){
		  while( a.get(left).getChildren().get(0).getEvaluation() >= pivot_item.getEvaluation()) left++;
		  while( a.get(right).getChildren().get(0).getEvaluation() <= pivot_item.getEvaluation()) right--;
		  if(left<right) {
			  Node84118410 temp = a.get(left);
			  a.set(left, a.get(right));
			  a.set(right, temp);
		  }
	  }
	  a.set(low, a.get(right));
	  a.set(right, pivot_item);
	  return right;
  }
  
  ArrayList<Node84118410> quicksortD(ArrayList<Node84118410> a, int low , int high){
	  int pivot;
	  if(high>low){
		  pivot = partitionDes(a , low , high);
		  quicksortD(a,low,pivot-1);
		  quicksortD(a,pivot+1,high);
	  }
	  return a;
  }
}
