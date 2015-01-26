package hexGame.model;

import hexGame.util.Contract;
import hexGame.util.Coord;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
	 * L'indice dans les tableaux pour le BitSet de l'accessibilité.
	 */
	private static final int ACC_INDEX = 0;
	/**
	 * L'indice dans les tableaux pour le BitSet de la co-accessibilité.
	 */
	private static final int CO_ACC_INDEX = 1;
	
	/**
	 * Les écouteurs du modèle.
	 */
	private EventListenerList listeners;
	/**
	 * Un ChangeEvent si jamais le modèle change.
	 */
	private ChangeEvent event;
	/**
	 * La taille du plateau de jeu.
	 */
	private int size;
	/**
	 * Le joueur dont c'est le tour.
	 */
	private PlayerId playerTurn;
	/**
	 * Les BitSet pour tous les joueurs. Un 1 signifie qu'un pion se trouve à 
	 * la case de coordonnées (indice / getSize(), indice % getSize()).
	 */
	private Map<PlayerId, BitSet> playersBoard;
	/**
	 * Les BitSets representant les pions accessibles des joueurs de PlayerId 
	 * ainsi que leurs pions co-accessibles.
	 */
	private Map<PlayerId, BitSet[]>  playersAcc;
	
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
		
		size = HexModel.MAX_SIZE_BOARD;
		playerTurn = PlayerId.values()[0];
		
		// Plateaux vides, accessibilités et co-accessibilités.
		playersBoard = new HashMap<PlayerId, BitSet>();
		playersAcc = new HashMap<PlayerId, BitSet[]>();
		for (PlayerId p : PlayerId.values()) {
			playersBoard.put(p, new BitSet(size * size));
			playersAcc.put(p, new BitSet[]{
					new BitSet(size * size), 
					new BitSet(size * size)});
		}
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
		return size;
	}

	/**
	 * Retourne le prochain joueur a jouer.
	 */
	public PlayerId getPlayer() {
		return playerTurn;
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
		
		int indice = convertCoordToIndex(c);
		boolean result = true;
		for (PlayerId p : PlayerId.values()) {
			result &= !playersBoard.get(p).get(indice);
		}
		return result;
	}

	/**
	 * Teste si une partie est terminée.
	 */
	public boolean isFinished() {
		for (PlayerId p : PlayerId.values()) {
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
		boolean result = true;
		for (PlayerId p : PlayerId.values()) {
			result &= playersBoard.get(p).cardinality() == 0;
		}
		return result;
	}

	/**
	 * Teste si le joueur p a gagne la partie.
	 * @pre <pre>
	 * 		p != null</pre>
	 */
	public boolean hasPlayerWon(PlayerId p) {
		Contract.checkCondition(p != null,
				"La référence vers le joueur est vide.");
		
		// Le joueur gagne si il y a des pions accessibles et co-accessibles.
		BitSet[] tab = playersAcc.get(p);
		return tab[ACC_INDEX].intersects(tab[CO_ACC_INDEX]);
	}

	/**
	 * Renvoie la liste des coordonnées où le joueur a des pièces.
	 * @pre <pre>
	 * 		p != null</pre>
	 */
	public Set<Coord> getPositions(PlayerId p) {
		Contract.checkCondition(p != null,
				"La référence vers le joueur est vide.");
		
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
		for (PlayerId p : PlayerId.values()) {
			s += p.getDefaultName() + " : ";
			s += "BitSet des pions : " + playersBoard.get(p) + "\n";
			for (Coord c : getPositions(p)) {
				s += c + ", ";
			}
			s += "\n";
			s += "Accessibles : " + playersAcc.get(p)[ACC_INDEX] + "\n";
			s += "CoAccessibles : " + playersAcc.get(p)[CO_ACC_INDEX] + "\n";
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

		int index = convertCoordToIndex(c);
		// On passe à 1 l'indice de la case dans le BitSet du joueur dont 
		// c'était le tour
		playersBoard.get(playerTurn).set(index);
		
		// On actualise les accessibles et les co-accessibles du même joueur
		checkForAccessibility(index);
		checkForCoAccessibility(index);
		
		// On passe au tour du joueur suivant
		nextPlayer();
		
		fireStateChanged();
	}

	/**
	 * Vide le plateau de jeu et donne le tour au joueur PlayerId.values()[0].
	 * @post <pre>
	 * 		isEmpty()</pre>
	 */
	public void reset() {
		for (PlayerId p : PlayerId.values()) {
			playersBoard.get(p).clear();
			playersAcc.get(p)[ACC_INDEX].clear();
			playersAcc.get(p)[CO_ACC_INDEX].clear();
		}
		
		playerTurn = PlayerId.values()[0];
		
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
		
		this.size = size;
		
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
	
	/**
	 * Passe l'attribut playerTurn au prochain joueur.
	 */
	private void nextPlayer() {
		PlayerId[] tab = PlayerId.values();
		for (int i = 0; i < tab.length; ++i) {
			if (tab[i] == playerTurn) {
				playerTurn = tab[(i + 1) % tab.length];
				break;
			}
		}
	}
	
	/**
	 * Retourne une liste d'indices ne contenant seulement que les voisins de 
	 * index de la meme couleur que le joueur en train de jouer.
	 */
	private List<Integer> getNeighboursIndex(int index) {
		List<Integer> l = new ArrayList<Integer>();
		BitSet bS = playersBoard.get(playerTurn);
		
		if (index - 1 >= 0
				&& bS.get(index - 1)) {
			l.add(index - 1);
		}
		if (index - getSize() + 1 >= 0
				&& bS.get(index - getSize() + 1)) {
			l.add(index - getSize() + 1);
		}
		if (index - getSize() >= 0
				&& bS.get(index - getSize())) {
			l.add(index - getSize());
		}
		if (index + 1 < getSize() * getSize()
				&& bS.get(index + 1)) {
			l.add(index + 1);
		}
		if (index + getSize() - 1 < getSize() * getSize()
				&& bS.get(index + getSize() - 1)) {
			l.add(index + getSize() - 1);
		} 
		if (index + getSize() < getSize() * getSize()
				&& bS.get(index + getSize())) {
			l.add(index + getSize());
		}
		
		return l;
	}
	
	/**
	 * Cherche si index est accessible ou bien peut le devenir, et si oui, 
	 * l'ajoute au BitSet d'accessibilité du joueur qui est en train de 
	 * jouer.
	 */
	private void checkForAccessibility(int index) {
		boolean isAccessible = false;
		// Si jamais on trouve que le pion à l'index est accessible de base, 
		// on l'ajoute ainsi que ses voisins.
		for (int accN : PlayerAcc.getAccIndex(this, playerTurn)) {
			if (index == accN) {
				isAccessible = true;
				break;
			}
		}
		// Si jamais ce n'est pas le cas.
		if (!isAccessible) {
			// On regarde si par hasard un de ses voisins ne serait pas déjà 
			// accessible.
			BitSet bS = playersAcc.get(playerTurn)[ACC_INDEX];
			for (int i : getNeighboursIndex(index)) {
				if (bS.get(i)) {
					isAccessible = true;
					break;
				}
			}
		}
		// Si on a trovué un voisin ou que index correspond à une position 
		// accessible de départ, on ajoute index et ses voisins aux 
		// accessibles.
		if (isAccessible) {
			putInSetNeighbours(playersAcc.get(playerTurn)[ACC_INDEX], 
					index);
		}
	}
	
	/**
	 * Cherche si index est co-accessible ou bien peut le devenir, et si oui, 
	 * l'ajoute au BitSet de co-accessibilité du joueur qui est en train de 
	 * jouer.
	 */
	private void checkForCoAccessibility(int index) {
		boolean isCoAccessible = false;
		// Si jamais on trouve que le pion à l'index est co-accessible de base, 
		// on l'ajoute ainsi que ses voisins.
		for (int accN : PlayerAcc.getCoAccIndex(this, playerTurn)) {
			if (index == accN) {
				isCoAccessible = true;
				break;
			}
		}
		// Si jamais ce n'est pas le cas.
		if (!isCoAccessible) {
			// On regarde si par hasard un de ses voisins ne serait pas déjà 
			// co-accessible.
			BitSet bS = playersAcc.get(playerTurn)[CO_ACC_INDEX];
			for (int i : getNeighboursIndex(index)) {
				if (bS.get(i)) {
					isCoAccessible = true;
					break;
				}
			}
		}
		// Si on a trovué un voisin ou que index correspond à une position 
		// co-accessible de départ, on ajoute index et ses voisins aux 
		// co-accessibles.
		if (isCoAccessible) {
			putInSetNeighbours(playersAcc.get(playerTurn)[CO_ACC_INDEX], 
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
		for (int indexNeighbours : getNeighboursIndex(index)) {
			if (!set.get(indexNeighbours)) {
				putInSetNeighbours(set, indexNeighbours);
			}
		}
	}
	
	/**
	 * Retourne l'indice de la coordonnée dans le tableau. 
	 * indice = c.getX() * getSize() + c.getY().
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
		private static List<Integer> getAccIndex(HexModel m, PlayerId p) {
			List<Integer> l = new ArrayList<Integer>();
			switch(p) {
				case PLAYER1:
					// Les indices accessibles du joueur 1 sont la premiere 
					// ligne du plateau de jeu
					for (int i = 0; i < m.getSize(); ++i) {
						l.add(i);
					}
					break;
				case PLAYER2:
					// Les indices accessibles du joueur 2 sont la premiere 
					// colonne du plateau de jeu
					for (int i = 0; i < m.getSize() * m.getSize();
							i += m.getSize()) {
						l.add(i);
					}
					break;
				default:
					System.err.println("Joueur inconnu.");
					throw new RuntimeException();
			}
			return l;
		}
		
		/**
		 * Renvoie les indices potentiellement co-accessibles de départ pour le 
		 * joueur p dans le plateau m.
		 */
		private static List<Integer> getCoAccIndex(HexModel m, PlayerId p) {
			List<Integer> l = new ArrayList<Integer>();
			switch(p) {
				case PLAYER1:
					// Les indices co-accessibles du joueur 1 sont la derniere 
					// ligne du plateau de jeu
					for (int i = m.getSize() * (m.getSize() - 1); 
							i < m.getSize() * m.getSize(); ++i) {
						l.add(i);
					}
					break;
				case PLAYER2:
					// Les indices accessibles du joueur 2 sont la derniere 
					// colonne du plateau de jeu
					for (int i = m.getSize() - 1; i < m.getSize() * m.getSize();
							i += m.getSize()) {
						l.add(i);
					}
					break;
				default:
					System.err.println("Joueur inconnu.");
					throw new RuntimeException();
			}
			return l;
		}
	}
}
