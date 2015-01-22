package model;

import java.util.Set;

import util.Coord;

/** 
 * @inv
 * 		getPlayer() != null
 * 		Soit c = getNextMoveAI() 
 * 			c != null
 * 			isFreeTile(c)
 * 		
 * @cons
 * 		Un modele avec un plateau de jeu de hex.
 * 		$ARGS$ /
 * 		$PRE$ /
 * 		$POST$ Un plateau vide de taille MAX_SIZE_BOARD
 */
public interface HexModel extends ObservableModel {
	/**
	 * La taille maximale du plateau.
	 */
	public static final int MAX_SIZE_BOARD = 11;
	/**
	 * La taille minimale du plateau.
	 */
	public static final int MIN_SIZE_BOARD = 4;
	
	// REQUETES
	
	/**
	 * Calcule le prochain mouvement et la renvoie.
	 * @pre
	 * 		!isFinished()
	 */
	Coord getNextMoveAI();
	
	/**
	 * La taille du plateau.
	 */
	int getSize();
	
	/**
	 * Le prochain joueur à jouer. 
	 * @pre
	 * 		!isFinished()
	 */
	PlayerId getPlayer();
	
	/**
	 * Teste si la case en c est vide.
	 * @pre
	 * 		c != null
	 */
	boolean isFreeTile(Coord c);
	
	/**
	 * Teste si une partie est terminée.
	 */
	boolean isFinished();
	
	/**
	 * Renvoie la liste des pièces jouées par joueur.
	 * @pre
	 * 		joueur != null
	 */
	Set<Coord> getPositions(PlayerId joueur); 
	
	// COMMANDES
	
	/**
	 * Joue le prochain mouvement en c.
	 * @pre
	 * 		c != null
	 * 		isFreeTile(c)
	 * 		!isFinished()
	 * @post
	 * 		old getPlayer() a joué en c
	 */
	void nextMove(Coord c);
	
	/**
	 * Reset le jeu.
	 */
	void reset();
	
	/**
	 * Joue le prochain mouvement calculé par l'IA.
	 * @pre
	 * 		!isFinished()
	 */
	void nextMoveAI();
	
	/**
	 * Définit la nouvelle taille du tableau.
	 * @pre
	 * 		MIN_SIZE_BOARD <= size <= MAX_SIZE_BOARD
	 */
	void setSize(int size);

}
