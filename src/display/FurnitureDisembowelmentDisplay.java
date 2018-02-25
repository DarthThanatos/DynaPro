package display;

import contract.SlabTree;
import model.Furniture;
import model.Project;
import model.slab.Slab;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class FurnitureDisembowelmentDisplay extends JPanel {

    private static int PREFERRED_NAME_JTEXT_WIDTH = 50;

    public void displayFurnitureSlabs(Furniture furniture){
        init();
        System.out.println("Displaying furniture slabs");
        furniture.getTreeSlabList().forEach(slab -> System.out.println(slab.getName() + ", " + slab.getFirstDimension() + ", " + slab.getSecondDimension()));
        displayFurnitureTree(furniture, 0);
    }

    public void displayAllProjectSlabs(Project project){
        init();
        System.out.println("Displaying project slabs");
        project.getTreeSlabList().forEach(slab -> System.out.println(slab.getName() + ", " + slab.getFirstDimension() + ", " + slab.getSecondDimension()));
        int gridy = 0;
        for(SlabTree slabTree: project.getChildren().values()){
            displayFurnitureTree(slabTree, gridy);
            gridy += 2;
        }
    }


    private GridBagConstraints newFurnitureNameConstraints(int gridy){
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = gridy;
        gridBagConstraints.weightx = 1;
        return gridBagConstraints;
    }


    private GridBagConstraints newRowConstraints(int gridy){
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = gridy;
        return gridBagConstraints;
    }

    private GridBagConstraints newRowElementConstraints(int gridx, int gridy){
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = gridx;
        gridBagConstraints.gridy = gridy;
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        return gridBagConstraints;
    }


    private JPanel newSlabTreePanel(){
        JPanel slabTreePanel = new JPanel(new GridBagLayout());
        slabTreePanel.setBorder(new EtchedBorder());
        slabTreePanel.setBackground(Color.WHITE);
        return  slabTreePanel;
    }

    private void displayFurnitureTree(SlabTree furnitureSlabTree, int furniturePanelGridy){
        mountFurnitureNamePanel(furnitureSlabTree, 2*furniturePanelGridy);
        int gridy = 0;
        JPanel slabTreePanel = newSlabTreePanel();
        for(Map.Entry<Dimension, List<Slab>> slabtree: furnitureSlabTree.slabsGroupedBySize(furnitureSlabTree.getTreeSlabList()).entrySet()){
            mountSlabRow(slabtree.getKey(), slabtree.getValue(), slabTreePanel, gridy ++);
        }
        add(slabTreePanel, newRowConstraints(2*furniturePanelGridy + 1));
    }

    private void mountFurnitureNamePanel(SlabTree slabTree,  int gridy){
        add(new NoBorderJTextField(((Furniture) slabTree).getName()), newFurnitureNameConstraints( gridy));
    }

    private void mountSlabRow(Dimension dimension, List<Slab> slabs, JPanel slabTreePanel, int gridy) {
        displayConcatenationOfSlabsNames(slabs, slabTreePanel, gridy);
        slabTreePanel.add(new NoBorderJTextField("("), newRowElementConstraints(1, gridy));
        displayDimension(dimension, slabTreePanel, gridy);
        slabTreePanel.add(new NoBorderJTextField(")"), newRowElementConstraints(5, gridy));
        displayNumberOfSlabs(slabs, slabTreePanel, gridy);
        slabTreePanel.add(new ElementScaleboardDisplay(slabs.get(0).getScaleboard()), newRowElementConstraints(8, gridy));
    }

    private void init(){
        removeAll();
        setBackground(Color.white);
        getParent().repaint();
    }


    private void displayConcatenationOfSlabsNames(List<Slab> slabs, JPanel slabTreePanel, int gridy){
        slabs.stream().map(Slab::getName).reduce((acc, slab) -> acc + ", " + slab).ifPresent(s -> slabTreePanel.add(new NoBorderJTextField(s, PREFERRED_NAME_JTEXT_WIDTH), newRowElementConstraints(0, gridy)));
    }

    private String mmToCmText(int dimensionInMm){
        return (dimensionInMm / 10) + "," + (dimensionInMm % 10);
    }

    private void displayDimension(Dimension dimension, JPanel slabTreePanel, int gridy){
        slabTreePanel.add(new NoBorderJTextField(mmToCmText(dimension.width)), newRowElementConstraints(2, gridy));
        slabTreePanel.add(new NoBorderJTextField("x"), newRowElementConstraints(3, gridy));
        slabTreePanel.add(new NoBorderJTextField(mmToCmText(dimension.height)), newRowElementConstraints(4, gridy));
    }

    private void displayNumberOfSlabs(List<Slab> slabs, JPanel slabTreePanel, int gridy){
        slabTreePanel.add(new NoBorderJTextField("x"), newRowElementConstraints(6, gridy));
        slabTreePanel.add(new NoBorderJTextField(Integer.toString(slabs.size())), newRowElementConstraints(7, gridy));
    }

    class NoBorderJTextField extends JTextField{
        NoBorderJTextField(String text){
            super(text, text.length());
            setHorizontalAlignment(JTextField.CENTER);
        }
        NoBorderJTextField(String text, int columns){
            super(text, columns);
            setHorizontalAlignment(JTextField.CENTER);
        }
        @Override public void setBorder(Border border) { }
    }
}
