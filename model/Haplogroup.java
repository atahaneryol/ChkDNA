/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chkdna.model;

/**
 *
 * @author Anıl Anar<anilanar@outlook.com>
 */
public class Haplogroup {
    protected String name;
    protected String ref;
    protected String mut1;
    protected String mut2;

    /**
     * @return the haploName
     */
    public String getName() {
        return name;
    }

    /**
     * @param haploName the haploName to set
     */
    public void setName(String haploName) {
        this.name = haploName;
    }

    /**
     * @return the refSnp
     */
    public String getRef() {
        return ref;
    }

    /**
     * @param refSnp the refSnp to set
     */
    public void setRef(String refSnp) {
        this.ref = refSnp;
    }

    /**
     * @return the mut1
     */
    public String getMut1() {
        return mut1;
    }

    /**
     * @param mut1 the mut1 to set
     */
    public void setMut1(String mut1) {
        this.mut1 = mut1;
    }

    /**
     * @return the mut2
     */
    public String getMut2() {
        return mut2;
    }

    /**
     * @param mut2 the mut2 to set
     */
    public void setMut2(String mut2) {
        this.mut2 = mut2;
    }
}
