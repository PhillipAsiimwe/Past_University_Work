//Phillip Amanya 
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SliderPuzzleApp extends Application {
    private SliderPuzzleGame    model;
    private SliderPuzzleView    view;

    private GamePiece           selectedPiece;
    private boolean             justGrabbed;
    private int                 lastX;
    private int                 lastY;

    public void start(Stage primaryStage) {
        model = new SliderPuzzleGame();
        view = new SliderPuzzleView(model);

        // Add event handlers to the inner game board buttons
        for (int w=1; w<=(GameBoard.WIDTH); w++) {
            for (int h=1; h<=(GameBoard.HEIGHT); h++) {
                view.getGridSection(w, h).setOnMousePressed(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent mouseEvent) {
                        handleGridSectionSelection(mouseEvent);
                    }

                });
                view.getGridSection(w, h).setOnMouseDragged(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent mouseEvent) {
                        handleGridSectionMove(mouseEvent);

                    }
                });
            }
        }

        // Plug in the Start button and NeaxtBoard button event handlers
        view.getStartButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                model.startBoard();
                view.update();
            }
        });
        view.getNextBoardButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                model.moveToNextBoard();
                System.out.println(model.getCurrentBoard().getNumGamePieces());
                view.update();
            }
        });

        primaryStage.setTitle("Slide Puzzle Application");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(view, -10+SliderPuzzleView.GRID_UNIT_SIZE*(GameBoard.WIDTH+2),45+SliderPuzzleView.GRID_UNIT_SIZE*(GameBoard.HEIGHT+2)));
        primaryStage.show();

        // Update the view upon startup
        view.update();
    }


    private void handleGridSectionSelection(MouseEvent mouseEvent) {
        selectedPiece=null;
        for (int w = 0; w < 7; w++) {
            for (int h = 0; h < 7; h++) {
                if (view.getGridSection(w,h)==mouseEvent.getSource()){
                    selectedPiece=model.getCurrentBoard().pieceAt(w-1,h-1);
                    lastX=w;
                    lastY=h;
                    }
                }
            }
            justGrabbed=false;
        }
	
	/** HandleGridSelectionMove is a mouse event that is activated when the mouse is used to move on of the pieces.
	*   It determines which peice is trying to be moved, checks if there is space for it to be moved into and finally moves the piece.
	*   The Method differntiates between up and down movement with left and right movement.
	*/
    private void handleGridSectionMove(MouseEvent mouseEvent) {

        int currentGridX = (int) mouseEvent.getX();
        int currentGridY = (int) mouseEvent.getY();
        if (selectedPiece != null) {
            int Horz = currentGridX - lastX;
            int Vert = currentGridY - lastY;
            if (Math.abs(Horz) > Math.abs(Vert) && Horz >= 40) {
                if (selectedPiece.canMoveRightIn(model.getCurrentBoard())&& selectedPiece instanceof HorizontalGamePiece) {
                    for (int i = 0; i < model.getCurrentBoard().getGamePieces().length; i++) {
                        if (model.getCurrentBoard().getGamePieces()[i] == selectedPiece) {
                            model.getCurrentBoard().getGamePieces()[i].moveRight();
                            lastX = currentGridX;
                            lastY = currentGridY;
                            if (!justGrabbed){
                                model.makeAMove();
                                justGrabbed=true;
                            }
                        }
                    }
                }
            }else if(Math.abs(Horz) > Math.abs(Vert) && Horz <= -40){
                if (selectedPiece.canMoveLeftIn(model.getCurrentBoard()) && selectedPiece instanceof HorizontalGamePiece) {
                    for (int i = 0; i < model.getCurrentBoard().getGamePieces().length; i++) {
                        if (model.getCurrentBoard().getGamePieces()[i] == selectedPiece) {
                            model.getCurrentBoard().getGamePieces()[i].moveLeft();
                            lastX = currentGridX;
                            lastY = currentGridY;
                            if (!justGrabbed){
                                model.makeAMove();
                                justGrabbed=true;
                            }
                        }
                    }
                }
            }else if(Math.abs(Horz) < Math.abs(Vert) && Vert <= -40){
                if (selectedPiece.canMoveUpIn(model.getCurrentBoard())&& selectedPiece instanceof VerticalGamePiece) {
                    for (int i = 0; i < model.getCurrentBoard().getGamePieces().length; i++) {
                        if (model.getCurrentBoard().getGamePieces()[i] == selectedPiece) {
                            model.getCurrentBoard().getGamePieces()[i].moveUp();
                            lastX = currentGridX;
                            lastY = currentGridY;
                            if (!justGrabbed){
                                model.makeAMove();
                                justGrabbed=true;
                            }
                        }
                    }
                }
            }else if(Math.abs(Horz) < Math.abs(Vert) && Vert >= 40){
                if (selectedPiece.canMoveDownIn(model.getCurrentBoard())&& selectedPiece instanceof VerticalGamePiece) {
                    for (int i = 0; i < model.getCurrentBoard().getGamePieces().length; i++) {
                        if (model.getCurrentBoard().getGamePieces()[i] == selectedPiece) {
                            model.getCurrentBoard().getGamePieces()[i].moveDown();
                            lastX = currentGridX;
                            lastY = currentGridY;
                            if (!justGrabbed){
                                model.makeAMove();
                                justGrabbed=true;
                            }
                        }
                    }
                }
            }
        }
        view.update();
        if (selectedPiece instanceof GoalPiece ){
            model.getCurrentBoard().checkCompletion(selectedPiece);
            if (model.getCurrentBoard().isCompleted()){
                model.completeBoard();
            }

        }

    }



    public static void main(String[] args) { launch(args); }
}

