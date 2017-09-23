package mmserver;

import datacom.MasterMindDC;
import java.net.*;
import java.io.*; 
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  This class defines the functionality of a master mind game session. 
 *  It will provide the required actions needed to run a master mind game 
 *  between 1 client.   
 * 
 * @author Renuchan
 */
public class MasterMindSession implements Runnable {
    
    private byte[] answer; 
    private MasterMindDC datacom; 
    private byte[] clues; 
    private Socket sock;
    
    public MasterMindSession(Socket sock)
    {
        datacom = new MasterMindDC(sock);     
        this.sock = sock; 
    }
    
    @Override
    public void run()
    {
        try {
            startGame();
        } catch (IOException ex) {
            //catch the closed connection and listen for a new request
            //will occur if user suddenly closes their socket               
            System.out.println("EXCEPTION THROWN " + ex.getMessage());
        }
        System.out.println("Finished Serving Client");
    }
    
    
    
    /**
     * This method starts a game between the client and handles clients guesses
     * and requests. 
     * 
     * @throws IOException        Closed socket or failed read/write operation  
     */
    public void startGame()throws IOException
    {
        boolean play = true;
        boolean win = false;  

        //loop for n amounts of games
        while(play)  
        {            
            generateAnswer();           
            
            int attempts = 0;
            while(!win && attempts < 10)// 1 game, 10 attempts
            {
               byte[] guess = datacom.readMessage();

               //check if player wishes to quit midway a game
               switch(guess[guess.length - 1])
               {
                   case 0:
                       System.out.println("Client " + sock.getInetAddress().getHostAddress() + 
                               " wishes to restart game");
                       generateAnswer();
                       attempts = 0; 
                       break;
                   
                   case -1: //user wishes to quit
                        play = false;
                        attempts = 10; //jump out of loop
                        break;
                   
                   case -2: //user wishes to change answer set 
                        System.out.println("Client " + sock.getInetAddress().getHostAddress() + " wishes to set answer");
                        setAnswer();
                        break;
                   
                   default:
                       win = generateClue(guess);     
                       //last guess was not a match, send back answer
                       if(!win && attempts == 9)
                       {
                           //read the lose message And then send answer 
                           
                           datacom.writeMessage(answer);
                       }
                       else
                           datacom.writeMessage(clues);                     
                       attempts++;
                       break;                                     
               }
               System.out.println("Attempts: " + attempts);
            }//close inner loop
            
            //check if the inner loop reached an end by 10 attempts or a quit 
            if(play)
            {
                System.out.println("Attempts finished with: " + attempts);
                
                //user did not quit the game midway, so 10 attempts or win 
                //was achieved               
                byte[] response = datacom.readMessage();
            
                // Packets 
                // 0 0 0 0 - play again
                // 0 0 0-1 - quit the game          
                if(response[response.length - 1] == 0)
                {
                    attempts = 0; 
                    win = false;
                    System.out.println("Play another game");
                }
                else
                    play = false;               
            }           
        }//close outer loop
        
        System.out.println("Finished Session");
    }
    
    /*
     * This method will generate the answer client will attempt to guess for 
     * 1 game. 
     */
    private void generateAnswer()
    {
        byte[] answer = new byte[4];
        
        for(int i = 0; i < answer.length; i++)
            answer[i] = (byte)((Math.random() * 8) + 1);
        
        System.out.println("Answer for current game: " 
                + java.util.Arrays.toString(answer));
        
        this.answer = answer; 
    }
    
    /*
     * This method will send the user the clues associated for the 
     * guess provided. Will also tell the caller if the guess was a win 
     * or not.
    
       @return          if the owner has found the answer 
     */
    private boolean generateClue(byte[] guess)throws IOException
    {
        byte[] cluePacket = new byte[4];
        
        System.out.println("Client " + sock.getInetAddress().getHostAddress() + 
                ": You guessed " + Arrays.toString(guess));
        
        //create copy
        byte[] answerCopy = copyAnswer();
       
        int arrayIndex = 0; 
        int matchPos =0; 
        
        //begin by determining correct number and position is guessed
        for(int i = 0; i < guess.length; i++)
        {
            if (answerCopy[i] == guess[i]){
                cluePacket[arrayIndex] = 2;
                arrayIndex++;
                answerCopy[i] = -9; //to avoid matching on that number again
                guess[i] = -10;     // clue generated for that number
                matchPos++; 
            }
        }
        
        // determine correct numbers guessed if user doesn't win
        if(arrayIndex != 4)  
        {
            for(int i=0; i < answerCopy.length; i++)
            {     
                for(int j = 0; j < guess.length; j++)
                //matched number found    
                if(answerCopy[i] == guess[j])
                {
                    answerCopy[i] = -9; //to avoid matching on that number again 
                    guess[j] = -10; // clue generated for that number
                    if(arrayIndex < cluePacket.length){
                        cluePacket[arrayIndex] = 1;
                        arrayIndex++;          
                    }
                }
            }   
        } 
        System.out.println("Clues: " + Arrays.toString(cluePacket) +
                           "Answer: " + Arrays.toString(answer));
        
        //set the clue field 
        this.clues = cluePacket;
        
        //tell caller if last guess was a win
        return matchPos == 4; 
    }
    
    /*
     *  This method will create a copy of the answers.
     */
    private byte[] copyAnswer()
    {
        byte[] copy = new byte[answer.length];
        for(int i=0; i<answer.length; i++)
            copy[i] = answer[i];
        return copy; 
    }
    
    private void setAnswer()throws IOException
    {            
        this.answer = datacom.readMessage();
        
        System.out.println("Answer for current game: " 
                + java.util.Arrays.toString(answer));
    }
    

}
