package display;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ElementScaleboardDisplay extends JPanel {

    private ArrayList<Boolean> scaleBoardConfig;
    private static final int OFFSET_X = 10, OFFSET_Y=10;
    private static final int MARK_SIZE = 10;
    private static final int PREFERRED_WIDTH = 100, PREFERRED_HEIGHT = 50;

    ElementScaleboardDisplay(ArrayList<Boolean> scaleboardConfig){
        this.scaleBoardConfig = scaleboardConfig;
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT);
    }

    private void markScaleBoard(Graphics g, int x, int y){
        // x,y - center of the mark
        int x1,x2,y1,y2;
        int offset = (MARK_SIZE - 1)/2;
        x1 = x  - offset;
        x2 = x  + offset;
        y1 = y  - offset;
        y2 = y  + offset;
        g.drawLine(x1, y1, x2, y2);
        x1 = x  + offset;
        x2 = x  - offset;
        y1 = y  - offset;
        y2 = y  + offset;
        g.drawLine(x1, y1, x2, y2);
    }

    @Override
    protected void paintComponent(Graphics g){
        g.drawRect(OFFSET_X, OFFSET_Y, getWidth() - 2 * OFFSET_X, getHeight() - 2 * OFFSET_Y);
        if(scaleBoardConfig.get(0)) markScaleBoard(g, OFFSET_X + (getWidth() - 2 * OFFSET_X) / 2, OFFSET_Y);
        if(scaleBoardConfig.get(1)) markScaleBoard(g, getWidth()  - OFFSET_X, OFFSET_Y + (getHeight() - 2 * OFFSET_Y) / 2);
        if(scaleBoardConfig.get(2)) markScaleBoard(g, OFFSET_X + (getWidth() - 2 * OFFSET_X) / 2, getHeight() - OFFSET_Y);
        if(scaleBoardConfig.get(3)) markScaleBoard(g, OFFSET_X, OFFSET_Y + (getHeight() - 2 * OFFSET_Y) / 2);
    }
}

