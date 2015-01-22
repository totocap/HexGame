package util;

public final class Contract {
	
	private Contract() {
		// Non instanciable.
	}
	
	public static void checkCondition(boolean test, String errorString) {
		if (!test) {
			throw new AssertionError(errorString);
		}		
	}
	public static void checkCondition(boolean test) {
		checkCondition(test, "");	
	}
}
