/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chkdna.view;

import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 *
 * @author Anıl Anar<anilanar@outlook.com>
 */
public class DragNDropHandler extends TransferHandler {
    private static final DataFlavor FILE_FLAVOR = DataFlavor.javaFileListFlavor;
    
    private IDropZone dropZone;
    
    public DragNDropHandler(IDropZone dz) {
        this.dropZone = dz;
    }

    @Override
    public boolean importData(JComponent comp, Transferable t) {
        if(canImport(comp, t.getTransferDataFlavors())) {
            if(canImport(comp, t.getTransferDataFlavors())) {
                try {
                    List<File> fileList = (List<File>) t.getTransferData(FILE_FLAVOR);
                    if(fileList != null && fileList.toArray() instanceof File[]) {
                        File[] files = (File[]) fileList.toArray();
                        dropZone.dropFiles(files);
                    }
                    return true;
                } catch(Exception ex) {
                    Logger.getLogger("DragNDropHandler").log(Level.INFO, "Flavors did not match.");
                }
            }
        }
        return false;
    }
    

    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }
    
    
    
    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        dropZone.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
    
    
    
    @Override
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        boolean result = false;
        for(DataFlavor f: flavors) {
            if(f.equals(FILE_FLAVOR)) result = true;
        }
        
        return result;
    }
}
