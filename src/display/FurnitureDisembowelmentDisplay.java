package display;

import contract.SlabTree;
import kotlin.Pair;
import model.Furniture;
import model.Project;
import model.slab.Slab;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.*;
import java.util.List;

public class FurnitureDisembowelmentDisplay extends JPanel {

    private static int PREFERRED_NAME_JTEXT_WIDTH = 50;

    private JTextField assessmentDisplay, scaleBoardLengthDisplay, cutLengthDisplay;

    private int currentNumberOfCommonBlocks = 0;

    public void displayFurnitureSlabs(Furniture furniture){
        init();
        displayFurnitureTree(furniture, 0);
        if(!furniture.getBackInserted()) displayHdf(furniture.getHdfsList().get(0), 2, 0);
        currentNumberOfCommonBlocks = 1;
        displayFurnitureSummary(furniture);
    }

    public void displayAllProjectSlabs(Project project){
        init();
        int gridy = 0;
        for(SlabTree slabTree: project.getChildren().values()){
            displayFurnitureTree(slabTree, gridy);
            gridy += 1;
        }
        currentNumberOfCommonBlocks = project.getChildren().size();
        displayHdfs(project, 2* gridy);
        displayProjectSummary(project);
    }


    private void displayHdfs(Project project, int gridy) {
        List<Slab> hdfsList = project.getHdfsList();
        for (int i = 0; i < hdfsList.size(); i++){
            Slab backSlab = hdfsList.get(i);
            displayHdf(backSlab, gridy++, i);
        }
    }

    private void displayHdf(Slab backSlab, int gridy, int i){
        JPanel slabTreePanel = newSlabTreePanel(false);
        slabTreePanel.add(new NoBorderJTextField("HDF" + i, true), newRowElementConstraints(0));
        slabTreePanel.add(new NoBorderJTextField(" ("), newRowElementConstraints(1));
        displayDimension(new Dimension(backSlab.getFirstDimension(), backSlab.getSecondDimension()), slabTreePanel);
        slabTreePanel.add(new NoBorderJTextField(") "), newRowElementConstraints(5));
        add(slabTreePanel, newRowConstraints(gridy));

    }

    private void displayDimension(Dimension dimension, JPanel slabTreePanel){
        slabTreePanel.add(new NoBorderJTextField(mmToCmText(dimension.width)), newRowElementConstraints(2));
        slabTreePanel.add(new NoBorderJTextField("x"), newRowElementConstraints(3));
        slabTreePanel.add(new NoBorderJTextField(mmToCmText(dimension.height)), newRowElementConstraints(4));
    }


    private GridBagConstraints newRowElementConstraints(int gridx){
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = gridx;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        return gridBagConstraints;
    }


    private void displayFurnitureSummary(SlabTree furnitureSlabTree){
        displayAssessment(furnitureSlabTree.getAssessment(((Furniture)furnitureSlabTree).getFrontUnitPrice(), ((Furniture)furnitureSlabTree).getElementUnitPrice(),furnitureSlabTree.getTreeSlabList()));
        displayCutLength(furnitureSlabTree.getCutLength(furnitureSlabTree.getTreeSlabList()));
        displayScaleBoardLength(furnitureSlabTree.getScaleBoardLength(furnitureSlabTree.getTreeSlabList()));
    }

    private void displayProjectSummary(SlabTree projectSlabTree){
        displayAssessment(((Project)projectSlabTree).getProjectAssessment());
        displayCutLength(projectSlabTree.getCutLength(projectSlabTree.getTreeSlabList()));
        displayScaleBoardLength(projectSlabTree.getScaleBoardLength(projectSlabTree.getTreeSlabList()));
    }

    private void displayAssessment(int assessment){
        assessmentDisplay.setText(String.valueOf(assessment));
    }

    private void displayScaleBoardLength(int scaleBoardLength){
        scaleBoardLengthDisplay.setText(String.valueOf(scaleBoardLength));
    }

    private void displayCutLength(int cutLength){
        cutLengthDisplay.setText(String.valueOf(cutLength));
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


    private JPanel newSlabTreePanel(boolean includeBorder){
        JPanel slabTreePanel = new JPanel(new GridBagLayout());
        if(includeBorder)slabTreePanel.setBorder(new EtchedBorder());
        slabTreePanel.setBackground(Color.WHITE);
        return  slabTreePanel;
    }

    private void displayFurnitureTree(SlabTree furnitureSlabTree, int furniturePanelGridy){
        mountFurnitureNamePanel(furnitureSlabTree, 2*furniturePanelGridy);
        int gridy = 0;
        JPanel slabTreePanel = newSlabTreePanel(true);
        for(Map.Entry<Pair<Dimension, ArrayList<Boolean>>, List<Slab>> slabtree: furnitureSlabTree.slabsGroupedBySizeAndScaleboard(furnitureSlabTree.getTreeSlabList()).entrySet()){
            mountSlabRow(slabtree.getKey().component1(), slabtree.getValue(), slabTreePanel, gridy ++);
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


    private int maxComponentHeight(){
        return Arrays.stream(getComponents()).map(Component::getHeight).max(Comparator.naturalOrder()).get();
    }

    public void printComponent() {
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setJobName(" Print Component ");
        pj.setPrintable((pg, pf, pageNum) -> {
            double scaleFactor = 0.7;
            Component[] components = FurnitureDisembowelmentDisplay.this.getComponents();
            int blockHeight = (int) ((components[0].getHeight() + maxComponentHeight()) * scaleFactor);
            int blocksOnPage = (int) ((pf.getImageableHeight() - (int) pf.getImageableHeight() / 10) / blockHeight);

            int hdfPanelHeight = (int) (components[2 * currentNumberOfCommonBlocks].getHeight() * scaleFactor);
            int hdfPanelsOnEmptyPageNumber = (int) ((pf.getImageableHeight() - (int) pf.getImageableHeight() / 10) / hdfPanelHeight);
            int spaceLeftOnBorderPage = (int) ((pf.getImageableHeight() - (int) pf.getImageableHeight() / 10 - (currentNumberOfCommonBlocks % blocksOnPage) * blockHeight));
            int hdfBlocksOnBorderPage = spaceLeftOnBorderPage / hdfPanelHeight;
            if (pageNum * blocksOnPage > currentNumberOfCommonBlocks && (hdfBlocksOnBorderPage + hdfPanelsOnEmptyPageNumber > components.length - 2 * currentNumberOfCommonBlocks)) {
                return Printable.NO_SUCH_PAGE;
            }

            Graphics2D g2 = (Graphics2D) pg;
            g2.scale(pf.getImageableWidth() / FurnitureDisembowelmentDisplay.this.getComponents()[1].getWidth(), scaleFactor);
            g2.translate(pf.getImageableX() + 2, pf.getImageableY() + 2);
            g2.drawRect(0, 4, components[1].getWidth() - 2, (int) pf.getImageableHeight() / 10);
            g2.translate(0, pf.getImageableHeight() / 10 + 4);

            for (int i = pageNum * blocksOnPage * 2; i < Math.min(pageNum * blocksOnPage * 2 + 2 * blocksOnPage, 2 * currentNumberOfCommonBlocks); i += 2) {
                g2.translate(components[i + 1].getWidth() / 2 - components[i].getWidth() / 2, 0);
                components[i].printAll(g2);
                g2.translate(-components[i + 1].getWidth() / 2 + components[i].getWidth() / 2, components[i].getHeight());
                components[i + 1].printAll(g2);
                g2.translate(0, components[i + 1].getHeight());
            }

            if ((pageNum + 1) * blocksOnPage > currentNumberOfCommonBlocks) {
                int partlyEmptyPageNum = currentNumberOfCommonBlocks / blocksOnPage;
                if (pageNum == partlyEmptyPageNum) {
                    for (int i = 2 * currentNumberOfCommonBlocks; i < Math.min(components.length, 2 * currentNumberOfCommonBlocks + hdfBlocksOnBorderPage); i++) {
                        components[i].printAll(g2);
                        g2.translate(0, components[i].getHeight());
                    }
                } else {
                    int startIndex = 2 * currentNumberOfCommonBlocks + hdfBlocksOnBorderPage + (pageNum - partlyEmptyPageNum) * hdfPanelsOnEmptyPageNumber;
                    for (int i = startIndex; i < Math.min(components.length, startIndex + hdfPanelsOnEmptyPageNumber); i++) {
                        components[i].printAll(g2);
                        g2.translate(0, components[i].getHeight());
                    }
                }
            }
            return Printable.PAGE_EXISTS;
        });
        if (!pj.printDialog()) {
            return;
        }

        try {
            pj.print();
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }
    }



    public void setCutLengthDisplay(JTextField cutLengthDisplay) {
        this.cutLengthDisplay = cutLengthDisplay;
    }

    public void setScaleBoardLengthDisplay(JTextField scaleBoardLengthDisplay) {
        this.scaleBoardLengthDisplay = scaleBoardLengthDisplay;
    }

    public void setAssessmentDisplay(JTextField assessmentDisplay) {
        this.assessmentDisplay = assessmentDisplay;
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
        NoBorderJTextField(String text, boolean redColor){
            this(text,  text.length() == 1 || text.length() == 2 ? text.length() : 6);
            if(redColor) setForeground(Color.red);
        }
        @Override public void setBorder(Border border) { }
    }

}
