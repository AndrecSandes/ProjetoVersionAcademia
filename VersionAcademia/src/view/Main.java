package view;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Configuração para remover o blur
            UIManager.put("TextComponent.background", new ColorUIResource(Color.WHITE));
            UIManager.put("TextComponent.foreground", new ColorUIResource(Color.BLACK));
            UIManager.put("TextField.background", new ColorUIResource(Color.WHITE));
            UIManager.put("FormattedTextField.background", new ColorUIResource(Color.WHITE));
            UIManager.put("TextArea.background", new ColorUIResource(Color.WHITE));
            
            // Usar Metal LookAndFeel (mais estável e sem blur)
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}