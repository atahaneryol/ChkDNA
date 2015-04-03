/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chkdna.view;

import javax.swing.text.*;
import javax.swing.text.html.CSS;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
 
public class MyEditorKit extends HTMLEditorKit {

    ViewFactory factory=new MyViewFactory();
 
    @Override
    public ViewFactory getViewFactory() {
        return factory;
    }

  
    
 
    class MyViewFactory extends HTMLFactory {
        @Override
        public View create(Element elem) {
            AttributeSet attrs = elem.getAttributes();
            Object elementName = attrs.getAttribute(AbstractDocument.ElementNameAttribute);
            Object o = (elementName != null) ? null : attrs.getAttribute(StyleConstants.NameAttribute);
            if (o instanceof HTML.Tag) {
                HTML.Tag kind = (HTML.Tag) o;
                if (kind == HTML.Tag.HTML) {
                    return new HTMLBlockView(elem);
                }
                else if (kind == HTML.Tag.IMPLIED) {
                    String ws = (String) elem.getAttributes().getAttribute(CSS.Attribute.WHITE_SPACE);
                    if ((ws != null) && ws.equals("pre")) {
                        return super.create(elem);
                    }
                    return new HTMLParagraphView(elem);
                } else if ((kind == HTML.Tag.P) ||
                        (kind == HTML.Tag.H1) ||
                        (kind == HTML.Tag.H2) ||
                        (kind == HTML.Tag.H3) ||
                        (kind == HTML.Tag.H4) ||
                        (kind == HTML.Tag.H5) ||
                        (kind == HTML.Tag.H6) ||
                        (kind == HTML.Tag.DT)) {
                    // paragraph

//                    return new ParagraphView(elem);
                    return new HTMLParagraphView(elem);
                }
            }
            return super.create(elem);
        }
 
    }
 
}