package com.riot.logPercentile;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class LogGenerator {

    private static final int MIN = 1;
    private static final int MAX = 60000;
    private static final int AVG = 3000;
    private static final int COUNT = 500000;
    private static final String LINE = "10.2.3.4 [2018/13/10:14:02:39] \"GET /api/playeritems?playerId=3\" 200 %s\n";

    public void generateDataSet(File file) {
        StringBuilder sb = new StringBuilder();
        int cnt = 0;
        try (FileWriter fw = new FileWriter(file)) {
            for (int i = 0; i < COUNT; i++) {
                Random rand = new Random();
                double x = rand.nextGaussian();
                x = x * 6000 + AVG;
                if (x < MIN)
                    continue;
                if (++cnt == 1000) {
                    fw.write(sb.toString());
                    sb = new StringBuilder();
                    cnt = 0;
                }
                sb.append(String.format(LINE, (int)x));
            }
            fw.write(sb.toString());
        } catch(IOException ex){
            log.error("{}", ex);
        }
    }



    public static void main(String[] args) {
        LogGenerator generator = new LogGenerator();
        String path = "/var/log/%s.log";
        int nCPU = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(nCPU);
        try {
            for (int i = 1; i <= 10; i++) {
                int seq = i;
                pool.submit(()->{
                    File file = new File(String.format(path, seq));
                    generator.generateDataSet(file);
                });
            }
        } finally {
            pool.shutdown();
        }
    }
}
