public class Gridmaker
{

    private String[][] board = new String[11][11];
    
    public Gridmaker ()
    {
        for (int i = 1; i <= 10; i++)
        {
            for (int j = 1; j <= 10; j++)
            {
                this.board[i][j] = "-";
            }
        }
    }
    
    public String[][] getBoard()
    {
        return this.board;
    }
    
    public boolean setBoard(int row, int col, int isVertical, Ship ship)
    {
    
            if (isVertical == 1)
               {
                   for (int i = row; i < row + ship.getSize(); i++)
                    {
                        for (int j = col; j <= col; j++)
                        {
                            this.board[i][j] = ship.getName();
                        }
                    }
                }
       
                else
                {
                    for (int i = row; i <= row; i++)
                    {
                        for (int j = col; j < col + ship.getSize(); j++)
                        {
                            this.board[i][j] = ship.getName();
                        }
                    }
                }
        
        return false;
    }
    public void setUnit(int x, int y, String str)
    {
        this.board[y][x] = str;
    }
    public boolean cellCheck(int row, int col, int isVertical, Ship ship)
    {
    try
    {
        if (isVertical == 1)
               {
                   for (int i = row; i < row + ship.getSize(); i++)
                    {
                        for (int j = col; j <= col; j++)
                        {
                        
                            if(col == 0 || row == 0)
                                throw new IndexOutOfBoundsException("Out of Bounds!\n");
                                
                            if(!this.board[i][j].equals("-"))
                            {
                                System.out.println("\nThis placement intersects a previous ship. Choose again.");
                                return false;
                            }
                        }
                    }
                }
       
                else
                {
                    for (int i = row; i <= row; i++)
                    {
                        for (int j = col; j < col + ship.getSize(); j++)
                        {
                        
                            if(col == 0 || row == 0)
                                throw new IndexOutOfBoundsException("Out of Bounds!\n");
                                
                            if(!this.board[i][j].equals("-"))
                            {
                                System.out.println("\nThis placement intersects a previous ship. Choose again.");
                                return false;
                            }
                        }
                    }
                }
                
      } catch (IndexOutOfBoundsException e)
      {
         System.out.println("Out of Bounds!\n");
          return false;
      }          
                
                return true;
    }

    @Override
    public String toString()
    {
    
        System.out.println();
        
        for (int i = 1; i <= 10; i++)
        {
            for (int j = 1; j <= 10; j++)
            {
                System.out.print(this.board[i][j] + "\t");
            }
            
            System.out.println();
        }
        
        return "Board Printed";
    }
    
    
    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;
            
        if (!(o instanceof String))
        {
            return false;
        }
        
        String strObj = (String) o;
        char charObj = strObj.charAt(0);
        
        if(charObj == '-')
            return true;
            
        else return false;
            
    }

}