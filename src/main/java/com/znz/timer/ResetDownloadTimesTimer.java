package com.znz.timer;

import com.znz.dao.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Administrator on 2015/9/5.
 */

@Component
@Slf4j
public class ResetDownloadTimesTimer {

    @Resource
    private UserMapper userMapper;

    @Scheduled(cron="59 59 23  * * ? ")
    private void reset(){
       log.info("----------start jobs------------");
       int i =  userMapper.downloadTimes();
        log.info("----------end jobs------------"+i);
    }

}
