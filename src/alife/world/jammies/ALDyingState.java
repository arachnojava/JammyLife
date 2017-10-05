package alife.world.jammies;

public class ALDyingState extends ALState
{

    @Override
    public void advance(ALJammy jammy)
    {
        // If animation over, set state to "dead".
        if (jammy.isAnimationFinished())
        {
            jammy.setState(jammy.dead);
            jammy.setAnimationSequence(ALJammy.DEAD);
        }
    }

}
