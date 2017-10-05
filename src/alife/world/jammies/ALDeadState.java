package alife.world.jammies;

public class ALDeadState extends ALState
{

    @Override
    public void advance(ALJammy subject)
    {
        // If we've been in this state long enough...
        if (++subject.deadTimer > 100)
            subject.setDead(true);
    }

}
