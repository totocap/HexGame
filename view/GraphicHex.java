package hexGame.view;

import hexGame.model.HexModel;
import hexGame.model.PlayerId;
import hexGame.util.Coord;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GraphicHex extends JComponent {
    //ATTRIBUTS
    
    private HexModel model;
    // Taille verticale préférée d'un GraphicHex
    private static final int PREFERRED_SIZE_VERTICAL = 500;
    // Taille horizontale préférée d'un GraphicHex
    private static final int PREFERRED_SIZE_HORIZONTAL = 
            (int) (PREFERRED_SIZE_VERTICAL * 1.75);
    // marge interne de part et d'autre du composant
    private static final int MARGIN = 40;
    
    //CONSTRUCTEUR
    
    public GraphicHex(HexModel model) {
        this.model = model;
        setPreferredSize(new Dimension(PREFERRED_SIZE_HORIZONTAL + 2 * MARGIN,
                PREFERRED_SIZE_VERTICAL + 2 * MARGIN));
        setMinimumSize(new Dimension(100, 100));
        createView();
        
    }
    
    //REQUETES
    
    public HexModel getModel() {
        return model;
    }
    
    /*public Coord getCoord(int x, int y) {
        
        return new Coord(1, 1);
    }*/
    
    //COMMANDES
    
    public void paintComponent(Graphics g) {
        Dimension dim = new Dimension();
        dim.setSize(getSize().getWidth() - 2 * MARGIN, 
                getSize().getHeight() - 2 * MARGIN);
        
        int sizeBoard = model.getSize();
        g.setColor(Color.BLACK);
        
        //Le nombre de moitié d'hexagone sur tout le plateau à l'horizontale
        int numberHalfHex = sizeBoard * 2 + sizeBoard - 1;
        
        //La taille de la largeur d'un hexagone
        int sizeHexWidth;
        Dimension beginPoint = new Dimension();
        //Tests empiriques pour le 3.1
        if (dim.getWidth() - dim.getWidth() / 3.1  < dim.getHeight() ) {
            sizeHexWidth = 2 * (int) (dim.getWidth() / numberHalfHex);
            beginPoint.setSize(MARGIN, 
                    (dim.getHeight() - sizeHexWidth * sizeBoard) / 2 + MARGIN);
        } else {
            sizeHexWidth = (int) (dim.getHeight() / sizeBoard);
            beginPoint.setSize(
                    (dim.getWidth() - numberHalfHex * sizeHexWidth / 2) / 2 + MARGIN, MARGIN);
        }
        
        int sizeOneSide = (int) (sizeHexWidth / Math.sqrt(3));
        int pointu = (int) Math.sqrt(sizeHexWidth * sizeHexWidth / 4.5);
        
        int charASCII;
        for (int i = 0; i < sizeBoard; i++) {
            charASCII = 65 + i;
            g.drawString(Character.toString((char) charASCII), 
                    (int) beginPoint.getWidth() + sizeHexWidth / 2 - 3
                    + i * sizeHexWidth, 
                    (int) beginPoint.getHeight() - 4);
        }
        
        for (int i = 0; i < sizeBoard; i++) {
            g.drawString(String.valueOf(i + 1), (int) (beginPoint.getWidth() - sizeHexWidth / 2
                    + i * sizeHexWidth / 2), 
                    (int) (beginPoint.getHeight() + pointu + sizeOneSide / 1.5
                            + i * (pointu + sizeOneSide)));
        }
        
        for (int j = 0; j < sizeBoard; j++) {
            for (int i = 0; i < sizeBoard; i++) {
                
                //Ca dessine la ligne e carré puis ca dessine une ligne de
                //  carré en décaler par rapport a l'ancienne ligne
                drawPoly(g, sizeOneSide, sizeHexWidth / 2, pointu, i * sizeHexWidth + j * sizeHexWidth / 2 
                        + (int) beginPoint.getWidth(), j * sizeHexWidth 
                        + (int) beginPoint.getHeight());
                
            }
        }
        g.setColor(Color.BLUE);
        Set<Coord> coord = model.getPositions(PlayerId.PLAYER1);
        int xCoord;
        int yCoord;
        for (Coord c : coord) {
          //Calcule l'endroit ou sera l'abssice en fonction de la coordonnée
            //  + le nombre de décalage entre les étages
            xCoord = (int) beginPoint.getWidth() 
                    + sizeHexWidth * (c.getX() + 1 - 1) + sizeHexWidth / 4
                    + (sizeHexWidth / 2) * (c.getY() + 1 - 1);
            //Calcule l'endroit ou sera la coordonnée en fonction de l'ordonnée
            yCoord = (int) beginPoint.getHeight() + pointu 
                    + (c.getY() + 1 - 1) * sizeOneSide + (c.getY() + 1 - 1) * pointu;
            
            g.fillOval(xCoord, yCoord, sizeHexWidth / 2, sizeHexWidth / 2);
        }
        
        g.setColor(Color.RED);
        coord = model.getPositions(PlayerId.PLAYER2);
        for (Coord c : coord) {
            //Calcule l'endroit ou sera l'abssice en fonction de la coordonnée
            //  + le nombre de décalage entre les étages
            xCoord = (int) beginPoint.getWidth() 
                    + sizeHexWidth * (c.getX() + 1 - 1) + sizeHexWidth / 4
                    + (sizeHexWidth / 2) * (c.getY() + 1 - 1);
            //Calcule l'endroit ou sera la coordonnée en fonction de l'ordonnée
            yCoord = (int) beginPoint.getHeight() + pointu 
                    + (c.getY() + 1 - 1) * sizeOneSide + (c.getY() + 1 - 1) * pointu;
            
            g.fillOval(xCoord, yCoord, sizeHexWidth / 2, sizeHexWidth / 2);
        }
       
    }
    
    private void drawPoly(Graphics g, int sizeSide, int half, int pointu, int x, int y) {
        int xPoint[] = {x, x + half, x + half * 2, x + half * 2, x + half, x};
        int yPoint[] = {y + pointu, y, y + pointu, 
                y + pointu + sizeSide, y + pointu * 2 + sizeSide, y + pointu + sizeSide};
        
        g.drawPolygon(xPoint, yPoint, 6);
        
    }
    
    private void createView() {
        getModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                repaint();
            }
        });
    }
    
    //OUTILS
    
    private static void drawMove() {
        
    }
    
}
