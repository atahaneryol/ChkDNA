/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chkdna;

import chkdna.controller.DrugGeneParser;
import chkdna.controller.HaplogroupParser;
import chkdna.controller.Parser;
import chkdna.controller.PhenotypeParser;
import chkdna.controller.VariantParser;
import chkdna.model.ParseResult;
import chkdna.view.ParseProgress;
import chkdna.view.VCFPicker;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.w3c.dom.Document;
import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.gui.HtmlPanel;
import org.lobobrowser.html.parser.DocumentBuilderImpl;
import org.lobobrowser.html.parser.InputSourceImpl;
import org.lobobrowser.html.test.SimpleHtmlRendererContext;
import org.lobobrowser.html.test.SimpleUserAgentContext;
import org.xml.sax.SAXException;

/**
 *
 * @author Anıl Anar<anilanar@outlook.com>
 */
public class ChkDNA {
    
    private File vcfFile;
    private boolean runSnpEff;
    private Thread vcfPickerThread;
    private static ChkDNA instance;
    public static final String windowTitle = "ChkDNA Demo";
    
    public static void start() {
        instance = new ChkDNA();
        instance.init();
    }
    
    public static ChkDNA getInstance() {
        return instance;
    }
    
    public ChkDNA() {
        vcfFile = null;
    }
    
    private void init() {
        
        final Object vcfPickerLock = new Object();
        
        vcfPickerThread = new Thread(new Runnable() {

            @Override
            public void run() {
                VCFPicker vcfPicker = new VCFPicker(vcfPickerLock);
            }
        });
        vcfPickerThread.start();
        
        synchronized(vcfPickerLock) {
            try {
                vcfPickerLock.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ChkDNA.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        if(vcfFile != null) {
            ParseProgress pp = new ParseProgress(vcfFile, runSnpEff);
            pp.start();
        }
        else {
            
        }
}
    
    public void setVCFFile(File f) {
        vcfFile = f;
    }
    
    public void setRunSnpEff() {
        this.runSnpEff = true;
    }
    
    public boolean getRunSnpEff() {
        return this.runSnpEff;
    }
    
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        /*if (args.length == 0) {  
            try {  
                // re-launch the app itselft with VM option passed  
                //Runtime.getRuntime().exec(new String[] {"java", "-Xms4000m", "-jar", "ChkDna.jar","-test"});
                ProcessBuilder pb = new ProcessBuilder("java", "-Xms4000m","-Xmx4000m", "-jar", "ChkDna.jar","-test");
                Process p = pb.start();
                BufferedReader br = new BufferedReader( new InputStreamReader(p.getInputStream()));
                String line ="";
                line = br.readLine();
                while (line != null) {
                    System.out.println(line);
                    line = br.readLine();
                }
                p.waitFor();
                
                
                System.out.println("No args");
                System.out.println("Press Any Key To Continue...");
                //new java.util.Scanner(System.in).nextLine();
            } catch (IOException ioe) {  
                ioe.printStackTrace();  
            }  
            System.exit(0);  
        }*/ 
        ChkDNA.start();
//        HaploMain hm = new HaploMain("test.vcf");
//        hm.parse();
//        DrugGeneParser dgp = new DrugGeneParser("test.vcf");
//        ParseResult p = dgp.parse();
//        
//        
//        ArrayList<ArrayList<String>> a1 = p.getDrugGeneResultsPheno();
//        for(ArrayList<String> a2:a1) {
//            for(String s:a2) {
//                System.out.println(s);
//            }
//            System.out.println();
//        }
//        ArrayList<Parser> parsers = new ArrayList<>();
//        parsers.add(new DrugGeneParser("test.vcf"));
//        parsers.add(new PhenotypeParser("test.vcf"));
//        parsers.add(new HaplogroupParser("test.vcf"));
//        parsers.add(new VariantParser("test.vcf"));
        
        
//        for(Parser p: parsers) {
//            ParseResult pr = p.parse();
//            List<List<String>> r = pr.getResult();
//            if(r != null) {
//                for(List<String> l : r) {
//                    if(l != null) {
//                        for(String s : l) {
//                            System.out.print(s + " ");
//                        }
//                    }
//                    System.out.println();
//                }
//            }   
//        }
        
        
    }
    

    
}
