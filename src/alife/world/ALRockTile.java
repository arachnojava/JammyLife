package alife.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import alife.ui.ALImages;
import mhframework.MHRandom;

public class ALRockTile extends ALTile
{
    public ALRockTile()
    {
        hut = false;
        plant = true;
        setTileImage(getImage());
        setMovementCost(3);
    }

    @Override
    public Image getImage()
    {
        // Grass background.
        Image i = new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) i.getGraphics();
        g.drawImage(ALImages.grass, 0, 0, null);
        
        // Rock foreground.
        g.drawImage(ALImages.rocks, 0, 0, null);
        return i;
    }
}
