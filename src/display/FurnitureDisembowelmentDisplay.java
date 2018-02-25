package display;

import model.Furniture;
import model.Project;
import model.slab.Slab;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FurnitureDisembowelmentDisplay extends JPanel {

    public void displayFurnitureSlabs(Furniture furniture){
        init();
        System.out.println("Displaing furniture slabs");
        furniture.getTreeSlabList().forEach(slab -> System.out.println(slab.getName() + ", " + slab.getFirstDimension() + ", " + slab.getSecondDimension()));
        furniture.slabsGroupedBySize(furniture.getTreeSlabList()).forEach(this::mountSlabRow);
    }

    public void displayAllProjectSlabs(Project project){
        init();
        System.out.println("Displaing project slabs");
        project.getTreeSlabList().forEach(slab -> System.out.println(slab.getName() + ", " + slab.getFirstDimension() + ", " + slab.getSecondDimension()));
        project.getChildren().values().forEach(
                slabTree -> slabTree.slabsGroupedBySize(slabTree.getTreeSlabList()).forEach(this::mountSlabRow)
        );

    }

    private void init(){
        removeAll();
        getParent().repaint();
    }

    private void mountSlabRow(Dimension dimension, List<Slab> slabs) {
        displayConcatenationOfSlabsNames(slabs);
        add(new JTextField("("));
        displayDimension(dimension);
        add(new JTextField(")"));
        displayNumberOfSlabs(slabs);
        add(new ElementScaleboardDisplay(slabs.get(0).getScaleboard()));
    }

    private void displayConcatenationOfSlabsNames(List<Slab> slabs){
        slabs.stream().map(Slab::getName).reduce((acc, slab) -> acc + ", " + slab).ifPresent(s -> add(new JTextField(s)));
    }

    private String mmToCmText(int dimensionInMm){
        return (dimensionInMm / 10) + "," + (dimensionInMm % 10);
    }

    private void displayDimension(Dimension dimension){
        add(new JTextField(mmToCmText(dimension.width)));
        add(new JTextField("x"));
        add(new JTextField(mmToCmText(dimension.height)));
    }

    private void displayNumberOfSlabs(List<Slab> slabs){
        add(new JTextField("x"));
        add(new JTextField(Integer.toString(slabs.size())));
    }
}
