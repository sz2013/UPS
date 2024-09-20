package com.zjf.ups.service;

import com.zjf.ups.util.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.RejectedExecutionException;

@Service
public class UpsService {

    private static final Logger logger = LoggerFactory.getLogger(UpsService.class);

    @Value("${ups.ip}")
    private String ip;

    @Value("${ups.log}")
    private boolean log;

    @Value("${ups.stop}")
    private boolean stop;

    @Value("${ups.interval}")
    private int interval;

    @Value("${ups.waitTime}")
    private int waitTime;

    @Autowired
    @Qualifier("threadPoolUps")
    private ThreadPoolTaskExecutor threadPoolUpsExecutor;

    public void onlineStatusChecker() {
        try {
            threadPoolUpsExecutor.submit(() -> {
                int notOnLineCount = 0;
                while (true) {
                    long start = System.currentTimeMillis();
                    try {
                        if (stop) {
                            logger.info("检查在线状态停止");
                            return;
                        }

                        InetAddress address = InetAddress.getByName(ip);
                        if (!address.isReachable(2000)) {
                            throw new IOException("ip=" + ip + "不可达");
                        }

                        if (log) {
                            logger.info("检查状态完成ip={}在线,耗时={}", ip, System.currentTimeMillis() - start);
                        }
                        if (notOnLineCount > 0) {
                            MailUtil.sendEmail("来电了", "ip=" + ip + "重新上线，请检查服务器状态");
                            notOnLineCount = 0;
                        }
                    } catch (IOException e) {
                        logger.info("检查状态完成ip={}不在线,耗时={}", ip, System.currentTimeMillis() - start);
                        MailUtil.sendEmail("可能停电了", "ip=" + ip + "不在线了，可能停电了，请及时关闭服务器");
                        notOnLineCount++;

                        //如果下线超过等待时间
                        if (notOnLineCount > waitTime * 60 / interval) {
                            MailUtil.sendEmail("停电很久了", "ip=" + ip + "不在线已经超过" + waitTime + "分钟，可能停电了，ups电量即将耗尽，请及时关闭服务器");
                            return;
                        }
                    }

                    if (interval > 0) {
                        try {
                            Thread.sleep(interval * 1000);
                        } catch (InterruptedException e) {
                            logger.error("线程休眠失败", e);
                        }
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            logger.error("ups检查在线状态线程池已满", e);
            return;
        }
    }

    public void wake() {

        try {
            Process process = Runtime.getRuntime().exec("etherwake 54:a0:50:e7:08:95");
            process.waitFor();
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                logger.info("开机成功");
                MailUtil.sendEmail("开机成功", "开机成功");
            } else {
                logger.info("开机失败,exitCode={}", exitCode);
                MailUtil.sendEmail("开机失败", "开机失败");
            }
        } catch (Exception e) {
            logger.error("开机失败", e);
            MailUtil.sendEmail("开机失败", "开机失败");
        }
    }
}
