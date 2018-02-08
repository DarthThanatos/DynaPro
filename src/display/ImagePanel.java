package display;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImagePanel extends JPanel{


    public ImagePanel(String imagePath){
        try{
            BufferedImage image = ImageIO.read(new File("src/" + imagePath));
            JLabel picLabel = new JLabel(new ImageIcon(image.getScaledInstance(300, 175, 0)));
            add(picLabel);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
