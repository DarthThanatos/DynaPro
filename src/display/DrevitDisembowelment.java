package display;

import contract.SlabTree;
import kotlin.Pair;
import model.Project;
import model.slab.Slab;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DrevitDisembowelment extends JPanel {

    private JTextField assessmentDisplay, scaleBoardLengthDisplay, cutLengthDisplay;

    public void displayAllProjectSlabs(Project project){
        init();
        displaySlabs(project);
        displayProjectSummary(project);
    }


    private void displaySlabs(Project project){
        int gridy = 0;
        for(Map.Entry<Pair<Dimension, ArrayList<Boolean>>, List<Slab>> slabtree: project.slabsGroupedBySizeAndScaleboard(project.getTreeSlabList()).entrySet()){
            JPanel slabTreePanel = newSlabTreePanel(true);
            mountSlabRow(slabtree.getKey().component1(), slabtree.getValue(), slabTreePanel);
            add(slabTreePanel, newRowConstraints(gridy++));
        }
        displayHdfs(project, gridy);
    }

    private void displayHdfs(Project project, int gridy) {
        List<Slab> hdfsList = project.getHdfsList();
        for (int i = 0; i < hdfsList.size(); i++){
            Slab backSlab = hdfsList.get(i);
            JPanel slabTreePanel = newSlabTreePanel(false);
            slabTreePanel.add(new NoBorderJTextField("HDF" + i, true), newRowElementConstraints(0));
            slabTreePanel.add(new NoBorderJTextField(" ("), newRowElementConstraints(1));
            displayDimension(new Dimension(backSlab.getFirstDimension(), backSlab.getSecondDimension()), slabTreePanel);
            slabTreePanel.add(new NoBorderJTextField(") "), newRowElementConstraints(5));
            add(slabTreePanel, newRowConstraints(gridy++));
        }
    }

    private void mountSlabRow(Dimension dimension, List<Slab> slabs, JPanel slabTreePanel) {
        slabTreePanel.add(new NoBorderJTextField(" ("), newRowElementConstraints(1));
        displayDimension(dimension, slabTreePanel);
        slabTreePanel.add(new NoBorderJTextField(") "), newRowElementConstraints(5));
        displayNumberOfSlabs(slabs, slabTreePanel);
        slabTreePanel.add(new ElementScaleboardDisplay(slabs.get(0).getScaleboard()), newRowElementConstraints(8));
    }


    private String mmToCmText(int dimensionInMm){
        return (dimensionInMm / 10) + "," + (dimensionInMm % 10);
    }

    private void displayDimension(Dimension dimension, JPanel slabTreePanel){
        slabTreePanel.add(new NoBorderJTextField(mmToCmText(dimension.width)), newRowElementConstraints(2));
        slabTreePanel.add(new NoBorderJTextField("x"), newRowElementConstraints(3));
        slabTreePanel.add(new NoBorderJTextField(mmToCmText(dimension.height)), newRowElementConstraints(4));
    }


    private void displayNumberOfSlabs(List<Slab> slabs, JPanel slabTreePanel){
        slabTreePanel.add(new NoBorderJTextField("x"), newRowElementConstraints(6));
        slabTreePanel.add(new NoBorderJTextField(Integer.toString(slabs.size())), newRowElementConstraints(7));
    }


    private GridBagConstraints newRowElementConstraints(int gridx){
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = gridx;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        return gridBagConstraints;
    }

    private GridBagConstraints newRowConstraints(int gridy){
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = gridy;
        return gridBagConstraints;
    }

    private JPanel newSlabTreePanel(boolean includeEtchedBorder){
        JPanel slabTreePanel = new JPanel(new GridBagLayout());
        if(includeEtchedBorder) slabTreePanel.setBorder(new EtchedBorder());
        slabTreePanel.setBackground(Color.WHITE);
        return  slabTreePanel;
    }

    private void init(){
        removeAll();
        setBackground(Color.white);
        getParent().repaint();
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


    public void setCutLengthDisplay(JTextField cutLengthDisplay) {
        this.cutLengthDisplay = cutLengthDisplay;
    }

    public void setScaleBoardLengthDisplay(JTextField scaleBoardLengthDisplay) {
        this.scaleBoardLengthDisplay = scaleBoardLengthDisplay;
    }

    public void setAssessmentDisplay(JTextField assessmentDisplay) {
        this.assessmentDisplay = assessmentDisplay;
    }


    public void printComponent(){
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setJobName(" Print Component ");
        pj.setPrintable ((pg, pf, pageNum) -> {
            double scaleFactor = 0.7;
            Component[] components = DrevitDisembowelment.this.getComponents();
            int blockHeight = (int) ((components[0].getHeight()) * scaleFactor);
            int blocksOnPage = (int) ((pf.getImageableHeight() - (int)pf.getImageableHeight()/10) / blockHeight);
            if((pageNum) * blocksOnPage > components.length){return Printable.NO_SUCH_PAGE;}
            Graphics2D g2 = (Graphics2D) pg;
            g2.scale( pf.getImageableWidth()/DrevitDisembowelment.this.getComponents()[0].getWidth(), scaleFactor);
            g2.translate(pf.getImageableX() + 2, pf.getImageableY() + 2);
            g2.drawRect(0,4, components[0].getWidth() - 10, (int)pf.getImageableHeight()/10);
            g2.translate(0, pf.getImageableHeight()/10 + 4);
            g2.scale( 0.4, 1);
            for (int i = pageNum * blocksOnPage; i < Math.min(pageNum * blocksOnPage  +  blocksOnPage, components.length); i++) {
                components[i].printAll(g2);
                g2.translate(0,components[i].getHeight());
            }
            return Printable.PAGE_EXISTS;
        });
        if (!pj.printDialog())
            return;

        try {
            pj.print();
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }
    }


    class NoBorderJTextField extends JTextField{
        NoBorderJTextField(String text){
            super(text, text.length() == 1 || text.length() == 2 ? text.length() : 6);
            setHorizontalAlignment(JTextField.CENTER);
        }

        NoBorderJTextField(String text, boolean redColor){
            this(text);
            if(redColor) setForeground(Color.red);
        }
        @Override public void setBorder(Border border) { }
    }
}
