package alife;

import alife.ui.ALMainMenuScreen;
import mhframework.MHAppLauncher;
import mhframework.MHDisplayModeChooser;
import mhframework.MHGameApplication;
import mhframework.MHScreen;
import mhframework.MHVideoSettings;


public class ALMain
{
    //public static final boolean DEBUG = true;
    
    public static void main(final String[] args)
    {
        final MHScreen screen = new ALMainMenuScreen();

        final MHVideoSettings settings = new MHVideoSettings();
        settings.showSplashScreen = true;
        settings.displayWidth = 1024;
        settings.displayHeight = 768;
        settings.fullScreen = MHAppLauncher.showDialog(MHDisplayModeChooser.getFrame(), false);
        settings.windowCaption = "Jammy Life";

        new MHGameApplication(screen, settings);

        System.exit(0);
    }
}
