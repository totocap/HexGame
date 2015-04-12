package hexGame.model;

import hexGame.util.Coord;

import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Implémente l'interface HexBoard ; modélise un plateau de jeu de Hex.
 */
public class DefaultHexBoard implements HexBoard {
	// ATTRIBUTS
	
	/**
	 * L'indice dans les tableaux pour le BitSet de l'accessibilité.
	 */
	private static final int ACC_INDEX = 0;
	/**
	 * L'indice dans les tableaux pour le BitSet de la co-accessibilité.
	 */
	private static final int EARLY_ACC_INDEX = 1;
	/**
	 * L'indice dans les tableaux pour le BitSet de la co-accessibilité.
	 */
	private static final int EARLY_CO_ACC_INDEX = 2;
	/**
	 * Le tableau contenant tous les joueurs.
	 */
	private static final PlayerId[] tabPlayers = PlayerId.values();
	
	/**
	 * La taille du plateau de jeu.
	 */
	private int size;
	/**
	 * Le joueur dont c'est le tour.
	 */
	private PlayerId nextPlayer;
	/**
	 * Les BitSet pour tous les joueurs. Un 1 signifie qu'un pion se trouve à 
	 * la case de coordonnées (indice / getSize(), indice % getSize()).
	 */
	private Map<PlayerId, BitSet> playersBoard;
	/**
	 * Les BitSets representant les pions accessibles des joueurs de PlayerId 
	 * ainsi que les positions des pions co-accessibles.
	 */
	private Map<PlayerId, BitSet[]>  playersAcc;
	
	// CONSTRUCTEURS
	
	/**
	 * Construit un plateau de jeu pour le jeu de Hex.
	 * @pre <pre>
	 * 		HexModel.MIN_SIZE_BOARD <= size <= HexModel.MAX_SIZE_BOARD
	 * </pre>
	 * @post <pre>
	 * 		getSize() == size
	 * 		getPlayer() == tabPlayers[0]
	 * 		isEmpty()
	 * </pre>
	 */
	public DefaultHexBoard(int size) {
		this.size = size;
		nextPlayer = tabPlayers[0];
		
		// Plateaux vides, accessibilités et co-accessibilités.
		playersBoard = new HashMap<PlayerId, BitSet>();
		playersAcc = new HashMap<PlayerId, BitSet[]>();
		for (PlayerId p : tabPlayers) {
			playersBoard.put(p, new BitSet(size * size));
			initAcc(p);
		}
	}
	
	// REQUETES

	/**
	 * Retourne la taille du plateau.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Retourne le prochain joueur a jouer.
	 */
	public PlayerId getPlayer() {
		return nextPlayer;
	}

	/**
	 * Teste si la case en c est vide.
	 * @pre <pre>
	 * 		c != null
	 * 		0 <= c.getX() < getSize()
	 * 		0 <= c.getY() < getSize()</pre>
	 */
	public boolean isFreeTile(Coord c) {
		int indice = convertCoordToIndex(c);
		for (PlayerId p : tabPlayers) {
			if (playersBoard.get(p).get(indice)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Teste si une partie est terminée.
	 */
	public boolean isFinished() {
		for (PlayerId p : tabPlayers) {
			if (hasPlayerWon(p)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Renvoie vrai si le plateau est vide.
	 */
	public boolean isEmpty() {
		for (PlayerId p : tabPlayers) {
			if (playersBoard.get(p).cardinality() != 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Teste si le joueur p a gagne la partie.
	 * @pre <pre>
	 * 		p != null</pre>
	 */
	public boolean hasPlayerWon(PlayerId p) {
		// Le joueur gagne si il y a des pions accessibles et co-accessibles.
		BitSet[] tab = playersAcc.get(p);
		return tab[ACC_INDEX].intersects(tab[EARLY_CO_ACC_INDEX]);
	}

	/**
	 * Renvoie la liste des coordonnées où le joueur a des pièces.
	 * @pre <pre>
	 * 		p != null</pre>
	 */
	public Set<Coord> getPositions(PlayerId p) {
		BitSet bS = playersBoard.get(p);
		Set<Coord> setC = new HashSet<Coord>();
		// Parcourt tous les 1 du BitSet
		for (int i = bS.nextSetBit(0); i >= 0; i = bS.nextSetBit(i + 1)) {
		     setC.add(convertIndexToCoord(i));
		 }
		return setC;
	}
	
	/**
	 * Affiche toutes les informations du plateau.
	 */
	public String toString() {
		String s = new String();
		
		s += "Taille du plateau : " + getSize() + "\n";
		s += "Prochain joueur : " + getPlayer().getDefaultName() + "\n";
		if (isEmpty()) {
			s += "Plateau vide.\n";
		}
		s += "-------\n";
		for (PlayerId p : tabPlayers) {
			s += p.getDefaultName() + " : ";
			s += "BitSet des pions : " + playersBoard.get(p) + "\n";
			for (Coord c : getPositions(p)) {
				s += c + ", ";
			}
			s += "\n";
			s += "Accessibles : " + playersAcc.get(p)[ACC_INDEX] + "\n";
			s += "Potentiellement accessibles : " 
					+ playersAcc.get(p)[EARLY_ACC_INDEX] + "\n";
			s += "CoAccessibles : " + playersAcc.get(p)[EARLY_CO_ACC_INDEX] + "\n";
			if (hasPlayerWon(p)) {
				s += "Le joueur " + p.getDefaultName() + " a gagné.\n";
			}
			s += "------\n";
		}
		if (isFinished()) {
			s += "Partie terminée.\n";
		}
		
		return s;
	}
	
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
	public void nextMove(Coord c) {
		int index = convertCoordToIndex(c);
		// On passe à 1 l'indice de la case dans le BitSet du joueur dont 
		// c'était le tour
		playersBoard.get(nextPlayer).set(index);
		
		// On actualise les accessibles du même joueur
		checkForAccessibility(index);
		
		// On passe au tour du joueur suivant
		nextPlayer();
	}

	/**
	 * Vide le plateau de jeu et donne le tour au joueur tabPlayers[0].
	 * @post <pre>
	 * 		isEmpty()</pre>
	 */
	public void reset() {
		for (PlayerId p : tabPlayers) {
			playersBoard.get(p).clear();
			initAcc(p);
		}
		
		nextPlayer = tabPlayers[0];
	}

	/**
	 * Défini la nouvelle taille du plateau de jeu.
	 * @pre <pre>
	 * 		HexModel.MIN_SIZE_BOARD <= size <= HexModel.MAX_SIZE_BOARD
	 * 		isEmpty()</pre>
	 * @post <pre>
	 * 		getSize() == size</pre>
	 */
	public void setSize(int size) {
		// On change la taille
		this.size = size;
		// On recalcule les co-accessibles pour chaque joueur
		for (PlayerId p : tabPlayers) {
			initAcc(p);
		}
	}

	// OUTILS
	
	/**
	 * S'occupe d'initialiser les Maps d'accessibilité et de co-accessibilité 
	 * pour le joueur p.
	 */
	private void initAcc(PlayerId p) {
		playersAcc.put(p, new BitSet[]{
				new BitSet(size * size),
				PlayerAcc.getAccIndex(this, p),
				PlayerAcc.getCoAccIndex(this, p)});
	}
	
	/**
	 * Passe l'attribut nextPlayer au prochain joueur.
	 */
	private void nextPlayer() {
		nextPlayer = tabPlayers[(nextPlayer.ordinal() + 1) % tabPlayers.length];
	}
	
	/**
	 * Retourne une liste d'indices ne contenant seulement que les voisins de 
	 * index de la meme couleur que le joueur en train de jouer.
	 */
	private BitSet getNeighboursIndex(int index) {
		BitSet set = new BitSet(getSize() * getSize());
		BitSet bS = playersBoard.get(nextPlayer);
		
		if (index - 1 >= 0
				&& bS.get(index - 1)) {
			set.set(index - 1);
		}
		if (index - getSize() + 1 >= 0
				&& bS.get(index - getSize() + 1)) {
			set.set(index - getSize() + 1);
		}
		if (index - getSize() >= 0
				&& bS.get(index - getSize())) {
			set.set(index - getSize());
		}
		if (index + 1 < getSize() * getSize()
				&& bS.get(index + 1)) {
			set.set(index + 1);
		}
		if (index + getSize() - 1 < getSize() * getSize()
				&& bS.get(index + getSize() - 1)) {
			set.set(index + getSize() - 1);
		} 
		if (index + getSize() < getSize() * getSize()
				&& bS.get(index + getSize())) {
			set.set(index + getSize());
		}
		
		return set;
	}
	
	/**
	 * Cherche si index est accessible ou bien peut le devenir, et si oui, 
	 * l'ajoute au BitSet d'accessibilité du joueur qui est en train de 
	 * jouer.
	 */
	private void checkForAccessibility(int index) {
		// Si jamais on trouve que le pion à l'index est accessible de base, 
		// on l'ajoute ainsi que ses voisins.
		// Si jamais ce n'est pas le cas, on regarde si par hasard un de ses 
		// voisins ne serait pas déjà accessible.
		// Si on a trovué un voisin ou que index correspond à une position 
		// accessible de départ, on ajoute index et ses voisins aux 
		// accessibles.
		if (playersAcc.get(nextPlayer)[EARLY_ACC_INDEX].get(index)
				|| playersAcc.get(nextPlayer)[ACC_INDEX].intersects(
						getNeighboursIndex(index))) {
			putInSetNeighbours(playersAcc.get(nextPlayer)[ACC_INDEX], 
					index);
		}
	}
	
	/**
	 * Parcourt tous les voisins de l'index pour remplir le set avec les 
	 * voisins qui ne sont pas encore dedans. Fonction récursive.
	 */
	private void putInSetNeighbours(BitSet set, int index) {
		// On commence par ajouter index dans le set.
		set.set(index);
		// Puis on s'occupe de tous ses voisins.
		BitSet bS = getNeighboursIndex(index);
		for (int i = bS.nextSetBit(0); i >= 0; i = bS.nextSetBit(i + 1)) {
			if (!playersAcc.get(nextPlayer)[ACC_INDEX].get(i)) {
				putInSetNeighbours(set, i);
			}
		}
	}
	
	/**
	 * Retourne l'indice de la coordonnée dans le tableau. 
	 * indice = c.getX() + c.getY() * getSize().
	 * @pre <pre>
	 * 		c != null</pre>
	 */
	private int convertCoordToIndex(Coord c) {
		return c.getX() + c.getY() * getSize();
	}
	
	/**
	 * Retourne la coordonnée correspondant à index, un indice. 
	 * c.getX() = indice / getSize(), 
	 * c.getY() = indice % getSize().
	 * @pre <pre>
	 * 		c != null</pre>
	 */
	private Coord convertIndexToCoord(int index) {
		return new Coord(index % getSize(), index / getSize());
	}
	
	// CLASSES INTERNES
	
	/**
	 * Classe statique permettant de demander les indices accessibles et 
	 * co-accessibles de départ de chaque joueur.
	 */
	private static class PlayerAcc {
		/**
		 * Renvoie les indices potentiellement accessibles de départ pour le 
		 * joueur p dans le plateau m.
		 */
		private static BitSet getAccIndex(HexBoard b, PlayerId p) {
			BitSet set = new BitSet(b.getSize() * b.getSize());
			switch(p) {
				case PLAYER1:
					// Les indices accessibles du joueur 1 sont la premiere 
					// ligne du plateau de jeu
					for (int i = 0; i < b.getSize(); ++i) {
						set.set(i);
					}
					break;
				case PLAYER2:
					// Les indices accessibles du joueur 2 sont la premiere 
					// colonne du plateau de jeu
					for (int i = 0; i < b.getSize() * b.getSize();
							i += b.getSize()) {
						set.set(i);
					}
					break;
				default:
					System.err.println("Joueur inconnu.");
					throw new RuntimeException();
			}
			return set;
		}
		
		/**
		 * Renvoie un BitSet des indices co-accessibles pour le joueur p dans 
		 * le plateau m.
		 */
		private static BitSet getCoAccIndex(HexBoard b, PlayerId p) {
			BitSet set = new BitSet(b.getSize() * b.getSize());
			switch(p) {
				case PLAYER1:
					// Les indices co-accessibles du joueur 1 sont la derniere 
					// ligne du plateau de jeu
					for (int i = b.getSize() * (b.getSize() - 1); 
							i < b.getSize() * b.getSize(); ++i) {
						set.set(i);
					}
					break;
				case PLAYER2:
					// Les indices accessibles du joueur 2 sont la derniere 
					// colonne du plateau de jeu
					for (int i = b.getSize() - 1; i < b.getSize() * b.getSize();
							i += b.getSize()) {
						set.set(i);
					}
					break;
				default:
					System.err.println("Joueur inconnu.");
					throw new RuntimeException();
			}
			return set;
		}
	}
}
