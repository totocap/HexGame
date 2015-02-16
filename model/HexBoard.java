package hexGame.model;

import java.util.Set;

import hexGame.util.Coord;

/**
 * Specifie un plateau de jeu pour le jeu de Hex.
 * <em>Atention, aucun contrat ne sera vérifié ici.</em>
 * @inv <pre>
 * 		HexModel.MIN_SIZE_BOARD <= size <= HexModel.MAX_SIZE_BOARD
 * 		getPlayer() != null 
 *		isFreeTile(getNextMoveAI())
 * 		isFinished() <=> Il existe un unique PlayerId p tel que hasPlayerWon(p)
 * 		isEmpty() <=>
 * 			forall Coord c : (0, 0) .. (getSize() - 1, getSize() - 1) : 
 * 				isFreeTile(c)
 * 		getPositions(p) != null
 * 		toString() != null
 * </pre>
 * @cons <pre>
 * 		Construit un plateau de jeu pour le jeu de Hex.
 * 		$ARGS$
 * 			int size
 * 		$PRE$
 * 			HexModel.MIN_SIZE_BOARD <= size <= HexModel.MAX_SIZE_BOARD
 * 		$POST$
 * 			getSize() == size
 * 			getPlayer() == PlayerId.values()[0]
 * 			isEmpty()</pre>
 */
public interface HexBoard {
	// REQUETES
	
	/**
	 * Retourne la taille actuelle du plateau.
	 */
	int getSize();
	
	/**
	 * Retourne le prochain joueur a jouer.
	 */
	PlayerId getPlayer();
	
	/**
	 * Teste si la case en c est vide.
	 * @pre <pre>
	 * 		c != null
	 * 		0 <= c.getX() < getSize()
	 * 		0 <= c.getY() < getSize()</pre>
	 */
	boolean isFreeTile(Coord c);
	
	/**
	 * Teste si une partie est terminée.
	 */
	boolean isFinished();
	
	/**
	 * Renvoie vrai si le plateau est vide.
	 */
	boolean isEmpty();
	
	/**
	 * Teste si le joueur p a gagne la partie.
	 * @pre <pre>
	 * 		p != null</pre>
	 */
	boolean hasPlayerWon(PlayerId p);
	
	/**
	 * Renvoie la liste des coordonnées où le joueur a des pièces.
	 * @pre <pre>
	 * 		p != null</pre>
	 */
	Set<Coord> getPositions(PlayerId p);
	
	/**
	 * Affiche toutes les informations du plateau.
	 */
	String toString();
	
	// COMMANDES
	
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
	void nextMove(Coord c);
	
	/**
	 * Vide le plateau de jeu et donne le tour au joueur PlayerId.values()[0].
	 * @post <pre>
	 * 		isEmpty()</pre>
	 */
	void reset();
	
	/**
	 * Défini la nouvelle taille du plateau de jeu.
	 * @pre <pre>
	 * 		HexModel.MIN_SIZE_BOARD <= size <= HexModel.MAX_SIZE_BOARD
	 * 		isEmpty()</pre>
	 * @post <pre>
	 * 		getSize() == size</pre>
	 */
	void setSize(int size);
}
