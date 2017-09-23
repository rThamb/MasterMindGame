package mmserver;

import java.net.*; 
import java.io.*;

/**
 *  This class provides implementation of the MasterMind server 
 * 
 * @author Renuchan
 */
public class MasterMindServer {
    
    private ServerSocket serverSock;
       
    public MasterMindServer(int port)throws IOException
    {
        serverSock = new ServerSocket(port);
    }
    
    /** 
     * This method runs the server, and allows it to listen for client request
     * and handle multiple clients at a time. 
     * 
     * @throws IOException      Server incapable of listening to requests
     */
    public void run()throws IOException 
    {
        while(true)
        {
            Socket clSock =  serverSock.accept();                
            
            MasterMindSession player = new MasterMindSession(clSock);
            System.out.println("Serving Client at IP#: " + clSock.getInetAddress().getHostAddress());
            //open a new thread and handle users request
            Thread game = new Thread(player);          
            game.start();           
            System.out.println("Handling client " + clSock.getInetAddress().getHostAddress() 
                    + " in seperate thread, waiting for a new client");
        }
    }
    
    
}
