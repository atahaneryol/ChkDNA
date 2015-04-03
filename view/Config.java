/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chkdna.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 *
 * @author Atahan
 */
public class Config
{
    static String reportSavePath = "Report";
    static String snpeffPath;
    static String phenotypeTablePath = "data/wikigwa.csv";
    static String pharmGKBPath = "data/druggene.csv";
    static String mitokondriPath = "data/HR/chrm.txt";
    static String chromosomeYPath = "data/HR/hap_snp_mut.txt";
    

    public static String getReportSavePath()
    {
        return reportSavePath;
    }

    public static void setReportSavePath(String reportSavePatht)
    {
        reportSavePath = reportSavePatht;
    }

    public static String getSnpeffPath()
    {
        return snpeffPath;
    }

    public static void setSnpeffPath(String snpeffPatht)
    {
        snpeffPath = snpeffPatht;
    }

    public static String getPhenotypeTablePath()
    {
        return phenotypeTablePath;
    }

    public static void setPhenotypeTablePath(String phenotypeTablePatht)
    {
        phenotypeTablePath = phenotypeTablePatht;
    }

    public static String getPharmGKBPath()
    {
        return pharmGKBPath;
    }

    public static void setPharmGKBPath(String pharmGKBPatht)
    {
        pharmGKBPath = pharmGKBPatht;
    }

    public static String getMitokondriPath()
    {
        return mitokondriPath;
    }

    public static void setMitokondriPath(String mitokondriPatht)
    {
        mitokondriPath = mitokondriPatht;
    }

    public static String getChromosomeYPath()
    {
        return chromosomeYPath;
    }

    public static void setChromosomeYPath(String chromosomeYPatht)
    {
        chromosomeYPath = chromosomeYPatht;
    }
    //-----------------------------------------------------------------------------------------
    public void duringInteration()
    {
        HashMap<String,Integer> phenotypes = new HashMap<String,Integer>();
        String line = "test";
        // OR
        // Something like ===>  String phentoypeName= x.get(7?);
        if(phenotypes.containsKey(line))
        {
            Integer n = phenotypes.get(line);
            n = n+1;
            phenotypes.put(line, n);
        }
        else
        {
            phenotypes.put(line,1);
        }
        
        //During printing summary
        Iterator< Entry<String,Integer> >  it = phenotypes.entrySet().iterator();
        while(it.hasNext())
        {
            Entry<String,Integer> temp = it.next();
            int val = temp.getValue();
            if(val > 5)
            {
                //Include this type of phenotype in the general summary
                String inc = temp.getKey();
                
            }
            
        }
    }
    
    public static void move(String source, String dest)
    {
        InputStream inStream = null;
        OutputStream outStream = null;
 
    	try{
 
    	    File afile =new File(source);
    	    File bfile =new File(dest);
 
    	    inStream = new FileInputStream(afile);
    	    outStream = new FileOutputStream(bfile);
 
    	    byte[] buffer = new byte[1024];
 
    	    int length;
    	    //copy the file content in bytes 
    	    while ((length = inStream.read(buffer)) > 0){
    	    	outStream.write(buffer, 0, length);
    	    }
 
    	    inStream.close();
    	    outStream.close();
 
    	    //delete the original file
    	    //afile.delete();
 
    	    System.out.println("File is copied successful!");
 
    	}catch(IOException e){
    	    e.printStackTrace();
    	}
    
    }
}
