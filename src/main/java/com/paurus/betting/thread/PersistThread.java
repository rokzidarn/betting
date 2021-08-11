package com.paurus.betting.thread;

public class PersistThread implements Runnable {

    private String data;

    public PersistThread(String data){
        this.data = data;
    }

    @Override
    public void run() {
        String arg = data;
        process(arg);
    }

    private void process(String arg) {
        try {
            System.out.println(arg);
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}