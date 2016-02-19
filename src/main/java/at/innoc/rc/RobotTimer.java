package at.innoc.rc;

import at.innoc.rc.gui.DisplayFrame;
import at.innoc.rc.gui.OperatorFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Aaron on 30.10.2015.
 */
public class RobotTimer {

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                int screen = 0;
                if(args.length > 0) {
                    try {
                        screen = Integer.parseInt(args[0]);
                    } catch (NumberFormatException e) {
                        exit("Invalid argument: \"" + args[0] + "\"");
                    }
                }

                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }

                GraphicsDevice[] screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

                if(screen >= screens.length){
                    exit("Screen " + screen + " not found");
                }

                DisplayFrame dFrame = new DisplayFrame(screens[screen]);
                OperatorFrame opFrame = new OperatorFrame(dFrame);

                opFrame.setVisible(true);
                dFrame.setVisible(true);
            }
        });
    }

    private static void exit(String reason){
        System.err.println(reason + ", program exits now.");
        System.exit(-1);
    }

}
