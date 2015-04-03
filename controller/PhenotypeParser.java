package chkdna.controller;

//Test for phenotype parser (GIT)
import chkdna.model.ParseResult;
import chkdna.model.ProgressLog;
import chkdna.view.Config;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

//Atahan Eryol
//Using SNP and GWAS, Wiki GWA
//Phenotype parser için hangi database kullanılacak emin olmak için mail at.
public class PhenotypeParser extends Parser
{

    public PhenotypeParser(String vcfFile) throws FileNotFoundException
    {
        super(vcfFile);
    }

    @Override
    protected ParseResult doInBackground() throws Exception {
        super.doInBackground();
        ProgressLog.add("Running Phenotype Parser...");
        ParseResult pr = runParser();
        setProgress(93);
        return pr;
    }

    @Override
    protected void done() {
        try {
            super.done();
            Parser.parseResults.add(get());
            DrugGeneParser dgp = new DrugGeneParser(super.getVcfFilePath());
            dgp.addPropertyChangeListener(getPropertyChangeSupport().getPropertyChangeListeners()[0]);
            dgp.execute();
        } catch (Exception ex) {
            Logger.getLogger(PhenotypeParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected ParseResult runParser()
    {
        List<List<String>> result = new ArrayList<List<String>>();
        List<List<String>> wikigwa = FileManager.readCsvFile(Config.getPhenotypeTablePath());
        
        for(List<String> l : wikigwa) {
            String snp = l.get(2);
            if(vcfContainsRsId(snp)) {
                result.add(l);
            }
        }
        
        ParseResult res = new ParseResult("phenotype");
        res.setResult(result);
        return res;
    }
}
