package view;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.put("TextComponent.background", new ColorUIResource(Color.WHITE));
            UIManager.put("TextComponent.foreground", new ColorUIResource(Color.BLACK));
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        SwingUtilities.invokeLater(() -> {
            new LoginView();
        });
    }
}