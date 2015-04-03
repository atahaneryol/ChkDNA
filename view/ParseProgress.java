/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chkdna.view;

import chkdna.ChkDNA;
import chkdna.controller.PhenotypeParser;
import chkdna.controller.VariantParser;
import chkdna.model.ProgressLog;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

/**
 *
 * @author Anıl Anar<anilanar@outlook.com>
 */
public class ParseProgress extends JFrame implements PropertyChangeListener, Observer {
    
    private JTextArea logArea;
    private JProgressBar progressBar;
    private File vcfFile;
    private boolean runSnpEff;
    
    public ParseProgress(File vcfFile, boolean runSnpEff) {
        super(ChkDNA.windowTitle + " Parse Progress");
        super.setSize(new Dimension(300, 300));
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.vcfFile = vcfFile;
        this.runSnpEff = runSnpEff;
        
        progressBar = new JProgressBar(0,100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        logArea = new JTextArea();
        logArea.setEditable(false);
        
        setLocationRelativeTo(null);
        setLocationByPlatform(true);
        setResizable(false);
        
        getContentPane().setLayout(new BorderLayout());
        add(progressBar, BorderLayout.PAGE_START);
        add(logArea, BorderLayout.CENTER);
        
        super.setVisible(true);
    }
    
    public void start() { 
        ProgressLog.observe(this);
        if(this.runSnpEff) {
            VariantParser p = new VariantParser(vcfFile.getPath());
            p.addPropertyChangeListener(this);
            p.execute();
        } else {
            PhenotypeParser p = null;
            try {
                p = new PhenotypeParser(vcfFile.getPath());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ParseProgress.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(p != null) {
                p.addPropertyChangeListener(this);
                p.execute();
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if("progress".equals(evt.getPropertyName())) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setIndeterminate(false);
            progressBar.setValue(progress);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        logArea.setText(arg.toString());
    }
}
