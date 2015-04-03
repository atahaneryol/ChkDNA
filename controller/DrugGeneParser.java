package chkdna.controller;

import chkdna.model.ParseResult;
import chkdna.model.ProgressLog;
import chkdna.view.Config;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DrugGeneParser extends Parser
{

    public DrugGeneParser(String vcfFile) throws FileNotFoundException
    {
        super(vcfFile);
        
    } 

    @Override
    protected ParseResult doInBackground() throws Exception {
        super.doInBackground();
        ProgressLog.add("Running Drug-Gene Parser...");
        ParseResult pr = runParser();
        setProgress(96);
        return pr;
    }

    @Override
    protected void done() {
        try {
            super.done();
            try {
                Parser.parseResults.add(get());
            } catch ( Exception ex) {
                Logger.getLogger(DrugGeneParser.class.getName()).log(Level.SEVERE, null, ex);
            }
            HaplogroupParser hp = new HaplogroupParser(super.getVcfFilePath());
            hp.addPropertyChangeListener(getPropertyChangeSupport().getPropertyChangeListeners()[0]);
            hp.execute();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DrugGeneParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    
    
    
    
    @Override
    protected ParseResult runParser()
    {
//        try {
//            //ArrayList<String> chroms = getChroms();
//            //Hashtable<String, Integer> valIds = getValidVariantIds(chroms);
//            readAll();
//        } catch (IOException ex) {
//            Logger.getLogger(DrugGeneParser.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        List<List<String>> comparedData = compareWithDrugs(Config.getPharmGKBPath());
        
        
        //We give the title to understand what this parse result object is connected with
        ParseResult res = new ParseResult("drug-gene");
        res.setResult(comparedData);

        return res;
    }

    public HashMap<String, Integer> getValidVariantIds(ArrayList<String> chroms)
    {        // returns valid variant ids (rs12344...) (checks not known ids (".") in vcf)

        HashMap<String, Integer> valIds = new HashMap<String, Integer>();
        ArrayList<String> specs;
        for (int i = 0; i < chroms.size(); i++)
        {
            specs = getChromSpecs(chroms, i);
            if (!(getVariantId(specs, i).equals(".")) && !valIds.containsKey(getVariantId(specs, i)))
            {
                valIds.put(getVariantId(specs, i), i);
            }
        }

        Integer n = valIds.get(".");
        if (n != null)
        {
            valIds.remove(".");
        }
        return valIds;
    }

    public List<List<String>> compareWithDrugs(String dbName)
    {
        List<List<String>> result = new ArrayList<List<String>>();
        List<List<String>> drugs = FileManager.readCsvFile(dbName);

        for(List<String> drug : drugs) {
            if (Parser.vcfContainsRsId(drug.get(2))) {
                result.add(drug);
            }
        }
        return result;
    }
}