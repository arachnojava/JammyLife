package alife.world.jammies;
import java.awt.Graphics2D;
import mhframework.MHActor;
import mhframework.MHPoint;
import mhframework.MHRandom;
import mhframework.tilemap.MHMapCellAddress;
import alife.ui.ALImages;
import alife.world.ALGameWorld;
import alife.world.ALHut;
import alife.world.ALTile;


public class ALJammy extends MHActor
{
    public static final int FATAL = 500;
    
    private static int COUNT = 0;
    public int id = COUNT++;
    
    // Animation sequences
    public static int RUNNING  = 0;
    public static int EATING   = 1;
    public static int DRINKING = 2;
    public static int DYING    = 3;
    public static int DEAD     = 4;

    public static int oldAgeDeaths = 0;
    public static int starvationDeaths = 0;
    public static int dehydrationDeaths = 0;
    public static int fatigueDeaths = 0;
    public static int largestPopulation = 0;
    
    private int hunger;
    private int thirst;
    private int fatigue;
    private int age;
    private int deathAge;
    private boolean isDead = false;
    public int deadTimer = 0;
    private ALGameWorld world;
    MHMapCellAddress gridLocation;
    public boolean flip = false;
    
    // Jammy states.
    private ALState state;
    public ALState wandering = new ALWanderingState();
    public ALState dying     = new ALDyingState();
    public ALState hungry    = new ALHungryState();
    public ALState thirsty   = new ALThirstyState();
    public ALState tired     = new ALTiredState();
    public ALState eating    = new ALEatingState();
    public ALState drinking  = new ALDrinkingState();
    public ALState resting   = new ALRestingState();
    public ALState dead      = new ALDeadState();

    
    public ALJammy(ALGameWorld world)
    {
        this.world = world;
        int spawnX = MHRandom.random(ALTile.TILE_SIZE, ALGameWorld.WORLD_WIDTH-ALTile.TILE_SIZE*2);
        int spawnY = MHRandom.random(ALTile.TILE_SIZE, ALGameWorld.WORLD_HEIGHT-ALTile.TILE_SIZE*2);
        this.setLocation(spawnX, spawnY);
        
        // Initialize hunger, etc.
        hunger = FATAL/2; //MHRandom.random(0, FATAL/4);
        thirst = FATAL/2; //MHRandom.random(0, FATAL/4);
        fatigue = FATAL/2; //MHRandom.random(0, FATAL/4);
        age = 0;
        deathAge = MHRandom.random(FATAL*3, FATAL*6);
        
        state = wandering;
        
        this.setImageGroup(ALImages.getJammyAnimations());
    }

    
    public ALJammy(ALGameWorld world, MHPoint spawnPoint)
    {
        this(world);
        setLocation(spawnPoint.getX(), spawnPoint.getY());
    }

    
    public void render(Graphics2D g)
    {
        if (state == resting) return;
        
        int sx = (int)(getX() - world.getScreenAnchorX());
        int sy = (int)(getY() - world.getScreenAnchorY());
        
        if (flip)
            g.drawImage(getImage(), sx+getImage().getWidth(null), sy, -getImage().getWidth(null), getImage().getHeight(null), null);
        else
            g.drawImage(getImage(), sx, sy, null);
    }

    @Override
    public void advance()
    {
        super.advance();
        age++;
        
        checkForDeath();
        
        state.advance(this);
    }

    
    public void updateStats()
    {
        int row = (int)((ALGameWorld.getScreenAnchorY() + getY()) / ALTile.TILE_SIZE);
        int col = (int)((ALGameWorld.getScreenAnchorX() + getX()) / ALTile.TILE_SIZE);
        
        int terrainEffect = world.getMapcell(row, col).getMovementCost();
        
        int whichStat = MHRandom.random(0, 2);
        
        switch (whichStat)
        {
            case 0:  hunger += terrainEffect;
                break;
            case 1:  thirst += terrainEffect;
                break;
            case 2:  fatigue += terrainEffect;
                break;
        }
    }
    
    
    private void checkForDeath()
    {
        if (state == dying || state == dead) return;
        
        if (getAge() > deathAge)
        {
            // If jammy died in its sleep, remove it from the hut.
            if (state == resting)
            {
                ALHut hut = (ALHut)getWorld().getHut(getGridLocation());
                hut.setOccupancy(hut.getOccupancy()-1);
            }

            // TODO:  Set old age animation.
            System.out.println("Jammy " + id + " died of old age.");
            ALJammy.countOldAge();
            setAnimationSequence(DYING);
            setState(dying);
            
            
            return;
        }


        if (getHunger() > ALJammy.FATAL)
        {
            // TODO:  Set starving animation.
            System.out.println("Jammy " + id + " starved to death.");
            ALJammy.countStarvation();
            setAnimationSequence(DYING);
            setState(dying);
            return;
        }

        
        if (getThirst() > ALJammy.FATAL)
        {
            // TODO:  Set dehydrating animation.
            System.out.println("Jammy " + id + " died of dehydration.");
            ALJammy.countDehydration();
            setAnimationSequence(DYING);
            setState(dying);
            return;
        }
        

        if (getFatigue() > ALJammy.FATAL)
        {
            // TODO:  Set fatigue animation.
            System.out.println("Jammy " + id + " died from fatigue.");
            ALJammy.countFatigue();
            setAnimationSequence(DYING);
            setState(dying);
            return;
        }
    }

    
    public int getHunger()
    {
        return hunger;
    }


    public void setHunger(int hunger)
    {
        this.hunger = hunger;
    }


    public int getThirst()
    {
        return thirst;
    }


    public void setThirst(int thirst)
    {
        this.thirst = thirst;
    }


    public int getFatigue()
    {
        return fatigue;
    }


    public void setFatigue(int fatigue)
    {
        this.fatigue = fatigue;
    }


    public int getAge()
    {
        return age;
    }


    public void setAge(int age)
    {
        this.age = age;
    }


    public ALGameWorld getWorld()
    {
        return world;
    }
    
    
    public ALState getState()
    {   
        return state;
    }


    public void setState(ALState state)
    {
        this.state = state;
    }

    
    public boolean isDead()
    {
        return isDead;
    }
    
    public void setDead(boolean d)
    {
        isDead = d;
    }


    public double getWalkingSpeed()
    {
        return 3.0;
        //if (gridLocation == null) return 2;
        
        //ALTile t = world.getMapcell(gridLocation.row, gridLocation.column);
        //return (4 - t.getMovementCost()) * 1.5;
    }


    public void setGridLocation(int row, int column)
    {
        gridLocation = new MHMapCellAddress(row, column);
    }
    

    public MHMapCellAddress getGridLocation()
    {
        setGridLocation((int)((getY()+ALTile.TILE_SIZE/2) / ALTile.TILE_SIZE), (int)(getX()+ALTile.TILE_SIZE/2) / ALTile.TILE_SIZE);
        return gridLocation;
    }
    
    
    public static int getOldAgeDeaths()
    {
        return oldAgeDeaths;
    }
    
    
    public static int getStarvationDeaths()
    {
        return starvationDeaths;
    }


    public static int getDehydrationDeaths()
    {
        return dehydrationDeaths;
    }


    public static int getFatigueDeaths()
    {
        return fatigueDeaths;
    }

    
    public static int getLargestPopulation()
    {
        return largestPopulation;
    }
    
    
    public static void countLargestPopulation(int numJammies)
    {
        if (numJammies > largestPopulation)
            largestPopulation = numJammies;
    }

    public static void countOldAge()
    {
        oldAgeDeaths++;
    }
    
    
    public static void countStarvation()
    {
        starvationDeaths++;
    }
    
    
    public static void countDehydration()
    {
        dehydrationDeaths++;
    }
    
    
    public static void countFatigue()
    {
        fatigueDeaths++;
    }


    @Override
    public String toString()
    {
        String output = "Jammy " + id;
        output += ": H:" + getHunger();
        output += "  T:" + getThirst();
        output += "  F:" + getFatigue();
        output += "  A:" + getAge();
        
        return output;
    }

    
}



