package gamemain;

import gamemain.entities.Player;
import gamemain.game.Level.Level;
import gamemain.gfx.Colors;
import gamemain.gfx.Font;
import gamemain.gfx.Screen;
import gamemain.gfx.SpriteSheet;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;

/**
 *
 * @author Chuck Maple III
 */
public class GameMain extends Canvas implements Runnable
{
    
    public static final int WIDTH = 160;
    public static final int HEIGHT = WIDTH / 12 * 9;
    public static final int SCALE = 3;
    public static final String NAME = "Game";
    
    private JFrame frame;
   
    public boolean running = false;
    public int tickCount = 0;
    
    private BufferedImage image = new BufferedImage(WIDTH,HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private int[] colors = new int[216];
    
    private Screen screen;
    
    public inputHandler input;
    public Level level;
    
    public Player player;
    
    public GameMain()
    {
        setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        
        frame = new JFrame(NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(this, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
   
    public synchronized void start()
    {
        running = true;
        new Thread(this).start();
    }
    
    public synchronized void stop()
    {
        running = false;
    }
    
    @Override
    public void run() 
    {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / 60D;
        
        int frames = 0;
        int ticks = 0;
        
        long lastTimer = System.currentTimeMillis();
        double delta = 0;
        
        init();
        
        while(running)
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            
            boolean shouldRender = true;
            
            while(delta >= 1)
            {
                ticks++;
                tick();
                delta -= 1;
                shouldRender = true;
            }
            
            try {
                Thread.sleep(2);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            
            
            
            if(shouldRender)
            {
            frames++;
            render();
            }
            
            if(System.currentTimeMillis() - lastTimer > 1000)
            {
                lastTimer += 1000;
                System.out.println(ticks + "ticks " + frames + "Frames");
                frames = 0;
                ticks = 0;
                
            }
        }
    }
    
    public void init()                                          //populates colors
    {
        int index = 0;
        for(int r = 0; r < 6; r++)
        {
            for(int g = 0; g < 6; g++)
            {
                for(int b = 0; b < 6; b++)
                {
                    int rr = (r * 255 / 5);
                    int gg = (g * 255 / 5);
                    int bb = (b * 255 / 5);
                    
                    colors[index++] = rr << 16 | gg << 8 | bb;
                }
            }
        }
        
        screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));
        input = new inputHandler(this);
        level = new Level("/Levels/Test_Level.png");
        player = new Player(level, 0, 0, input);
        level.addEntity(player);
    }
    
    public void tick()
    {
        tickCount++;
        level.tick();
    }
    
    public void render()
    {
        BufferStrategy bs = getBufferStrategy();
        
        if(bs==null)
        {
            createBufferStrategy(3);
            return;
        }
        
        int xOffset = player.x - (screen.width / 2);
        int yOffset = player.y - (screen.height / 2);
        
        level.renderTiles(screen, xOffset, yOffset);
        
    //    for(int x = 0; x < level.width; x++)
    //    {
    //        int color = Colors.get(-1, -1, -1, 000);
    //        if(x % 10 == 0 & x != 0)
    //        {
    //            color = Colors.get(-1, -1, -1, 500);
    //       }
    //        Font.render((x % 10) + "", screen, 0 + (x * 8), 0, color);
    //    }
        
        level.renderEntities(screen);
        
        for(int y = 0; y < screen.height; y++)
        {
            for(int x = 0; x < screen.width; x++)
            {
                int colorCode = screen.pixels[x + y * screen.width];
                if(colorCode < 255) pixels[x + y * WIDTH] = colors[colorCode];
            }
        }
        
        Graphics g = bs.getDrawGraphics();
        g.drawRect(0,0,getWidth(), getHeight());
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }
    public static void main(String[] args)
    {
        new GameMain().start();
    }
}
