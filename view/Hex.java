package view;

import java.awt.BorderLayout;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import model.HexModel;

public class Hex {
    
    //ATTRIBUTS
    
    // mainFrame de l'application
    private JFrame mainFrame;
    // Contient le model
    private HexModel model;
    // Contient le graphic qui se redessinera
    private GraphicHex graphic;
    // JMenu game : contenant newGame
    private JMenu game;
    // JMenu size : contenant gameSize[]
    private JMenu size;
    // JMenu type : contenant PvP, PvE, EvE
    private JMenu type;
    // JMenu langue : contenant fr, en
    private JMenu langue;
    // Permet de refaire une partie
    private JMenuItem newGame;
    // Permet de changer la taille d'un plateau.
    private JCheckBoxMenuItem[] gameSize;
    // Permet la génération d'un indice pour le prochain coup
    private JMenu hint;
    // Permet le PVP
    private JMenuItem PvP;
    // Permet le PvE
    private JMenuItem PvE;
    // Permet le EvE
    private JMenuItem EvE;
    // Permet de changer la langue en francais
    private JCheckBoxMenuItem francais;
    // Permet de changer la langue en anglais
    private JCheckBoxMenuItem anglais;
    
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

        game = new JMenu(" ");
        size = new JMenu(" ");
        type = new JMenu(" ");
        langue = new JMenu(" ");
        newGame = new JMenuItem(" ");
        gameSize = new JCheckBoxMenuItem
                [HexModel.MAX_SIZE_BOARD - HexModel.MIN_SIZE_BOARD + 1];
        for (int i = 0; 
                i <= HexModel.MAX_SIZE_BOARD - HexModel.MIN_SIZE_BOARD; i++) {
            gameSize[i] = new JCheckBoxMenuItem
                    ((i + HexModel.MIN_SIZE_BOARD) 
                    + " x " + (i + HexModel.MIN_SIZE_BOARD));
        }
        hint = new JMenu(" ");
        PvP = new JMenuItem(" ");
        PvE = new JMenuItem(" ");
        EvE = new JMenuItem(" ");
        francais = new JCheckBoxMenuItem("Francais");
        anglais = new JCheckBoxMenuItem("English");
    }
    
    private void placeComponents() {
        mainFrame.add(graphic, BorderLayout.CENTER);
        JMenuBar menuBar = new JMenuBar();
        
        game.add(newGame);
        menuBar.add(game);
        
        for (int i = 0; 
                i <= HexModel.MAX_SIZE_BOARD - HexModel.MIN_SIZE_BOARD; i++) {
            size.add(gameSize[i]);
        }
        menuBar.add(size);
        
        type.add(PvP);
        type.add(PvE);
        type.add(EvE);
        menuBar.add(type);
        
        langue.add(francais);
        langue.add(anglais);
        menuBar.add(langue);
        
        menuBar.add(hint);
        
        mainFrame.setJMenuBar(menuBar);
        
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