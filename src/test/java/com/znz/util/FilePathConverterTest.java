package com.znz.util;

import org.junit.*;

import java.io.File;

import static org.junit.Assert.*;

public class FilePathConverterTest {

    @org.junit.Test
    public void testEncode() throws Exception {
      String s =   FilePathConverter.encode(new File("d:\\git\\znz2").getAbsolutePath());
      assertEquals("d:FILE_SEPARATORgitFILE_SEPARATORznz2",s);
    }

    @org.junit.Test
    public void testdecode() throws Exception {
        String s =   FilePathConverter.decode("d:FILE_SEPARATORgitFILE_SEPARATORznz2");
        assertEquals("d:"+File.separator+"git"+File.separator+"znz2",s);
       // System.out.println(FilePathConverter.decode("D:FILE_SEPARATORgitFILE_SEPARATORznz-webFILE_SEPARATORsrcFILE_SEPARATORmainFILE_SEPARATORwebappFILE_SEPARATORuploadFILE_SEPARATORZNZ"));
    }
}