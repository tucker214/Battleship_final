import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class GameClientHandler implements Runnable{

    private final Socket clientSocket;
    private volatile BufferedReader input;
    private PrintWriter output;
    private boolean isReady = false;
    private int order;
    private volatile boolean isGameOver = false;
    private int clientID;
    private int unitsShot = 0;
    private volatile int alternateTurns = 1;
    private volatile String [][] playerBoard = new String[11][11];
    private  volatile Gridmaker enemyBoard = new Gridmaker();
    private HashMap<Integer, GameClientHandler> clients;

    public GameClientHandler(Socket clientSocket, int order, int clientID) throws IOException
    {
        this.clientSocket = clientSocket;
        this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        this.output = new PrintWriter(this.clientSocket.getOutputStream(), true);
        this.order = order;
        this.clientID = clientID;
    }
    public String[][] getPlayerBoard() {
        return playerBoard;
    }
    public void setPlayerBoard(String[][] playerBoard) {
        this.playerBoard = playerBoard;
    }
    public void setClients(HashMap<Integer, GameClientHandler> clients) {
        this.clients = clients;
    }
    public int getOrder() {
        return order;
    }
    public boolean isGameOver() {
        return isGameOver;
    }
    public int getClientID() {
        return clientID;
    }
    public int getAlternateTurns() {
        return alternateTurns;
    }
    public void setAlternateTurns(int alternateTurns) {
        this.alternateTurns = alternateTurns;
    }

    @Override
    public void run() {

                                                    //GameClientHandler was created because the GameServer should mostly be used for listening and not for doing too many calculations / sending messages.
                                                    //as well as the need for threads.
        Shiplist ships = new Shiplist();            //A lot is copy and pasted code from the previous GameClient file
                                                    //I thought it would be easier to store the board making process in this thread class. I'm not sure if that's the case, but I've already gone too far.
        ships.addShip(2, "D");
        ships.addShip(3, "S");
        ships.addShip(3, "Cr");
        ships.addShip(4, "B");
        ships.addShip(5, "Ca");

        Gridmaker yourBoard = new Gridmaker();

        //yourBoard.toString();

        output.println("\nSet your board.");

        Ship currentShip = ships.getHead();
        int xCoord = 1;
        int yCoord = 1;
        int isVertical = 1;
        boolean isValid;

        for (int i = 1; i <= 5; i++)
        {

            if (i > 1)
                currentShip = currentShip.getShip();

            do
            {
                try {
                    output.println();
                    output.println("Place the following ship: " + currentShip.getName() + " with length of " + currentShip.getSize() + " spaces.\r\n");
                    output.println("Enter X coordinate: ");

                    try {
                        xCoord = Integer.parseInt(input.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    output.println();

                    output.println("Enter Y coordinate: ");
                    try {
                        yCoord = Integer.parseInt(input.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    output.println();

                    output.println("Enter 0 to place Horizontally - 1 to place Vertically: ");
                    try {
                        isVertical = Integer.parseInt(input.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (xCoord < 1 || xCoord > 10 || yCoord < 1 || yCoord > 10)
                        output.println("Out of Bounds!");

                    isValid = yourBoard.cellCheck(yCoord, xCoord, isVertical, currentShip);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    isValid = false;
                }

            } while (!isValid);

        yourBoard.setBoard(yCoord, xCoord, isVertical, currentShip);

        for (int x = 1; x <= 10; x++)
        {
            for (int y = 1; y <= 10; y++)
            {
                output.print("@" + yourBoard.getBoard()[x][y] + "\t");
            }
            output.println("newline");
        }
        output.println();

        }

        setPlayerBoard(yourBoard.getBoard());

        output.println("Board successfully set!");                                           //this is where we left off, except the above portion where we will be receiving the input from the client through the socket streams.
                                                                                             //below will be where loops and strings will be used to communicate with their counterparts in the GameClient class.
        isReady = true;
        boolean doesOpponentExist = false;

        while (!doesOpponentExist)
        {
            doesOpponentExist = clients.containsKey((this.order % 2) + 1);
        }


        boolean isOpponentReady = false;

        while (!isOpponentReady)
        {
            isOpponentReady = clients.get((this.order % 2) + 1).isReady;
        }

        output.println("Game Start");

        int winner = attackPhase(yourBoard, enemyBoard);

        output.println("Game Over");
        output.println("Client #" + winner + " has won the game");

        this.isGameOver = true;
        //this.input.close();
    }

    public int attackPhase(Gridmaker yourBoard, Gridmaker enemyBoard)
    {
        while (this.unitsShot < 17 && clients.get((this.order % 2) + 1).unitsShot < 17)
        {
            int attackX = -1;
            int attackY = -1;
            boolean falseInput = true;
            if (this.getAlternateTurns() == this.getOrder())
            {
                output.println("Your turn.");

                while ((attackX < 1 || attackX > 10) && (attackY < 1 || attackY > 10)) {

                    while (falseInput) {
                        falseInput = false;
                        output.println(("Enter an X coordinate to attack: "));

                        try {
                            attackX = Integer.parseInt(input.readLine());
                            if (!Character.isDigit((attackX + "").charAt(0)) || (attackX + "").length() > 2 || attackX > 10 || attackX < 1)
                            {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException e){
                            e.printStackTrace();
                            falseInput = true;
                        }catch (IOException e) {
                            e.printStackTrace();
                            falseInput = true;
                        }
                    }

                    falseInput = true;

                    while (falseInput) {
                        falseInput = false;
                        output.println(("Enter an Y coordinate to attack: "));

                        try {
                            attackY = Integer.parseInt(input.readLine());
                            if (!Character.isDigit((attackY + "").charAt(0)) || (attackY + "").length() > 2 || attackY > 10 || attackY < 1)
                            {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException e){
                            e.printStackTrace();
                            falseInput = true;
                        }catch (IOException e) {
                            e.printStackTrace();
                            falseInput = true;
                        }
                    }
                }
                output.println("Attacking phase finished. Printing boards: ");

                if (equals(clients.get((this.getOrder() % 2) + 1).getPlayerBoard(), attackY, attackX))
                {
                    enemyBoard.setUnit(attackX, attackY, "H");
                    clients.get((this.getOrder() % 2) + 1).getPlayerBoard()[attackY][attackX] = "H";
                    unitsShot++;
                }

                else
                {
                    this.enemyBoard.setUnit(attackX, attackY, "M");
                    clients.get((this.getOrder() % 2) + 1).getPlayerBoard()[attackY][attackX] = "M";
                }

                sendBoard(enemyBoard);
                sendBoard(yourBoard);
                this.setAlternateTurns((this.getAlternateTurns() % 2) + 1);
                clients.get((this.getOrder() % 2) + 1).setAlternateTurns((clients.get((this.getOrder() % 2) + 1).getAlternateTurns() % 2) + 1);

                System.out.println("Client #" + clients.get(this.getOrder()).getClientID() + " has shot down " + unitsShot + " unit(s)");
            }
        }

        if (clients.get(this.getOrder()).unitsShot >= 17)
        {
            return clients.get(this.getOrder()).getClientID();
        }

        else return clients.get((this.getOrder() % 2) + 1).getClientID();
    }

    public void sendBoard(Gridmaker yourBoard)
    {
        for (int x = 1; x <= 10; x++)
        {
            for (int y = 1; y <= 10; y++)
            {
                output.print("@" + yourBoard.getBoard()[x][y] + "\t");
            }
            output.println("newline");

        }
        output.println("newline");
        output.println("set");
    }

    public boolean equals(String[][] arr, int x, int y)
    {
        if (arr[x][y].contains("-"))
            return false;
        if (arr[x][y].contains("H"))
            return false;
        if (arr[x][y].contains("M"))
            return false;

        return true;
    }
}
