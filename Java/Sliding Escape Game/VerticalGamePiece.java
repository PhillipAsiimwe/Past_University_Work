//Phillip Amanya 
import javafx.scene.paint.Color;


public class VerticalGamePiece extends GamePiece {
	
    public VerticalGamePiece(int h, Color c, int x, int y) {
        super(1, h, c, x, y);
    }
	//Method to determine Whether the piece can be moved Down
    public boolean canMoveDownIn(GameBoard b) {
        if ((b.pieceAt(getTopLeftX(), getTopLeftY()+getHeight()) == null && !(getTopLeftY()+getHeight() == 6))) {
            return true;
        }
        return false;
    }
	
	//Method to determine Whether the piece can be moved Up
    public boolean canMoveUpIn(GameBoard b) {
        if ((b.pieceAt(getTopLeftX(), getTopLeftY() - 1) == null && !(getTopLeftY() == 0))) {
            return true;
        }
        return false;
    }
}