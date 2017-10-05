package alife.ui;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import mhframework.MHDisplayModeChooser;
import mhframework.MHGame;
import mhframework.MHScreen;
import mhframework.gui.MHGUIButton;
import mhframework.media.MHFont;
import mhframework.media.MHImageFont;


public class ALMainMenuScreen extends MHScreen
{
    private MHFont titleFont;
    private MHFont dataFont;
    private MHGUIButton btnStart, btnExit, btnHowToPlay, btnCredits;
    private Color buttonColor = new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), 128);
    
    
    public ALMainMenuScreen()
    {
    }
    
    public void render(Graphics2D g)
    {
        fill(g, Color.BLACK);

        g.drawImage(ALImages.titleScreen, 0, 0, 1024, 768, null);
        
        // DEBUG
        //g.setColor(Color.WHITE);
        //dataFont.drawString(g, "Screen size: " + MHDisplayModeChooser.getWidth() + "x" + MHDisplayModeChooser.getHeight(), 10, 20);
        
        super.render(g);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == btnStart)
        {
            setNextScreen(new ALGameScreen());
            setFinished(true);
        }
        else if (e.getSource() == btnHowToPlay)
        {
            setNextScreen(new ALImageScreen("HowToPlayScreen.png"));
            setFinished(true);
        }
        else if (e.getSource() == btnCredits)
        {
            setNextScreen(new ALImageScreen("CreditsScreen.png"));
            setFinished(true);
        }
        else if (e.getSource() == btnExit)
            MHGame.setProgramOver(true);
    }


    @Override
    public void load()
    {
        setCursor(ALImages.mouseCursor);
        setFinished(false);
        

        
        titleFont = new MHFont(MHImageFont.EngineFont.TAHOMA_BLUE);
        dataFont = new MHFont("SansSerif", Font.BOLD, 10);
        
        int buttonSpacing = 60;
        int y = 500;

        btnStart = new MHGUIButton();
        btnStart.setText("Start");
        btnStart.setSize(250, 50);
        btnStart.setFont(titleFont);
        btnStart.addActionListener(this);
        btnStart.setY(y);
        btnStart.setNormalColor(buttonColor);
        btnStart.setOverColor(Color.BLACK);
        btnStart.setDownColor(Color.DARK_GRAY);
        centerComponent(btnStart);
        add(btnStart);

        y += buttonSpacing;
        
        btnHowToPlay = new MHGUIButton();
        btnHowToPlay.setText("How To Play");
        btnHowToPlay.setSize(btnStart.getWidth(), btnStart.getHeight());
        btnHowToPlay.setFont(btnStart.getFont());
        btnHowToPlay.addActionListener(this);
        btnHowToPlay.setY(y);
        btnHowToPlay.setNormalColor(buttonColor);
        btnHowToPlay.setOverColor(Color.BLACK);
        btnHowToPlay.setDownColor(Color.DARK_GRAY);
        centerComponent(btnHowToPlay);
        add(btnHowToPlay);

        y += buttonSpacing;
        
        btnCredits = new MHGUIButton();
        btnCredits.setText("Credits");
        btnCredits.setSize(btnStart.getWidth(), btnStart.getHeight());
        btnCredits.setFont(btnStart.getFont());
        btnCredits.addActionListener(this);
        btnCredits.setY(y);
        btnCredits.setNormalColor(buttonColor);
        btnCredits.setOverColor(Color.BLACK);
        btnCredits.setDownColor(Color.DARK_GRAY);
        centerComponent(btnCredits);
        add(btnCredits);

        y += buttonSpacing;
        
        btnExit = new MHGUIButton();
        btnExit.setText("Exit");
        btnExit.setSize(btnStart.getWidth(), btnStart.getHeight());
        btnExit.setFont(btnStart.getFont());
        btnExit.addActionListener(this);
        btnExit.setY(y);
        btnExit.setNormalColor(buttonColor);
        btnExit.setOverColor(Color.BLACK);
        btnExit.setDownColor(Color.DARK_GRAY);
        centerComponent(btnExit);
        add(btnExit);
    }


    @Override
    public void unload()
    {
        // TODO Auto-generated method stub
    }


    @Override
    public void keyPressed(KeyEvent e)
    {
        super.keyPressed(e);
        
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            MHGame.setProgramOver(true);
    }

}
