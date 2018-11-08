package com.znz.timer;

import java.io.IOException;
import java.util.List;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FtpTimer {

    @Scheduled(cron="0 0/10 * * * ?")
    private  void reset() throws IOException {
        log.info("----------start jobs FtpTimer------------");
        List<String> list = Lists.newArrayList(".","..","a","cfj","css","data","flash","group","gxjy","images","img","include","m","plus","special","templets","uploads");
        FTPClient ftpClient = new FTPClient();
        String parentPath = "/www";
        ftpClient.connect("cd1057.gotoip.net",21);
        ftpClient.login("480580","6730011asA");
        ftpClient.enterLocalPassiveMode();
        FTPFile[] ftpFiles = ftpClient.listDirectories(parentPath);
        for (FTPFile ftpFile:ftpFiles){
            String fileName = ftpFile.getName();
            if(list.contains(fileName)){
                continue;
            }
           boolean success =  ftpClient.removeDirectory(parentPath+"/"+fileName);
           log.info("delete Directory success:{}",success);
        }
    }
    /*public static void main(String[] args) throws IOException {
        new FtpTimer().reset();
    }*/
}
