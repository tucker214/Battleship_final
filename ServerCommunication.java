import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerCommunication extends Thread{

    private ServerSocket s;
    private HashMap<Integer, GameClientHandler> clients;

    public ServerCommunication(ServerSocket server, HashMap<Integer, GameClientHandler> clients) throws IOException {
        this.s = server;
        this.clients = clients;
    }

    @Override
    public void run() {
        while (true)
        {
            if (this.clients.get(1).isGameOver() && this.clients.get(2).isGameOver())
            {
                try {
                    this.s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
