package model;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class Hex {
    
    private JFrame mainFrame;
    private HexModel model;
    private GraphicHex graphic;
    
    //CONSTRUCTEURS
    public Hex() {
        createModel();
        createView();
        placeComponents();
        createController();
    }
    
    //COMMANDES
    public void display() {
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        model.notifyObservers();
    }
    
    //OUTILS
    private void createModel() {
        model = new StdHexModel();
        graphic = new GraphicHex(model);
    }
    
    private void createView() {
        mainFrame = new JFrame("Hex");
        
    }
    
    private void placeComponents() {
        mainFrame.add(graphic, BorderLayout.CENTER);
        
    }
    
    private void createController() {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
    // POINT D'ENTREE
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Hex().display();
            }
        });
    }
}