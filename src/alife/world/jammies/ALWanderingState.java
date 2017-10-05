package alife.world.jammies;

public class ALWanderingState extends ALState
{
    @Override
    public void advance(ALJammy subject)
    {
        subject.updateStats();
        subject.setSpeed(0);
        
        int h = subject.getHunger();
        int t = subject.getThirst();
        int f = subject.getFatigue();

        
        if (t >= h && t >= f)
            subject.setState(subject.thirsty);
        else if (h >= t && h >= f)
            subject.setState(subject.hungry);
        else if (f >= h && f >= t)
            subject.setState(subject.tired);
    }

}
