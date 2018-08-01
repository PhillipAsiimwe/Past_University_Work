//Phillip Amanya 
import javafx.scene.paint.Color;

public class GameBoard {
    public static final int WIDTH = 6;//Wiidth of the GameBoard
    public static final int HEIGHT = 6;// Height of the GameBoard 
    public static final int MAX_GAME_PIECES = 15; // Numebr of total peieces on the Board 

    private GamePiece[]     gamePieces; //Array Contaning all the game peieces
    private int             numGamePieces; //Number of Game pieces
    private boolean         completed;//Boolean Check to determine if the game has been completed

	/** Constructor to initialize the Game Board. 
	*@Param numberPieces the number of Pieces wanted on the board
	*/
    public GameBoard(int numPieces) {
        gamePieces = new GamePiece[numPieces];
        numGamePieces = 0;
        completed = false;
    }

	/** GameBoard one used for testing and used to display on the app*/
    public static GameBoard board1() {
        GameBoard b = new GameBoard(2);
        b.add(new VerticalGamePiece(2, Color.YELLOW, 5, 0));
        b.add(new GoalPiece(1, 2));
        return b;
    }

	/** GameBoard two used for testing and used to display on the app*/   
	public static GameBoard board2() {
        GameBoard b = new GameBoard(8);
        b.add(new GoalPiece(1, 2));
        b.add(new HorizontalGamePiece(2, Color.LIGHTGREEN, 0, 0));
        b.add(new HorizontalGamePiece(2, Color.LIGHTBLUE, 4, 4));
        b.add(new HorizontalGamePiece(3, Color.GREEN, 2, 5));
        b.add(new VerticalGamePiece(3, Color.YELLOW, 5, 0));
        b.add(new VerticalGamePiece(3, Color.PURPLE, 0, 1));
        b.add(new VerticalGamePiece(3, Color.BLUE, 3, 1));
        b.add(new VerticalGamePiece(2, Color.ORANGE, 0, 4));
        return b;
    }

	/** GameBoard Three used for testing and used to display on the app*/
    public static GameBoard board3() {
        GameBoard b = new GameBoard(9);
        b.add(new GoalPiece(1, 2));
        b.add(new HorizontalGamePiece(2, Color.PINK, 3, 4));
        b.add(new HorizontalGamePiece(2, Color.PURPLE, 0, 5));
        b.add(new VerticalGamePiece(3, Color.GOLD, 0, 0));
        b.add(new VerticalGamePiece(3, Color.VIOLET, 3, 0));
        b.add(new VerticalGamePiece(3, Color.BLUE, 5, 2));
        b.add(new VerticalGamePiece(2, Color.GREEN, 5, 0));
        b.add(new VerticalGamePiece(2, Color.ORANGE, 4, 2));
        b.add(new VerticalGamePiece(2, Color.LIGHTBLUE, 2, 4));
        return b;
    }

	/** GameBoard Four used for testing and used to display on the app*/
    public static GameBoard board4() {
        GameBoard b = new GameBoard(13);
        b.add(new GoalPiece(2, 2));
        b.add(new HorizontalGamePiece(3, Color.BLUE, 0, 0));
        b.add(new HorizontalGamePiece(2, Color.PINK, 1, 1));
        b.add(new HorizontalGamePiece(2, Color.LIGHTGREEN, 0, 3));
        b.add(new HorizontalGamePiece(2, Color.YELLOW, 2, 5));
        b.add(new HorizontalGamePiece(2, Color.BROWN, 4, 4));
        b.add(new HorizontalGamePiece(2, Color.GRAY, 4, 5));
        b.add(new VerticalGamePiece(3, Color.GOLD, 4, 0));
        b.add(new VerticalGamePiece(2, Color.SADDLEBROWN, 3, 0));
        b.add(new VerticalGamePiece(2, Color.GREEN, 5, 0));
        b.add(new VerticalGamePiece(2, Color.PURPLE, 0, 1));
        b.add(new VerticalGamePiece(2, Color.ORANGE, 2, 3));
        b.add(new VerticalGamePiece(2, Color.LIGHTBLUE, 1, 4));
        return b;
    }


	/** GameBoard Five used for testing and used to display on the app*/
    public static GameBoard board5() {
        GameBoard b = new GameBoard(13);
        b.add(new GoalPiece(3, 2));
        b.add(new HorizontalGamePiece(2, Color.LIGHTGREEN, 1, 0));
        b.add(new HorizontalGamePiece(3, Color.BLUE, 0, 3));
        b.add(new HorizontalGamePiece(2, Color.BLACK, 4, 4));
        b.add(new HorizontalGamePiece(2, Color.BROWN, 0, 5));
        b.add(new HorizontalGamePiece(2, Color.YELLOW, 3, 5));
        b.add(new VerticalGamePiece(3, Color.GOLD, 0, 0));
        b.add(new VerticalGamePiece(2, Color.LIGHTBLUE, 1, 1));
        b.add(new VerticalGamePiece(2, Color.PINK, 2, 1));
        b.add(new VerticalGamePiece(2, Color.ORANGE, 4, 0));
        b.add(new VerticalGamePiece(3, Color.VIOLET, 5, 1));
        b.add(new VerticalGamePiece(2, Color.PURPLE, 3, 3));
        b.add(new VerticalGamePiece(2, Color.GREEN, 2, 4));
        return b;
    }

	/** Adds GamePiece gp to the Array of GamePieces available
	*@Param gp GamePiece to be added
	*/
    public void add(GamePiece gp) {
        if (numGamePieces < MAX_GAME_PIECES)
            gamePieces[numGamePieces++] = gp;
    }

    public int getNumGamePieces() { return numGamePieces; }//@Return The number of Gamepieces 
    public GamePiece[] getGamePieces() { return gamePieces; }//@Return an array of Gamepieces
    public boolean isCompleted() { return completed; }//@Return The boolean on the completion of the game 

	/** Searches the Array of GamesPieces and determines if the piece is horizontal or vertical. 
	*   it then returns that gamepiece at the specified x and y and returns null if no piece is located there.
	*@Param x the x coordinate
	*@Param y the Y coordinate 
	*@Return Gamepeice at that location if any was found
	*/
    public GamePiece pieceAt(int x, int y) {
       for (int i=0;i<getNumGamePieces();i++){
           if (getGamePieces()[i] instanceof HorizontalGamePiece){
               for (int w=0;w<getGamePieces()[i].getWidth();w++){
                   if ((getGamePieces()[i].getTopLeftX()+w)==x &&(getGamePieces()[i].getTopLeftY()==y)){
                       return getGamePieces()[i];
                   }
               }

           }else if(getGamePieces()[i] instanceof VerticalGamePiece) {
               for (int h = 0; h < (getGamePieces()[i].getHeight()); h++) {
                   if (((getGamePieces()[i].getTopLeftY() + h) == y) && (getGamePieces()[i].getTopLeftX() == x)) {
                       return getGamePieces()[i];
                   }
               }
           }
       }
        return null;

    }

    // Check if the board has been completed, and set the boolean accordingly
    public void checkCompletion(GamePiece gp) {
        if (gp.getTopLeftX()==5 & gp.getTopLeftY()==2){
            completed=true;
       }
    }
}
