package alife.world;

import java.awt.Image;
import alife.ui.ALImages;

public class ALWaterTile extends ALTile
{
    public static final int FULL = 1000;
    private double waterLevel = FULL;
    
    public ALWaterTile()
    {
        hut = false;
        plant = false;
        setTileImage(getImage());
        setMovementCost(2);
    }
    
    
     
    
    public double getWaterLevel()
    {
        return waterLevel;
    }

    
    public void setWaterLevel(double waterLevel)
    {
        if (waterLevel > FULL)
            this.waterLevel = FULL;
        else if (waterLevel < 0)
            this.waterLevel = 0;
        else
            this.waterLevel = waterLevel;
    }

    
    @Override
    public Image getImage()
    {
        double pct = waterLevel/FULL;
        if (pct <= 0.01)
            return ALImages.water0;
        if (pct <= 0.25)
            return ALImages.water25;
        if (pct <= 0.5)
            return ALImages.water50;
        if (pct <= 0.75)
            return ALImages.water75;

        return ALImages.water100;
    }
}
