package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JComponent;

import model.HexModel;
import model.PlayerId;
import util.Coord;

public class GraphicHex extends JComponent {
    
    //ATTRIBUTS
    
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
    
    // Tableau static de PlayerId
    private static final PlayerId[] PLAYERID_VALUES = PlayerId.values();
    
    //CONSTRUCTEUR
    
    public GraphicHex(HexModel model) {
        this.model = model;
        setPreferredSize(new Dimension(PREFERRED_SIZE_HORIZONTAL + 2 * MARGIN
                , PREFERRED_SIZE_VERTICAL + 2 * MARGIN));
        createView();
    }
    
    //REQUETE
    
    public HexModel getModel() {
        return model;
    }
    
    //COMMANDES
    
    public void paintComponent(Graphics g) {
        //La dimension du graphe
        Dimension dim = getSize();
        
        int sizeBoard = model.getSize();
        //Taille verticale du plateau = dim.getWidth() - 2 * MARGIN
        //Taille horizontale du plateau = dim.getHeight() - 2 * MARGIN
        
        g.setColor(Color.BLACK);
        
        //Le nombre de moitié d'hexagone sur tout le plateau à l'horizontale
        int numberHalfHex = sizeBoard * 2 + sizeBoard - 1;
        
        //La taille d'un coté
        int sizeOneSide = 2 * (int) ((Math.min(dim.getWidth(), dim.getHeight())) / numberHalfHex);
        for (int j = 0; j < sizeBoard; j++) {
            for (int i = 0; i < sizeBoard; i++) {
                
                //Ca dessine la ligne e carré puis ca dessine une ligne de
                //  carré en décaler par rapport a l'ancienne ligne
                g.drawRect(i * sizeOneSide + j * sizeOneSide / 2,j * sizeOneSide, sizeOneSide, sizeOneSide); 
            }
        }
        g.setColor(Color.BLUE);
        Set<Coord> coord = model.getPositions(PlayerId.PLAYER1);
        int xCoord;
        int yCoord;
        //Permet de placer le coup joué au centre
        int centerSquare = - sizeOneSide + sizeOneSide / 4;
        for (Coord c : coord) {
            //Calcule l'endroit ou sera l'abssice en fonction de la coordonnée
            //  + le nombre de décalage entre les étages
            xCoord = sizeOneSide * c.getX() + 1
                    + sizeOneSide / 2 * (c.getY() + 1 - 1) 
                    + centerSquare;
            //Calcule l'endroit ou sera la coordonnée en fonction de l'ordonnée
            yCoord = sizeOneSide * c.getY() + 1 + centerSquare;
            
            g.fillOval(xCoord, yCoord, sizeOneSide / 2, sizeOneSide / 2);
        }
        
        g.setColor(Color.RED);
        coord = model.getPositions(PlayerId.PLAYER2);
        for (Coord c : coord) {
            //Calcule l'endroit ou sera l'abssice en fonction de la coordonnée
            //  + le nombre de décalage entre les étages
            xCoord = sizeOneSide * c.getX() + 1
                    + sizeOneSide / 2 * (c.getY() + 1 - 1) 
                    + centerSquare;
            //Calcule l'endroit ou sera la coordonnée en fonction de l'ordonnée
            yCoord = sizeOneSide * c.getY() + 1 + centerSquare;
            
            g.fillOval(xCoord, yCoord, sizeOneSide / 2, sizeOneSide / 2);
        }
    }
    
    private void createView() {
        getModel().addObserver(new Observer() {
            public void update(Observable o, Object arg) {
                repaint();
            }
        });
    }
    
}
