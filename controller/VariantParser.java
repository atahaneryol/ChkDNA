package chkdna.controller;

import chkdna.model.ParseResult;
import chkdna.model.ProgressLog;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

public class VariantParser extends SwingWorker<Void, Void>{
    
    private String vcfFilePath;

    public VariantParser(String vcfFilePath) {
        this.vcfFilePath = vcfFilePath;
    }

    @Override
    protected void done() {
        super.done();
        try {
            PhenotypeParser p = new PhenotypeParser(vcfFilePath);
            p.addPropertyChangeListener(getPropertyChangeSupport().getPropertyChangeListeners()[0]);
            p.execute();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VariantParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public ParseResult runParser() {
        try {
            String line;
            File file = new File("Util/snpEff.jar");
            File file2 = new File(this.vcfFilePath);
            File file3 = new File("Util/snpEff.config");
            /*ArrayList<String> str = new ArrayList<String>();
            str.add("java"); str.add("-Xms4000m"); str.add("-jar");
            str.add(file.getAbsolutePath());
            str.add("eff"); str.add("-v"); str.add("-c"); 
            str.add(file3.getAbsolutePath());
            str.add("-i"); str.add("vcf"); str.add("-o"); str.add("vcf"); str.add("GRCh37.69");
            str.add(file2.getAbsolutePath());
            System.out.println(str);*/
            
            ProcessBuilder pb;
            
             //pb.redirectOutput(ProcessBuilder.Redirect.to(new File("snpEffResult.txt")));
            File winBatch = new File(System.getProperty("user.dir") + "/Util/run-win.bat");
            File utilPath = new File(System.getProperty("user.dir") + "/Util");
            
            String OS = System.getProperty("os.name").toLowerCase();
            if (OS.indexOf("win") >= 0) {
                pb = new ProcessBuilder(winBatch.getAbsolutePath(),utilPath.getAbsolutePath(), file2.getAbsolutePath(), "output.txt");
                System.out.println(pb.command().toString());
                //pb.directory(workingDir);
            } else {
                pb = new ProcessBuilder("Util/run-bash", file2.getAbsolutePath(), "output.txt");
            }
            Process p = pb.start();
            
            
            
            
            
            //pb.redirectOutput(ProcessBuilder.Redirect.to(new File("snpEffResult.txt")));
            
            
            
            
            
            /*final BufferedReader processIn = new BufferedReader(new InputStreamReader(p.getInputStream()));
            Thread t = new Thread() {

                @Override
                public void run() {
                    File outFile = new File("snpEffResult.txt");
                    try {
                        PrintWriter w = new PrintWriter(outFile);
                        while(processIn.ready()) {
                            w.print(processIn.read());
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(VariantParser.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            };
            t.start();*/
            p.waitFor();
            //t.join();
            ProgressLog.add("snpEff finished!");
          
        } catch (Exception ex) {
            Logger.getLogger(VariantParser.class.getName()).log(Level.SEVERE, "snpEff failed.", ex);
        }
        return null;
    }

    @Override
    protected Void doInBackground() throws Exception {
        ProgressLog.add("Running snpEff...");
        runParser();
        return null;
    }
}
