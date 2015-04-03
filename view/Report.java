/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chkdna.view;

import chkdna.model.ParseResult;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Anıl Anar<anilanar@outlook.com>
 */
public class Report extends JScrollPane  {

    public Report(ParseResult pr) {
        super();
        JTable table = new JTable(new ReportTableModel(pr));
        super.setViewportView(table);
        table.setFillsViewportHeight(true);
    }
    
    private class ReportTableModel extends AbstractTableModel {
        
        private List<String> headers;
        private List<List<String>> visibleResult;

        public ReportTableModel(ParseResult parseResult) {
            this.headers = parseResult.getHeaders();
            this.visibleResult = parseResult.getVisibleResult();
        }

        @Override
        public String getColumnName(int column) {
            return headers.get(column);
        }        
        

        @Override
        public int getRowCount() {
            return visibleResult.size();
        }

        @Override
        public int getColumnCount() {
            return headers.size();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Object r = visibleResult.get(rowIndex).get(columnIndex);
            if(r != null)
                return visibleResult.get(rowIndex).get(columnIndex);
            else
                return "None";
        }
        
    }
    
}
