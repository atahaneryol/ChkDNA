package chkdna.controller;

import chkdna.ChkDNA;
import chkdna.model.Haplogroup;
import chkdna.model.ParseResult;
import chkdna.model.ProgressLog;
import chkdna.model.VcfLine;
import chkdna.view.Config;
import chkdna.view.MyEditorKit;
import chkdna.view.Report;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import org.w3c.dom.Document;
import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.gui.HtmlPanel;
import org.lobobrowser.html.parser.DocumentBuilderImpl;
import org.lobobrowser.html.parser.InputSourceImpl;
import org.lobobrowser.html.test.SimpleHtmlRendererContext;
import org.lobobrowser.html.test.SimpleUserAgentContext;

public class HaplogroupParser extends Parser {

    public static String haploFilePath = Config.getChromosomeYPath();
    public static String mitFilePath = Config.getMitokondriPath();

    public HaplogroupParser(String vcfFilePath) throws FileNotFoundException {
        super(vcfFilePath);
    }

    @Override
    protected ParseResult doInBackground() throws Exception {
        super.doInBackground();
        ProgressLog.add("Running Haplogroup Parser...");
        ParseResult pr = runParser();
        setProgress(100);
        return pr;
    }

    @Override
    protected void done() {
        try {
            super.done();
            Parser.parseResults.add(get());
            Parser.clear();
            String OS = System.getProperty("os.name").toLowerCase();
            
            ProgressLog.add("Complete!");
            for(ParseResult p : Parser.parseResults) {
                if(p.getType().equals("phenotype") || p.getType().equals("drug-gene") || p.getType().equals("haplogroup"))
                {
                    String path = Config.getReportSavePath();
                    System.out.println(path + "\\" + p.getType() + ".txt");
                    if (OS.indexOf("win") >= 0) {
                        p.write(path + "\\" + p.getType() + ".txt");
                    } else {
                        p.write(path + "/" + p.getType() + ".txt");
                    }
                        
                }
                else
                {
                    p.write(p.getType() + ".txt");
                }
            }
            JFrame f = new JFrame("Report");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JTabbedPane tabs = new JTabbedPane();
            //if(ChkDNA.getInstance().getRunSnpEff()) {
//                final File snpEffHtml = new File("snpEff_summary.html");
//                final JEditorPane htmlPane = new JEditorPane();
//                htmlPane.setEditorKit(new MyEditorKit());
//                htmlPane.setEditable(false);
//                final JScrollPane htmlPaneWithScroll = new JScrollPane(htmlPane);
//                htmlPane.getDocument().putProperty("i18n", Boolean.TRUE);
//                
//                final int old=htmlPaneWithScroll.getVerticalScrollBarPolicy();
//                htmlPaneWithScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//                
//                SwingUtilities.invokeLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            htmlPane.setPage(snpEffHtml.toURI().toURL());
//                            htmlPaneWithScroll.setVerticalScrollBarPolicy(old);
//                        } catch (IOException ex) {
//                            Logger.getLogger(HaplogroupParser.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                });
                
                if(ChkDNA.getInstance().getRunSnpEff()) {
                    System.out.println("caaaaaaaaaaaaaaaaannn");
                    String haploSuperGroup = "None";
                    if(Parser.parseResults.get(2).getVisibleResult().size() > 0)
                        haploSuperGroup = Parser.parseResults.get(2).getVisibleResult().get(0).get(0);
 
                    HtmlPanel panel = new HtmlPanel();
                    UserAgentContext ucontext = new SimpleUserAgentContext();
                    SimpleHtmlRendererContext rcontext = new SimpleHtmlRendererContext(panel,ucontext);
                    DocumentBuilderImpl dbi = new DocumentBuilderImpl(ucontext, rcontext);
                    // A documentURI should be provided to resolve relative URIs.
                    Document document = null;
                    File ff = new File("snpEff_summary.html");
                    PrintWriter pw = new PrintWriter(new FileWriter(ff, true));
                    pw.write("<center><hr><b>Haplogroups found:</b><p>"+ haploSuperGroup +"</center>");
                    pw.close();
                    
                    FileReader documentReader;
                    try {
                        documentReader = new FileReader(ff);
                        document = dbi.parse(new InputSourceImpl(documentReader, ff.toURI().toString()));
                    } catch (Exception ex) {
                        Logger.getLogger(HaplogroupParser.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    panel.setDocument(document, rcontext);
                    tabs.addTab("SNPEff", panel);
                    
                    if (OS.indexOf("win") >= 0) {
                        Config.move("snpEff_summary.html", Config.getReportSavePath() + "\\snpEff_summary.html");
                    } else {
                        Config.move("snpEff_summary.html", Config.getReportSavePath() + "/snpEff_summary.html");
                    }
                } 
                
                
                tabs.addTab("Phenotypes", new Report(Parser.parseResults.get(0)));
                tabs.addTab("Drug-Gene", new Report(Parser.parseResults.get(1)));
                if(!ChkDNA.getInstance().getRunSnpEff())
                    tabs.add("Haplogroups", new Report(Parser.parseResults.get(2)));
            //}
            
            
            
            
            f.setContentPane(tabs);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setLocationByPlatform(true);
            f.setVisible(true);
        } catch (Exception  ex) {
            Logger.getLogger(HaplogroupParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    
    

    @Override
    protected ParseResult runParser() {
        System.out.println(haploFilePath);
        System.out.println(mitFilePath);
        ArrayList<Haplogroup> hgroups1 = new ArrayList<Haplogroup>();
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(HaplogroupParser.haploFilePath)));
            while (reader.ready()) {

                Haplogroup hgroup = new Haplogroup();
                String line = reader.readLine();
                
                try {
                    Scanner scanner = new Scanner(line);
                    while (scanner.hasNext()) {
                        hgroup.setName(scanner.next());
                        hgroup.setRef(scanner.next());
                        hgroup.setMut1(scanner.next());
                        hgroup.setMut2(scanner.next());
                    }
                } catch (Exception e) {
                    Logger.getLogger(HaplogroupParser.class.getName()).log(Level.SEVERE, "Could not read " + HaplogroupParser.haploFilePath, e);
                }
                hgroups1.add(hgroup);
            }
        } catch (Exception ex) {
            Logger.getLogger(HaplogroupParser.class.getName()).log(Level.SEVERE, "Could not read " + HaplogroupParser.haploFilePath, ex);
            return null;
        }
        
        ArrayList<Haplogroup> hgroups2 = new ArrayList<Haplogroup>();
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(HaplogroupParser.mitFilePath)));
            while(reader.ready()) {
                Haplogroup hgroup = new Haplogroup();
                String line = reader.readLine();
                
                try {
                    Scanner scanner = new Scanner(line);
                    hgroup.setName(scanner.next());
                    hgroup.setRef(scanner.next());
                    hgroup.setMut1(scanner.next());
                    hgroup.setMut2(scanner.next());
                } catch (Exception e) {
                    Logger.getLogger(HaplogroupParser.class.getName()).log(Level.SEVERE, "Could not read " + HaplogroupParser.mitFilePath, e);
                }
                hgroups2.add(hgroup);
            }
        } catch(IOException ex) {
            Logger.getLogger(HaplogroupParser.class.getName()).log(Level.SEVERE, "Could not read " + HaplogroupParser.mitFilePath, ex);
            return null;
        }
        
        ArrayList<String> codesFound = new ArrayList<String>();

        for (Haplogroup hgroup : hgroups1) {
            String hgroupSnp = hgroup.getRef();
            if (hgroupSnp.startsWith("rs")) {
                VcfLine vcfLine = Parser.getVcfLine(hgroupSnp);
                if(vcfLine != null) {
                    String chrAsUpperCase = vcfLine.getColumn(1).toUpperCase();
                    if ( (chrAsUpperCase.equals("Y") || chrAsUpperCase.equals("CHRY"))
                            && vcfLine.getColumn(4).equalsIgnoreCase(hgroup.getMut2())
                            && vcfLine.getColumn(5).equalsIgnoreCase(hgroup.getMut1())) {

                        codesFound.add(hgroup.getName());
                    } 
                }
            }
        }
        
        for (Haplogroup hgroup : hgroups2) {
            Integer hgroupPos = Integer.parseInt(hgroup.getRef());
            
            VcfLine vcfLine = Parser.getVcfLineByPos(hgroupPos);
            if(vcfLine != null) {
                String chrAsUpperCase = vcfLine.getColumn(1).toUpperCase();
                if ( (chrAsUpperCase.equals("M") || chrAsUpperCase.equals("CHRM"))
                        && vcfLine.getColumn(4).equalsIgnoreCase(hgroup.getMut2())
                        && vcfLine.getColumn(5).equalsIgnoreCase(hgroup.getMut1())) {

                    codesFound.add(hgroup.getName());
                } 
            }
            
        }

        ParseResult pr = new ParseResult("haplogroup");
        List<List<String>> result = new ArrayList<List<String>>();
        result.add(codesFound);
        pr.setResult(result);
        return pr;
    }
}
