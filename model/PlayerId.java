package model;

import util.Contract;

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
	 * Un playerId.
	 */
	PlayerId(String defaultName, Color defaultColor) {
		Contract.checkCondition(testContractName(defaultName), 
				"Le premier argument est invalide : " 
				+ (defaultName == null 
					? "null" : defaultName));
		Contract.checkCondition(testContractColor(defaultColor), 
				"Le deuxième argument est invalide : " 
				+ (defaultColor == null 
					? "null" : defaultColor.toString()));
		
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
	
	// OUTILS
	
	/**
	 * Teste si nameTest est valide et n'appartient pas déja a un autre joueur.
	 */
	private boolean testContractName(String nameTest) {
		boolean result = (nameTest != null) && nameTest.length() > 0;
		PlayerId[] idTab = PlayerId.values();
		
		for (int k = 0; k < idTab.length && result; ++k) {
			result = (idTab[k].getDefaultName() != nameTest);
		}
		
		return result;
	}
	
	/**
	 * Test si colorTest est valide et n'appartient pas déja à un autre joueur.
	 */
	private boolean testContractColor(Color colorTest) {
		PlayerId[] idTab = PlayerId.values();
		boolean result = (colorTest != null);
		
		for (int k = 0; k < idTab.length && result; ++k) {
			result = (idTab[k].getDefaultColor() != colorTest);
		}
		
		return result;
	}
}
