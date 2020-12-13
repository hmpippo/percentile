package com.riot.logPercentile;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class MyAlgorithm implements PercentileAlgorithm {

    LongAdder count = new LongAdder();
    final Integer[] value = new Integer[2400];
    final AtomicLong[] countContainer = new AtomicLong[2400];

    public MyAlgorithm() {
        // init 1~1000, step = 1
        for (int i = 0; i < 1000; i++) {
            value[i] = i + 1;
        }

        // init 1001~10000, step = 10
        for (int i = 1000; i < 1900; i++) {
            value[i] = value[i - 1] + 10;
        }

        // int 10001~60000, step = 100
        for (int i = 1900; i < 2400; i++) {
            value[i] = value[i - 1] + 100;
        }
    }

    /**
     * populate countContainer
     * @param v
     */
    @Override
    public void accumulate(int v) {
        count.increment();
        int loc = getLocationOfArray(v);
        if (null == countContainer[loc]) {
            synchronized (value[loc]) {
                if (null == countContainer[loc]) {
                    countContainer[loc] = new AtomicLong();
                }
            }
        }
        countContainer[loc].incrementAndGet();
    }

    /**
     *
     * @param percentile: 0.9f/0.95f/0.99f
     * @return
     */
    @Override
    public int getPercentileResult(int percentile) {
        if (percentile <= 0 || percentile >= 100) {
            throw new IllegalArgumentException();
        }
        long total = count.longValue();
        long pos = total - (percentile * total / 100);
        int len = countContainer.length;
        long cnt = 0;
        int ret = 0;
        for (int i = len - 1; i >= 0; i--) {
            cnt += null == countContainer[i] ? 0 : countContainer[i].get();
            if (cnt > pos) {
                ret = value[i];
                break;
            }
        }
        return ret;
    }

    private int getLocationOfArray(int v) {
        int len = value.length;
        if (v >= value[len - 1]) {
            return len - 1;
        } else if (v <= value[0]) {
            return 0;
        }

        if (v <= 1000) {
            return v - 1;
        } else if (v <= 10000) {
            return (v - 1001) / 10 + 1000;
        } else {
            return (v - 10001) / 100 + 1900;
        }
    }
}
