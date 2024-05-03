public class Shiplist
{

Ship head;

public void addShip(int size, String name)
{
    if (head == null)
    {
         head = new Ship(size, name);
    }
    else
    {
        Ship current = head;
        
        while (current.getShip() != null)
            current = current.getShip();
        
        current.setShip(new Ship(size, name));
    }
}

public Ship getHead()
{
    return this.head;
}

@Override
public String toString()
{
    Ship current = head;
    
    if (current != null)
    {
        System.out.println(current.getName() + ": " + current.getSize());
    }
    
    while(current.getShip() != null)
    {
        System.out.println(current.getShip().getName() + ": " + current.getShip().getSize());
        current = current.getShip();
    }
    
    return "Results Printed.";
}

}