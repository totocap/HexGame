package model;

import java.util.Observer;

/**
 * Une interface pour pallier le manque de sp�cification sur ce sujet dans
 *  l'API.
 * @inv <pre>
 *     countObservers() >= 0 </pre>
 */
public interface ObservableModel {
    
    // REQUETES
    
    /**
     * Le nombre d'observateurs enregistr�s aupr�s de ce mod�le.
     */
    int countObservers();

    // COMMANDES
    
    /**
     * Enregistre un observateur sur ce mod�le s'il n'y est pas d�j�.
     * @pre <pre>
     *     o != null </pre>
     * @post <pre>
     *     o est enregistr� aupr�s du mod�le
     *     si o n'�tait pas d�j� enregistr� alors
     *         countObservers() == old countObservers() + 1
     *     sinon
     *         rien n'a chang� </pre>
     * @throws NullPointerException si o est null
     */
    void addObserver(Observer o);
    
    /**
     * D�sinscrit un observateur de ce mod�le.
     * Si o vaut null, cette m�thode n'a pas d'effet.
     * @post <pre>
     *     o n'est pas enregistr� aupr�s du mod�le
     *     si o �tait enregistr� alors
     *         countObservers() == old countObservers() - 1
     *     sinon
     *         rien n'a chang� </pre>
     */
    void deleteObserver(Observer o);
    
    /**
     * D�sinscrit tous les observateurs de ce mod�le.
     * @post <pre>
     *     countObservers() == 0 </pre>
     */
    void deleteObservers();
    
    /**
     * Notifie tous les observateurs de ce mod�le si n�cessaire, c'est-�-dire
     *  s'il n'y a pas eu de notification depuis la derni�re modification
     *  du mod�le.
     * Chaque observateur enregistr� ex�cute en s�quence sa m�thode update
     *  avec pour arguments ce mod�le et null.
     */
    void notifyObservers();
    
    /**
     * Notifie tous les observateurs de ce mod�le si n�cessaire, c'est-�-dire
     *  s'il n'y a pas eu de notification depuis la derni�re modification
     *  du mod�le.
     * Chaque observateur enregistr� ex�cute en s�quence sa m�thode update
     *  avec pour arguments ce mod�le et arg.
     */
    void notifyObservers(Object arg);
}
