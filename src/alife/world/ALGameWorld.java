package alife.world;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import mhframework.MHDisplayModeChooser;
import mhframework.MHPoint;
import mhframework.MHRandom;
import mhframework.tilemap.MHMapCellAddress;
import alife.world.jammies.ALJammy;


public class ALGameWorld
{
    public static final int INITIAL_POPULATION = 10;
    public static final int MAP_WIDTH = 25;
    public static final int MAP_HEIGHT = 25;
    public static final int SCROLL_SPEED = 10;
    public static final int WORLD_WIDTH = MAP_WIDTH * ALTile.TILE_SIZE;
    public static final int WORLD_HEIGHT = MAP_HEIGHT * ALTile.TILE_SIZE;

    private static final Rectangle2D screenSpace = new Rectangle2D.Double(0, 0, MHDisplayModeChooser.getWidth(), MHDisplayModeChooser.getHeight());
    private static final Rectangle2D worldSpace = new Rectangle2D.Double(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
    
    private ArrayList<ALJammy> jammies;
    private ArrayList<ALJammy> newbies;
    private ArrayList<ALPlant> plants;
    private ArrayList<ALHut> huts;
    private ArrayList<ALWaterTile> water;
    private ALTile[][] map;
    private int plantReproductionTimer = 0;
    
    // Resource limits.
    public int totalResources = 10;
    public int spentResources = 0;
    public int resourcePointTimer = 0;
    
    
    public ALGameWorld()
    {
        jammies = new ArrayList<ALJammy>();
        newbies = new ArrayList<ALJammy>();
        plants = new ArrayList<ALPlant>();
        huts = new ArrayList<ALHut>();
        water = new ArrayList<ALWaterTile>();

        generateMap();
        initializePopulation();
    }
    
    
    public void generateMap()
    {
        map = new ALTile[MAP_WIDTH][MAP_HEIGHT];
        
        for (int r = 0; r < map.length; r++)
        {
            for (int c = 0; c < map[r].length; c++)
            {
                // Decide what terrain to generate.
                int t = MHRandom.rollD6();
                if (t <= 5)
                    map[r][c] = new ALGrassTile();
                else
                {
                    t = MHRandom.rollD10();
                    if (t <= 3)
                    {
                        ALWaterTile w = new ALWaterTile(); 
                        map[r][c] = w;
                        w.setGridLocation(r, c);
                        water.add(w);
                    }
                    else if (t < 7)
                    {
                        ALRockTile rt = new ALRockTile();
                        map[r][c] = rt;
                    }
                    else
                        map[r][c] = new ALSandTile();
                }
            }
        }

        for (int p = 0; p < 20; p++)
            placeRandomPlant();
        
        boolean placed = false;
        ALHut h = new ALHut();
        while (!placed)
        {
            int col = MHRandom.random(1, ALGameWorld.MAP_WIDTH-2);
            int row = MHRandom.random(1, ALGameWorld.MAP_HEIGHT-2);
            if (supportsHut(row, col))
            {
                h.setGridLocation(row, col);
                addHut(h);
                placed = true;
            }
        }

    }
    
    
    private void placeRandomPlant()
    {
        boolean placed = false;
        ALPlant p = new ALPlant();
        int attempts = 0;
        while (!placed && attempts < 20)
        {
            int col = MHRandom.random(1, ALGameWorld.MAP_WIDTH-2);
            int row = MHRandom.random(1, ALGameWorld.MAP_HEIGHT-2);
            if (supportsPlant(row, col))
            {
                p.setGridLocation(row, col);
                addPlant(p);
                placed = true;
            }
            attempts++;
        }
    }
    
    private void initializePopulation()
    {
        for (int i = 0; i < INITIAL_POPULATION; i++)
            createJammy();
    }
    
    
    public void advance()
    {
        removeDeadPlants();
        removeDeadJammies();
        
        // Check for plant reproduction conditions.
        if (++plantReproductionTimer > 10)
        {
            reproducePlants();
            plantReproductionTimer = 0;
        }
        
        synchronized(jammies)
        {
            for (ALJammy jammy : jammies)
                jammy.advance();
            
            // Welcome the newborn jammies!
            for (ALJammy jammy : newbies)
                jammies.add(jammy);
            
            newbies.clear();
        }
        
        if (resourcePointTimer % 2 == 0)
        {
            synchronized(water)
            {
                for (ALWaterTile w : water)
                    w.setWaterLevel(1+w.getWaterLevel()*1.001);
            }
        }
        
        // Update resource points.
        if (++resourcePointTimer > 500)
        {
            totalResources += 1; //(int)(jammies.size() * 0.2);
            resourcePointTimer = 0;
        }
    }

    
    public int getResourcePoints()
    {
        return totalResources - spentResources;
    }
    
    
    private void reproducePlants()
    {
        int i = 0;
        while (i < plants.size())
        {
            ALPlant p = plants.get(i);
            p.setAge(p.getAge()+1);
            // Is this plant old enough?
            if (p.getAge() >= ALPlant.REPRO_MATURITY)
            {
                // Is there another plant nearby?
                for (ALPlant other : plants)
                {
                    int rDiff = Math.abs(other.getGridLocation().row - p.getGridLocation().row);
                    int cDiff = Math.abs(other.getGridLocation().column - p.getGridLocation().column);
                    if (rDiff + cDiff <= ALPlant.REPRO_RANGE)
                    {
                        placeRandomPlant();
                        p.setAge(0);
                        other.setAge(0);
                        break;
                    }
                }
            }
            i++;
        }
    }
    
    private void removeDeadJammies()
    {
        for (int i = 0; i < jammies.size(); i++)
        {
            while (jammies.size() > i && jammies.get(i).isDead())
                jammies.remove(i);
        }
    }
    
    
    private void removeDeadPlants()
    {
        for (int i = 0; i < plants.size(); i++)
        {
            while (plants.size() > i && plants.get(i).getFoodPoints() <= 0)
                plants.remove(i);
        }
    }
    
    
    public void scrollRelative(int horizontal, int vertical)
    {
        double x = screenSpace.getX() + horizontal;
        double y = screenSpace.getY() + vertical;
        
        if (x < 0)
            x = 0;
        else if (x > WORLD_WIDTH - screenSpace.getWidth())
            x = WORLD_WIDTH - screenSpace.getWidth();
        
        if (y < 0)
            y = 0;
        else if (y > WORLD_HEIGHT - screenSpace.getHeight())
            y = WORLD_HEIGHT - screenSpace.getHeight();
        
        screenSpace.setRect(x, y, screenSpace.getWidth(), screenSpace.getHeight());
    }
    
    
    
    public void scroll(double x, double y)
    {
        //x += screenSpace.getX();
        //y += screenSpace.getY();
        
        if (x < 0)
            x = 0;
        else if (x > WORLD_WIDTH - screenSpace.getWidth())
            x = WORLD_WIDTH - screenSpace.getWidth();
        
        if (y < 0)
            y = 0;
        else if (y > WORLD_HEIGHT - screenSpace.getHeight())
            y = WORLD_HEIGHT - screenSpace.getHeight();
        
        screenSpace.setRect(x, y, screenSpace.getWidth(), screenSpace.getHeight());
    }
    
    
    public void render(Graphics2D g)
    {
        for (int r = 0; r < map.length; r++)
        {
            for (int c = 0; c < map[r].length; c++)
            {
                map[r][c].render(g, (int)(c*ALTile.TILE_SIZE-screenSpace.getX()), (int)(r*ALTile.TILE_SIZE-screenSpace.getY()));
            }
        }
        
        synchronized(plants)
        {
        for (ALPlant p : plants)
        {
            int x = (int)(p.getGridLocation().column*ALTile.TILE_SIZE-screenSpace.getX());
            int y = (int)(p.getGridLocation().row   *ALTile.TILE_SIZE-screenSpace.getY());
            p.render(g, x, y);
        }
        }

        synchronized(huts)
        {
        for (ALHut h : huts)
        {
            int x = (int)(h.getGridLocation().column*ALTile.TILE_SIZE-screenSpace.getX());
            int y = (int)(h.getGridLocation().row   *ALTile.TILE_SIZE-screenSpace.getY());
            h.render(g, x, y);
        }
        }
        
        for (ALJammy j : jammies)
        {
            //int x = (int)(j.getGridLocation().column*ALTile.TILE_SIZE-screenSpace.getX());
            //int y = (int)(j.getGridLocation().row   *ALTile.TILE_SIZE-screenSpace.getY());
            j.render(g);//, x, y);
        }
    }

    
    public boolean supportsPlant(int row, int col)
    {
        return (map[row][col].supportsPlant() && !hutFound(row, col) && !plantFound(row, col));
    }


    public boolean supportsHut(int row, int col)
    {
        return (map[row][col].supportsHut() && !hutFound(row, col) && !plantFound(row, col));
    }

    
    private boolean hutFound(int row, int col)
    {
        for (ALHut h : huts)
            if (h.getGridLocation().row == row && h.getGridLocation().column == col)
                return true;
        
        return false;
    }
    

    private boolean plantFound(int row, int col)
    {
        for (ALPlant p : plants)
            if (p.getGridLocation().row == row && p.getGridLocation().column == col)
                return true;
        
        return false;
    }
    

    public static int getScreenAnchorX()
    {
        return (int)screenSpace.getX();
    }

    
    public static int getScreenAnchorY()
    {
        return (int)screenSpace.getY();
    }


    public void addPlant(ALPlant plant)
    {
        synchronized(plants)
        {
            plants.add(plant);
        }
    }
    
    public void addHut(ALHut hut)
    {
        synchronized(huts)
        {
            huts.add(hut);
        }
    }


    public ALTile getMapcell(int row, int col)
    {
        if (row < 0)
            row = 0;
        else if (row >= map.length)
            row = map.length-1;
        
        if (col < 0)
            col = 0;
        else if (col >= map[row].length)
            col = map[row].length-1;
        
        return map[row][col];
    }


    public MHPoint findNearestHut(MHPoint start)
    {
        ALHut nearestHut = null;
        double nearestDistance = Double.MAX_VALUE;
        MHPoint hutLocation;
        for (ALHut h : huts)
        {
            if (h.getOccupancy() == ALHut.MAX_OCCUPANCY)
                continue;
            
            hutLocation = h.getWorldLocation();
            double dist = start.distanceTo(hutLocation);
            if (dist < nearestDistance)
            {
                nearestDistance = dist;
                nearestHut = h;
            }
        }
        
        return nearestHut.getWorldLocation();
    }

    
    public int countWaterTiles()
    {
        int count = 0;
        
        synchronized(water)
        {
            for (ALWaterTile w : water)
            {
                if (w.getWaterLevel() > 0.001)
                    count++;
            }
        }
        
        return count;
    }
    
    
    public int countPlants()
    {
        return plants.size();
    }
    
    
    public int countHuts()
    {
        int count = 0;
        
        synchronized(huts)
        {
            for (ALHut h : huts)
            {
                if (h.getOccupancy() < ALHut.MAX_OCCUPANCY)
                    count++;
            }
        }
        
        return count;
    }
    
        
    public MHPoint findNearestWater(MHPoint start)
    {
        ALWaterTile nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        MHPoint location;
        for (ALWaterTile t : water)
        {
            if ((double)t.getWaterLevel()/ALWaterTile.FULL < 0.05)
                continue;
            
            location = t.getWorldLocation();
            double dist = start.distanceTo(location);
            if (dist < nearestDistance)
            {
                nearestDistance = dist;
                nearest = t;
            }
        }
        
        if (nearest != null)
            return nearest.getWorldLocation();
        
        return null;
    }


    public MHPoint findNearestPlant(MHPoint start)
    {   
        ALPlant nearest = plants.get(0);
        double nearestDistance = Double.MAX_VALUE;
        MHPoint location;
        synchronized(plants)
        {
        for (ALPlant t : plants)
        {
            location = t.getWorldLocation();
            double dist = start.distanceTo(location);
            if (dist < nearestDistance && t.getFoodPoints() > 0)
            {
                nearestDistance = dist;
                nearest = t;
            }
        }
        }
//        MHPoint center = 
//            new MHPoint(nearest.getWorldLocation().getX() + ALTile.TILE_SIZE/2,
//                        nearest.getWorldLocation().getY() + ALTile.TILE_SIZE/2);
        
        return nearest.getWorldLocation();
    }


    public ALHut getHut(MHMapCellAddress addr)
    {
        synchronized(huts)
        {
            for (ALHut h : huts)
                if (h.getGridLocation().equals(addr))
                    return h;
        }
        return null;
    }


    public ALPlant getPlant(MHMapCellAddress addr)
    {
        synchronized(plants)
        {
            for (ALPlant p : plants)
                if (p.getGridLocation().equals(addr))
                    return p;
        }
        return null;
    }


    public void createJammy()
    {
        jammies.add(new ALJammy(this));
        ALJammy.countLargestPopulation(jammies.size());
    }

    
    public void createJammy(MHPoint spawnPoint)
    {
        newbies.add(new ALJammy(this, spawnPoint));
        ALJammy.countLargestPopulation(jammies.size() + newbies.size());
    }


    public int countJammies()
    {
        int count = 0;
        
        for (ALJammy j : jammies)
        {
            if (j.getState() != j.dead && j.getState() != j.dying)
                count++;
        }
        
        return count;
    }
}
