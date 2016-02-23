package at.innoc.rc;

import at.innoc.rc.gui.OperatorListener;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 * Created by Aaron on 21.02.2016.
 */
public class TriggerListener implements Runnable{

    private SerialPort port;
    private OperatorListener opListener;

    public TriggerListener(String portName, OperatorListener opListener){
        this.port = new SerialPort(portName);
        this.opListener = opListener;
    }

    @Override
    public void run() {
        openConnection();

        while(true){
            try {
                int[] input = port.readIntArray();

                if(input != null) {
                    for (int x : input) {
                        if (x == 49) opListener.onTrigger();
                    }
                }

                Thread.sleep(10);
            } catch (SerialPortException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void openConnection(){
        try {
            System.out.println("Opening port... " + port.openPort());
            System.out.println("Setting parameters... " +
                    port.setParams(
                    SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE));

        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

}
