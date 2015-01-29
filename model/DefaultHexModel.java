package hexGame.model;

import hexGame.util.Contract;
import hexGame.util.Coord;

import java.util.Set;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * Implémente l'interface HexModel ; modélise un jeu de Hex pourvu d'une IA.
 */
public class DefaultHexModel implements HexModel {
	// ATTRIBUTS
	/**
	 * Les écouteurs du modèle.
	 */
	private EventListenerList listeners;
	/**
	 * Un ChangeEvent si jamais le modèle change.
	 */
	private ChangeEvent event;
	/**
	 * Le plateau de jeu.
	 */
	private HexBoard board;
	
	// CONSTRUCTEURS
	
	/**
	 * Construis un modele representant un jeu de Hex.
	 * @post <pre>
	 * 		getSize() == MAX_SIZE_BOARD
	 * 		getPlayer() == PlayerId.values()[0]
	 * 		isEmpty()</pre>
	 */
	public DefaultHexModel() {
		listeners = new EventListenerList();
		event = null;
		board = new DefaultHexBoard(HexModel.MAX_SIZE_BOARD);
		
		fireStateChanged();
	}

	/**
	 * Calcule le prochain mouvement que jouerait l'IA et le renvoie.
	 * @pre <pre>
	 * 		!isFinished()</pre>
	 */
	public Coord getNextMoveAI() {
		Contract.checkCondition(!isFinished(),
				"Le jeu est deja termine.");
		
		throw new UnsupportedOperationException();
	}

	/**
	 * Retourne la taille du plateau.
	 */
	public int getSize() {
		return board.getSize();
	}

	/**
	 * Retourne le prochain joueur a jouer.
	 */
	public PlayerId getPlayer() {
		return board.getPlayer();
	}

	/**
	 * Teste si la case en c est vide.
	 * @pre <pre>
	 * 		c != null
	 * 		0 <= c.getX() < getSize()
	 * 		0 <= c.getY() < getSize()</pre>
	 */
	public boolean isFreeTile(Coord c) {
		Contract.checkCondition(c != null,
				"La référence vers la coordonnées est vide.");
		Contract.checkCondition(0 <= c.getX() && c.getX() < getSize(),
				"Abscisse de la coordonnée non dans le plateau : " + c);
		Contract.checkCondition(0 <= c.getY() && c.getY() < getSize(),
				"Ordonnée de la coordonnée non dans le plateau : " + c);
		
		return board.isFreeTile(c);
	}

	/**
	 * Teste si une partie est terminée.
	 */
	public boolean isFinished() {
		return board.isFinished();
	}
	
	/**
	 * Renvoie vrai si le plateau est vide.
	 */
	public boolean isEmpty() {
		return board.isEmpty();
	}

	/**
	 * Teste si le joueur p a gagne la partie.
	 * @pre <pre>
	 * 		p != null</pre>
	 */
	public boolean hasPlayerWon(PlayerId p) {
		Contract.checkCondition(p != null,
				"La référence vers le joueur est vide.");
		
		return board.hasPlayerWon(p);
	}

	/**
	 * Renvoie la liste des coordonnées où le joueur a des pièces.
	 * @pre <pre>
	 * 		p != null</pre>
	 */
	public Set<Coord> getPositions(PlayerId p) {
		Contract.checkCondition(p != null,
				"La référence vers le joueur est vide.");
		
		return board.getPositions(p);
	}
	
	/**
	 * Affiche toutes les informations du plateau.
	 */
	public String toString() {
		return board.toString();
	}
	
	// COMMANDES

	/**
	 * Ajoute un ChangeListener ecoutant le modele.
	 * @pre <pre>
	 * 		cL != null</pre>
	 */
	public void addChangeListener(ChangeListener cL) {
		Contract.checkCondition(cL != null, "Reference vide vers cL.");
		
		listeners.add(ChangeListener.class, cL);
	}

	/**
	 * Retire un ChangeListener ecoutant le modele.
	 * @pre <pre>
	 * 		cL != null</pre>
	 */
	public void removeChangeListener(ChangeListener cL) {
		Contract.checkCondition(cL != null, "Reference vide vers cL.");
		
		listeners.remove(ChangeListener.class, cL);
	}

	/**
	 * Joue pour le joueur getPlayer() un mouvement en c.
	 * @pre <pre>
	 * 		c != null
	 * 		0 <= c.getX() < getSize()
	 * 		0 <= c.getY() < getSize()
	 * 		isFreeTile(c)
	 * 		!isFinished()</pre>
	 * @post <pre>
	 * 		getPlayer() != old getPlayer()
	 * 		c in getPositions(old getPlayer())</pre>
	 */
	public void nextMove(Coord c) {
		Contract.checkCondition(c != null,
				"Reference vide vers la coordonnée.");
		Contract.checkCondition(0 <= c.getX() && c.getX() < getSize(),
				"Abscisse de la coordonnée non dans le plateau : " + c);
		Contract.checkCondition(0 <= c.getY() && c.getY() < getSize(),
				"Ordonnée de la coordonnée non dans le plateau : " + c);
		Contract.checkCondition(isFreeTile(c),
				"La case à la cordonnée " + c + " n'est pas vide.");
		Contract.checkCondition(!isFinished(),
				"La partie est dejà terminée.");

		board.nextMove(c);
		
		fireStateChanged();
	}

	/**
	 * Vide le plateau de jeu et donne le tour au joueur PlayerId.values()[0].
	 * @post <pre>
	 * 		isEmpty()</pre>
	 */
	public void reset() {
		board.reset();
		
		fireStateChanged();
	}

	/**
	 * Défini la nouvelle taille du plateau de jeu.
	 * @pre <pre>
	 * 		MIN_SIZE_BOARD <= size <= MAX_SIZE_BOARD
	 * 		isEmpty()</pre>
	 * @post <pre>
	 * 		getSize() == size</pre>
	 */
	public void setSize(int size) {
		Contract.checkCondition(HexModel.MIN_SIZE_BOARD <= size
				&& HexModel.MAX_SIZE_BOARD >= size,
				"La taille n'est pas située entre les valeurs minimales et "
				+ "maximales : " + size);
		Contract.checkCondition(isEmpty(),
				"Le plateau n'est pas vide.");
		
		board.setSize(size);
		
		fireStateChanged();
	}
	
	// OUTILS
	
	/**
	 * Avertit tous les ChangeListeners qu'il y a eu un changement.
	 */
	protected void fireStateChanged() {
		ChangeListener[] lst = listeners.getListeners(ChangeListener.class);
		for (ChangeListener cL : lst) {
			if (event == null) {
				event = new ChangeEvent(this);
			}
			cL.stateChanged(event);
		}
	}
}
