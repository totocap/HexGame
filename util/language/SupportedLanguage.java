package hexGame.util.language;

import java.io.File;

/**
 * Les langages supportés par l'appli.
 * @inv <pre>
 *		getFile() != null
 * </pre>
 */
public enum SupportedLanguage {
	FRANCAIS("src/hexGame/lang/fr.lang", "Français"),
	ENGLISH("src/hexGame/lang/en.lang", "English");
	
	// ATTRIBUTS
	
	/**
	 * Le fichier associé au langage.
	 */
	private final File file;
	/**
	 * La variable d'abstraction.
	 */
	private final String name;
	
	// CONSTRUCTEUR
	
	/**
	 * Un langage supporté.
	 * @post <pre>
	 * 		getFile().getName() == fileName
	 * </pre>
	 */
	private SupportedLanguage(String fileName, String name) {
		this.name = name;
		file = new File(fileName);
	}
	
	// REQUETES
	
	/**
	 * Le fichier associé au langage.
	 */
	public File getFile() {
		return file;
	} 
	
	/**
	 * Fonction d'abstraction.
	 */
	public String getName() {
		return name;
	}
}
