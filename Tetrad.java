import java.util.*;
import java.awt.Color;
import java.util.concurrent.Semaphore;
/**
 * Describes and manipulates tetrad objects.
 * 
 * @author Morgan Douglas
 * @version 03/08/17
 */
public class Tetrad
{
    private Block[] blocks;
    private MyBoundedGrid<Block> grid;
    private Location[] locs;
    private String shape;
    private String color;
    private Semaphore lock;
    private Boolean term; 

    /**
     * Constructor for objects of class Tetrad
     * @param gr the grid tetrad is being put into
     */
    public Tetrad(MyBoundedGrid gr)
    {
        lock = new Semaphore(1,true);
        HashMap<String, String> stats = new HashMap<String, String>();
        grid = gr;
        blocks = new Block[4];
        for(int i=0; i<4; i++)
        {
            blocks[i] = new Block();
        }
        stats.put("I", "RED");
        stats.put("T", "GRAY");
        stats.put("O", "CYAN");
        stats.put("L", "YELLOW");
        stats.put("J", "MAGENTA");
        stats.put("S", "BLUE");
        stats.put("Z", "GREEN");
        Object[] shapes = stats.keySet().toArray();
        shape = (String) shapes[(int) ((Math.random()*7))];
        color = stats.get(shape);
        locs = new Location[4];
        if(shape.equals("I"))
        {
            locs[1] = new Location(0, 5);
            locs[0] = new Location(1, 5);
            locs[2] = new Location(2, 5);
            locs[3] = new Location(3, 5);
            for(Block b : blocks)
            {
                b.setColor(Color.RED);
            }
        }
        else if(shape.equals("T"))
        {
            locs[1] = new Location(0, 4);
            locs[0] = new Location(0, 5);
            locs[2] = new Location(0, 6);
            locs[3] = new Location(1, 5);
            for(Block b : blocks)
            {
                b.setColor(Color.GRAY);
            }
        }
        else if(shape.equals("O"))
        {
            locs[0] = new Location(0, 5);
            locs[1] = new Location(0, 6);
            locs[2] = new Location(1, 5);
            locs[3] = new Location(1, 6);
            for(Block b : blocks)
            {
                b.setColor(Color.CYAN);
            }
        }
        else if(shape.equals("L"))
        {
            locs[1] = new Location(0, 5);
            locs[2] = new Location(1, 5);           
            locs[0] = new Location(2, 5);
            locs[3] = new Location(2, 6);
            for(Block b : blocks)
            {
                b.setColor(Color.YELLOW);
            }
        }
        else if(shape.equals("J"))
        {
            locs[2] = new Location(0, 6);
            locs[1] = new Location(1, 6);
            locs[0] = new Location(2, 6);
            locs[3] = new Location(2, 5);
            for(Block b : blocks)
            {
                b.setColor(Color.MAGENTA);
            }
        }
        else if(shape.equals("S"))
        {
            locs[1] = new Location(0, 6);
            locs[0] = new Location(0, 5);
            locs[2] = new Location(1, 5);
            locs[3] = new Location(1, 4);
            for(Block b : blocks)
            {
                b.setColor(Color.BLUE);
            }
        }
        else
        {
            locs[1] = new Location(0, 4);
            locs[0] = new Location(0, 5);
            locs[2] = new Location(1, 5);            
            locs[3] = new Location(1, 6);
            for(Block b : blocks)
            {
                b.setColor(Color.GREEN);
            }
        }
        addToLocations(grid, locs);
    }

    /**
     * @param gr the grid tetrad is to be added to
     * @param lok the locations the tetrad's blocks are to be added to
     * @postcondition tetrad has been placed at locations locs. If any blocks
     * are displaced in process, term becomes false; otherwise, term becomes true.
     */
    public void addToLocations(MyBoundedGrid<Block> gr, Location[] lok)
    {
        grid = gr;
        int z =0;
        for(int i =0; i<4; i++)
        {
            z += blocks[i].putSelfInGrid(grid, lok[i]);

        }
        if(z>0)
        {
            term=false;
        }
        else
        {
            term=true;
        }
    }

    /**
     * @return term
     */
    public Boolean getTerm()
    {
        return term;
    }

    /**
     * @precondition Blocks are in the grid
     * @postcondition Returns old locations of blocks; blocks have been removed from grid.
     */
    private Location[] removeBlocks()
    {
        for(int i=0; i<4; i++)
        {
            blocks[i].removeSelfFromGrid();
        }
        return locs;
    }

    /**
     * @param g the grid being searched
     * @param lok the locations being evaluated
     * @return true if each locs is valid and empty in grid; false otherwise.
     */
    private boolean areEmpty(MyBoundedGrid<Block> g,
    Location[] lok)
    {
        int row = g.getNumRows();
        int col = g.getNumCols();
        for(int i=0; i<4; i++)
        {
            if(lok[i].getRow() < row && lok[i].getCol() < col)
            {
                if(g.get(locs[i])==null)
                {
                }
                else 
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        return true;
    }

    /**
     * @param deltaRow the number of rows tetrad is being translated
     * @param deltaCol the number of columns tetrad is being translated
     * @precondition tetrad in a grid at some location
     * @postcondition tetrad moved deltaRow rows and deltaCol columns, if those locations
     * are valid. If not, tetrad is not moved.
     * @return true if tetrad moves; false otherwise (new location not valid)
     */
    public boolean translate(int deltaRow, int deltaCol)
    {
        try 
        {
            lock.acquire();
            Location[] pocs = new Location[4];
            for(int i=0; i<4; i++)
            {
                if((0 <= locs[i].getRow() + deltaRow) && 
                (locs[i].getRow() + deltaRow < grid.getNumRows()) && 
                (0<= locs[i].getCol() +deltaCol) && 
                (locs[i].getCol() +deltaCol < grid.getNumCols()))
                {
                    pocs[i] = new Location(locs[i].getRow() + deltaRow, 
                        locs[i].getCol() +deltaCol);
                }
                else
                {
                    return false;
                }
            }
            Location[] k = removeBlocks();
            if(areEmpty(grid, pocs))
            {
                locs = pocs;
                addToLocations(grid, locs);
                return true;
            }
            else
            {
                addToLocations(grid, k);
                return false;
            }
            // your lousy code here
        }
        catch (InterruptedException e)
        {
            // did not modify the tetrad
            return false;
        }
        finally
        {
            lock.release();
        }
    }

    /**
     * @precondition tetrad at regular position
     * @postcondition tetrad rotated 90 degrees clockwise if necessary positions empty.
     * Otherwise tetrad stays in current position.
     * @return true if tetrad is successfully rotated; false otherwise.
     */
    public boolean rotate()
    {
        try 
        {
            lock.acquire();
            Location cent = blocks[0].getLocation();
            Location[] pocs = new Location[4];
            pocs[0] = cent;
            for(int i =1; i<4; i++)
            {            
                Location b = blocks[i].getLocation();
                pocs[i] = new Location(cent.getRow()-cent.getCol() + b.getCol(), 
                    cent.getCol()+cent.getRow() - b.getRow());
            }
            Location[] k = removeBlocks();
            if(areEmpty(grid, pocs))
            {
                locs = pocs;
                addToLocations(grid, locs);
                return true;
            }
            else
            {
                addToLocations(grid, k);
                return false;
            }
        }
        catch (InterruptedException e)
        {
            // did not modify the tetrad
            return false;
        }
        finally
        {
            lock.release();
        }
    }

    /**
     * @return array of locations of tetrad
     */
    public Location[] getLocs()
    {
        return locs;
    }
}

