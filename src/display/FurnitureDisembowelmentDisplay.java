package display;

import contract.SlabTree;
import model.Furniture;
import model.Project;
import model.slab.Slab;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class FurnitureDisembowelmentDisplay extends JPanel {

    private static int PREFERRED_JTEXT_WIDTH = 10;

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
        return gridBagConstraints;
    }


    private void displayFurnitureTree(SlabTree furnitureSlabTree, int furniturePanelGridy){
        mountFurnitureNamePanel(furnitureSlabTree, 2*furniturePanelGridy);
        JPanel slabTreePanel = new JPanel(new GridBagLayout());
        slabTreePanel.setBorder(new EtchedBorder());
        int gridy = 0;
        for(Map.Entry<Dimension, List<Slab>> slabtree: furnitureSlabTree.slabsGroupedBySize(furnitureSlabTree.getTreeSlabList()).entrySet()){
            mountSlabRow(slabtree.getKey(), slabtree.getValue(), slabTreePanel, gridy ++);
        }
        add(slabTreePanel, newRowConstraints(2*furniturePanelGridy + 1));
    }

    private void mountFurnitureNamePanel(SlabTree slabTree,  int gridy){
        add(new JTextField(((Furniture) slabTree).getName()), newFurnitureNameConstraints( gridy));
    }

    private void mountSlabRow(Dimension dimension, List<Slab> slabs, JPanel slabTreePanel, int gridy) {
        displayConcatenationOfSlabsNames(slabs, slabTreePanel, gridy);
        slabTreePanel.add(new JTextField("("), newRowElementConstraints(1, gridy));
        displayDimension(dimension, slabTreePanel, gridy);
        slabTreePanel.add(new JTextField(")"), newRowElementConstraints(5, gridy));
        displayNumberOfSlabs(slabs, slabTreePanel, gridy);
        slabTreePanel.add(new ElementScaleboardDisplay(slabs.get(0).getScaleboard()), newRowElementConstraints(8, gridy));
    }

    private void init(){
        removeAll();
        getParent().repaint();
    }


    private void displayConcatenationOfSlabsNames(List<Slab> slabs, JPanel slabTreePanel, int gridy){
        slabs.stream().map(Slab::getName).reduce((acc, slab) -> acc + ", " + slab).ifPresent(s -> slabTreePanel.add(new JTextField(s, PREFERRED_JTEXT_WIDTH), newRowElementConstraints(0, gridy)));
    }

    private String mmToCmText(int dimensionInMm){
        return (dimensionInMm / 10) + "," + (dimensionInMm % 10);
    }

    private void displayDimension(Dimension dimension, JPanel slabTreePanel, int gridy){
        slabTreePanel.add(new JTextField(mmToCmText(dimension.width), PREFERRED_JTEXT_WIDTH), newRowElementConstraints(2, gridy));
        slabTreePanel.add(new JTextField("x"), newRowElementConstraints(3, gridy));
        slabTreePanel.add(new JTextField(mmToCmText(dimension.height), PREFERRED_JTEXT_WIDTH), newRowElementConstraints(4, gridy));
    }

    private void displayNumberOfSlabs(List<Slab> slabs, JPanel slabTreePanel, int gridy){
        slabTreePanel.add(new JTextField("x"), newRowElementConstraints(6, gridy));
        slabTreePanel.add(new JTextField(Integer.toString(slabs.size()), PREFERRED_JTEXT_WIDTH), newRowElementConstraints(7, gridy));
    }
}
