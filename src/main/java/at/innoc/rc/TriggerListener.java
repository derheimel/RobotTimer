package at.innoc.rc;

import jssc.SerialPort;
import jssc.SerialPortException;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Aaron on 21.02.2016.
 */
public class TriggerListener implements Runnable{

    private SerialPort port;

    public TriggerListener(String portName){
        this.port = new SerialPort(portName);
    }

    @Override
    public void run() {
        openConnection();

        while(true){
            try {
                int[] input = port.readIntArray();
                for(int x : input){
                    if(x == 49) trigger();
                }

                Thread.sleep(10);
            } catch (SerialPortException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void trigger(){
        System.out.println("TRIGGERED");
    }

    private void openConnection(){
        try {
            System.out.println("Opening port... " + port.openPort());
            System.out.println("Setting parameters... " + port.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE));

        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

}
