package chkdna.model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Every different parser will provide a result set object according to their
 * own implementation. Example; the Drug Gene Parser may only support a 2D
 * String array to provide the results. Check for the specific parser
 * information for details. It is probably going to used as a singleton class.
 *
 */
public class ParseResult
{

    String type;
    
    //------------Every Parser will adds its own way of providing results.
    List<List<String>> result;
    
    
    //------------It is used as an encapsulating class.

    public ParseResult(String givenType)
    {
        type = givenType;
        result = null;
        
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public List<List<String>> getResult() { 
        return result;
    }
    
    public void setResult(List<List<String>> result) {
        this.result = result;
    } 
    
    public List<List<String>> getVisibleResult() {
        List<List<String>> r = new ArrayList<List<String>>();
            if(this.getType().equals("phenotype")) {
                for(List<String> row: this.getResult()) {
                    List<String> newRow = new ArrayList<String>();
                    newRow.add(row.get(1));
                    newRow.add(row.get(2));
                    newRow.add(row.get(3));
                    newRow.add(row.get(8));
                    newRow.add(row.get(9));
                    newRow.add(row.get(11));
                    r.add(newRow);
                }
                
            }
            else if(this.getType().equals("drug-gene")){
                for(List<String> row: this.getResult()) {
                    List<String> newRow = new ArrayList<String>();
                    newRow.add(row.get(2));
                    newRow.add(row.get(3));
                    newRow.add(row.get(4));
                    newRow.add(row.get(6));
                    newRow.add(row.get(7));
                    newRow.add(row.get(8));
                    newRow.add(row.get(9));
                    r.add(newRow);
                }
            }
            else if (this.getType().equals("haplogroup")) {
                
                if(this.getResult().get(0).size() > 0)
                {
                    String maxGroup = this.getResult().get(0).get(0);
                    int maxLength =  maxGroup.length();
                    //List<String> newRow = new ArrayList<String>();
                    //newRow.add(this.getResult().get(0).get(0));
                    for(int i = 1; i < this.getResult().get(0).size(); i++) {
                        
                        String thisGroup = this.getResult().get(0).get(i);
                        int newLength = thisGroup.length();
                        if(newLength > maxLength && thisGroup.contains(maxGroup)) {
                            maxLength = newLength;
                            maxGroup = thisGroup;
                        }
                    }

                    List<String> rssss2 = new ArrayList<String>();
                    rssss2.add(maxGroup);
                    r.add(rssss2);
                }
            }
            else {
            }
            
        return r;
    }
    
    public List<String> getHeaders() {
        List<String> r =  new ArrayList<String>();
        if(this.getType().equals("phenotype")) {
                r.add("Category");
                r.add("SNP ID");
                r.add("PMID");
                r.add("URL");
                r.add("Phenotype");
                r.add("Ethnicity");
            } else if(this.getType().equals("drug-gene")){
                r.add("SNP ID");
                r.add("Gene");
                r.add("Drug");
                r.add("Sec. Category");
                r.add("Significance");
                r.add("Notes");
                r.add("Effect");
            }
            else if (this.getType().equals("haplogroup")){
                r.add("Haplogroup");
            } else {
            
            }
        
        return r;
    }
    
    public void write(String filePath) {
        File f = new File(filePath);
        try {
            f.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(ParseResult.class.getName()).log(Level.SEVERE, "Cannot create a file to write results into.", ex);
            return;
        }
        try {
            PrintWriter out = new PrintWriter(f);
            for(String s : getHeaders()) {
                out.write(s);
                out.write("\t");
            }
            out.write("\n");
            
            if(getVisibleResult() != null){
            for(List<String> l : getVisibleResult()) {
                StringBuilder builder = new StringBuilder("");
                for(String s : l) {
                    builder.append(s);
                    builder.append("\t");
                }
                builder.append("\n");
                out.print(builder.toString());
            }
            out.flush();
            }
        } catch(IOException ex) {
            Logger.getLogger(ParseResult.class.getName()).log(Level.SEVERE, "Cannot write the parse result into a file", ex);
        }
    }

}