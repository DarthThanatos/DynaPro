package display;

import javax.swing.*;
import java.awt.*;

public class Arrangement extends GridBagConstraints {

    private final GridBagLayout gridBagLayout;

    public Arrangement(GridBagLayout gridBagLayout){
        this.gridBagLayout = gridBagLayout;
    }

    public void putAbove(JComponent benchmarkComponent, JComponent componentToAdd){
        ((GridBagLayout)benchmarkComponent.getLayout()).getConstraints(benchmarkComponent);

    }


    public void putBelow(JComponent benchmarkComponent, JComponent componentToAdd){

    }

    public void putToRightOf(JComponent benchmarkComponent, JComponent componentToAdd){

    }

    public void putToLeftOf(JComponent benchmarkComponent, JComponent componentToAdd){

    }
}
