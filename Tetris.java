import java.util.*;
import javax.swing.JOptionPane;
import  sun.audio.*;
import java.io.*;
/**
 * Tetris contains methods that allow a game to be played. 
 * 
 * @author Morgan Douglas
 * @version 03/08/17
 */
public class Tetris implements ArrowListener
{
    private MyBoundedGrid<Block> grid;
    private BlockDisplay display;
    private Tetrad active;
    private int level;
    private int points;
    private int cleared;

    /**
     * Constructor for objects of class Tetris
     */
    public Tetris()
    {
        try
        {
            InputStream in = new FileInputStream("music.wav");
            AudioStream as = new AudioStream(in); 
            AudioPlayer.player.start(as);  
        }
        catch (FileNotFoundException e)
        {
            System.out.println("music not found");
        }
        catch (IOException e)
        {
            System.out.println("no input audio");
        }
        grid = new MyBoundedGrid<Block>(20, 10);
        display = new BlockDisplay(grid);
        level = 1;
        points = 0;
        display.setTitle("Tetris | lvl: " + level + "| pts: " + points);
        display.setArrowListener(this);
        Tetrad t = new Tetrad(grid);
        active = t;
        play();
        display.showBlocks();
    }

    /**
     * @postcondition active tetrad rotated 90 degrees clockwise
     */
    public void upPressed()
    {
        active.rotate();
        display.showBlocks();
    }

    /**
     * @postcondition active tetrad translated downward one unit
     */
    public void downPressed()
    {
        active.translate(1, 0);
        display.showBlocks();
    }

    /**
     * @postcondition active tetrad translated leftward one unit
     */
    public void leftPressed()
    {
        active.translate(0, -1);
        display.showBlocks();
    }

    /**
     * @postcondition active tetrad translated rightward.
     */
    public void rightPressed()
    {
        active.translate(0, 1);
        display.showBlocks();
    }

    /**
     * Starts a game of tetris. When blocks reach top of grid, a message indicates the player
     * has lost and the game stops.
     */
    public void play()
    {
        Boolean b = true;
        try 
        {
            while(b)
            {
                Thread.sleep(500);
                Boolean t = active.translate(1, 0);
                if(!t)
                {

                    clearCompletedRows();
                    display.setTitle("Tetris || lvl: " + level + "|| pts: " + points);
                    active = new Tetrad(grid);
                    if(!active.getTerm())
                    {
                        JOptionPane.showMessageDialog(null, "You lose!");
                        b=false;
                    }
                }

                display.showBlocks();
            }
        }
        catch(InterruptedException e)
        {
            //ignore }
        }
    }

    /**
     * @param row the number of row being checked.
     * @precondition  0 <= row < number of rows
     * @postcondition Returns true if every cell in the given row is occupied;
     * returns false otherwise.
     */
    private boolean isCompletedRow(int row)
    {
        for(int i =0; i<10; i++)
            if(grid.get(new Location(row, i))==null)
                return false;
        return true;
    }

    /**
     * @param row the number row being cleared
     * @precondition 0<= row < number of rows. Given row is full of blocks
     * @postcondition Every block in the given row has been removed, and every
     * block above row has been moved down one row.
     */
    private void clearRow(int row)
    {
        for(int i =0; i<10; i++)
        {
            grid.get(new Location(row, i)).removeSelfFromGrid();
            display.showBlocks();
        }

        for(int z = row-1; z>=0; z--)
        {
            for(int y=0; y<10; y++)
            {
                Block b = grid.get(new Location(z, y));
                if(b!=null)
                {
                    b.moveTo(new Location (b.getLocation().getRow()+1, b.getLocation().getCol()));
                }
            }

        }
        display.showBlocks();
    }

    /**
     * @postcondition All completed rows have been cleared and levels and
     * points have been added
     */
    private void clearCompletedRows()
    {
        int z=0;
        for(int i =0; i<20; i++)
        {
            if(isCompletedRow(i))
            {
                clearRow(i);
                cleared++;
                z++;
            }
            if(cleared!=0 && cleared%10==0)
            {
                level++;
            }
        }
        if(z==1)
            points += 40*level;
        else if(z==2)
            points += 100*level;
        else if(z==3)
            points += 300*level;
        else if(z==4)
            points += 1200*level;
    }
}
