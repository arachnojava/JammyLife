package alife.ui;

import java.awt.Image;
import alife.world.jammies.ALJammy;
import mhframework.media.MHImageGroup;
import mhframework.media.MHResourceManager;

public class ALImages
{
    public static final String IMAGE_DIR = "images/";
    
    // Tiles
    public static final Image grass = MHResourceManager.loadImage(IMAGE_DIR + "Grass.PNG");
    public static final Image plant = MHResourceManager.loadImage(IMAGE_DIR + "Plant.PNG");
    public static final Image rocks = MHResourceManager.loadImage(IMAGE_DIR + "Rocks.PNG");
    public static final Image sand = MHResourceManager.loadImage(IMAGE_DIR + "Sand.PNG");
    public static final Image hut0 = MHResourceManager.loadImage(IMAGE_DIR + "Hut_01.PNG");
    public static final Image hut33 = MHResourceManager.loadImage(IMAGE_DIR + "Hut_02.PNG");
    public static final Image hut66 = MHResourceManager.loadImage(IMAGE_DIR + "Hut_03.PNG");
    public static final Image hut100 = MHResourceManager.loadImage(IMAGE_DIR + "Hut_04.PNG");
    public static final Image water0 = MHResourceManager.loadImage(IMAGE_DIR + "Water_05.PNG");
    public static final Image water25 = MHResourceManager.loadImage(IMAGE_DIR + "Water_04.PNG");
    public static final Image water50 = MHResourceManager.loadImage(IMAGE_DIR + "Water_03.PNG");
    public static final Image water75 = MHResourceManager.loadImage(IMAGE_DIR + "Water_02.PNG");
    public static final Image water100 = MHResourceManager.loadImage(IMAGE_DIR + "Water_01.PNG");

    // Screens
    public static final Image titleScreen = MHResourceManager.loadImage(IMAGE_DIR + "TitleScreen.png");
    //public static final Image creditsScreen = MHResourceManager.loadImage(IMAGE_DIR + "CreditsScreen.png");
    //public static final Image howToPlayScreen = MHResourceManager.loadImage(IMAGE_DIR + "HowToPlayScreen.png");
    
    
    // UI
    public static final Image mouseCursor = MHResourceManager.loadImage(IMAGE_DIR + "Cursor.png");
    public static final Image hutButtonImage = MHResourceManager.loadImage(IMAGE_DIR + "Hut_Button.png");
    public static final Image hutButtonOverImage = MHResourceManager.loadImage(IMAGE_DIR + "Hut_Button_Over.png");
    public static final Image plantButtonImage = MHResourceManager.loadImage(IMAGE_DIR + "Plant_Button.png");
    public static final Image plantButtonOverImage = MHResourceManager.loadImage(IMAGE_DIR + "Plant_Button_Over.png");
    
    private static MHImageGroup igJammy = null;
    public static final MHImageGroup getJammyAnimations()
    {
        if (igJammy == null)
        {    
            //public static int RUNNING  = 0;
            //public static int EATING   = 1;
            //public static int DRINKING = 2;
            //public static int DYING    = 3;
            //public static int DEAD     = 4;
            
            igJammy = new MHImageGroup();
            igJammy.addSequence(ALJammy.RUNNING);
            for (int i = 1; i <= 8; i++)
                igJammy.addFrame(ALJammy.RUNNING, IMAGE_DIR+"JammyWalk_"+twoDigits(i)+".png", 2);
            
            igJammy.addSequence(ALJammy.EATING);
            for (int i = 1; i <= 21; i++)
                igJammy.addFrame(ALJammy.EATING, IMAGE_DIR+"JammyEat_"+twoDigits(i)+".png", 2);
            
            igJammy.addSequence(ALJammy.DRINKING);
            for (int i = 1; i <= 19; i++)
                igJammy.addFrame(ALJammy.DRINKING, IMAGE_DIR+"JammyDrink_"+twoDigits(i)+".png", 2);
            
            igJammy.addSequence(ALJammy.DYING);
            for (int i = 1; i <= 15; i++)
                igJammy.addFrame(ALJammy.DYING, IMAGE_DIR+"JammyDie_"+twoDigits(i)+".png", 2);

            igJammy.addSequence(ALJammy.DEAD);
            //for (int i = 1; i <= 15; i++)
            igJammy.addFrame(ALJammy.DEAD, IMAGE_DIR+"JammyDie_15.png", 100);
        }
        
        return igJammy;
    }

    private static String twoDigits(int i)
    {
        if (i < 10)
            return "0"+i;

        return ""+i;
    }
}

