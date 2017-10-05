package alife.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import mhframework.MHPoint;
import mhframework.MHRandom;
import mhframework.tilemap.MHMapCellAddress;

public abstract class ALTile
{
    public static final int TILE_SIZE = 64;
    
    private int movementCost = 1;
    protected boolean hut, plant;
    private MHMapCellAddress gridLocation;
    private MHPoint worldPoint;
    
    public ALTile()
    {
        worldPoint = new MHPoint();
    }
    
    
    public void render(Graphics2D g, int x, int y)
    {
        g.drawImage(getImage(), x, y, null);
    }
    
    
    public abstract Image getImage();

    
    public boolean supportsHut()
    {
        return hut;
    }
    

    public boolean supportsPlant()
    {
        return plant;
    }
    

    public void setTileImage(Image img)
    {
    }


    public int getMovementCost()
    {
        return movementCost;
    }


    public void setMovementCost(int movementCost)
    {
        this.movementCost = movementCost;
    }
    
    public void setGridLocation(int row, int column)
    {
        gridLocation = new MHMapCellAddress(row, column);
    }
    

    public MHMapCellAddress getGridLocation()
    {
        return gridLocation;
    }
    
    public MHPoint getWorldLocation()
    {
        if (getGridLocation() == null) return null;
        
        int cx = getGridLocation().column * ALTile.TILE_SIZE + ALTile.TILE_SIZE/2;
        int cy = getGridLocation().row    * ALTile.TILE_SIZE + ALTile.TILE_SIZE/2;
        worldPoint.setLocation(cx, cy);
        return worldPoint;
    }
}
