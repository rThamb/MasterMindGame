
package clientBusiness;



import java.net.*;
import java.io.*;
import datacom.MasterMindDC;
/**
 *
 * This class defines the functionality required to preform data communication 
 * with a server to create a mastermind game 
 * 
 * @author Renuchan
 */
public class MasterMindGame {
 
    private MasterMindDC network; 
    private int attempts;
    
    public MasterMindGame(){}
    
    
    /**
     * This method will establish a connection with between the client and the 
     * server 
     * 
     * @param ipAddress                 IP Address of the server
     * @param port                      Port number of the server 
     * 
     * @throws UnknownHostException     If server cannot be found 
     * @throws IOException              Issue with socket
     */
    public void connect (String ipAddress, int port)
            throws UnknownHostException, IOException
    {
        Socket serverSock = new Socket(ipAddress, port);
        network = new MasterMindDC(serverSock);
    }
    
    /**
     * This method is called when you wish to send 1 attempt to 
     * the server.
     * 
     * @param guess                 guess for the current turn
     * @return                      clues associated with the guess 
     * @throws IOException 
     */
    public void sendGuess(byte[] guess)throws IOException
    {
        //make sure users cannot send 0000 array
        attempts++; 
        network.writeMessage(guess);
    }
  
    /**
     * This method is called to retrieve the clues for a guess sent 
     * 
     * @return              clues generated from guess
     * @throws IOException  if read failed 
     */
    public byte[] readReponse()throws IOException
    {
        return network.readMessage();
    }
    
    
    /**
     * This method is called when the user wishes to play another game. 
     * Will send a message to the server to allow it to reset the game
     * session 
     * 
     * @throws IOException          If packet transfer has failed
     */
    public void playAgain()throws IOException
    {
        byte[] packet = new byte[4]; 
        
        //Packet 0000 - play again 
        attempts = 0;
        network.writeMessage(packet);
                
    }
    
    /**
     * This method is called when the user wishes to end the game. Will send 
     * a message to the server to allow it to close the current session 
     * 
     * @throws IOException          If packet transfer has failed
     */
    public void quitGame()throws IOException
    {
        //Packet 000-1 - end game/session
        byte[] packet = new byte[4];
        packet[packet.length - 1] = -1;
        network.writeMessage(packet);
        
    }

    /**
     * Get the number of attempts made during the current game
     * 
     * @return          numbers of attempts 
     */
    public int getAttempts(){
        return attempts; 
    }
    
    /**
     * This method is called to signal to the server that you wish 
     * to enter an answer set 
     * 
     * @throws IOException          If packet transfer has failed
     */
    public void signalAnswerChange()throws IOException
    {
    	//tell the server you wish to set answers
    	byte[] packet = new byte[4];
    	packet[packet.length - 1] = -2;
    	network.writeMessage(packet);	
    }
    
    /**
     * This method will send your answer send to the server
     * 
     * @param answer             new answer set you wish to enter     
     * @throws IOException       If packet transfer has failed
     */
    public void sendAnswerSet(byte[] answer)throws IOException
    {
    	//validate 
    	network.writeMessage(answer);
    	
    }
    
}
