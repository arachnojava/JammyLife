package alife.world.jammies;

import alife.world.ALTile;
import alife.world.ALWaterTile;
import mhframework.MHPoint;
import mhframework.tilemap.MHMapCellAddress;

public class ALThirstyState extends ALState
{
    @Override
    public void advance(ALJammy subject)
    {
        subject.updateStats();
        
        // Is there any water around?
        if (subject.getWorld().countWaterTiles() <= 0)
        {
            if (subject.getHunger() > subject.getFatigue())
                subject.setState(subject.hungry);
            else
                subject.setState(subject.tired);
            
            subject.setAnimationSequence(ALJammy.RUNNING);
            return;
        }
        
        // Find nearest water source.
        MHPoint w = subject.getWorld().findNearestWater(subject.getLocation());
        if (w == null)
        {
            if (subject.getHunger() > subject.getFatigue())
                subject.setState(subject.hungry);
            else
                subject.setState(subject.tired);
            
            subject.setAnimationSequence(ALJammy.RUNNING);
            return;
        }
        

        // Start heading in that direction.
        MHPoint center = new MHPoint(subject.getX()+ALTile.TILE_SIZE/2, subject.getY()+ALTile.TILE_SIZE/2);
        MHPoint nextStep = center.clone();

        subject.setRotation(center.pointToward(w.getX(), w.getY()));
        subject.setSpeed(subject.getWalkingSpeed());

        nextStep.translate(subject.getRotation(), subject.getSpeed());
        subject.setX(subject.getX() + nextStep.getX());
        subject.setY(subject.getY() + nextStep.getY());
        
        center.setLocation(subject.getX()+ALTile.TILE_SIZE/2, subject.getY()+ALTile.TILE_SIZE/2);
        
        if (nextStep.getX() > 0 || w.getX() > center.getX())
            subject.flip = true;
        else
            subject.flip = false;
        
        double distance = center.distanceTo(w);

        if (distance <= ALTile.TILE_SIZE/2)
        {
            subject.setState(subject.drinking);
            subject.setAnimationSequence(ALJammy.DRINKING);
        }
    }
}
