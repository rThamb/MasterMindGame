package GUI;

import clientBusiness.MasterMindGame;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Renuchan
 */
public class Controller implements Initializable {
    
    @FXML private Button submitBtn;
    @FXML private MenuItem setAnswerMI;
    @FXML private Text resultMsg;
    
    @FXML private Circle[] answers;
    @FXML private Circle[] choices;
    @FXML private Circle[][] guesses;
    @FXML private Rectangle[][] clues;
    
    @FXML private Circle answer1, answer2, answer3, answer4;
    
    @FXML private Circle choice1, choice2, choice3, choice4, 
                            choice5, choice6, choice7, choice8;
    
    @FXML private Circle guess01, guess02, guess03, guess04,
                            guess11, guess12, guess13, guess14,
                            guess21, guess22, guess23, guess24,
                            guess31, guess32, guess33, guess34,
                            guess41, guess42, guess43, guess44,
                            guess51, guess52, guess53, guess54,
                            guess61, guess62, guess63, guess64,
                            guess71, guess72, guess73, guess74,
                            guess81, guess82, guess83, guess84,
                            guess91, guess92, guess93, guess94;
    
    @FXML private Rectangle clue01, clue02, clue03, clue04,
                            clue11, clue12, clue13, clue14,
                            clue21, clue22, clue23, clue24,
                            clue31, clue32, clue33, clue34,
                            clue41, clue42, clue43, clue44,
                            clue51, clue52, clue53, clue54,
                            clue61, clue62, clue63, clue64,
                            clue71, clue72, clue73, clue74,
                            clue81, clue82, clue83, clue84,
                            clue91, clue92, clue93, clue94;
    
    private MasterMindGame MMGame;
    private Circle currentChoice;
    
    private Stage setAnswerStage;
    private TextField newAnswerTf1;
    private TextField newAnswerTf2;
    private TextField newAnswerTf3;
    private TextField newAnswerTf4;
    private Text answerStageErrorMsg;
    
    
    /**
     * Overrides initialize to add Circle and Rectangle objects on 
     * the GUI into the corresponding arrays.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        answers = new Circle[] {
            answer1, answer2, answer3, answer4
        };
        
        choices = new Circle[] {
            choice1, choice2, choice3, choice4, 
            choice5, choice6, choice7, choice8
        };
        
        guesses = new Circle[][] {
            {guess01, guess02, guess03, guess04},
            {guess11, guess12, guess13, guess14},
            {guess21, guess22, guess23, guess24},
            {guess31, guess32, guess33, guess34},
            {guess41, guess42, guess43, guess44},
            {guess51, guess52, guess53, guess54},
            {guess61, guess62, guess63, guess64},
            {guess71, guess72, guess73, guess74},
            {guess81, guess82, guess83, guess84},
            {guess91, guess92, guess93, guess94}
        };
        
        clues = new Rectangle[][] {
            {clue01, clue02, clue03, clue04},
            {clue11, clue12, clue13, clue14},
            {clue21, clue22, clue23, clue24},
            {clue31, clue32, clue33, clue34},
            {clue41, clue42, clue43, clue44},
            {clue51, clue52, clue53, clue54},
            {clue61, clue62, clue63, clue64},
            {clue71, clue72, clue73, clue74},
            {clue81, clue82, clue83, clue84},
            {clue91, clue92, clue93, clue94}
        };
    }  
    
    /**
     * MMModel will give a MasterMindGame class with connected IP address and 
     * port to this controller in order to set and use its class method
     * @param MMG 
     */
    public void setMMGame(MasterMindGame MMG) {
        this.MMGame = MMG;
    }
    
    /**
     * Launches a new game by resetting everything to default. If the 
     * attempt counter is over 0 (meaning it isn't their first time 
     * playing the game), use MasterMindGame method to reset itself too.
     */
    public void launchNewGame() {
        
        //reset error message and enable submit button
        resultMsg.setText("");
        submitBtn.setDisable(false);
        setAnswerMI.setDisable(false);
        //make selected color to red (default color)
        if(currentChoice != null)
            currentChoice.setEffect(dimChoice());
        choices[0].setEffect(highlightChoice());
        currentChoice = choices[0];
        //set 1st row clickable and unplayed color
        //hide answer colors
        for(int i=0; i<4; i++) {
            guesses[0][i].setDisable(false);
            guesses[0][i].setFill(Color.web("#878282"));
            answers[i].setFill(Color.web("#878282"));
            clues[0][i].setFill(Color.web("#878282"));
        }
        //set row 2-10 not clickable and unplayed color
        for(int i=1; i<10; i++) {
            for(int j=0; j<4; j++) {
                guesses[i][j].setFill(Color.web("#878282"));
                guesses[i][j].setDisable(true);
                clues[i][j].setFill(Color.web("#878282"));
            }
        }
        
        try{
            //if user restarts and plays again
            if(MMGame.getAttempts() > 0)
                MMGame.playAgain();
        }
        catch(IOException e) {
            System.out.println("Error: could not start new game.");
            System.exit(1);
        }
    }
    
    /**
     * Action event method gets called when user picks a new 
     * color selection. Sets selected as new current Circle
     * @param event 
     */
    @FXML
    private void onPickColor(MouseEvent event) {
        
        currentChoice.setEffect(dimChoice());
        
        currentChoice = (Circle)event.getSource();
        currentChoice.setEffect(highlightChoice());
    }
    
    /**
     * Action event method gets called when user places the 
     * current color pick on the circle at that row.
     * @param event 
     */
    @FXML
    private void onSetChoice(MouseEvent event) {
        
        Circle selectedCircle = (Circle)event.getSource();
        selectedCircle.setFill(currentChoice.getFill());
    }
    
    /**
     * Action event method gets called user presses the submit 
     * button, which will check if all circles in that row are 
     * placed. If not, do nothing, if so, send a packet that 
     * contains 4 bytes corresponding to chosen colors. Get 
     * the clues after sending the attempt.
     * 
     * Then, check if the player won. If so, use endGame method. 
     * If not, check if player lost or continue the game.
     * @param event 
     */
    @FXML
    private void onSubmit(MouseEvent event) {
  
        boolean allSet = true;
        int currentRow = MMGame.getAttempts();
        
        //if 1 circle in that row has unplayed color, set allSet to false
        for(int i=0; i<4; i++)
            if(guesses[currentRow][i].getFill().equals(Color.web("#878282")))
                allSet = false;
        
        //if all colors are placed
        if(allSet) {
            
            byte[] rowGuess = new byte[4];
            //set circles in current row not clickable
            //get byte number by color from each 4 circles (e.g brown = 8)
            for(int i=0; i<4; i++) {
                guesses[currentRow][i].setDisable(true);
                rowGuess[i] = getNumByColor(guesses[currentRow][i].getFill());
            }
            
            try{
                //send guess packet to server and retrieve clues from server
                MMGame.sendGuess(rowGuess);     
                byte[] rowClues = MMGame.readReponse();
                
                //use method to display clues on GUI with retrieved clues packet
                //also checks if player won by returning a boolean
                boolean isWon = displayClues(rowClues, currentRow);
                
                //get new attempt value after increment
                currentRow = MMGame.getAttempts();
                
                if(isWon)
                    endGame(true, rowGuess); //true means win
                else {
                    //if player didnt win, either lose or continue
                    if(currentRow == 10)
                        endGame(false, rowClues); //false means lose
                    else {
                        //set next row circles to clickable
                        for(int i=0; i<4; i++) 
                            guesses[currentRow][i].setDisable(false);
                    }
                }
            }
            catch(IOException e) {
                System.out.println("Error: unable to send or receive bytes");
                System.exit(1);
            }
        }
    }
    
    /**
     * Given the clues packet array and current row, display the 
     * clues on the GUI. If all 4 clues are WHITE, return boolean 
     * true to signal player won, else return false.
     * 
     * @param rowClues
     * @param currentRow
     * @return isWon
     */
    private boolean displayClues(byte[] rowClues, int currentRow) {
        
        boolean isWon = true;
        //loop through each clues
        for(int i=0; i<4; i++) {
            
            switch (rowClues[i]) {
                case 2:
                    clues[currentRow][i].setFill(Color.BLACK);
                    break;
                case 1:
                    clues[currentRow][i].setFill(Color.WHITE);
                    isWon = false;
                    break;
                default:    //if == 0
                    isWon = false;
                    break;
            }
        }
        return isWon;
    }
    
    /**
     * Method gets called when player either wins or reach max(10) attempts. 
     * If player guessed right, send their chosen colors as answerCodes, 
     * otherwise, answerCodes will be the clues packet that's retrieved from 
     * server, which contains the answer only if attempt reached 10.
     * 
     * User answerCodes to display answer colors on GUI.
     * @param isWin
     * @param answerCodes 
     */
    private void endGame(boolean isWin, byte[] answerCodes) {
        
        //disable submit button and set answer menu item
        submitBtn.setDisable(true);
        setAnswerMI.setDisable(true);
        //display win or lose message
        if(isWin) 
            resultMsg.setText("YOU WIN! You got the answer! :)");
        else 
            resultMsg.setText("You lose, try harder next time! :)");
        
        //use method to get colors by numbers in answerCodes
        //then loop in colors array to display answer on GUI
        Color[] answerColors = getColorsByNums(answerCodes);
        for(int i=0; i<4; i++)
            answers[i].setFill(answerColors[i]);
    }
    
    /**
     * Exits the program.
     */
    public void quitGame() {
        try {
            MMGame.quitGame();
            System.exit(0);
        }
        catch(IOException ioe) {
            System.exit(1);
        }
    }
    
    /**
     * Resets and start a new game and signal server to restart as well.
     * @param event 
     */
    @FXML
    private void newGame(ActionEvent event) {
        launchNewGame();
    }
    
    /**
     * Opens a new window that will display the credits of the game.
     * @param event 
     */
    @FXML
    private void displayCredits(ActionEvent event) {
        
        Stage aboutStage = new Stage();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        
        Label gameLbl = new Label("MasterMind");
        gameLbl.setFont(Font.font(null, FontWeight.BOLD, 14));
        Label authorLbl = new Label("Created by:");
        Label a1 = new Label("Thanh Tung Nguyen");
        Label n1 = new Label("1436664");
        Label a2 = new Label("Renuchan Thambirajah");
        Label n2 = new Label("1437641");
        Label a3 = new Label("Georgi Veselinov Kichev");
        Label n3 = new Label("1333539");
        Label verLbl = new Label("Version 1.0");
        Label pubDate = new Label("2016-10-25");
        
        grid.setPadding(new Insets(25, 10, 25, 10));
        grid.add(gameLbl, 0, 0, 3, 1);
        grid.add(authorLbl, 0, 1);
        grid.add(a1, 1, 1);
        grid.add(n1, 2, 1);
        grid.add(a2, 1, 2);
        grid.add(n2, 2, 2);
        grid.add(a3, 1, 3);
        grid.add(n3, 2, 3);
        grid.add(verLbl, 0, 4, 3, 1);
        grid.add(pubDate, 0, 5, 3, 1);
        
        aboutStage.setTitle("Credits");
        aboutStage.setResizable(false);
        aboutStage.setScene(new Scene(grid));
        aboutStage.show();
    }
    
    /**
     * Opens a new window that will display the instructions to play.
     * @param event 
     */
    @FXML
    private void displayHelp(ActionEvent event) {
        
        Stage helpStage = new Stage();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        
        Label instrLbl = new Label("Instructions");
        Text i1 = new Text("In this game, you must guess the 4 right colors to win!");
        Text i2 = new Text("1. Select a color to use.");
        Text i3 = new Text("2. Place up to 4 colors on the corresponding row.");
        Text i4 = new Text("3. Click the button on the bottom to guess the answer.");
        Text i5 = new Text("4. Use the clues to figure out the answer!");
        Text legend1 = new Text("White clue: there's a right color, but wrong position.");
        Text legend2 = new Text("Black clue: there's a right color in the right position.");

        grid.setPadding(new Insets(25, 10, 25, 10));
        grid.add(instrLbl, 0, 0, 2, 1);
        grid.add(i1, 0, 1);
        grid.add(i2, 0, 2);
        grid.add(i3, 0, 3);
        grid.add(i4, 0, 4);
        grid.add(i5, 0, 5);
        grid.add(legend1, 0, 7);
        grid.add(legend2, 0, 8);
        
        helpStage.setTitle("Instructions");
        helpStage.setResizable(false);
        helpStage.setScene(new Scene(grid));
        helpStage.show();
    }
    
    /**
     * Calls quitGame method to exit the program.
     * @param event 
     */
    @FXML
    private void exitGame(ActionEvent event) {
        quitGame();
    }
    
    /**
     * Opens another window to allow the user to set their own answer. 
     * 
     * @param evt 
     */
    @FXML
    private void setAnswer(ActionEvent evt) {
        
        setAnswerStage = new Stage();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);

        Label lbl = new Label("Set your own answer");
        HBox hb = new HBox();
        newAnswerTf1 = new TextField("");
        newAnswerTf1.setPrefWidth(40);
        newAnswerTf2 = new TextField("");
        newAnswerTf2.setPrefWidth(40);
        newAnswerTf3 = new TextField("");
        newAnswerTf3.setPrefWidth(40);
        newAnswerTf4 = new TextField("");
        newAnswerTf4.setPrefWidth(40);
        hb.getChildren().addAll(newAnswerTf1, newAnswerTf2, 
                                newAnswerTf3, newAnswerTf4);
        hb.setSpacing(10);

        Button btn = new Button("Set");
        btn.setOnAction(event -> validateNewAnswer());
        answerStageErrorMsg = new Text("");

        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.add(lbl, 0, 0);
        grid.add(hb, 0, 1, 3, 1);
        grid.add(btn, 4, 1);
        grid.add(answerStageErrorMsg, 0, 2);

        setAnswerStage.setTitle("New Answer");
        setAnswerStage.setResizable(false);
        setAnswerStage.setScene(new Scene(grid));
        setAnswerStage.show();
    }
    
    /**
     * Method gets called when the set new answer button is pressed in 
     * the New Answer window. Validates the inputs in the text field. If 
     * all valid, signal the server that user wants to set new answer.
     */
    private void validateNewAnswer() {
        
        boolean isValid = true;
        byte[] answerPacket = new byte[4];
        String[] newAnswers = new String[4];
        //get string inputs from text fields into string array
        newAnswers[0] = newAnswerTf1.getText();
        newAnswers[1] = newAnswerTf2.getText();
        newAnswers[2] = newAnswerTf3.getText();
        newAnswers[3] = newAnswerTf4.getText();
        
        try{
            //loop through the string array of inputs
            //validate if only digits inside ranges 1 to 8
            for(int i=0; i < 4; i++) {
                int num = Integer.parseInt(newAnswers[i]);
                if((num > 0) && (num < 9))
                    answerPacket[i] = (byte)num;
                else
                    isValid = false;
            }
            //if all digits are valid
            if(isValid) {
                //tell server user wants to put new answer and give new answer
                MMGame.signalAnswerChange();
                MMGame.sendAnswerSet(answerPacket);
                setAnswerStage.hide();
            }
            else //if new answer not valid
                answerStageErrorMsg.setText("New answer invalid: single digit.");
        }
        catch(NumberFormatException e) {
            answerStageErrorMsg.setText("New answer invalid: digits only.");
        }
        catch(IOException e) {
            System.out.println("Unable to set new answer");
            System.exit(1);
        }
    }
     
    
    
    
    
    /**
     * Returns BoxBlur to highlight color choice.
     * @return BoxBlur
     */
    private BoxBlur highlightChoice() {
        return new BoxBlur(16.0, 16.0, 1);
    }
    
    /**
     * Returns BoxBlur to dim color choice.
     * @return BoxBlur
     */
    private BoxBlur dimChoice() {
        return new BoxBlur(0.0, 0.0, 1);
    }
    
    /**
     * Returns numbers 1 to 8 based on given color. Returns 0 
     * if color doesn't match any on the game.
     * 
     * @param color
     * @return numbers 1 to 8
     */
    private byte getNumByColor(Paint color) {
        
        byte num = 0;
        
        if(color.equals(Color.RED))
            num = 1;
        else if(color.equals(Color.ORANGE))
            num = 2;
        else if(color.equals(Color.YELLOW))
            num = 3;
        else if(color.equals(Color.GREEN))
            num = 4;
        else if(color.equals(Color.TEAL))
            num = 5;
        else if(color.equals(Color.BLUE))
            num = 6;
        else if(color.equals(Color.PURPLE))
            num = 7;
        else if(color.equals(Color.BROWN))
            num = 8;
        
        return num;
    }
    
    /**
     * Loops through numbers in byte array of 4 and sets a Color corresponding 
     * to specific numbers 1 to 8 in a new Color array of 4 and return it.
     * @param answers
     * @return Color array
     */
    private Color[] getColorsByNums(byte[] answers) {
        
        Color[] c = new Color[4];
        
        for(int i=0; i<4; i++) {
            switch(answers[i]) {
                
                case 1: 
                    c[i] = Color.RED;
                    break;
                case 2: 
                    c[i] = Color.ORANGE;
                    break;
                case 3: 
                    c[i] = Color.YELLOW;
                    break;
                case 4: 
                    c[i] = Color.GREEN;
                    break;
                case 5: 
                    c[i] = Color.TEAL;
                    break;
                case 6: 
                    c[i] = Color.BLUE;
                    break;
                case 7: 
                    c[i] = Color.PURPLE;
                    break;
                case 8: 
                    c[i] = Color.BROWN;
                    break;
                default: 
                    c[i] = Color.web("#878282");
                    break;
            }
        }
        
        return c;
    }
    
//    enum Colors {
//        
//        RED, ORANGE, YELLOW, GREEN, TEAL, BLUE, PURPLE, BROWN
//    }
}
