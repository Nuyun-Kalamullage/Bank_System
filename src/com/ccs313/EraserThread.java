package com.ccs313;

import java.io.*;

import static java.lang.Thread.sleep;

class EraserThread implements Runnable {
    private boolean stop;
    private PrintWriter out;


    public EraserThread(String prompt, PrintWriter out) {
        System.out.print(prompt);
        this.out =out;
    }


    public void run () {
        stop = true;
        while (stop) {
            try {
                out.print("\010*");
                out.print("\010*");
                out.print("\010*");
                sleep(01);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopMasking() {
        this.stop = false;
    }
    public void startMasking() {
        this.stop = true;
    }
}