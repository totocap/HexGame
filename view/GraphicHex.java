package hexGame.view;

import hexGame.model.HexModel;
import hexGame.model.PlayerId;
import hexGame.util.Contract;
import hexGame.util.Coord;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GraphicHex extends JComponent {
    //ATTRIBUTS
    
	/**
	 * Le modele a representer.
	 */
    private HexModel model;
    /*
     * Taille verticale préférée d'un GraphicHex
     */
    private static final int PREFERRED_SIZE_VERTICAL = 500;
    /*
     * Taille horizontale préférée d'un GraphicHex
     */
    private static final int PREFERRED_SIZE_HORIZONTAL = 
            (int) (PREFERRED_SIZE_VERTICAL * 1.75);
    /*
     * Marge interne de part et d'autre du composant.
     */
    private static final int MARGIN = 40;
    /**
     * Espace entre la premiere ligne d'hexagones et les lettres.
     */
    private static final int SPACE_LETTERS = 5;
    /**
     * Espace entre les chiffres et le cote des hexagones.
     */
    private static final int SPACE_NUMBERS = 10;
    
    /**
     * La taille d'un cote vertical d'un hexagone.
     */
    private int sizeOneSide;
    /**
     * La largeur de la corde sous la pointe d'un hexagone.
     */
    private int sizeHexWidth;
    /**
     * L'espace entre la pointe et la corde.
     */
    private int pointu;
    /**
     * Le point d'origine pour le dessin du palteau (en haut a gauche).
     */
    Dimension beginPoint;
    /**
     * Indique si les clics sont ignores ou non.
     */
    private boolean canClick;
    
    //CONSTRUCTEURS
    
    /**
     * Construit une repsentation d'un modele pour le jeu de Hex.
     * @pre <pre>
     * 		model != null
     * </pre>
     */
    public GraphicHex(HexModel model) {
    	Contract.checkCondition(model != null,
    			"La reference vers le modele est vide.");
    	
        this.model = model;
        beginPoint = new Dimension();
        canClick = true;
        
        setPreferredSize(new Dimension(PREFERRED_SIZE_HORIZONTAL + 2 * MARGIN,
                PREFERRED_SIZE_VERTICAL + 2 * MARGIN));
        setMinimumSize(new Dimension(100, 100));
        addListeners();
    }
    
    //COMMANDES
    
    /**
     * Indique si GraphicHex doit enregistrer les clics souris, ou non.
     */
    public void canClick(boolean b) {
    	canClick = b;
    }
    
    // OUTILS
    
    /**
     * peint un plateau de jeu de Hex representant le modele model.
     */
    protected void paintComponent(Graphics g) {

    	// Le nombre de moitiés d'hexagones pour tout le plateau à l'horizontale
        // 2 * nbCases + futur decalage <--> futur decalage = nbCases - 1
        int numberHalfHex = 2 * model.getSize() + model.getSize() - 1;
        
    	// Calcul de la largeur de la corde sous les pointes
    	calculateChordalWidth();
    	
    	// Calcul de la taille d'un cote vertical
        sizeOneSide = (int) ((sizeHexWidth / 2) / Math.cos(Math.toRadians(30)));
        // Calcul de la taille de l'espace entre une pointe et la corde
        pointu = (int) ((sizeHexWidth / 2) / Math.tan(Math.toRadians(60)));
        
        // Calcul esoterique pour le point du debut du dessin
        // A revoir
        beginPoint.setSize((getWidth() - (sizeHexWidth * numberHalfHex / 2)) / 2, 
                (getHeight() - model.getSize() * (sizeOneSide + pointu) - pointu) / 2);
        
        drawText(g);
        drawBoard(g);
    }
    
    /**
     * Calcule la largeur de la corde sous la pointe d'un hexagone.
     */
    private void calculateChordalWidth() {
    	int w = getWidth();
    	int h = getHeight();
    	
    	// Le nombre de moitiés d'hexagones pour tout le plateau à l'horizontale
        // 2 * nbCases + futur decalage <--> futur decalage = nbCases - 1
        int numberHalfHex = 2 * model.getSize() + model.getSize() - 1;
        
        // Calcul du point d'origine pour le dessin (celui en haut a gauche)
        // Si on a assez de place en largeur
        if (w - w / 3  < h) {
        	// La largeur d'un hexagone est de deux fois la taille accordee
        	// a une moitie de largeur
        	sizeHexWidth = 2 * (w / (numberHalfHex + 2));
        } else {
        	// On peut caser boardSize hexagone dans la hauteur
        	sizeHexWidth = h / model.getSize();
        }
    }
    
    /**
     * Ecrit les lettres et les chiffres pour les coordonnées.
     */
    private void drawText(Graphics g) {
    	g.setColor(Color.BLACK);
    	FontMetrics fontMetrics = g.getFontMetrics();
        for (int i = 0; i < model.getSize(); ++i) {
            int charASCII = 65 + i;
            g.drawString(Character.toString((char) charASCII), 
                    beginPoint.width + i * sizeHexWidth + sizeHexWidth / 2 - fontMetrics.charWidth(charASCII) / 2,
                    beginPoint.height - SPACE_LETTERS);
        }
        
        for (int i = 0; i < model.getSize(); ++i) {
            g.drawString(String.valueOf(i + 1),
            		beginPoint.width + i * sizeHexWidth / 2 - SPACE_NUMBERS - fontMetrics.stringWidth(Integer.toString(i)) / 2, 
                    beginPoint.height + i * (sizeOneSide + pointu) + sizeOneSide / 2 + pointu + fontMetrics.getAscent() / 2);
        }
    }
    
    /**
     * Dessine le plateau de jeu.
     */
    private void drawBoard(Graphics g) {
    	g.setColor(Color.BLACK);
    	for (int i = 0; i < model.getSize(); i++) {
            for (int j = 0; j < model.getSize(); j++) {
                drawPoly(g,
                		beginPoint.width + j * sizeHexWidth + i * sizeHexWidth / 2,
                		beginPoint.height + i * (sizeOneSide + pointu),
                		sizeOneSide,
                		sizeHexWidth / 2,
                		pointu,
                		false);
            }
        }
    	
    	for (PlayerId p : PlayerId.values()) {
    		if (p == PlayerId.PLAYER1) {
    			g.setColor(Color.BLUE);
    		} else {
    			g.setColor(Color.RED);
    		}
    		for (Coord c : model.getPositions(p)) {
    			drawPoly(g,
                		beginPoint.width + c.getY() * sizeHexWidth + c.getX() * sizeHexWidth / 2,
                		beginPoint.height + c.getX() * (sizeOneSide + pointu),
                		sizeOneSide,
                		(sizeHexWidth - 1) / 2,
                		pointu,
                		true);
    		}
    	}
    }
    
    /**
     * Dessine un hexagone. x est l'abscisse, y l'ordonnee, sizeSide la taille 
     * d'un cote vertical, half la moitie de la taille de la corde en dessous 
     * de la pointe, et pointu la taille entre cette corde et la pointe.
     * filled indique si on rempli le polygone ou si on ne dessine que les 
     * contours.
     */
    private void drawPoly(Graphics g, int x, int y, int sizeSide, int half, int pointu, boolean filled) {
        int xPoint[] = {x,
        		x + half,
        		x + half * 2,
        		x + half * 2,
        		x + half,
        		x};
        int yPoint[] = {y + pointu,
        		y,
        		y + pointu, 
                y + pointu + sizeSide,
                y + pointu * 2 + sizeSide,
                y + pointu + sizeSide};
        
        if (filled) {
        	g.fillPolygon(xPoint, yPoint, 6);
        } else {
        	g.drawPolygon(xPoint, yPoint, 6);
        }
    }
    
    /**
     * Ajotue els ecouteurs au GraphicsHex.
     */
    private void addListeners() {
        model.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                repaint();
            }
        });
    }
}
