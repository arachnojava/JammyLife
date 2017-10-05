package alife.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import alife.ui.ALImages;
import mhframework.MHRandom;

public class ALGrassTile extends ALTile
{
    public ALGrassTile()
    {
        hut = true;
        plant = true;
        setTileImage(getImage());
        setMovementCost(1);
    }

    @Override
    public Image getImage()
    {
        return ALImages.grass;
//        Image i = new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_RGB);
//        Graphics2D g = (Graphics2D) i.getGraphics();
//        g.setColor(Color.GREEN.darker().darker());
//        g.fillRect(0, 0, TILE_SIZE, TILE_SIZE);
//        for (int c = 0; c < 64; c++)
//        {
//            g.setColor(new Color(0, MHRandom.random(64, 255), 0));
//            g.fillRect(MHRandom.random(0, TILE_SIZE), MHRandom.random(0, TILE_SIZE), 2, 2);
//        }
//        
//        return i;
    }
}
