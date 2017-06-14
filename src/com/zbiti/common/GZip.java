package com.zbiti.common;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;

 

/**

 * 解压tar.gz文件包

 *

 */


public class GZip {

 

    private BufferedOutputStream bufferedOutputStream;

 

    String zipfileName = null;

 

    public GZip(String fileName) {

       this.zipfileName = fileName;

    }

    /*

     * 执行入口,rarFileName为需要解压的文件路径(具体到文件),destDir为解压目标路径

     */

    public GZip() { 
	}

	public static void unTargzFile(String rarFileName, String destDir) throws Exception {

       GZip gzip = new GZip(rarFileName);

       String outputDirectory = destDir;

       File file = new File(outputDirectory);

       if (!file.exists()) {

           file.mkdir();

       }

       gzip.unzipOarFile(outputDirectory);

 

    }

 

    public void unzipOarFile(String outputDirectory) throws Exception {

       FileInputStream fis = null;

       ArchiveInputStream in = null;

       BufferedInputStream bufferedInputStream = null;

       try {

           fis = new FileInputStream(zipfileName);

           GZIPInputStream is = new GZIPInputStream(new BufferedInputStream(

                  fis));

           in = new ArchiveStreamFactory().createArchiveInputStream("tar", is);

           bufferedInputStream = new BufferedInputStream(in);

           TarArchiveEntry entry = (TarArchiveEntry) in.getNextEntry();

           while (entry != null) {

              String name = entry.getName();

              String[] names = name.split("/");

              String fileName = outputDirectory;

              for(int i = 0;i<names.length;i++){

                  String str = names[i];

                  fileName = fileName + File.separator + str;

              }

              if (name.endsWith("/")) {

                  mkFolder(fileName);

              } else {

                  File file = mkFile(fileName);

                  bufferedOutputStream = new BufferedOutputStream(

                         new FileOutputStream(file));

                  int b;

                  while ((b = bufferedInputStream.read()) != -1) {

                     bufferedOutputStream.write(b);

                  }

                  bufferedOutputStream.flush();

                  bufferedOutputStream.close();

              }

              entry = (TarArchiveEntry) in.getNextEntry();

           }

 

       } finally {

           try {

              if (bufferedInputStream != null) {

                  bufferedInputStream.close();

              }

           } catch (IOException e) {

              e.printStackTrace();

           }

       }

    }

 

    private void mkFolder(String fileName) {

       File f = new File(fileName);

       if (!f.exists()) {

           f.mkdir();

       }

    }

 

    private File mkFile(String fileName) {

       File f = new File(fileName);

       try {

           f.createNewFile();

       } catch (IOException e) {

           e.printStackTrace();

       }

       return f;

    }

    public static void gzip(String gzipFilePath, String destFilePath) throws Exception {
		File fileG = new File(gzipFilePath);
		if(!fileG.exists()){
			System.out.println("file not exits," +gzipFilePath);
		}
		File fileD = new File(destFilePath);
		FileInputStream fis = null;
		FileOutputStream fos = null;
		GZIPInputStream gzi = null;
		int length = 0;
		byte[] buffer = new byte[1024 * 1024];

		try {
			fis = new FileInputStream(fileG);
			gzi = new GZIPInputStream(fis);
			fos = new FileOutputStream(fileD);
			length = gzi.read(buffer);
			while (length != -1) {
				fos.write(buffer, 0, length);
				length = gzi.read(buffer);
			}
			fos.close();
		} finally{
			try {
				gzi.close();
			} catch (Exception e) { 
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (Exception e) { 
				e.printStackTrace();
			}
			try {
				fis.close();
			} catch (Exception e) { 
				e.printStackTrace();
			}
		}
		
	}
	
	public void zip(String srcFilePath, String gzipFilePath) throws Exception {
		File fileR = new File(srcFilePath);
		File fileG = new File(gzipFilePath);
		FileInputStream fis = null;
		FileOutputStream fos = null;
		GZIPOutputStream gzo = null;
		int length = 0;
		byte[] buffer = new byte[1024 * 1024];

		try {
			fis = new FileInputStream(fileR);
			fos = new FileOutputStream(fileG);
			gzo = new GZIPOutputStream(fos);
			length = fis.read(buffer);
			while (length != -1) {
				gzo.write(buffer, 0, length);
				length = fis.read(buffer);
			}
			
		}  finally{
			if(gzo!=null){
				try {
					gzo.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	public static void compressFile(String inFileName,String outFileName) throws Exception{
        FileInputStream in = null;
        GZIPOutputStream out = null;
        
        try{
        	in = new FileInputStream(new File(inFileName));
        	out = new GZIPOutputStream(new FileOutputStream(outFileName));
        	byte[] buf = new byte[1024];
            int len = 0;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.flush();
        }catch (Exception e) {
			throw e;
		}finally{
			if(in!=null)
				in.close();
			if(out!=null)
				out.close();
		}
    }
}
