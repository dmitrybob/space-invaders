import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Resources {
    // to add an image to the environment:
    // 1. put the file into the res folder.
    // 2. Declare a variable before the static block.
    // 3. Initialize the variable by copying and pasting and modifying the
    //    ImageIO line.


    public static BufferedImage test;
    public static BufferedImage projectile;
    public static BufferedImage enemy;

    static{
        try{
            test = ImageIO.read(new File("./res/yct20hubfk061.png"));
            projectile = ImageIO.read(new File("./res/874fa0e5bf7b854 (1).png"));
            enemy = ImageIO.read(new File("./res/NicePng_space-ship-png_176093.png"));
        }catch(Exception e){e.printStackTrace();}
    }
}
