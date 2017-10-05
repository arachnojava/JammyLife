package alife.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import alife.ui.ALImages;

public class ALPlant extends ALTile implements ALResource
{
    public static final int REPRO_MATURITY = 100;
    public static final int REPRO_RANGE = 5;
    public static final int FOOD_POINTS = 400;
    private int foodPoints;
    private int age;
    
    
    public ALPlant()
    {
        super.hut = false;
        super.plant = false;
        setMovementCost(2);
        setFoodPoints(FOOD_POINTS);
        age = 0;
    }
    
    
    public void setFoodPoints(int fp)
    {
        foodPoints = fp;
    }


    public static Image createButtonIcon()
    {
        return ALImages.plantButtonImage;
    }

    
    public static Image createButtonOverIcon()
    {
        return ALImages.plantButtonOverImage;
    }
    
    
    public int getAge()
    {
        return age;
    }


    public Image getImage()
    {
        return ALImages.plant;
    }

    
    @Override
    public Type getType()
    {
        return Type.PLANT;
    }


    public int getFoodPoints()
    {
        return foodPoints;
    }


    public void setAge(int a)
    {
        age = a;
    }
}
