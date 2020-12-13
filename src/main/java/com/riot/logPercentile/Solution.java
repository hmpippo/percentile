package com.riot.logPercentile;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
public class Solution {

    private PercentileAlgorithm algorithm;

    public Solution() {
        algorithm = new MyAlgorithm();
    }

    public void parseLogFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            //10.2.3.4 [2018/13/10:14:02:39] "GET /api/playeritems?playerId=3" 200 1300\n
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(" ");
                String reponseTime = split[split.length - 1];
                log.info(reponseTime);
                algorithm.accumulate(Integer.parseInt(reponseTime));
            }
        } catch (IOException ex) {
            log.error("{}", ex);
        }
    }

    public void computeLogPercentile(int percentile) {
        int ret = algorithm.getPercentileResult(percentile);
        System.out.println(percentile + "% of requests return a response within " + ret + " ms");
    }


    public static void main(String[] args) throws InterruptedException {
        Solution solution = new Solution();
        File logDir = new File("/var/log/httpd");
        List<File> files = Arrays.stream(Objects.requireNonNull(logDir.listFiles())).filter(f -> f.isFile() && f.getName().endsWith(".log"))
                .collect(Collectors.toList());
        /*
        int nCpu = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = new ThreadPoolExecutor(nCpu, nCpu, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        CountDownLatch doneSignal = new CountDownLatch(files.size());
        try {
            files.stream().forEach(f -> pool.submit(() -> {
                solution.parseLogFile(f);
                doneSignal.countDown();
            }));
        } finally {
            pool.shutdown();
        }
        doneSignal.await();
        */

        files.stream().forEach(f -> solution.parseLogFile(f));
        solution.computeLogPercentile(90);
        solution.computeLogPercentile(95);
        solution.computeLogPercentile(99);
    }
}
