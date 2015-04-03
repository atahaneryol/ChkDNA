/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chkdna.view;

import java.awt.Cursor;
import java.io.File;

/**
 *
 * @author Anıl Anar<anilanar@outlook.com>
 */
public interface IDropZone {
    public void dropFiles(File[] files);
    public void setCursor(Cursor c);
}
