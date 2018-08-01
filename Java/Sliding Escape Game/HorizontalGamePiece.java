//Phillip Amanya
import javafx.scene.paint.Color;

public class HorizontalGamePiece extends GamePiece {
	//Constructer for HorizontalGamePiece
    public HorizontalGamePiece(int w, Color c, int x, int y) {
        super(w, 1, c, x, y);
    }
	/** Boolean to determine if the piece can move left*/
    public boolean canMoveLeftIn(GameBoard b) {
        if((b.pieceAt(getTopLeftX()-1,getTopLeftY())==null && !(getTopLeftX()==0))){
            return true;
        }
        return false;
    }
	/** Boolean to determine if the piece can move right*/
    public boolean canMoveRightIn(GameBoard b) {
        if((b.pieceAt((getTopLeftX()+getWidth()),getTopLeftY())==null  && !(getTopLeftX()+getWidth()==6))){
            return true;
        }
        return false;
    }
}