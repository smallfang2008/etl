package com.zbiti.etl.extend.executer.convert.lte4g;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zbiti.common.GZip;
import com.zbiti.common.StringUtil;
  

public abstract  class   AbstractBinaryForASN {
	
	//private String machineName = "";//抽取数据的主机名
	public String fileName = "";//抽取文件的文件名
    private final int FILE_HEADER_LENGTH = 32;//文件头字节数
	public  final Log logger = LogFactory.getLog(AbstractBinaryForASN.class);
	public void convert(String fromPathFile, String dest) throws Exception {
		init();
		fileName=fromPathFile;
		GZip mygzip =new GZip();
		if(fromPathFile.endsWith(".gz")){
			mygzip.gzip(fromPathFile, fromPathFile+".txt");
			convert(fromPathFile+".txt",fromPathFile+".txt.convert",0);
		}else{
			convert(fromPathFile,fromPathFile+".txt.convert",0);
		}
		
	}
	
	public long convert(String gzip, String dest, long position) {
		logger.info("[CNV]" + gzip + "[CONVERT]START");
        long start = System.currentTimeMillis();
		RandomAccessFile raf = null;
		FileWriter fw = null;
		FileChannel fc = null;
		try {
			raf = new RandomAccessFile(new File(gzip),"r");
			fw = new FileWriter(new File(dest), true);
            fc = raf.getChannel();
			long fileLength = fc.size();
            if(fileLength<this.FILE_HEADER_LENGTH){
                return fileLength;
            }
            
            //去掉文件头
            byte[] fileHeaderBytes = new byte[this.FILE_HEADER_LENGTH];
            raf.read(fileHeaderBytes);
            //获取话单实际长度，包括每个话单前4位
            long billLength = this.getActBillLength(fileHeaderBytes);
            //文件剩余字节数
            long fileRemainLength = fileLength-this.FILE_HEADER_LENGTH;
            //文件尾部字节数
            long fileTailLength = fileRemainLength-billLength;

            //开始解析
            ASN asn = this.convertOneAsn(raf);
            int i=0;
            while (asn!=null){
            	
                String str = this.convertASN2String(asn);
                if(str!=null &&!"".equals(str)){ 
                	fw.write(str);
                }                               
                if(i++%10==1){
                    fw.flush();
                }
                fileRemainLength = fileLength - fc.position();
                //当文件剩余字节数小于或等于文件尾部字节数时结束
                if(fileRemainLength <= fileTailLength){
                    break;
                }
                str=null;
                asn = this.convertOneAsn(raf);
            }
			fw.flush();
			logger.info("[CNV]"+gzip+"[CNV_TIME]"+(System.currentTimeMillis()-start));
			return fileLength;
		} catch (Exception e) {
			logger.info("[CNV_ERROR] FILE: "+gzip);
            e.printStackTrace();
		}
        finally {
            try {
                if(raf!=null)raf.close();
                if(fc!=null)fc.close();
                if(fw!=null)fw.close();
            } catch (IOException e) {
            	logger.info("[CNV_ERROR] FILE: "+gzip+e.getMessage());
            }
		}
		return 0;
	}

    public ASN convertOneAsn(RandomAccessFile raf) throws Exception{
        //去掉话单头4位
        byte[] billHeaderBytes = new byte[4];
        try{
            raf.read(billHeaderBytes);
        }catch (Exception e){
            return null;
        }

        byte b;
        try{
           b = raf.readByte();
        }catch (Exception e){
            return null;
        }
        ASN asnObj = new ASN();
        asnObj.setType((b & 0xFF)>>6);
        asnObj.setStructured(((b & 0xFF)>>5 & 0x01)==1); 
        if((b & 0x1F)>30){
            //较大标签值
            b = raf.readByte();
            while ((b & 0xFF)>>7==1){
                b = raf.readByte();
            }
            //此处如果标签是多个表示将会有BUG
            asnObj.setTag(b & 0xFF);
        }
        else{
            asnObj.setTag(b & 0x1F);
           
        }

        //add Length
        b = raf.readByte();
        int length = b&0xFF ;
        if(length<128){
           asnObj.setLength(b & 0xFF);
        }
        else if(length>128){
            int ls =  b &  0x7F ;
            if(ls==1){
                b = raf.readByte();
                asnObj.setLength(b & 0xFF);
            }
            else{
                for(int i=0; i<ls; i++){
                    b = raf.readByte();
                    asnObj.setLength(asnObj.getLength()+(b&0xFF)<<(8*(ls-1-i)));
                }
            }
        }
        else{
            asnObj.setLength(-1);
        }

        //add Content
        if(asnObj.getLength()==-1){
            b = raf.readByte();
            byte bn = raf.readByte();
            while((b&0xFF)!=0||(bn&0xFF)!=0){
                System.out.println("有不定长1");
                b = bn;
                bn = raf.readByte();
            }
        }else if(asnObj.getLength()>0){
            if(asnObj.getLength()>10000000){
                System.out.println("length is too long! "+asnObj.getLength());
                return null;
            }

            byte[] bs = new byte[asnObj.getLength()];
            raf.read(bs);
            asnObj.setContent(bs);
        }
        if(asnObj.isStructured()){
            convertAsn(asnObj.getContent(),asnObj.getAsnList());
        }
        if(asnObj.getTag()==34){
        	System.out.println("-------------------");
        	//a
        }
        return asnObj;
    }

    public void convertAsn(byte[] asnBytes,List<ASN> asnList){

        ASN asnObj = new ASN();
        asnList.add(asnObj);
        int i=0;
        byte b = asnBytes[i++];
        asnObj.setType((b & 0xFF)>>6);
        asnObj.setStructured(((b & 0xFF)>>5 & 0x01)==1);

        if((b & 0x1F)>30){
            //较大标签值
            b = asnBytes[i++];
            while (((b & 0xFF)>>7)==1){
                b = asnBytes[i++];
            }
            //此处如果表标是多个表示将会有BUG
            asnObj.setTag(b & 0xFF);
        }
        else{
            asnObj.setTag(b & 0x1F);
        }
        //add Length
        b = asnBytes[i++];
        int length = b&0xFF ;
        if(length<128){
           asnObj.setLength(b & 0xFF);
        }
        else if(length>128){
            int ls =  b &  0x7F ;
            if(ls==1){
                b = asnBytes[i++];
                asnObj.setLength(b & 0xFF);
            }
            else{
                for(int j=0; j<ls; j++){
                    b = asnBytes[i++];
                    asnObj.setLength(asnObj.getLength()+(b&0xFF)<<(8*(ls-1-j)));
                }
            }
        }
        else{
            asnObj.setLength(-1);
        }

        //add Content
        if(asnObj.getLength()==-1){
            b = asnBytes[i++];
            byte bn = asnBytes[i++];
            while((b&0xFF)!=0||(bn&0xFF)!=0){
                System.out.println("有不定长2");
                b = bn;
                bn = asnBytes[i++];
            }
        }else if(asnObj.getLength()>0){
            if(asnObj.getLength()>asnBytes.length){
                System.out.println("length is too long! "+asnObj.getLength());
                return;
            }
            byte[] bs = new byte[asnObj.getLength()];
            System.arraycopy(asnBytes,i,bs,0,asnObj.getLength());
            i += asnObj.getLength();
            asnObj.setContent(bs);


        }

        if(asnObj.isStructured()){
            convertAsn(asnObj.getContent(),asnObj.getAsnList());
        }
        if(asnBytes.length-i==0)
            return ;
        byte[] bs = new byte[asnBytes.length-i];
        System.arraycopy(asnBytes,i,bs,0,bs.length);
        convertAsn(bs,asnList);

    }

    /**
     * 获取话单实际长度
     * @param fileHeaderBytes
     * @return
     */
    private long getActBillLength(byte[] fileHeaderBytes){
        long result = 0;
        byte[] lengthFlagBytes = new byte[4];
        System.arraycopy(fileHeaderBytes,24,lengthFlagBytes,0,4); //文件头第25～28四个字节，需要翻转
        result = StringUtil.hexStringToDec(StringUtil.binaryToHexStringTrans(lengthFlagBytes).replaceAll(" ",""));
        return result;
    }

    public abstract String convertASN2String(ASN asn) throws Exception;
    
	/*public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}*/
	
	public abstract void init();

}
