package alife.world.jammies;

import mhframework.tilemap.MHMapCellAddress;
import alife.world.ALPlant;
import alife.world.ALTile;

public class ALEatingState extends ALState
{
    @Override
    public void advance(ALJammy subject)
    {
        //subject.updateStats();
        subject.setHunger(subject.getHunger()-1);
        
        MHMapCellAddress addr = subject.getGridLocation();
        ALPlant plant = subject.getWorld().getPlant(addr);
        
        if (plant != null)
        {
            plant.setFoodPoints(plant.getFoodPoints() - 1);

            if (plant.getWorldLocation().getX() >= subject.getX() + ALTile.TILE_SIZE/2)
                subject.flip = true;
            else
                subject.flip = false;
        }
        else
        {
            subject.setState(subject.wandering);
            subject.setAnimationSequence(ALJammy.RUNNING);
        }
        
        if (subject.getHunger() <= 0)
        {
            subject.setHunger(0);
            subject.setState(subject.wandering);
            subject.setAnimationSequence(ALJammy.RUNNING);
        }

    }
}
