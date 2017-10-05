package alife.ui;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import mhframework.MHActor;
import mhframework.MHDisplayModeChooser;
import mhframework.MHPoint;
import mhframework.MHScreen;
import mhframework.gui.MHGUIButton;
import mhframework.gui.MHGUIDialogBox;
import mhframework.media.MHFont;
import mhframework.media.MHImageFont;
import alife.world.ALGameWorld;
import alife.world.ALHut;
import alife.world.ALPlant;
import alife.world.ALResource;
import alife.world.ALTile;
import alife.world.jammies.ALJammy;


public class ALGameScreen extends MHScreen
{
    private ALGameWorld world;
    private static final int scrollRange = 32;
    private static final int scrollSpeed = 16;    
    private int scrollX = 0;
    private int scrollY = 0;
    private MHGUIButton btnPlant, btnHut;
    private ALResource selectedResource;
    private MHFont font;
    
    private int spacing;
    
    private Color infoBoxBG = new Color(0, 0, 0, 180);
    
    // DEBUG
    TestSprite sprite;

    public ALGameScreen()
    {
        world = new ALGameWorld();

        Image plantIcon = ALPlant.createButtonIcon();
        btnPlant = new MHGUIButton(plantIcon, plantIcon, ALPlant.createButtonOverIcon());
        btnPlant.addActionListener(this);
        btnPlant.setPosition(10, MHDisplayModeChooser.getHeight() - plantIcon.getHeight(null) - 10);
        add(btnPlant);
        
        Image hutIcon = ALHut.createButtonIcon();
        btnHut = new MHGUIButton(hutIcon, hutIcon, ALHut.createButtonOverIcon());
        btnHut.addActionListener(this);
        btnHut.setPosition(btnPlant.getX() + btnPlant.getWidth() + 10, btnPlant.getY());
        add(btnHut);
        
        font = new MHFont(new MHImageFont(MHImageFont.EngineFont.TAHOMA_BLUE));
        font.setScale(0.66);
        
        ALJammy.oldAgeDeaths = 0;
        ALJammy.starvationDeaths = 0;
        ALJammy.dehydrationDeaths = 0;
        ALJammy.fatigueDeaths = 0;
        ALJammy.largestPopulation = 0;
        
        // DEBUG
        //sprite = new TestSprite();
    }
    
    
    public void render(Graphics2D g)
    {
        fill(g, Color.BLACK);
        
        world.render(g);
        
        //sprite.render(g);
        showJammyInfo(g);
        showMapResourceInfo(g);
        
        g.fillRoundRect(10, 768-64-spacing-20, 256, spacing, 10, 10);
        font.drawString(g, world.getResourcePoints() + " Resources Available", 15, 768-64-20);
        
        super.render(g);
    }
    
    
    
    private void showMapResourceInfo(Graphics2D g)
    {
        int numLines = 3;
        
        spacing = (int)(font.getHeight() * 1.1);
        int y = spacing + 10;
        int x = MHDisplayModeChooser.getWidth() - 130;
        g.setColor(infoBoxBG);
        g.fillRoundRect(x-5, 10, 120, spacing*numLines, 20, 20);
        font.drawString(g, "Plants: " + world.countPlants(), x, y);
        y += spacing;
        font.drawString(g, "Huts: " + world.countHuts(), x, y);
        y += spacing;
        font.drawString(g, "Water: " + world.countWaterTiles(), x, y);
    }
    
    
    private void showJammyInfo(Graphics2D g)
    {
        g.setColor(infoBoxBG);
        g.fillRoundRect(10, 10, 256, spacing*6, 20, 20);
        
        spacing = (int)(font.getHeight() * 1.1);
        int y = spacing + 10;
        font.drawString(g, "Jammies: " + world.countJammies(), 15, y);
        y += spacing;
        font.drawString(g, "Peaked at: " + ALJammy.getLargestPopulation(), 15, y);
        y += spacing;
        font.drawString(g, "Old Age Deaths: " + ALJammy.getOldAgeDeaths(), 15, y);
        y += spacing;
        font.drawString(g, "Starvations: " + ALJammy.getStarvationDeaths(), 15, y);
        y += spacing;
        font.drawString(g, "Dehydrations: " + ALJammy.getDehydrationDeaths(), 15, y);
        y += spacing;
        font.drawString(g, "Fatigue Deaths: " + ALJammy.getFatigueDeaths(), 15, y);
    }
    
    
    public void advance()
    {
        //sprite.advance();
        
        world.advance();
        
        //Check for game over.
        if (!isFinished() && world.countJammies() <= 0)
        {
            MHFont titleFont = new MHFont(MHImageFont.EngineFont.TAHOMA_BLUE);
            MHFont textFont = new MHFont("Arial", Font.BOLD, 20);
            MHGUIDialogBox b = new MHGUIDialogBox(this, "The jammy race is now extinct.", "Extinction!", textFont, titleFont);
            setNextScreen(b);
            setFinished(true);
        }
        
        
        world.scrollRelative(scrollX, scrollY);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (world.getResourcePoints() < 1) return;
        
        if (e.getSource() == btnPlant)
        {
            // Set the plant as the selected resource.
            selectedResource = new ALPlant();
            // Change mouse cursor to plant icon.
            setCursor(selectedResource.getImage());
        }
        else if (e.getSource() == btnHut)
        {
            // Set the hut as the selected resource.
            selectedResource = new ALHut();
            // Change mouse cursor to hut icon.
            super.setCursor(selectedResource.getImage());
        }
    }

    @Override
    public void load()
    {
        if (world.countJammies() <= 0)
        {
            setNextScreen(null);
            setFinished(true);
            setDisposable(true);
        }
    }

    @Override
    public void unload()
    {
        // TODO Auto-generated method stub
        
    }

    
    
    
    
    @Override
    public void mousePressed(MouseEvent e)
    {
        super.mousePressed(e);
        
        if (selectedResource == null) return;
        if (btnPlant.getBounds().contains(e.getPoint())) return;
        if (btnHut.getBounds().contains(e.getPoint())) return;
        if (world.getResourcePoints() < 1) return;
        
        // Figure out where to put it.
        int col = (ALGameWorld.getScreenAnchorX() + e.getX()) / ALTile.TILE_SIZE;
        int row = (ALGameWorld.getScreenAnchorY() + e.getY()) / ALTile.TILE_SIZE;
        
        // Make sure we can put it there.
        if (selectedResource.getType() == ALResource.Type.PLANT)
        {
            if (world.supportsPlant(row, col))
            {
                ALPlant plant = (ALPlant)selectedResource;
                plant.setGridLocation(row, col);
                world.addPlant(plant);
                setCursor(ALImages.mouseCursor);
                selectedResource = null;
                world.spentResources++;
            }
        }
        else if (selectedResource.getType() == ALResource.Type.HUT)
        {
            if (world.supportsHut(row, col))
            {
                ALHut hut = new ALHut();
                hut.setGridLocation(row, col);
                world.addHut(hut);
                setCursor(ALImages.mouseCursor);
                selectedResource = null;
                world.spentResources++;
            }
        }
    }


    @Override
    public void mouseReleased(MouseEvent e)
    {
        // TODO Auto-generated method stub
        super.mouseReleased(e);
    }

    
    
    

    @Override
    public void mouseDragged(MouseEvent e)
    {
        super.mouseDragged(e);
        //world.scroll(e.getX(), e.getY());
    }


    @Override
    public void mouseMoved(MouseEvent e)
    {
        super.mouseMoved(e);
        //sprite.setTarget(e.getX()+ALGameWorld.getScreenAnchorX(), e.getY()+ALGameWorld.getScreenAnchorY());
        
        if (e.getX() < scrollRange)
            scrollX = -scrollSpeed;
        else if (e.getX() > MHDisplayModeChooser.getWidth() - scrollRange)
            scrollX = scrollSpeed;
        else
            scrollX = 0;

        if (e.getY() < scrollRange)
            scrollY = -scrollSpeed;
        else if (e.getY() > MHDisplayModeChooser.getHeight() - scrollRange)
            scrollY = scrollSpeed;
        else
            scrollY = 0;
    }
    
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        super.keyPressed(e);
        
        int scrollSpeed = 16;
        
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            setNextScreen(null);
            setFinished(true);
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP)
            world.scrollRelative(0, -scrollSpeed);
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
            world.scrollRelative(0, scrollSpeed);
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            world.scrollRelative(scrollSpeed, 0);
        else if (e.getKeyCode() == KeyEvent.VK_LEFT)
            world.scrollRelative(-scrollSpeed, 0);
    }

}


class TestSprite extends MHActor
{
    int width = 32;
    int targetX, targetY;
    MHPoint nextStep;
    
    public TestSprite()
    {
        setLocation(512, 380);
        setSpeed(2);
        nextStep = getLocation().clone();
    }
    
    
    public void render(Graphics2D g)
    {
        g.setColor(Color.BLUE);
        g.fillOval((int)getX()-ALGameWorld.getScreenAnchorX(), (int)getY()-ALGameWorld.getScreenAnchorY(), width, width);
        
        g.setColor(Color.CYAN);
        nextStep = getLocation().clone();
        nextStep.setLocation(nextStep.getX() + width/2, nextStep.getY() + width/2);
        nextStep.translate(getRotation(), getSpeed());
        g.drawLine((int)(getX()-ALGameWorld.getScreenAnchorX()+width/2), (int)(getY()-ALGameWorld.getScreenAnchorY()+width/2), (int)(getX()-ALGameWorld.getScreenAnchorX()+width/2+nextStep.getX()), (int)(getY()-ALGameWorld.getScreenAnchorY()+width/2+nextStep.getY()));
        
    }
    
    
    public void setTarget(int x, int y)
    {
        targetX = x;
        targetY = y;
    }
    
    
    public void advance()
    {
        setRotation(getLocation().pointToward(targetX, targetY));
        
        nextStep.translate(getRotation(), getSpeed());
        setX(getX() + nextStep.getX());
        setY(getY() + nextStep.getY());
    }
}