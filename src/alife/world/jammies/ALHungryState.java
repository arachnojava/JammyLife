package alife.world.jammies;

import mhframework.MHPoint;
import alife.world.ALTile;

public class ALHungryState extends ALState
{
    @Override
    public void advance(ALJammy subject)
    {
        subject.updateStats();
        
        // Is there any food around?
        if (subject.getWorld().countPlants() <= 0)
        {
            if (subject.getThirst() > subject.getFatigue())
                subject.setState(subject.thirsty);
            else
                subject.setState(subject.tired);
            
            subject.setAnimationSequence(ALJammy.RUNNING);
            return;
        }
        
        // Find nearest food source.
        MHPoint p = subject.getWorld().findNearestPlant(subject.getLocation());

        // Start heading in that direction.
        MHPoint center = new MHPoint(subject.getX()+ALTile.TILE_SIZE/2, subject.getY()+ALTile.TILE_SIZE/2);
        MHPoint nextStep = center.clone();

        subject.setRotation(center.pointToward(p.getX(), p.getY()));
        subject.setSpeed(subject.getWalkingSpeed());

        nextStep.translate(subject.getRotation(), subject.getSpeed());
        subject.setX(subject.getX() + nextStep.getX());
        subject.setY(subject.getY() + nextStep.getY());
        
        center.setLocation(subject.getX()+ALTile.TILE_SIZE/2, subject.getY()+ALTile.TILE_SIZE/2);
        
        if (nextStep.getX() > 0 || p.getX() > center.getX())
            subject.flip = true;
        else
            subject.flip = false;
        
        double distance = center.distanceTo(p);

        if (distance <= ALTile.TILE_SIZE/2)
        {
            subject.setState(subject.eating);
            subject.setAnimationSequence(ALJammy.EATING);
        }
    }
}
