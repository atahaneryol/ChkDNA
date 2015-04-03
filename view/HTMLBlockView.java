/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chkdna.view;

import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.html.BlockView;

public class HTMLBlockView extends BlockView {

    public HTMLBlockView(Element elem) {
        super(elem,  View.Y_AXIS);
    }
 
        @Override
    protected void layout(int width, int height) {
        if (width<Integer.MAX_VALUE) {
            super.layout(width, height);
        }
    }
}