package hexGame.view;

import hexGame.model.DefaultHexModel;
import hexGame.model.HexModel;
import hexGame.model.PlayerId;
import hexGame.util.language.ExpressionLanguage;
import hexGame.util.language.ILanguage;
import hexGame.util.language.Language;
import hexGame.util.language.SupportedLanguage;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Hex {
    
    //ATTRIBUTS
	
	/**
	 * Langage par défaut.
	 */
    public static final SupportedLanguage INIT_LANGUAGE = 
    		SupportedLanguage.FRANCAIS;
    /**
     * Enregistrement de chaque composant menu à son expression correspondante.
     */
    private Map<AbstractButton, ExpressionLanguage> mapComponentExpression;
    /**
     * Enregistrement de chaque composant language à son SupportedLanguage.
     */
    private Map<JComponent, SupportedLanguage> languageComponent;
    /**
     * Enregistrement de chaque boutons size avec la taille correspondante.
     */
    private HashMap<String, Integer> sizeItems;
    /**
     * Tableau des différents JMenuItem qui concerne la taille.
     */
    private JMenuItem[] sizeMenuItemArray;
	/** 
	 * Le language de l'appli.
	 */
	private ILanguage language;
    /**
     * MainFrame de l'application.
     */
    private JFrame mainFrame;
    /**
     * Contient le model.
     */
    private HexModel model;
    /**
     * Contient le graphic qui se redessinera.
     */
    private GraphicHex graphic;
    /**
     * Permet de refaire une partie.
     */
    private JMenuItem newGame;
    /**
     * Permet le PVP.
     */
    private JMenuItem pvp;
    /**
     * Permet le PvE.
     */
    private JMenuItem pve;
    /**
     * Permet le EvE.
     */
    private JMenuItem eve;
    /**
     * Tableau des langues.
     */
    private JMenuItem[] languageArray;
    /**
     * Indique le coup que l'IA jouerait.
     */
	private JMenuItem hint;
	/**
	 * Les préférences des joueurs.
	 */
	private JMenuItem settings;
	/**
	 * Permet d'afficher le numéro des coups joués.
	 */
	private JMenuItem numberDisplay;
    /**
     * Panel des options pour l'IA.
     */
	private JPanel aiCommands;
	/**
	 * Action de lancer le prochain mouvement d'IA.
	 */
	private JButton nextMoveAI;
	/**
	 * Menu général.
	 */
	private JMenu generalMenu;
	/**
	 * Menu affichage.
	 */
	private JMenu displayMenu;
	/**
	 * Menu Tailles.
	 */
	private JMenu sizeMenu;
	/**
	 * Menu Type.
	 */
	private JMenu typeMenu;
	/**
	 * Menu language.
	 */
	private JMenu languageMenu;
	
    //CONSTRUCTEURS
    
	/**
	 * Une application de Hex.
	 */
    public Hex() {
    	// Initialisation pour les langues
		initLanguage(INIT_LANGUAGE);
		// Création du model
        createModel();
        // Création de la vue
        createView();
        // Placement des composants
        placeComponents();
        // Création du controleur
        createController();
        // Affichage des textes.
        forceChangeLanguageComponent();
    }
    
    //COMMANDES
    
    /**
     * Affiche la fenetre.
     */
    public void display() {
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
    
    //OUTILS
    
    /**
     * Crée le model.
     */
    private void createModel() {
        model = new DefaultHexModel();
    }
    
    /**
     * Crée la vue.
     */
    private void createView() {
    	// fenetre principale
        mainFrame = new JFrame();
        // composant d'affichage du jeu de Hex.
        graphic = new GraphicHex(model);
    	// Action de lancer le prochain mouvement d'IA
    	nextMoveAI = new JButton();
        mapComponentExpression.put(nextMoveAI, ExpressionLanguage.AI_NEXT_MOVE);
        // menuItem nouvelle partie
        newGame = new JMenuItem();
        newGame.setAccelerator(KeyStroke.getKeyStroke('n'));
        mapComponentExpression.put(newGame, ExpressionLanguage.NEW_GAME);
        // liste des tailles disponibles
        sizeItems = new HashMap<String, Integer>();
        sizeMenuItemArray = new JMenuItem[HexModel.MAX_SIZE_BOARD - HexModel.MIN_SIZE_BOARD + 1];
        for (int i = HexModel.MIN_SIZE_BOARD; 
                i <= HexModel.MAX_SIZE_BOARD; i++) {
        	String str = (i) + " x " + (i);
        	sizeMenuItemArray[i - HexModel.MIN_SIZE_BOARD] = new JMenuItem(str);
            sizeItems.put(str, i);
        }
        // bouton d'aide de jeu
        hint = new JMenuItem();
        hint.setAccelerator(KeyStroke.getKeyStroke('i'));
        mapComponentExpression.put(hint, ExpressionLanguage.HINT_BUTTON);
        // boutons de type de jeu.
        ButtonGroup group = new ButtonGroup(); {
	        pvp = new JRadioButtonMenuItem();
	        mapComponentExpression.put(pvp, ExpressionLanguage.PVP_MENU_ITEM);
	        group.add(pvp);
	        
	        pve = new JRadioButtonMenuItem();
	        mapComponentExpression.put(pve, ExpressionLanguage.PVE_MENU_ITEM);
	        group.add(pve);
	        // On commence en Joueur contre IA
	        pve.setSelected(true);
	        
	        eve = new JRadioButtonMenuItem();
	        mapComponentExpression.put(eve, ExpressionLanguage.EVE_MENU_ITEM);
	        group.add(eve);
        }
        
        // boutons des langues
        SupportedLanguage[] languageTabTmp = SupportedLanguage.values();
        languageArray = new JMenuItem[languageTabTmp.length]; 
        group = new ButtonGroup();
        for (SupportedLanguage l : languageTabTmp) {
        	JMenuItem langueTmp = new JRadioButtonMenuItem(l.getName());
        	languageArray[l.ordinal()] = langueTmp;
        	group.add(langueTmp);
        	languageComponent.put(langueTmp, l);
        }
        // On selectionne le langage de départ
        languageArray[INIT_LANGUAGE.ordinal()].setSelected(true);
        
        // boutons des préférences
    	settings = new JMenuItem();
    	mapComponentExpression.put(settings, ExpressionLanguage.SETTINGS_MENU_ITEM);
    	
    	// boutons d'affichage des numéros de coups
    	numberDisplay = new JRadioButtonMenuItem();
    	numberDisplay.setAccelerator(KeyStroke.getKeyStroke('d'));
    	mapComponentExpression.put(numberDisplay, 
    			ExpressionLanguage.NUMBER_DISPLAY_MENU_ITEM);
    }
    
    /**
     * Place les composants.
     */
    private void placeComponents() {
    	// graphic
        mainFrame.add(graphic, BorderLayout.CENTER);
        
        // option IA
        aiCommands = new JPanel(); {
        	aiCommands.add(nextMoveAI);
        }
        mainFrame.add(aiCommands, BorderLayout.NORTH);
        
        // menu
        JMenuBar menuBar = new JMenuBar(); {
	        // onglet général
	        generalMenu = new JMenu(); {
	        	mapComponentExpression.put(generalMenu, 
	        			ExpressionLanguage.GENERAL_MENU); 
	        	generalMenu.add(newGame);
	        	generalMenu.add(hint);
	        	generalMenu.add(settings);
	        }
	        menuBar.add(generalMenu);
	        
	        // onglet affichage
	        displayMenu = new JMenu(); {
	        	mapComponentExpression.put(displayMenu, 
	        			ExpressionLanguage.DISPLAY_MENU); 
	        	displayMenu.add(numberDisplay);
	        }
	        menuBar.add(displayMenu);	        
	        
	        // onglet taille
	        sizeMenu = new JMenu(); {
	        	mapComponentExpression.put(sizeMenu, 
		        		ExpressionLanguage.SIZE_BOARD_MENU);
		        for (JMenuItem m : sizeMenuItemArray) {
		        	sizeMenu.add(m);
		        }
	        }
	        menuBar.add(sizeMenu);

	        // onglet type de partie
	        typeMenu = new JMenu(); {
	        	mapComponentExpression.put(typeMenu, 
	        			ExpressionLanguage.TYPE_OPPONENT_MENU); 
	        	typeMenu.add(pvp);
	        	typeMenu.add(pve);
	        	typeMenu.add(eve);
	        }
	        menuBar.add(typeMenu);
	        
	        // onglet langue
	        languageMenu = new JMenu(); {
	        	mapComponentExpression.put(languageMenu, 
	        			ExpressionLanguage.LANGUAGE_MENU); 
		        for (JMenuItem l : languageArray) {
		        	languageMenu.add(l);
		        }
	        }
	        menuBar.add(languageMenu);
        }
        mainFrame.setJMenuBar(menuBar);
    }
    
    /**
     * Crée le controleur.
     */
    private void createController() {
    	// Action de fermeture de fenetre
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        model.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				// Blocage si c'est au joueur 2 de jouer ou si 
				// c'est de l'IA vers IA
				if ((pve.isSelected() && model.getPlayer() == PlayerId.PLAYER2)
						|| eve.isSelected()) {
					// Bloquer les clics vers le plateau
					// graphic.setClick(false);
				} else {
					// Autoriser les clics vers le plateau
					// graphic.setClick(true);
				}
			}
        });
        
        // Action de changement de langue
        ActionListener alTmp = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SupportedLanguage l = 
						languageComponent.get(((JMenuItem) e.getSource()));
				changeLanguage(l);
			}
        };
        for (JMenuItem languageChoice : languageArray) {
        	languageChoice.addActionListener(alTmp);
        }
        
        // Action de nouvelle partie
        newGame.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				model.reset();
			}
        });
        
        // Action de indice
        hint.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				popUpError("Action non supportée", "Impossible d'exécuter cette commande.");
				// Un truc du genre :
				// graphic.indicate(model.getNextMoveAI());
			}
        });
        
    	// Action changement de taille
        alTmp = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setSize(sizeItems.get(
						((JMenuItem)e.getSource()).getText()));
			}
        };
        for (JMenuItem sizeMenu : sizeMenuItemArray) {
        	sizeMenu.addActionListener(alTmp);
        }
        
        // Joueur contre joueur
        // Désactivation des commandes pour gérer l'IA
        pvp.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				popUpError("Action non supportée", "Impossible d'exécuter cette commande.");
				aiCommands.setVisible(false);
			}
        });
        
        // Joueur contre IA, bonne chance
        // Activation des commandes pour gérer l'IA
        pve.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				popUpError("Action non supportée", "Impossible d'exécuter cette commande.");
				aiCommands.setVisible(true);
			}
        });
        
        // IA contre IA, fin du monde
        // Activation des commandes pour gérer les IAs.
        // N'essayez pas de les contrôler. Elles sont plus fortes que vous.
        eve.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				popUpError("Action non supportée", "Impossible d'exécuter cette commande.");
				aiCommands.setVisible(true);
			}
        });
        
    	// Affiche les préférences
    	settings.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				popUpError("Action non supportée", "Impossible d'exécuter cette commande.");
			}
        });
    	
    	// Affiche la numérotation dans chaque coup joué
    	numberDisplay.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				popUpError("Action non supportée", "Impossible d'exécuter cette commande.");
			}
        });
    	
    	// Joue le prochain coup de l'IA
    	nextMoveAI.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				popUpError("Action non supportée",
						"Intelligence artificielle pas encore implémentée par Totocap.");
				//model.nextMove(model.getNextMoveAI());
			}
        });
    	
    }
    
    /**
     * Change la langue de tous les textes de la vue.
     * Si une erreur survient durant le chargement de la langue, 
     * l'ancienne langue est réhabilitée. Le programme s'arrête.
     */
    private void changeLanguage(SupportedLanguage l) {
    	SupportedLanguage oldLanguage = language.getLanguage();
    	if (oldLanguage != l) {
    		try {
				language.changeLanguage(l);
			} catch (IOException e) {
				try {
					language.changeLanguage(oldLanguage);
					popUpError(language.getText(
								ExpressionLanguage.ERROR_LANGUAGE_CHANGE_TITLE),
							language.getText(
								ExpressionLanguage.ERROR_LANGUAGE_CHANGE));
				} catch (IOException e1) {
					popUpError("The application can't continu."
							+ "The language files are corrupted.", 
							"Fatal Error");
					System.exit(0);
				}
			}
            forceChangeLanguageComponent();
    	}
    }
    
    /**
     * Force le changement de texte de tous les composants traduisibles.
     */
    private void forceChangeLanguageComponent() {
    	mainFrame.setTitle(language.getText(ExpressionLanguage.WINDOW_TITLE));
    	for (AbstractButton button : mapComponentExpression.keySet()) {
    		button.setText(language.getText(mapComponentExpression.get(button)));
    	}
    }

    /**
     * Initialise le langage par lInit. 
     * S'il n'arrive pas à charger, le programme quitte sur un message d'erreur.
     */
    private void initLanguage(SupportedLanguage lInit) {
    	mapComponentExpression = new HashMap<AbstractButton, ExpressionLanguage>();
    	languageComponent = new HashMap<JComponent, SupportedLanguage>();
	    try {
			language = new Language(lInit);
		} catch (IOException e) {		
		    popUpError("The application can't start."
					+ "The language files are corrupted.", 
					"Fatal Error");
			System.exit(0);
		}
    }
    
    /**
     * Affiche une erreur de titre errorTitle et de texte error. 
     */
    private void popUpError(String errorTitle, String error) {
    	JOptionPane.showMessageDialog(null, error, errorTitle, 
    			JOptionPane.ERROR_MESSAGE);
    }
    
    // POINT D'ENTREE
    
    /**
     * Lance l'application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Hex().display();
            }
        });
    }
}
