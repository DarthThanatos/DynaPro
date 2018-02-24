package display;

import model.Furniture;
import model.Project;

import javax.swing.*;

public class FurnitureDisembowelmentDisplay extends JPanel {

    public void displayFurnitureSlabs(Furniture furniture){
        System.out.println("Displaing furniture slabs");
        furniture.getTreeSlabList().forEach(slab -> System.out.println(slab.getName() + ", " + slab.getFirstDimension() + ", " + slab.getSecondDimension()));
    }

    public void displayAllProjectSlabs(Project project){
        System.out.println("Displaing project slabs");
        project.getTreeSlabList().forEach(slab -> System.out.println(slab.getName() + ", " + slab.getFirstDimension() + ", " + slab.getSecondDimension()));
    }
}
