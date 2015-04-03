/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chkdna.model;

/**
 *
 * @author Anıl Anar<anilanar@outlook.com>
 */
public class VcfLine {
    
    private String[] cols;
    
    public VcfLine(String[] splitLine) {
        //this.cols = splitLine;
        this.cols = new String[5];
        for(int i = 0; i < 5; i++) {
            cols[i] = new String(splitLine[i]);
        }
        //System.arraycopy(splitLine, 0, cols, 0, 5);
    }
    
    public String getColumn(int index) {
        if(index < 1 || index > 5) return null;
        return cols[index-1];
    }
    
}
