package display;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageButton extends JButton  {


    private BufferedImage image;
    private static final int BORDER_WIDTH = 3;

    ImageButton(String imagePath){
        try{
            image = ImageIO.read(new File("src/" + imagePath));

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unused")
    public ImageButton(String imagePath, MouseListener mouseListener){
        this(imagePath);
        addMouseListener(mouseListener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image.getScaledInstance(getWidth() - BORDER_WIDTH, getHeight() - BORDER_WIDTH, 0),0,0, null);
    }

}
