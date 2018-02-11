package display;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImagePanel extends JPanel implements ComponentListener{

    private JLabel picLabel;
    private BufferedImage image;
    private static final int BORDER_WIDTH = 3;

    public ImagePanel(String imagePath){
        try{
            image = ImageIO.read(new File("src/" + imagePath));
            addComponentListener(this);
            picLabel = new JLabel(new ImageIcon(image));
            add(picLabel);

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }



    private void resizeImage(){
        if(getWidth() == 0 || getHeight() == 0) return;
        picLabel .setIcon(new ImageIcon(image.getScaledInstance(getWidth() - BORDER_WIDTH, getHeight() - BORDER_WIDTH, 0)));
    }

    @Override
    public void componentResized(ComponentEvent e) {
        resizeImage();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

}
