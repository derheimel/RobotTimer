package at.innoc.rc;

import at.innoc.rc.gui.DisplayFrame;
import at.innoc.rc.gui.OperatorFrame;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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

                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice[] screens = ge.getScreenDevices();
                Font freeride = null;
                try {
                    freeride = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("fonts/Freeride.otf")).deriveFont(Font.BOLD + Font.ITALIC, 160);
                    ge.registerFont(freeride);
                } catch (FontFormatException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(screen >= screens.length){
                    exit("Screen " + screen + " not found");
                }
                Properties props = getProperties();

                DisplayFrame dFrame = new DisplayFrame(screens[screen], freeride);
                OperatorFrame opFrame = new OperatorFrame(dFrame);

                TriggerListener tl = new TriggerListener(props.getProperty("serial"));
                new Thread(tl).start();

                opFrame.setVisible(true);
                dFrame.setVisible(true);
                dFrame.setAlwaysOnTop(true);
            }
        });
    }

    public static void exit(String reason){
        System.err.println(reason + ", program exits now.");
        System.exit(-1);
    }

    public static Properties getProperties(){
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("config.ini"));
        } catch (IOException e) {
            e.printStackTrace();
            exit("Couldn't read config file");
        }
        return props;
    }

}
