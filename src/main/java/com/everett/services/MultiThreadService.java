package com.everett.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ejb.Singleton;

@Singleton
public class MultiThreadService {
    private ExecutorService executorService = Executors.newFixedThreadPool(27);

    public void submitTask(Runnable task) {
        executorService.submit(task);
    }
}
