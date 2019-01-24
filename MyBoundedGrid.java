import java.util.ArrayList;
/**
 * A MyBoundedGrid is a rectangular grid with a finite number of rows and columns.
 * @author Morgan Douglas
 * @version 02/23/17
 * @param <E> type of object being put into grid
 */
public class MyBoundedGrid<E>
{
    private Object[][] occupantArray; 

    /**
     * Constructor
     * @precondition rows > 0 and cols > 0
     * @param rows the number of rows in grid
     * @param cols the number of columns in grid
     */
    public MyBoundedGrid(int rows, int cols)
    {
        occupantArray = new Object[rows][cols];
    }

    /**
     * @return number of rows
     */
    public int getNumRows()
    {
        return occupantArray.length;
    }

    /**
     * @return number of columns
     */
    public int getNumCols()
    {
        return occupantArray[0].length;

    }

    /**
     * @precondition: loc is not null
     * @param loc the location being evaluated
     * @return true if location is valid on grid; false otherwise
     */
    public boolean isValid(Location loc)
    {
        return (loc.getRow()<getNumRows() && loc.getCol() < getNumCols()) && 
        (loc.getRow()>=0 && loc.getCol()>=0);

    }

    /**
     * @precondition loc is valid in grid
     * @param loc the location being looked at
     * @return object at loc
     */
    public E get(Location loc)
    {

        return (E) occupantArray[loc.getRow()][loc.getCol()];

        //(You will need to promise the return value is of type E.)
    }

    /**
     * @precondition loc is valid in grid
     * @param obj the object being placed at loc
     * @param loc the location obj is being placed
     * @return item previously at loc; null if no item there
     */
    public E put(Location loc, E obj)
    {
        E save = get(loc);
        occupantArray[loc.getRow()][loc.getCol()] = obj;
        return save;
    }

    /**
     * removes object at given location
     * @param loc the location from which object is being removed
     * @return object previously contained at loc
     */
    public E remove(Location loc)
    {
        E save = get(loc);
        occupantArray[loc.getRow()][loc.getCol()] = null;
        return save;
    }

    /**
     * @return an ArrayList containing all occupied locations in grid
     */
    public ArrayList<Location> getOccupiedLocations()
    {
        ArrayList<Location> locs = new ArrayList<Location>();
        for(int i=0; i<getNumRows(); i++)
        {
            for( int z=0; z<getNumCols(); z++)
            {
                if(occupantArray[i][z]!=null)
                {
                    locs.add(new Location(i, z));
                }
            }
        }
        return locs;
    }
}