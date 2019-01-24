import java.awt.Color;
/**
 * Class that describes Block objects
 * @author Morgan Douglas
 * @version 02/24/17
 */
public class Block
{
    private MyBoundedGrid<Block> grid;
    private Location location;
    private Color color;

    /**
     * constructs a blue block
     */
    public Block()
    {
        color = Color.BLUE;
        grid = null;
        location = null;
    }

    /**
     * @return color of block
     */
    public Color getColor()
    {
        return color;
    }

    /**
     * @param newColor color to be set 
     */
    public void setColor(Color newColor)
    {
        color = newColor;
    }

    /**
     * @return the grid that the block is in
     */
    public MyBoundedGrid<Block> getGrid()
    {
        return grid;
    }

    /**
     * @return Location of block; null if block not contained in grid
     */
    public Location getLocation()
    {
        return location;
    }

    /**
     * removes block from grid
     * @precondition block contained in grid
     */
    public void removeSelfFromGrid()
    {
        grid.remove(location);
        location = null;
        grid = null;
    }

    /**
     * puts block into location loc of grid gr, removing any other block at loc
     *
     * @param gr grid being put into
     * @param loc location being put into
     * @precondition (1) this block is not contained in a grid (2) loc is valid in gr
     * @postcondition block is in grid gr at location loc
     * @return 1 if block was displaced; 0 otherwise
     */
    public Integer putSelfInGrid(MyBoundedGrid<Block> gr, Location loc)
    {
        Block b = gr.get(loc);
        int i =0;
        if(b!=null)
        {
            i++;
            b.removeSelfFromGrid();
        }
        gr.put(loc, this);
        grid = gr;
        location = loc;
        return i;
    }

    /**
     * moves this block to newLocation, if there is another block at newLocation,
     * it is removed
     * @precondition (1) this block is contained in a grid 
     * (2) newLocation is valid in the grid of this block
     * @param newLocation the location being moved to
     */
    public void moveTo(Location newLocation)
    {
        grid.remove(location);
        Block b = grid.get(newLocation);
        if(b!=null)
            b.removeSelfFromGrid();
        grid.put(newLocation, this);
        location = newLocation;
    }

    /**
     * @return a string with the location and color of this block
     */
    public String toString()
    {
        return "Block[location=" + location + ",color=" + color + "]";
    }
}