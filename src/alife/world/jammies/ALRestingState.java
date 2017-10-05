package alife.world.jammies;

import alife.world.ALHut;

public class ALRestingState extends ALState
{

    @Override
    public void advance(ALJammy subject)
    {
        //subject.updateStats();
        
        subject.setFatigue(subject.getFatigue()-1);
        
        if (subject.getFatigue() <= 0)
        {
            subject.setFatigue(0);
            
            if (subject.getHunger() < (int)(ALJammy.FATAL * 0.5) && subject.getThirst() < (int)(ALJammy.FATAL * 0.5))
            {
                System.out.println("Jammy " + subject.id + " reproduced!");
                subject.getWorld().createJammy(subject.getLocation());
                subject.setHunger(subject.getHunger()*2);
                subject.setThirst(subject.getThirst()*2);
            }
            
            ALHut hut = (ALHut)subject.getWorld().getHut(subject.getGridLocation());
            hut.setOccupancy(hut.getOccupancy()-1);
            
            subject.setState(subject.wandering);
        }
    }

}
