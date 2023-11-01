package org.arzije.ziberovska.main;

import org.arzije.ziberovska.controller.Controller;

import org.arzije.ziberovska.view.GUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        Controller controller = new Controller();
        SwingUtilities.invokeLater(() -> {
            controller.initGUI();
            controller.initAfterGUI(); // Initiera consumers efter GUI:n
        });
    }
}