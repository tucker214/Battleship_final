import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer{

    public static HashMap<Integer, GameClientHandler> hashClients = new HashMap<>();    //Create HashMaps that will store the parameterized types, so they can be accessed later. Integer will be for the order variable

    public static void main(String[] args) throws IOException
    {
        int PORT = 5000;
        int numLoops = 0;                                                               //numLoops will count the number of times the Server has assigned a client. Once it hits 2, it will stop accepting clients.
        int order = (int)(((Math.random()*100) % 2) + 1);                                     // order is randomized and will be used to randomize who goes first. As of now it keeps assigning the clients to the same order. It isn't working as intended.
        ServerSocket playerServerSocket = new ServerSocket(PORT);                       //create server socket and bind it to the port
        ExecutorService pool = Executors.newFixedThreadPool(2);                 //will create a pool of two threads. Only 2 threads can be used for this program, one for each of the clients.
        ServerCommunication gameOver = new ServerCommunication(playerServerSocket, hashClients);


        while(true)                                                                     //Loop will continuously listen for new clients
        {
            try
            {

                numLoops++;
                if (numLoops <= 2)
                    System.out.print("Awaiting connection to a client -> ");

                Socket playerSocket = playerServerSocket.accept();

                PrintWriter output = new PrintWriter(playerSocket.getOutputStream(), true);     //create printwriter to send message to client.


                try
                {
                    if (numLoops > 2)
                        throw new RuntimeException();                                                    //throw exception for when more than 2 clients show up to disconnect them.
                    else
                        sendMessage(playerSocket, "Connected to server as client #" + numLoops + ".", output);

                    System.out.println("client #" + numLoops + " connected.");


                    GameClientHandler clientThread = new GameClientHandler(playerSocket, order, numLoops);
                    System.out.println("client #" + numLoops + " will play on turn " + order);
                    System.out.println();

                    hashClients.put(order, clientThread);                                               //hashClients is probably all that the program will need out of these three HashMaps
                    hashClients.get(order).setClients(hashClients);
                    if (numLoops == 2)
                    {
                        gameOver.start();
                    }
                    order = (order % 2) + 1;

                    pool.execute(clientThread);                                                         //run the client thread in GameClientHandler run() method
                } catch (RuntimeException e) {
                    output.println("Server is full. Disconnecting...");
                    System.err.println("Server capacity is full. Client disconnected.");
                }

            } catch (SocketException e)
            {
                System.out.println("The connection has been closed");
                pool.shutdown();
                System.exit(0);
            } catch (IOException e) {
                System.out.println("Connection error has occurred.");
            }
        }
    }

    public static void sendMessage(Socket client, String message, PrintWriter output)
    {
            output.println(message);
    }

    public void serverListener (ServerSocket ss)
    {

    }
}
