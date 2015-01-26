package hexGame.util.language;

import java.io.IOException;

/**
 * Un Ilanguage est un objet contenant la totalité des expressions 
 * linguistiques.
 * @cons <pre>
 * 		$DESC$
 * 			Un language initialisé avec les commandes de l.getFile()
 * 		$ARGS$
 * 			SupportedLanguage l
 * 		$PRE$
 * 			l != null
 * 		$POST$
 * 			for all ExpressionLanguage.values() : e
 * 				getText(e) != null
 * 				getText(e) est une ligne du fichier l.getFile()	 
 *				(Toutes les expressions d'expressionLanguage ont été initialisé 
 *					avec chacun une ligne de l.getFile())
 *			getLanguage() == l
 * </pre>
 */
public interface ILanguage {
	
	// ATTRIBUTS
	
	/**
	 * Le message par défaut pour tout expression non renseigné.
	 */
	String UNDEFINED_EXPRESSION = "Undefined expression";
	
	// REQUETES
	
	/**
	 * Le texte associé à l'expression expr dans le langage.
	 * @pre <pre>
	 * 		expr != null
	 * </pre>
	 */
	String getText(ExpressionLanguage expr);
	
	/**
	 * Le langage.
	 */
	SupportedLanguage getLanguage();
	
	// COMMANDES
	
	/**
	 * Change le language utilisé.
	 * @throws IOException 
	 * @pre <pre>
	 * 		l != null
	 * </pre>
	 * @post <pre>
	 * 		for all ExpressionLanguage.values() : e
	 *			getText(e) != null
	 *			getText(e) est une ligne du fichier l.getFile()	 
	 *			(Toutes les expressions d'expressionLanguage ont été initialisé 
	 *				avec chacun une ligne de l.getFile())
	 *			getLanguage() == l
	 * </pre>
	 */
	void changeLanguage(SupportedLanguage l) throws IOException;
}
