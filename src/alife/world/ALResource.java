package alife.world;

import java.awt.Image;
import mhframework.tilemap.MHMapCellAddress;

public interface ALResource
{
    enum Type
    {
        PLANT,
        HUT
    }
    
    public Image getImage();
    public Type getType();
}
