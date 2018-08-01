//Phillip Amanya 

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class SliderPuzzleView extends Pane {
    public static final int GRID_UNIT_SIZE = 40;

    private Button[][] gridSections;
    private Button startButton, nextBoardButton;
    private TextField numMovesField;

    private SliderPuzzleGame model;

    public SliderPuzzleView(SliderPuzzleGame m) {
        model = m;

        gridSections = new Button[GameBoard.WIDTH + 2][GameBoard.HEIGHT + 2];

        // Create the wall (i.e., non-pressable) buttons
        for (int i = 0; i < 8; i++) {
            gridSections[i][0] = new Button();
            gridSections[i][0].setDisable(true);
            gridSections[i][0].relocate(i * GRID_UNIT_SIZE, 0);
            gridSections[i][0].setPrefSize(GRID_UNIT_SIZE, GRID_UNIT_SIZE);
            gridSections[i][0].setStyle("-fx-base: BLACK");
            gridSections[i][7] = new Button();
            gridSections[i][7].setDisable(true);
            gridSections[i][7].relocate(i * GRID_UNIT_SIZE, 7 * GRID_UNIT_SIZE);
            gridSections[i][7].setPrefSize(GRID_UNIT_SIZE, GRID_UNIT_SIZE);
            gridSections[i][7].setStyle("-fx-base: BLACK");
        }
        for (int i = 1; i < 7; i++) {
            gridSections[0][i] = new Button();
            gridSections[0][i].setDisable(true);
            gridSections[0][i].relocate(0, i * GRID_UNIT_SIZE);
            gridSections[0][i].setPrefSize(GRID_UNIT_SIZE, GRID_UNIT_SIZE);
            gridSections[0][i].setStyle("-fx-base: BLACK");

            gridSections[7][i] = new Button();
            gridSections[7][i].setDisable(true);
            gridSections[7][i].relocate(7 * GRID_UNIT_SIZE, i * GRID_UNIT_SIZE);
            gridSections[7][i].setPrefSize(GRID_UNIT_SIZE, GRID_UNIT_SIZE);


            if (i != 3) {
                gridSections[7][i].setStyle("-fx-base: BLACK");
            } else {
                gridSections[7][i].setStyle("-fx-base: WHITE");
                gridSections[7][i].setText("EXIT");
            }
        }

        // Create the inner "pressable" Buttons
        for (int w = 1; w < 7; w++) {
            for (int h = 1; h < 7; h++) {
                gridSections[w][h] = new Button();
                gridSections[w][h].relocate(w * GRID_UNIT_SIZE, h * GRID_UNIT_SIZE);
                gridSections[w][h].setPrefSize(GRID_UNIT_SIZE, GRID_UNIT_SIZE);
                gridSections[w][h].setStyle("-fx-base: WHITE; -fx-text-fill: RED;");
                gridSections[w][h].setFocusTraversable(false);
            }
        }

        // Add all the buttons to the window
        for (int w = 0; w < 8; w++) {
            for (int h = 0; h < 8; h++) {
                //if (!((h == 3) && (w == 7)))
                getChildren().add(gridSections[w][h]);
            }
        }
        // Add the Start and NextBoard buttons
        startButton = new Button("Start");
        startButton.setPrefSize(100, 25);
        startButton.relocate(10, 340);

        nextBoardButton = new Button("Next Board");
        nextBoardButton.setPrefSize(100, 25);
        nextBoardButton.relocate(120, 340);

        // Add the Num Moves Field
        numMovesField = new TextField("0");
        numMovesField.setPrefSize(50, 25);
        numMovesField.setStyle("-fx-alignment:center-right;");
        numMovesField.relocate(260, 340);


        getChildren().addAll(startButton, nextBoardButton, numMovesField);

        update(); // Update with no board
    }

    public Button getGridSection(int w, int h) {
        return gridSections[w][h];
    }

    public Button getStartButton() {
        return startButton;
    }

    public Button getNextBoardButton() {
        return nextBoardButton;
    }

    public void update() {
		//Sets everything to white
        for (int w = 1; w < 7; w++) {
            for (int h = 1; h < 7; h++) {
                gridSections[w][h].setStyle("-fx-base: WHITE;");
                gridSections[w][h].setDisable(true);
                nextBoardButton.setDisable(true);
            }
        }
		//Recolours the board with the updated moves.
        GameBoard toPlay = model.getCurrentBoard();
        for (int i = 0; i < toPlay.getNumGamePieces(); i++) {
            GamePiece toshow = toPlay.getGamePieces()[i];
            if (toshow instanceof HorizontalGamePiece) {
                for (int w = 1; w <= toshow.getWidth(); w++) {
                    int width = toshow.getTopLeftX() + w;
                    if (width < 8) {
                        gridSections[width][toshow.getTopLeftY() + 1].setStyle("-fx-base: #" + toshow.getColor().toString().substring(2, 8));
                    }
                }
            } else {
                for (int h = 1; h <= toshow.getHeight(); h++) {
                    int height = toshow.getTopLeftY() + h;
                    gridSections[toshow.getTopLeftX() + 1][height].setStyle("-fx-base: #" + toshow.getColor().toString().substring(2, 8));
                }

            }
        }
        if (model.isBoardInProgress()){//checks the status of the board and sets buttons and sections
            for (int w = 1; w < 7; w++) {
                for (int h = 1; h < 7; h++) {
                    gridSections[w][h].setDisable(false);
                    startButton.setDisable(true);
                    nextBoardButton.setDisable(false);
                }
            }

        }
        else if (model.areWeWaitingToStartABoard()){
            startButton.setDisable(false);
            gridSections[7][3].setStyle("-fx-base: WHITE;");
            gridSections[6][3].setStyle("-fx-base: WHITE;");


        }else if (model.getCurrentBoard().isCompleted()){
            for (int w = 1; w < 7; w++) {
                for (int h = 1; h < 7; h++) {
                    gridSections[w][h].setStyle("-fx-base: WHITE;");
                    gridSections[w][h].setDisable(true);
                }
            }
            gridSections[7][3].setStyle("-fx-base: WHITE;");
            nextBoardButton.setDisable(false);
        }
        else {
            nextBoardButton.setDisable(false);
        }
            numMovesField.setText(Integer.toString(model.getNumberOfMovesMade()));


        }
    }
