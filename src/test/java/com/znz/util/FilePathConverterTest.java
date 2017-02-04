package com.znz.util;

import org.junit.*;

import java.io.File;

import static org.junit.Assert.*;

public class FilePathConverterTest {

    @org.junit.Test
    public void testEncode() throws Exception {
      String s =   FilePathConverter.encode(new File("d:\\git\\znz2").getAbsolutePath());
      assertEquals("d:FILE_SEPARATORgitFILE_SEPARATORznz2",s);

        String ss  = FilePathConverter.decode("FILE_SEPARATORrootFILE_SEPARATORtomcatznzFILE_SEPARATORapache-tomcat-8.0.21FILE_SEPARATORwebappsFILE_SEPARATORznz-webFILE_SEPARATORuploadFILE_SEPARATORZNZFILE_SEPARATORdfds");
        System.out.println(ss);
    }

    @org.junit.Test
    public void testdecode() throws Exception {
        String s =   FilePathConverter.decode("d:FILE_SEPARATORgitFILE_SEPARATORznz2");
        assertEquals("d:"+File.separator+"git"+File.separator+"znz2",s);
       // System.out.println(FilePathConverter.decode("D:FILE_SEPARATORgitFILE_SEPARATORznz-webFILE_SEPARATORsrcFILE_SEPARATORmainFILE_SEPARATORwebappFILE_SEPARATORuploadFILE_SEPARATORZNZ"));
    }
}