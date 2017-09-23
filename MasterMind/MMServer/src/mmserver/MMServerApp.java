
package mmserver;

import java.io.*; 
import java.util.*; 

/**
 *
 *  This class starts the server and allows it to start listening to client 
 *  requests at a specific port
 * 
 * @author Renuchan
 */
public class MMServerApp {

    public static void main(String[] args) {
        
        int port = 50000;
        try{
            
            MasterMindServer server = new MasterMindServer(port);
            server.run();
        }
        catch(IOException e)
        {
            System.out.println("Server cannot run " + e.getMessage());
        }

    }   
}
