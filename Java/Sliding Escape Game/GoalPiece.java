//Phillip Amanya
import javafx.scene.paint.Color;
// GoalPiece class, the piece can only move horizontally
public class GoalPiece extends HorizontalGamePiece {
    public GoalPiece(int x, int y) {
        super(2, Color.RED, x, y);
    }
	/** Method to determine if the Goal piece can move right
	*@Param b Board to be passed 
	*@Return boolean on whether the GoalPiece can move 
	*/
    public boolean canMoveRightIn(GameBoard b) {
        if((b.pieceAt((getTopLeftX()+getWidth()),getTopLeftY())==null)){
            return true;
        }
        return false;
    }
}
