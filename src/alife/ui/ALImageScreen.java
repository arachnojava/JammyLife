package alife.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import mhframework.MHDisplayModeChooser;
import mhframework.MHScreen;
import mhframework.media.MHResourceManager;

public class ALImageScreen extends MHScreen
{
    private Image image;
    
    public ALImageScreen(String imageFile)
    {
        super();
        image = MHResourceManager.loadImage(ALImages.IMAGE_DIR + imageFile);
    }

    
    
    
    @Override
    public void render(Graphics2D g)
    {
        fill(g, Color.BLACK);
        g.drawImage(image, 0, 0, MHDisplayModeChooser.getWidth(), MHDisplayModeChooser.getHeight(), null);
        super.render(g);
    }



    @Override
    public void keyPressed(KeyEvent e)
    {
        super.keyPressed(e);
        this.setNextScreen(null);
        this.setFinished(true);
    }




    @Override
    public void mousePressed(MouseEvent e)
    {
        super.mousePressed(e);
        this.setNextScreen(null);
        this.setFinished(true);
    }




    @Override
    public void actionPerformed(ActionEvent e)
    {
    }

    @Override
    public void load()
    {
    }

    @Override
    public void unload()
    {
    }

}
