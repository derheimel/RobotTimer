package at.innoc.rc.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Displays the timer - is shown to the audience
 */
public class DisplayFrame extends JFrame{

    /**
     * If one presses this button, the frame will initialize
     * it's content and extend to fullscreen
     */
    private JButton theButton;

    public DisplayFrame(){
        super("Robot Timer"); //set title
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void createWidgets(){

    }

    private void addWidgets(){

    }
}
