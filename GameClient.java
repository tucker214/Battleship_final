import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class GameClient
{

    public static void main(String[] args)
    {
        try
        {
            Socket playerSocket = new Socket("localhost", 5000);
            BufferedReader input = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            PrintWriter output = new PrintWriter(playerSocket.getOutputStream(), true);
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

            String serverStatus = input.readLine();

            if (serverStatus.contains("full"))
            {
                System.err.println(serverStatus);
                System.exit(0);
            }
            else
                System.out.println(serverStatus);

            String boardSetup = "";
            while (!boardSetup.contains("set"))
            {
                boardSetup = input.readLine();


                if (boardSetup.contains("@"))
                {
                    boardSetup = boardSetup.replaceAll("[^A-Za-z\\d \\t-]", "");

                }
                if (boardSetup.contains("newline"))
                {
                    boardSetup = boardSetup.replace("newline", "");

                }
                if (boardSetup.length() > 0)
                    System.out.println(boardSetup);
                if (boardSetup.contains("Enter"))
                {
                    output.println(keyboard.readLine());
                }
            }

            System.out.println();
            System.out.println("Waiting for other player to set up board.");
            System.out.println();

            String setup = "";
            while(!setup.contains("Start"))
            {
                setup = input.readLine();
                System.out.println(setup);
            }

            System.out.println();

            String yourTurn = "";
            String gameOverMessage = "";
            String message = "";

            while(!gameOverMessage.contains("Over"))
            {
                while (!yourTurn.contains("Your turn") && !gameOverMessage.contains("Over"))
                {
                    yourTurn = input.readLine();

                    if (yourTurn.contains("Over"))
                    {
                        gameOverMessage = yourTurn;
                    }

                    System.out.println(yourTurn);
                }

                yourTurn = "";


                while (!message.contains("finished") && !gameOverMessage.contains("Over"))
                {
                    message = input.readLine();
                    if (message.contains("Over"))
                    {
                        gameOverMessage = message;
                    }
                    System.out.println(message);
                    if (message.contains("Enter"))
                        output.println(keyboard.readLine());
                }
                if (!gameOverMessage.contains("Over")) {
                    printBoard(output, input, keyboard);
                    printBoard(output, input, keyboard);
                }
            }

            String winMessage = input.readLine();

            System.out.println(winMessage);
            input.close();
            playerSocket.close();


        }catch (SocketException e){
            System.out.println("Connection has been closed.");
        } catch (IOException e){
            System.out.println("Error Connecting...");
            e.printStackTrace();
        }
    }

    public static void printBoard(PrintWriter output, BufferedReader input, BufferedReader keyboard) throws IOException
    {
        String boardSetup = "";
        while (!boardSetup.contains("set"))
        {
            boardSetup = input.readLine();


            if (boardSetup.contains("@"))
            {
                boardSetup = boardSetup.replaceAll("[^A-Za-z\\d \\t-]", "");

            }
            if (boardSetup.contains("newline"))
            {
                boardSetup = boardSetup.replace("newline", "");

            }
            if (boardSetup.length() > 0 && (!boardSetup.contains("set")))
            {
                    System.out.println(boardSetup);
            }

            if (boardSetup.contains("Enter"))
            {
                output.println(keyboard.readLine());
            }

            if (boardSetup.contains("set"))
            {
                System.out.println(boardSetup.replaceAll("set", ""));
            }
        }
    }
}