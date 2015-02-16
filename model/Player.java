package hexGame.model;

import java.awt.Color;
/**
 * La représentation d'un joueur et les préférences de ce dernier. 
 * @inv
 * 		getId() != null
 * 		getName() != null
 * 		getName().length() > 0
 * 		getColorPieces() != null
 */
public interface Player {
	
	// REQUETES
	
	/**
	 * PlayerId représentant la signature unique du joueur. 
	 */
	PlayerId getId();
	
	/**
	 * Le nom du joueur. 
	 */
	String getName();
	
	/**
	 * La couleur du joueur. 
	 */
	Color getColorPieces();
	
	// COMMANDES
	
	/**
	 * Change le nom du joueur.
	 * @pre
	 * 		name != null
	 * 		name.length > 0
	 * @post
	 * 		getName() == name
	 */
	void setName(String name);
	
	/**
	 * Change la coueleur d'un joueur.
	 * @pre
	 * 		color != null
	 * @post
	 * 		getColorPieces() != null
	 */
	void setColor(Color color);
}
