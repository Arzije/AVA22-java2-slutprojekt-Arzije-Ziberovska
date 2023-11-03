package org.arzije.ziberovska.main;

import org.arzije.ziberovska.controller.Controller;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        Controller controller = new Controller(100);
        SwingUtilities.invokeLater(() -> {
            controller.initGUI();
            controller.initAfterGUI();
        });
    }
}