package com.mx.elasticsearch.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;

public class ReadThread implements Runnable {

    private StringBuilder outStr;
    private BufferedReader reader;
    private CountDownLatch threadsSignal;
    private String type;

    public ReadThread(){

    }
    public ReadThread(InputStream in, String charSet, CountDownLatch threadsSignal, String type){
        this.outStr = new StringBuilder();
        this.type = type;
        this.threadsSignal = threadsSignal;
        try {
            this.reader = new BufferedReader(new InputStreamReader(in, charSet));
        } catch (UnsupportedEncodingException e) {
            this.reader = null;
            e.printStackTrace();
        }
    }

    public void run() {
        System.out.println("Thread "+type+" is called...");
        // TODO Auto-generated method stub
        String line = null;
        try{
            while((line = reader.readLine()) != null){
                System.out.println(type+"########"+line+"\n");
                if(line.endsWith("\n")){
                    outStr.append(line);
                }else{
                    outStr.append(line+"\n");
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            if (reader != null){
                try{
                    reader.close();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            threadsSignal.countDown();
            System.out.println("Thread "+type+" is end...");
        }
    }

    public StringBuilder getOutStr(){
        return outStr;
    }
}
