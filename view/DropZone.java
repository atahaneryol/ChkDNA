/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chkdna.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Anıl Anar<anilanar@outlook.com>
 */
public class DropZone extends JPanel implements DropTargetListener {
    
    private enum DragState {
        ACCEPT,
        REJECT
    }
    
    private final static BasicStroke dashed1 = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, 
            new float[] {10.0f}, 5f);
    private final static BasicStroke dashed2 = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, 
            new float[] {5.0f}, 2.5f);
    
    private File file;
    private DragState state;
    private JTextField path;
    
    public DropZone(JTextField path) {
        this.path = path;
        DropTarget dt = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this, true);
        setLayout(new FlowLayout());
        //setBorder(new WindowsBorders.DashedBorder(Color.black));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setPaint(Color.gray);
        g2.setStroke(dashed1);
        g2.draw(new RoundRectangle2D.Double(10, 10, 165, 111, 10, 10));
        g2.setStroke(dashed2);
        g2.fill(new Rectangle2D.Double(82.5, 20, 20, 40));
        Path2D triangle = new Path2D.Double();
        triangle.moveTo(109.5, 60);
        triangle.lineTo(92.5, 82.5);
        triangle.lineTo(75.5, 60);
        
        g2.fill(triangle);
        g2.drawString("DROP VCF FILE", 45, 110);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(185, 132);
    }

    @Override
    public Dimension getMaximumSize() {
        return this.getPreferredSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return this.getPreferredSize();
    }
    
    public File getFile() {
        return file;
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
        
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        //dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
    }
    

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
        Transferable t = dtde.getTransferable();
        
        File f = null;
        
        try {
            f = ((List<File>)dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor)).get(0);
        } catch (Exception ex) {
            Logger.getLogger(DropZone.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(f.getName().endsWith(".vcf")) {
            path.setText(f.getAbsolutePath());
            dtde.dropComplete(true);
        } else {
            dtde.dropComplete(false);
        }
    }
}
