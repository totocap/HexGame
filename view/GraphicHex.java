package hexGame.view;

import hexGame.model.HexModel;
import hexGame.model.PlayerId;
import hexGame.util.Contract;
import hexGame.util.Coord;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GraphicHex extends JComponent {
    
	//ATTRIBUTS
    
	/**
	 * Le modele a representer.
	 */
    private HexModel model;
    /**
     * Taille minimale de longueur de fenetre.
     */
    private static final int MINIMUM_WIDTH_WINDOW = 500;
    /**
     * Taille minimale de hauteur de fenetre.
     */
    private static final int MINIMUM_HEIGHT_WINDOW = 500;
    /**
     * Taille verticale préférée d'un GraphicHex
     */
    private static final int PREFERRED_SIZE_VERTICAL = 500;
    /**
     * Taille horizontale préférée d'un GraphicHex
     */
    private static final int PREFERRED_SIZE_HORIZONTAL = 
            (int) (PREFERRED_SIZE_VERTICAL * 1.75);
    /**
     * Marge interne de part et d'autre du composant.
     */
    private static final int MARGIN_WINDOW = 20;
    /**
     * Ratio de marge pour les Geometry.CIRCLE.
     */
    private static final double MARGIN_RATIO_CIRCLE = 0.9;
    /**
     * Marge entre les hexagones.
     */
    private static final int MARGIN_HEX = 5;
    /**
     * Couleur de la grille.
     */
    private static final Color GRID_COLOR = Color.BLACK;
    /**
     * Couleur des joueurs
     */
    private static final Color[] PLAYERS_COLOR; 
    static {
    	PlayerId[] joueurs = PlayerId.values();  
    	PLAYERS_COLOR = new Color[joueurs.length];
    	for (PlayerId id : joueurs) {
    		PLAYERS_COLOR[id.ordinal()] = id.getDefaultColor();
    	}
    }
    /**
     * Couleur de la numérotation.
     */
    private static final Color NUMEROTATION_COLOR = Color.BLACK;
    /**
     * Espace entre la premiere ligne d'hexagones et les lettres.
     */
    private static final int SPACE_LETTERS = 5;
    /**
     * Espace entre les chiffres et le cote des hexagones.
     */
    private static final int SPACE_NUMBERS = 10;
    /**
     * La forme des pièces.
     */
    private Geometry form;
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
    Point beginPoint;
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
        canClick = true;
        form = Geometry.CIRCLE;
        setPreferredSize(new Dimension(PREFERRED_SIZE_HORIZONTAL + 2 * MARGIN_WINDOW,
                PREFERRED_SIZE_VERTICAL + 2 * MARGIN_WINDOW));
        setMinimumSize(new Dimension(MINIMUM_WIDTH_WINDOW, MINIMUM_HEIGHT_WINDOW));
        addListeners();
        model.setSize(4);
        model.nextMove(new Coord(2,3));
        model.nextMove(new Coord(0,0));
    }
    
    //COMMANDES
    
    /**
     * Indique si GraphicHex doit enregistrer les clics souris, ou non.
     */
    public void canClick(boolean b) {
    	canClick = b;
    }
    /**
     * Change la forme des pièces.
     * @pre
     * 		form != null
     */
    public void setForm(Geometry form) {
    	Contract.checkCondition(form != null, 
    			"Le paramètre doit être différent de null.");
    	this.form = form;
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
        beginPoint = new Point((getWidth() - (sizeHexWidth * numberHalfHex / 2)) / 2, 
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
        int s = model.getSize();
        int numberHalfHex = 3 * s - 1;
        // Calcul du point d'origine pour le dessin (celui en haut a gauche)
        // Si on a assez de place en largeur
        if (w - w / 3  < h) {
        	// La largeur d'un hexagone est de deux fois la taille accordee
        	// a une moitie de largeur
        	sizeHexWidth = (2 * (w - 2 * MARGIN_WINDOW) - s * MARGIN_HEX) / numberHalfHex;
        } else {
        	// On peut caser boardSize hexagone dans la hauteur
        	sizeHexWidth = ((h - 2 * MARGIN_WINDOW) - s * MARGIN_HEX) / s;
        }
    }
    
    /**
     * Ecrit les lettres et les chiffres pour les coordonnées.
     */
    private void drawText(Graphics g) {
    	g.setColor(NUMEROTATION_COLOR);
    	FontMetrics fontMetrics = g.getFontMetrics();
        for (int i = 0; i < model.getSize(); ++i) {
            Coord c = new Coord(i,0);
            g.drawString(c.getAbstractX(), 
                    beginPoint.x + i * sizeHexWidth + sizeHexWidth / 2 - fontMetrics.stringWidth(c.getAbstractX()) / 2,
                    beginPoint.y - SPACE_LETTERS);
        }
        
        for (int i = 0; i < model.getSize(); ++i) {
        	Coord c = new Coord(0,i);
            g.drawString(c.getAbstractY(),
            		beginPoint.x + i * sizeHexWidth / 2 - SPACE_NUMBERS - fontMetrics.stringWidth(c.getAbstractY()) / 2, 
                    beginPoint.y + i * (sizeOneSide + pointu) + sizeOneSide / 2 + pointu + fontMetrics.getAscent() / 2);
        }
    }
    
    /**
     * Dessine le plateau de jeu.
     */
    private void drawBoard(Graphics g) {
    	g.setColor(GRID_COLOR);
    	for (int i = 0; i < model.getSize(); i++) {
            for (int j = 0; j < model.getSize(); j++) {
            	Point pt = getOriginHex(new Coord(i, j));
                drawHex(g, pt.x, pt.y, sizeOneSide, (int) (sizeHexWidth / 2.),
                		pointu, false);
            }
        }
    	for (PlayerId p : PlayerId.values()) {
    		g.setColor(PLAYERS_COLOR[p.ordinal()]);
    		drawPieces(g, p);
    	}
    }
    
    /**
     * Dessine les pièces.
     */
    private void drawPieces(Graphics g, PlayerId p) {
		switch (form) {
		case HEXAGONE : 
			for (Coord c : model.getPositions(p)) {
				Point pt = getOriginHex(c);
				drawHex(g,
						pt.x,
	            		pt.y,
	            		sizeOneSide,
	            		(int) (sizeHexWidth/ 2.),
	            		pointu,
	            		true);
			}
			break;
		case CIRCLE :
			for (Coord c : model.getPositions(p)) {
				int width = (int) (sizeHexWidth * MARGIN_RATIO_CIRCLE);
				Point pt = getOriginHex(c);
				g.fillOval(
						(int) (pt.x + (sizeHexWidth - width) / 2.), 
						 pt.y + pointu + (int) ((sizeOneSide - width) / 2.),
        				width,
        				width);
			}
			break;
		}
    }
    /**
     * Dessine un hexagone. x est l'abscisse, y l'ordonnee, sizeSide la taille 
     * d'un cote vertical, half la moitie de la taille de la corde en dessous 
     * de la pointe, et pointu la taille entre cette corde et la pointe.
     * filled indique si on rempli le polygone ou si on ne dessine que les 
     * contours.
     */
    private void drawHex(Graphics g, int x, int y, int sizeSide, int half, int pointu, boolean filled) {
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
     * Ajoute les ecouteurs au GraphicsHex.
     */
    private void addListeners() {
        model.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                repaint();
            }
        });
    }
    /**
     * Renvoie l'origine de l'hexagone de coordonnée c.
     */
    private Point getOriginHex(Coord c) {
    	int x = beginPoint.x + (int) ((c.getY() + c.getX() / 2.) * (sizeHexWidth + MARGIN_HEX));
		int y = beginPoint.y + c.getX() * (sizeOneSide + pointu + MARGIN_HEX);
		return (new Point(x, y));
    }
    public enum Geometry {
    	HEXAGONE,
    	CIRCLE;
    }
}
