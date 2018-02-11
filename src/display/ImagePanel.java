package display;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImagePanel extends JPanel {

    private BufferedImage image;
    private static final int BORDER_WIDTH = 3;

    public ImagePanel(String imagePath){
        try{
            image = ImageIO.read(new File("src/" + imagePath));

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image.getScaledInstance(getWidth() - BORDER_WIDTH, getHeight() - BORDER_WIDTH, 0),0,0, null);
    }
}
