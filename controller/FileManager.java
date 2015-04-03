/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chkdna.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anıl Anar<anilanar@outlook.com>
 */
public class FileManager {
    
    public static List<List<String>> readCsvFile(String name) {
        List<List<String>> result = new ArrayList<List<String>>();
        File f;
        if(name.contains(".csv") || name.contains(".tsv")) {
            f = new File(name);
        } else {
            f = new File(name + ".csv");
        }
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String columnLine = reader.readLine();
            while(reader.ready()) {
                String line = reader.readLine();
                List<String> splitLine = splitCsvLine(line);
                result.add(splitLine);
            }
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    private static List<String> splitCsvLine(String line)
    {
        List<String> result = new ArrayList<String>();
        boolean skipCommas = false;
        String s = "";
        for ( int i = 0; i < line.length(); i++)
        {
            char c = line.charAt(i);
            
            if ( c == ',' && !skipCommas)
            {
                result.add(s);
                s = "";
            } else {
                if(c == '"') skipCommas = !skipCommas;
                s += c;
            }
        }
        // Thanks PoeHah for pointing it out. This adds the last element to it.
        result.add(s);
        return result;
    }
}
