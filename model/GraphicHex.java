package model;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

public class GraphicHex extends JComponent {
    
    private HexModel model;
    // Taille verticale préférée d'un GraphicHex
    private static final int PREFERRED_SIZE_VERTICAL = 550;
    // Ratio entre la taille horizontale et la taille verticale
    private static final double RATIO = 1.75;
    // Taille horizontale préférée d'un GraphicHex
    private static final int PREFERRED_SIZE_HORIZONTAL = 
            (int) (PREFERRED_SIZE_VERTICAL * 1.75);
    // marge interne de part et d'autre du composant
    private static final int MARGIN = PREFERRED_SIZE_VERTICAL / 10;
    
    public GraphicHex(HexModel model) {
        this.model = model;
        setPreferredSize(new Dimension(PREFERRED_SIZE_HORIZONTAL + 2 * MARGIN
                , PREFERRED_SIZE_VERTICAL + 2 * MARGIN));
        createView();
    }
    
    public HexModel getModel() {
        return model;
    }
    
    public void paintComponent(Graphics g) {
        //Nombre d'hexagone sur la ligne verticale centrale
        int taille = model.getSize();
        
        // Taille verticale d'un hexagone
        int sizeHex = PREFERRED_SIZE_VERTICAL / taille;
    }
    
    private void createView() {
        getModel().addObserver(new Observer() {
            public void update(Observable o, Object arg) {
                repaint();
            }
        });
    }
    
}
