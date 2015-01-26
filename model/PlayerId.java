package hexGame.model;

import java.awt.Color;

/**
 * Des Ids de joueur. 
 * @inv
 * 		getDefaultName() != null
 * 		getDefaultColor() != null
 * 		getDefaultName().length > 0
 * 		for all id1, id2 in PlayerId.values()
 * 			id1.getDefaultName() == id2.getDefaultName()
 * 			|| id1.getDefaultColor() == id2.getDefaultColor() 
 * 					==> id1 == id2
 */
public enum PlayerId {
	PLAYER1("Blanc", Color.WHITE),
	PLAYER2("Noir", Color.BLACK);
	
	// ATTRIBUTS
	
	/**
	 * Nom par défaut du player.
	 */
	private String defaultName;
	
	/**
	 * Couleur par défaut. 
	 */
	private Color defaultColor;
	
	// CONSTRUCTEUR
	
	/**
	 * Un PlayerId.
	 */
	private PlayerId(String defaultName, Color defaultColor) {
		this.defaultName = defaultName;
		this.defaultColor = defaultColor;
	}
	
	// REQUETES
	
	/**
	 * Le nom par défaut.
	 */
	public String getDefaultName() {
		return defaultName;
	}
	
	/**
	 * La couleur par défaut.
	 */
	public Color getDefaultColor() {
		return defaultColor;
	}
}
