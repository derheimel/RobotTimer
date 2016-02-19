package at.innoc.rc;

import at.innoc.rc.gui.DisplayFrame;
import at.innoc.rc.gui.OperatorFrame;

import javax.swing.*;

/**
 * Created by Aaron on 30.10.2015.
 */
public class RobotTimer {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }

                DisplayFrame dFrame = new DisplayFrame();
                OperatorFrame opFrame = new OperatorFrame(dFrame);

                opFrame.setVisible(true);
                dFrame.setVisible(true);
            }
        });
    }

}
