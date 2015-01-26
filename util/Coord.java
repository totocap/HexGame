package hexGame.util;


/**
 * Une representation des coordonnees par une suite de caracteres 
 * suivis d'un entier les caracteres representant 
 * l'abscisse et l'entier l'ordonnee.
 * @inv
 * 		getX() >= 0
 * 		getY() >= 0
 * 		toAbstractX() == an + ... + a0 (ai char)
 * 			tel que getX() == somme(ai * ALPHABET_SIZE ^ i)
 * 		toAbstractY() == getY() + FIRST_INT
 * 		toString() == toAbstractX() + " , " + toAbstractY()
 * 		Soit c une coord :
 * 			Soit newX = c.getX() + xVect * nbVect 
 * 			et newY = c.getY() + yVect * nbVect
 * 				Si newX < 0 || newY < 0 
 * 					c.translate(xVect, yVect, nbVect) == null 
 * 				sinon 
 * 					c.translate(xVect, yVect, nbVect).getX() == newX
 * 					c.translate(xVect, yVect, nbVect).getY() == newY
 * 		c1.equals(c2) <==> c2 != null && c1.getClass() == c2.getClass() 
 *							&& c1.x == c2.x && c1.y == c2.y;
 * @pre
 * 		ALPHABET_SIZE >= 1
 */
public class Coord {
	
	// ATTRIBUTS
	
	/**
	 * Constante definissant la premiere lettre.
	 */
	public static final char FIRST_CHAR = '0';
	/**
	 * Constante definissant la taille de l'alphabet.
	 */
	public static final int ALPHABET_SIZE = 10;
	/**
	 * Constante definissant l'entier de depart.
	 */
	public static final int FIRST_INT = 0;
	/**
	 * Abscisse.
	 */
	private final int x;
	/**
	 * Ordonnee.
	 */
	private final int y;
	
	// CONSTRUCTEURS
	
	/**
	 * Une coordonnée.
	 * @pre
	 * 		x >= 0
	 * 		y >= 0
	 * @post
	 * 		getX() == x
	 * 		getY() == y
	 */
	public Coord(int x, int y) {
		Contract.checkCondition(x >= 0, "Le premier parametre definissant "
				+ "l'abscisse selon un entier est invalide.");
		Contract.checkCondition(y >= 0, "Le premier parametre definissant "
				+ "l'abscisse selon un entier est invalide.");
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Une coordonnée.
	 * @pre
	 * 		str != null
	 * 		str.length() > 0
	 * 		Soit al l'aphabet des lettres comprises entre FIRST_CHAR 
	 * 		et FIRST_CHAR + ALPHABET_SIZE
	 * 			forall c in str : c in al
	 * @post
	 * 		getAbstractX() == str
	 * 		getAbstractY() == String.valueOf(y)
	 */
	public Coord(String str, int y) {
		Contract.checkCondition((str != null), 
				"Le premiere paramètre vaut null."); 
		Contract.checkCondition(isValidString(str), "Le premier parametre "
				+ "definissant l'abscisse ne correspond pas a un string "
				+ "valide.");
		this.x = toInt(str);
		this.y = y - FIRST_INT;
	}
	
	// REQUETES
	
	/**
	 * L'abscisse.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * L'ordonnée.
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * L'abscisse abstraite.
	 */
	public String getAbstractX() {
		return toStr(x);
	}
	
	/**
	 * L'ordonnee abstraite.
	 */
	public String getAbstractY() {
		return String.valueOf(y + FIRST_INT);
	}
	
	/**
	 * Fonction d'abstraction.
	 */
	public String toString() {
		return getAbstractX() + "." + getAbstractY();
	}
	
	/**
	 * La coordonnée par translation.
	 */
	public Coord translate(int xVect, int yVect, int nbVect) {
		int newX = x + xVect * nbVect; 
		int newY = y + yVect * nbVect; 
		return (newX < 0 || newY < 0) ? null : new Coord(newX, newY);
	}

	/**
	 * Teste l'égalité.
	 */
	public boolean equals(Coord c) {
		return c != null && this.getClass() == c.getClass() 
				&& c.x == x && c.y == y;
	}
	
	// OUTILS

	/**
	 * Fonction de hachage.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	/**
	 * Teste si str est ecrit dans l'alphabet donnee.
	 */
	private static boolean isValidString(String str) {
		boolean result = true;
		int k = 0;
		while (result && k < str.length()) {
			if (str.charAt(k) < FIRST_CHAR 
					|| str.charAt(k) > ALPHABET_SIZE * FIRST_CHAR) {
				result = false;
			}
		}
		return result;
	}
	
	/**
	 * Transforme un string str en l'entier qu'il represente d'après l'alphabet.
	 * @post
	 * 		soit an + ... + a0 (ai char) = str
	 * 		toInt(str) == somme(ai * ALPHABET_SIZE ^ i)
	 */
	private static int toInt(String str) {
		int resultat = 0;
		for (int k = 0; k < str.length(); ++k) {
			resultat = (ALPHABET_SIZE * resultat) + str.charAt(k) - FIRST_CHAR;
		}
		return resultat;
	}
	
	/**
	 * Transforme un entier x dans un String le représentant.
	 */
	private static String toStr(int x) {
		int tmp = x % ALPHABET_SIZE; 
		String str = String.valueOf(FIRST_CHAR + tmp);
		x /= ALPHABET_SIZE;
		while (x > 0) {
			tmp = x % ALPHABET_SIZE; 
			str = String.valueOf(FIRST_CHAR + tmp) + str;
			x /= ALPHABET_SIZE;
		}
		return str;
	}
}
