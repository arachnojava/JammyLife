package alife.world.jammies;

import mhframework.tilemap.MHMapCellAddress;
import alife.world.ALWaterTile;

public class ALDrinkingState extends ALState
{

    @Override
    public void advance(ALJammy subject)
    {
        //subject.updateStats();
        subject.setThirst(subject.getThirst()-1);
        MHMapCellAddress addr = subject.getGridLocation();
        ALWaterTile water = (ALWaterTile)subject.getWorld().getMapcell(addr.row, addr.column);
        water.setWaterLevel(water.getWaterLevel()-1);
        if (water.getWaterLevel()/ALWaterTile.FULL <= 0.1)
        {
            water.setWaterLevel(0.0);
            subject.setState(subject.wandering);
            subject.setAnimationSequence(ALJammy.RUNNING);
            return;
        }
            
        if (subject.getThirst() <= 0) 
        {
            subject.setThirst(0);
            subject.setState(subject.wandering);
            subject.setAnimationSequence(ALJammy.RUNNING);
        }
    }
}
