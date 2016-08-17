/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tiles;

import gamemain.gfx.Screen;
import gamemain.game.Level.Level;
import gamemain.gfx.Colors;

/**
 *
 * @author ninjalo
 */
public abstract class Tile 
{
    public static final Tile[] tiles = new Tile[256];
    public static final Tile VOID = new BasicSolidTile(0, 0, 0,Colors.get(000, -1, -1, -1), 0xff000000);
    public static final Tile STONE = new BasicSolidTile(1, 1, 0,Colors.get(-1, 333, -1, -1), 0xff555555);
    public static final Tile GRASS = new BasicTile(2, 2, 0,Colors.get(-1, 131, -1, -1), 0xff00ff00);
    
    protected byte id;
    protected boolean solid;
    protected boolean emitter;
    private int levelColor;
    
    public Tile(int id, boolean isSolid, boolean isEmitter, int levelColor)
    {
        this.id = (byte) id;
        if(tiles[id] != null) throw new RuntimeException("Duplicate tileID on " + id);
        this.solid = isSolid;
        this.emitter = isEmitter;
        this.levelColor = levelColor;
        tiles[id] = this;
    }
    
    public byte getId()
    {
        return id;
    }
    
    public boolean isSolid()
    {
        return solid;
    }
    
    public boolean isEmitter()
    {
        return emitter;
    }
    
    public void render(Screen screen, Level level, int x, int y) 
    {
        
    }
    public int getLevelColor()
    {
        return levelColor;
    }
    
}
