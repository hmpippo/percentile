package com.riot.logPercentile;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.CompletableFuture;


@Slf4j
public class Application {

    public static void main(String[] args) throws Exception {

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            log.info("supply");
            return 1;
        }).thenApply((input) -> {
            log.info("apply");
            return input * 2;
        }).whenComplete((ret, ex) -> {
            if (ex == null) {
                log.info("successfully! ret: {}", ret);
            } else {
                log.error("{}", ex);
            }
        });

        future.get();
    }

}
