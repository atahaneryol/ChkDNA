package chkdna.model;

import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Anıl Anar<anilanar@outlook.com>
 */
public class ProgressLog extends Observable {
    
    private static ProgressLog instance = new ProgressLog();
    private static StringBuilder builder = new StringBuilder("");
    
    public static void add(String log) {
        builder.append(log);
        builder.append('\n');
        
        instance.setChanged();
        instance.notifyObservers(builder.toString());
    }
    
    public static void observe(Observer o) {
        instance.addObserver(o);
    } 
}
