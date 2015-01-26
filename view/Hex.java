package hexGame.view;

import hexGame.model.DefaultHexModel;
import hexGame.model.HexModel;

import hexGame.util.language.ExpressionLanguage;
import hexGame.util.language.ILanguage;
import hexGame.util.language.Language;
import hexGame.util.language.SupportedLanguage;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
    private Map<JMenuItem, ExpressionLanguage> traductiveMenu;
    /**
     * Enregistrement de chaque composant menu à son expression correspondante.
     */
    private Map<JButton, ExpressionLanguage> traductiveButton;
    /**
     * Enregistrement de chaque composant language à son SupportedLanguage.
     */
    private Map<JComponent, SupportedLanguage> languageComponent;
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
     * Permet de changer la taille d'un plateau.
     */
    private JMenuItem[] gameSizeArray;
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
        traductiveButton.put(nextMoveAI, ExpressionLanguage.AI_NEXT_MOVE);
        // menuItem nouvelle partie
        newGame = new JMenuItem();
        traductiveMenu.put(newGame, ExpressionLanguage.NEW_GAME);
        // liste des tailles disponibles
        gameSizeArray = new JMenuItem[HexModel.MAX_SIZE_BOARD 
                                 - HexModel.MIN_SIZE_BOARD + 1];
        for (int i = HexModel.MIN_SIZE_BOARD; 
                i <= HexModel.MAX_SIZE_BOARD; i++) {
            gameSizeArray[i - HexModel.MIN_SIZE_BOARD] = 
            		new JMenuItem((i) + " x " + (i));
        }
        // bouton d'aide de jeu
        hint = new JMenuItem();
        traductiveMenu.put(hint, ExpressionLanguage.HINT_BUTTON);
        // boutons de type de jeu.
        ButtonGroup group = new ButtonGroup(); {
	        pvp = new JRadioButtonMenuItem();
	        traductiveMenu.put(pvp, ExpressionLanguage.PVP_MENU_ITEM);
	        group.add(pvp);
	        
	        pve = new JRadioButtonMenuItem();
	        traductiveMenu.put(pve, ExpressionLanguage.PVE_MENU_ITEM);
	        group.add(pve);
	        
	        eve = new JRadioButtonMenuItem();
	        traductiveMenu.put(eve, ExpressionLanguage.EVE_MENU_ITEM);
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
        
        // boutons des préférences
    	settings = new JMenuItem();
    	traductiveMenu.put(settings, ExpressionLanguage.SETTINGS_MENU_ITEM);
    	
    	// boutons d'affichage des numéros de coups
    	numberDisplay = new JRadioButtonMenuItem();
    	traductiveMenu.put(numberDisplay, 
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
	        	traductiveMenu.put(generalMenu, 
	        			ExpressionLanguage.GENERAL_MENU); 
	        	generalMenu.add(newGame);
	        	generalMenu.add(hint);
	        	generalMenu.add(settings);
	        }
	        menuBar.add(generalMenu);
	        
	        // onglet affichage
	        displayMenu = new JMenu(); {
	        	traductiveMenu.put(displayMenu, 
	        			ExpressionLanguage.DISPLAY_MENU); 
	        	displayMenu.add(numberDisplay);
	        }
	        menuBar.add(displayMenu);	        
	        
	        // onglet taille
	        sizeMenu = new JMenu(); {
		        traductiveMenu.put(sizeMenu, 
		        		ExpressionLanguage.SIZE_BOARD_MENU);
		        for (int i = 0; 
		        		i <= HexModel.MAX_SIZE_BOARD - HexModel.MIN_SIZE_BOARD; 
		        		i++) {
		        	sizeMenu.add(gameSizeArray[i]);
		        }
	        }
	        menuBar.add(sizeMenu);

	        // onglet type de partie
	        typeMenu = new JMenu(); {
	        	traductiveMenu.put(typeMenu, 
	        			ExpressionLanguage.TYPE_OPPONENT_MENU); 
	        	typeMenu.add(pvp);
	        	typeMenu.add(pve);
	        	typeMenu.add(eve);
	        }
	        menuBar.add(typeMenu);
	        
	        // onglet langue
	        languageMenu = new JMenu(); {
	        	traductiveMenu.put(languageMenu, 
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
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // 
        ActionListener alTmp = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SupportedLanguage l = 
						languageComponent.get(((JMenuItem) e.getSource()));
				changeLanguage(l);
			}
        };
        for (JMenuItem m : languageArray) {
        	m.addActionListener(alTmp);
        }
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
    	for (JMenuItem menuItem : traductiveMenu.keySet()) {
    		menuItem.setText(language.getText(traductiveMenu.get(menuItem)));
    	}
    	for (JButton button : traductiveButton.keySet()) {
    		button.setText(language.getText(traductiveButton.get(button)));
    	}
    }

    /**
     * Initialise le langage par lInit. 
     * S'il n'arrive pas à charger, le programme quitte sur un message d'erreur.
     */
    private void initLanguage(SupportedLanguage lInit) {
    	traductiveMenu = new HashMap<JMenuItem, ExpressionLanguage>();
    	traductiveButton = new HashMap<JButton, ExpressionLanguage>();
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