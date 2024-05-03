public class Ship
{

private Ship ship;
private int size;
private String name;

public Ship(int size, String name)
{
    this.size = size;
    this.name = name;
}

public Ship getShip()
{
    return this.ship;
}

public void setShip(Ship setShip)
{
    this.ship = setShip;
}

public String getName()
{
    return this.name;
}

public int getSize()
{
    return this.size;
}
}

