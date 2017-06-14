package com.zbiti.etl.extend.executer.convert.lte4g;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: wuhf
 * Date: 13-11-7
 * Time: ����6:18
 * To change this template use File | Settings | File Templates.
 */
public class ASN {
    private int type;
    private boolean isStructured;
    private int tag;
    private int length=0;
    private byte[] content = new byte[1];
    private List<ASN> asnList = new ArrayList<ASN>();


    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }



    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isStructured() {
        return isStructured;
    }

    public void setStructured(boolean structured) {
        isStructured = structured;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public List<ASN> getAsnList() {
        return asnList;
    }

    public void setAsnList(List<ASN> asnList) {
        this.asnList = asnList;
    }

    public String toString(){
        String cls = "";
        if(this.isStructured()){
             for(int i=0; i<asnList.size(); i++){
                 cls += asnList.get(i).toString();
             }
        }
        return "TAG:"+this.getTag()+" TYPE:"+this.getType()+" LENGTH:"+this.getLength()+cls;
    }
}
