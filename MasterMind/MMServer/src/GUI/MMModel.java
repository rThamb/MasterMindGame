package GUI;

import clientBusiness.MasterMindGame;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * MasterMind Model class that starts the entire Application
 * 
 * @author Renuchan
 * @version 1.2
 */
public class MMModel extends Application {
    
    private MasterMindGame MMGame;
    private TextField ipTf;
    private TextField portTf;
    private Label errorMsg;
    private Stage gameStage;
    private Stage connectStage;
    
    /**
     * First method that will start when running the Mastermind App. 
     * It will start the connect window that will be required to connect 
     * to the server in order to play the game.
     * 
     * Connect button has an event handler that call onConnect method to 
     * check if the given IP Address and port is valid.
     * 
     * @param primaryStage
     * @throws Exception 
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        //create 2 window stages
        gameStage = primaryStage;
        connectStage = new Stage();
        
        //make containers and controls
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        
        Label txt = new Label("Connect to play Mastermind");
        Label ipLbl = new Label("IP Address");
        ipTf = new TextField("");
        Label portLbl = new Label("Port:");
        portTf = new TextField("");
        
        Button connectBtn = new Button("Connect");
        connectBtn.setOnAction(event -> onConnect());
        
        errorMsg = new Label("");
        errorMsg.setTextFill(Color.RED);
        
        //place controls onto grid container at specific row & column
        grid.setPadding(new Insets(25, 25, 10, 25));
        grid.add(txt, 0, 0);
        grid.add(ipLbl, 0, 1);
        grid.add(ipTf, 1, 1);
        grid.add(portLbl, 0, 2);
        grid.add(portTf, 1, 2);
        grid.add(connectBtn, 1, 4);
        grid.add(errorMsg, 1, 5);
        
        //set settings on stage, add grid scene, and show
        connectStage.setTitle("Mastermind Connectivity");
        connectStage.setResizable(false);
        connectStage.setScene(new Scene(grid));
        connectStage.show();
    }
    
    /**
     * Method will trigger on connect button. It will get the text 
     * from text fields and check if given inputs are valid. 
     * 
     * Hide the connect window and use initGame method with ip and 
     * port parameters to start the actual game window.
     */
    private void onConnect() {
        
        MMGame = new MasterMindGame();
        try{
            //get IP text and parse port input
            String ipAddress = ipTf.getText();
            int portNum = -1;
            
            try {  portNum = Integer.parseInt(portTf.getText()); }
            catch(NumberFormatException e) {
                errorMsg.setText("Port can only contain digits.");
            }
            //connect to ip with port
            MMGame.connect(ipAddress, portNum);
            
            //if no exception thrown, it means connection was successful
            //hide connect window and start game window
            connectStage.hide();
            System.out.println("Connected to IP Address: " + ipAddress);
            initGame();
        }
        catch(IOException e) {
            errorMsg.setText("Unable to connect to server.");
        }
    }
    
    /**
     * Get and load the fxml file with absolute path to instantiate 
     * a Scene object. Get the controller class from that FXMLLoader, 
     * set its valid IP and port, and use its connectGame method 
     * launch the game. Show new game window.
     * 
     * @param ip
     * @param port 
     */
    private void initGame() {
        
        try {
            //open game window with fxml
            URL path = Paths.get("./src/resources/fxml/GameWindow.fxml").toUri().toURL();
            
            //set fxml path in loader
            //give builder factory for suitable instance constructing
            FXMLLoader gameLoader = new FXMLLoader();
            gameLoader.setLocation(path);
            gameLoader.setBuilderFactory(new JavaFXBuilderFactory());
            //load into scene
            Scene gameScene = new Scene(gameLoader.load());
            //get controller from fxmlloader to use Controller class
            Controller controller = gameLoader.getController();
            controller.setMMGame(MMGame);
            controller.launchNewGame();
            
            //set settings on stage, add scene, and show
            gameStage.setTitle("Mastermind");
            gameStage.setScene(gameScene);
            gameStage.setResizable(false);
            gameStage.show();
        }
        catch(IOException e) {
            System.out.println("Error: Game scene load failure.\n"+e.getMessage());
            System.exit(1);
        }
        
    }

    /**
     * used to start app
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
