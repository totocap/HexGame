package hexGame.util.language;

import hexGame.util.Contract;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Language implements ILanguage {
	
	// ATTRIBUTS
	
	/**
	 * Table des string correspondant à chaque expression.
	 */
	private Map<ExpressionLanguage, String> expression;
	
	/**
	 * Le langage représenté.
	 */
	private SupportedLanguage language;
	
	// CONSTRUCTEUR
	
	/**
	 * Un language initialisé avec les commandes de l.getFile().
	 * @throws IOException 
	 * @pre <pre>
	 * 		l != null
	 * </pre>
	 * @post <pre>
	 * 		for all ExpressionLanguage.values() : e
	 * 			getText(e) != null
	 * 			getText(e) est une ligne du fichier l.getFile()	 
	 *			(Toutes les expressions d'expressionLanguage ont été initialisé 
	 *				avec chacun une ligne de l.getFile())
	 *		getLanguage() == l
	 * </pre>
	 */
	public Language(SupportedLanguage l) throws IOException {
		Contract.checkCondition(l != null, "L'argument est invalide.");
		expression = new HashMap<ExpressionLanguage, String>();
		language = l;
		loading();
	}

	// REQUETES
	
	public String getText(ExpressionLanguage expr) {
		Contract.checkCondition(expr != null, "L'argument est invalide.");
		return expression.get(expr);
	}
	
	public SupportedLanguage getLanguage() {
		return language;
	}
	// COMMANDES
	
	public void changeLanguage(SupportedLanguage l) throws IOException {
		Contract.checkCondition(l != null, "L'argument est invalide.");
		language = l;
		loading();
	}
	
	// OUTILS
	
	/**
	 * Charge à partir du getLanguage.getFile() les String 
	 * correspondant à chaque expression.
	 * Toutes les lignes n'ayant pu être initialisé grace au fichier sont 
	 * initialisé avec UNDEFINED_EXPRESSION.
	 * @throws FileNotFoundException <pre>
	 * 		Le fichier contenant les expressions est inaccessibles.
	 * </pre>
	 * @throws IOException <pre>
	 * 		La lecture du fichier a avorté.
	 * </pre>
	 */
	private void loading() throws IOException {
		expression.clear();
		BufferedReader fluxIn = new BufferedReader(
				new FileReader(getLanguage().getFile()));
		try {
			String str = fluxIn.readLine();
			while (str != null) {
				int begin = str.indexOf(" ");
				ExpressionLanguage expr = ExpressionLanguage.getExpression(
						str.substring(0, begin));
				if (expr != null) {
					expression.put(expr, str.substring(begin + 1));
					str = fluxIn.readLine();
				} 
			}
		} finally {
			fluxIn.close();
		}
		for (ExpressionLanguage expr : ExpressionLanguage.values()) {
			if (!expression.containsKey(expr)) {
				expression.put(expr, UNDEFINED_EXPRESSION);
			}
		}
	}
}
