/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chkdna.view;

import javax.swing.text.Element;
import javax.swing.text.FlowView;
import javax.swing.text.View;

public class HTMLParagraphView extends javax.swing.text.html.ParagraphView {

    public final static int MAX_VIEW_SIZE=100;
 
    public HTMLParagraphView(Element elem) {
        super(elem);
        strategy = new HTMLParagraphView.HTMLFlowStrategy();
    }
 
    public static class HTMLFlowStrategy extends FlowView.FlowStrategy {
        @Override
        protected View createView(FlowView fv, int startOffset, int spanLeft, int rowIndex) {
            View res=super.createView(fv, startOffset, spanLeft, rowIndex);
            if (res.getEndOffset()-res.getStartOffset()> MAX_VIEW_SIZE) {
                res = res.createFragment(startOffset, startOffset+ MAX_VIEW_SIZE);
            }
            return res;
        }

    }
    @Override
    public int getResizeWeight(int axis) {
        return 0;
    }
 
}