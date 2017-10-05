package alife.world.jammies;

import mhframework.MHPoint;
import alife.world.ALHut;
import alife.world.ALTile;

public class ALTiredState extends ALState
{
    @Override
    public void advance(ALJammy subject)
    {
        subject.updateStats();
        
        // Are there any huts around?
        if (subject.getWorld().countHuts() <= 0)
        {
            if (subject.getThirst() > subject.getHunger())
                subject.setState(subject.thirsty);
            else
                subject.setState(subject.hungry);
            
            subject.setAnimationSequence(ALJammy.RUNNING);
            return;
        }
        
        // Find nearest food source.
        MHPoint h = subject.getWorld().findNearestHut(subject.getLocation());

        // Start heading in that direction.
        MHPoint center = new MHPoint(subject.getX()+ALTile.TILE_SIZE/2, subject.getY()+ALTile.TILE_SIZE/2);
        MHPoint nextStep = center.clone();

        subject.setRotation(center.pointToward(h.getX(), h.getY()));
        subject.setSpeed(subject.getWalkingSpeed());

        nextStep.translate(subject.getRotation(), subject.getSpeed());
        subject.setX(subject.getX() + nextStep.getX());
        subject.setY(subject.getY() + nextStep.getY());
        
        center.setLocation(subject.getX()+ALTile.TILE_SIZE/2, subject.getY()+ALTile.TILE_SIZE/2);
        
        if (nextStep.getX() > 0 || h.getX() > center.getX())
            subject.flip = true;
        else
            subject.flip = false;
        
        double distance = center.distanceTo(h);

        if (distance <= ALTile.TILE_SIZE/2)
        {
            ALHut hut = (ALHut)subject.getWorld().getHut(subject.getGridLocation());
            hut.setOccupancy(hut.getOccupancy()+1);
            subject.setState(subject.resting);
        }
    }
}
