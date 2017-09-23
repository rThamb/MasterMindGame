
package datacom;

/**
 *  This class will handle all read operations and write operations between 
 *  2 computers
 * 
 * @author Renuchan
 */
import java.net.*; 
import java.io.*;

public class MasterMindDC {
    
    private Socket sock; 
    
    public MasterMindDC(Socket sock)
    {
        this.sock = sock; 
    }
    
    /**
     * This method will read packets sent from the sender to the socket.
     * 
     * @return                  message sent from the sender 
     * @throws IOException      If read operation was unsuccessful
     */
    public byte[] readMessage()throws IOException
    {     
        InputStream in = sock.getInputStream();

      // Size of the buffer that mirrors the size expected
      byte[] messageBuffer = new byte[4];
      
      int totalBytesRcvd = 0;						
      int bytesRcvd;
      
      while (totalBytesRcvd < messageBuffer.length)
      {
        if ((bytesRcvd = in.read(messageBuffer, totalBytesRcvd,
                        messageBuffer.length - totalBytesRcvd)) == -1)
            throw new SocketException("Connection close prematurely");
            
        totalBytesRcvd += bytesRcvd;
      }
           
       return messageBuffer;              
    }
    
    /** 
     * This method will write packets to a receiver.
     * 
     * @param packet            message to write
     * @throws IOException      If write operation failed
     */
    public void writeMessage(byte[] packet)throws IOException
    {
        OutputStream out = sock.getOutputStream();       
        out.write(packet);
    }
    
}
