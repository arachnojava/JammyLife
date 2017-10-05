package alife.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import mhframework.MHPoint;
import mhframework.tilemap.MHMapCellAddress;
import alife.ui.ALImages;

public class ALHut extends ALTile implements ALResource
{
    public static final int MAX_OCCUPANCY = 3;
    private MHMapCellAddress gridLocation;
    MHPoint worldPoint;
    private int occupancy = 0;
    
    public ALHut()
    {
        super.hut = false;
        super.plant = false;
        setMovementCost(0);
    }
    
    
    public int getOccupancy()
    {
        return occupancy;
    }


    public void setOccupancy(int occupancy)
    {
        this.occupancy = occupancy;
    }


    public static Image createButtonIcon()
    {
        return ALImages.hutButtonImage;
    }
    
    
    public static Image createButtonOverIcon()
    {
        return ALImages.hutButtonOverImage;
    }
    
    
    
    public Image getImage()
    {
        double pct = (double)occupancy/MAX_OCCUPANCY;
        if (pct <= 0.33)
            return ALImages.hut0;
        else if (pct <= 0.66)
            return ALImages.hut33;
        else if (pct <= 0.99)
            return ALImages.hut66;

        return ALImages.hut100;
    }


    @Override
    public Type getType()
    {
        return Type.HUT;
    }
}
