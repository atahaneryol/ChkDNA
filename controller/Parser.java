package chkdna.controller;

import chkdna.model.ParseResult;
import chkdna.model.ProgressLog;
import chkdna.model.VcfLine;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

public abstract class Parser extends SwingWorker<ParseResult, Void>
{
    private String vcfFilePath;
    private static Map<String, VcfLine> vcfInMem;
    private static Map<Integer, VcfLine> vcfInMemByLoc;
    private static BufferedReader reader;
    private static long fileSize;
    private static long totalRead;
    private static final Object lock = new Object();
    private static boolean needToRead;
    private static final StringBuilder sb = new StringBuilder(8000);
    protected static final List<ParseResult> parseResults = new ArrayList<ParseResult>();
   
    
    public Parser(String vcfFilePath) throws FileNotFoundException {
        super();
        if(reader == null) {
            File f = new File(vcfFilePath);
            Parser.fileSize = f.length();
            Parser.reader = new BufferedReader(new FileReader(f));
            Parser.vcfInMem = new HashMap<String, VcfLine>();
            Parser.vcfInMemByLoc = new TreeMap<Integer, VcfLine>();
            Parser.totalRead = 0;
            Parser.needToRead = true;
        }
        this.vcfFilePath = vcfFilePath;
    }
    
    

    @Override
    protected ParseResult doInBackground() throws Exception {
        boolean isItMe = false;
        
        synchronized(Parser.lock) {
            if(Parser.needToRead) {
                Parser.needToRead = false;
                isItMe = true;
            }
        }
        
        if(isItMe) {
            ProgressLog.add("Reading VCF File...");
            this.readAll();
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    @Override
    protected void done() {
        super.done();
    }
    
    protected String getVcfFilePath() {
        return vcfFilePath;
    }
    
    protected static void clear() {
        
        reader = null;
        vcfInMem = null;
        vcfInMemByLoc = null;
        System.gc();
    }
    
    
    
    
    /**
     * This method gets the generic attributes of the vcf file. It
     * is public because all the different parsers in the program might
     * need to use the header information of the VCF file.
     * @return 
     */
    
    public void readAll() throws IOException {
        
        while(reader.ready()) {
            Parser.readLine();

            int prog = (int)(90L * totalRead/fileSize);
            setProgress(prog);
        }
    }

    
    public static void readLine() throws IOException {
        String line = reader.readLine();
        if(line == null) return;
        if (line.charAt(0) != '#') {
            String[] splitLine = line.split("\t", 6);
            if (!splitLine[2].equals(".")) {
                VcfLine vcfLine = new VcfLine(splitLine);
                vcfInMem.put(new String(splitLine[2]), vcfLine);
                vcfInMemByLoc.put(Integer.parseInt(splitLine[1]), vcfLine);
            }
        }
        
        Parser.totalRead += (line.length() + 1);
    }
    
    public static void efficientReadLine() throws IOException {
        int i = reader.read();
        Parser.totalRead++;
        switch(i) {
            case -1: 
            case '#': 
                totalRead += reader.readLine().length() + 1;
                return;
            default: sb.append((char)i);
        }
        int j = 0;
        String[] result = new String[5];
        while(j < 5) {
            i = reader.read();
            if(i == '\t') {
                result[j] = sb.toString();
                Parser.totalRead += result[j].length();
                sb.setLength(0);
                j++;
            } else {
                sb.append((char)i);
            }
        }
        Parser.totalRead += reader.readLine().length() + 1;
        
        if(!result[2].equals(".")) {
            VcfLine vcfLine = new VcfLine(result);
            vcfInMem.put(vcfLine.getColumn(3), vcfLine);
            vcfInMemByLoc.put(Integer.parseInt(vcfLine.getColumn(2)), vcfLine);
        }
    }
    
    public static boolean vcfContainsRsId(String rsid) {
        return vcfInMem.containsKey(rsid);
    }
    
    public static VcfLine getVcfLine(String rsid) {
        return vcfInMem.get(rsid);
    }
    
    public static boolean vcfContainsPos(Integer pos) {
        return vcfInMemByLoc.containsKey(pos);
    }
    
    public static VcfLine getVcfLineByPos(Integer pos) {
        return vcfInMemByLoc.get(pos);
    }
    
    
    
    /**
     * Every different parser will provide a result set object according to 
     * their own implementation. Example; the Drug Gene Parser may only support
     * a 2D String array to provide the results. Check for the specific parser
     * information for details.
     * 
     * @param parameters
     */
    protected abstract ParseResult runParser();

    /**
     *
     * @param url
     */
    protected void setURL(URI url)
    {
        throw new UnsupportedOperationException();
    }

    //-----------------------------------GENERIC METHODS FOR ANY VCF FILE---------------------------------------
    public String getFileFormat()
    {         // Gets the vcf file format (4.0 or 4.1)

        String format = null;
        String line;

        try
        {
            BufferedReader br = reader;
            while (br.ready())
            {
                line = br.readLine();
                if (line.substring(0, 12).compareToIgnoreCase("##fileformat") == 0)
                {
                    format = line.substring(13);
                }
            }
            return format;

        } catch (IOException e)
        {
            return e.getMessage() + "::ERROR in getFileFormat()";
        }


    }

    public String getFileDate()
    {         // Returns the date of the file

        String date = null;
        String line;

        try
        {
            BufferedReader br = reader;
            while (br.ready())
            {
                line = br.readLine();
                if (line.substring(0, 10).compareToIgnoreCase("##fileDate") == 0)
                {
                    date = line.substring(11, 15) + "-" + line.substring(15, 17) + "-" + line.substring(17);
                }
            }
            return date;

        } catch (IOException e)
        {
            return e.getMessage() + "::ERROR in getFileDate()";
        }


    }

    public String getSource()
    {         // Returns the source of the file

        String source = null;
        String line;

        try
        {
            BufferedReader br = reader;
            while (br.ready())
            {
                line = br.readLine();
                if (line.substring(0, 8).compareToIgnoreCase("##source") == 0)
                {
                    source = line.substring(9);
                }
            }
            return source;

        } catch (IOException e)
        {
            return e.getMessage() + "::ERROR in getSource()";
        }


    }

    public String getReference()
    {         // Returns the reference

        String reference = null;
        String line;

        try
        {
            BufferedReader br = reader;
            while (br.ready())
            {
                line = br.readLine();
                if (line.substring(0, 11).compareToIgnoreCase("##reference") == 0)
                {
                    reference = line.substring(12);
                }
            }
            return reference;

        } catch (IOException e)
        {
            return e.getMessage() + "::ERROR in getReference()";
        }


    }

    public String getPhasing()
    {         // Returns the phase (partial, complete, ...)

        String phase = null;
        String line;

        try
        {
            BufferedReader br = reader;
            while (br.ready())
            {
                line = br.readLine();
                if (line.substring(0, 9).compareToIgnoreCase("##phasing") == 0)
                {
                    phase = line.substring(10);
                }
            }
            return phase;

        } catch (IOException e)
        {
            return e.getMessage() + "::ERROR in getPhasing()";
        }


    }

    public ArrayList<String> getInfo()
    {


        ArrayList<String> info = new ArrayList<String>();
        String line;

        try
        {
            BufferedReader br = reader;
            while (br.ready())
            {
                line = br.readLine();
                if (line.substring(0, 6).compareToIgnoreCase("##info") == 0)
                {
                    System.out.println(line.substring(7));
                    info.add(line.substring(7));
                }
            }
            return info;

        } catch (IOException e)
        {
            System.out.println(e.getMessage() + "::ERROR in getInfo()");
            return new ArrayList<String>();
        }


    }

    public ArrayList<String> getFilter()
    {


        ArrayList<String> filter = new ArrayList<String>();
        String line;

        try
        {
            BufferedReader br = reader;
            while (br.ready())
            {
                line = br.readLine();
                if (line.substring(0, 8).compareToIgnoreCase("##filter") == 0)
                {
                    System.out.println(line.substring(9));
                    filter.add(line.substring(9));
                }
            }
            return filter;

        } catch (IOException e)
        {
            System.out.println(e.getMessage() + "::ERROR in getFilter()");
            return new ArrayList<String>();
        }


    }

    public ArrayList<String> getFormat()
    {


        ArrayList<String> format = new ArrayList<String>();
        String line;

        try
        {
            BufferedReader br = reader;
            while (br.ready())
            {
                line = br.readLine();
                if (line.substring(0, 8).compareToIgnoreCase("##format") == 0)
                {
                    System.out.println(line.substring(9));
                    format.add(line.substring(9));
                }
            }
            return format;

        } catch (IOException e)
        {
            System.out.println(e.getMessage() + "::ERROR in getFormat()");
            return new ArrayList<String>();
        }

    }
    
    public ArrayList<String> getChroms()
    {                                          // returns all of the content about chromosomes in an arraylist
        ArrayList<String> chroms = new ArrayList<String>();
        String line;
        boolean chromSecFound = false;
        int count = 0;
        try
        {
            BufferedReader br = reader;
            while (br.ready())
            {
                line = br.readLine();
                if (line.substring(0, 6).compareToIgnoreCase("#chrom") == 0)
                {
                    chromSecFound = true;
                } else if (chromSecFound)
                {
                    chroms.add(line);
                }
                count++;
            }
            return chroms;

        } catch (IOException e)
        {
            System.out.println(e.getMessage() + "::ERROR in getChroms()");
            return new ArrayList<String>();
        }
    }
    
    public ArrayList<String> getChromSpecs(ArrayList<String> chroms, int lineId)
    {    // returns the specs of a specified variant ***id starts from 0***
        ArrayList<String> specs = new ArrayList<String>();
        if (lineId < chroms.size() && lineId >= 0)
        {
            String line = chroms.get(lineId);
            String specsString[] = line.split("\t"); // Change according to vcf format with "\t"
            for (int i = 0; i < specsString.length; i++)
            {
                if (!(specsString[i].equals("")))
                {
                    specsString[i] = specsString[i].replace(" ", "");
                    specs.add(specsString[i]);
                }
            }
            return specs;
        } else
        {
            System.out.println("ERROR in getChromSpecs:: id exceeds the range!");
            return null;
        }
    }
    
    public String getVariantId(ArrayList<String> specs, int lineId)
    {              // returns the variant id of a specified variant ***id starts from 0***
        String variantId;

        if (!specs.isEmpty())
        {
            variantId = specs.get(2);
            return variantId;
        } else
        {
            System.out.println("ERROR in getVariantId :: no such line in the vcf file");
            return null;
        }
    }
    
    public String getPosition(ArrayList<String> chroms, int lineId)
    {               // returns the position of a specified variant ***id starts from 0***
        String pos;
        ArrayList<String> specs = getChromSpecs(chroms, lineId);
        if (!specs.isEmpty())
        {
            pos = specs.get(1);
            return pos;
        } else
        {
            System.out.println("ERROR in getPosition :: no such line in the vcf file");
            return null;
        }

    }

    public String getChromNum(ArrayList<String> chroms, int lineId)
    {
        String chromNum;
        ArrayList<String> specs = getChromSpecs(chroms, lineId);
        if (!specs.isEmpty())
        {
            chromNum = specs.get(0);
            return chromNum;
        } else
        {
            System.out.println("ERROR in getChromNum :: no such line in the vcf file");
            return null;
        }

    }
    
    public HashMap<String, Integer> retrieveSNP()
    {
        HashMap<String, Integer> validSNP = new HashMap<String, Integer>();
        try
        {
            BufferedReader br = reader;
            while (br.ready())
            {
                String line = br.readLine();
                if (line.substring(0, 1).compareToIgnoreCase("#") != 0) // Means its not metadata- it is actual data
                {
                    String specsString[] = line.split("\t");
                    String tempSNP = specsString[2];
                    if (!tempSNP.equals("."))
                    {
                        validSNP.put(tempSNP, 1);
                    }
                }
            }
            return validSNP;
        } catch (IOException e)
        {
            System.out.println(e.getMessage() + "::ERROR in getFileFormat()");
        }
        return validSNP;
    }
}
