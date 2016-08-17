package Tiles;
import gamemain.gfx.Screen;
import gamemain.game.Level.Level;
/**
 *
 * @author Chuck Maple III
 */
public class BasicTile extends Tile
{
    protected int tileId;
    protected int tileColor;

    public BasicTile(int id, int x, int y, int tileColor, int levelColor) 
    {
        super(id, false, false, levelColor);
        this.tileId = x + y;
        this.tileColor = tileColor;
    }
    
    @Override
    public void render(Screen screen,Level level, int x, int y)
   {
      screen.render(x, y, tileId, tileColor, 0x00, 1);
   }
}
