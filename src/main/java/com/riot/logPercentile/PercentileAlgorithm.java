package com.riot.logPercentile;

public interface PercentileAlgorithm {

    /**
     * accept each value of our data set
     * @param v
     */
    public void accumulate(int v);


    /**
     * compute the corresponding percentile of our data set
     * @param percentile
     * @return
     */
    public int getPercentileResult(int percentile);
}
