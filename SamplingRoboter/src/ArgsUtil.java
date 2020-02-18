
public class ArgsUtil {

	private String[] args = null;
	
	public ArgsUtil(String[] args) {
		this.args = args;
	}
	
	/**
	 * Checks if the test string is in the arguments list
	 * @param teststr The string to be tested
	 * @return True if test string is found, False otherwise
	 */
	public boolean check(String teststr) {
		for (int i=0; i<args.length; i++) {
			if(args[i].equals(teststr))
				return true;
		}
		return false;
	}
	
	/**
	 * Checks if any string from the arguments list starts with test string.
	 * @param teststr The string to be tested
	 * @return If a suiting argument is found, the remaining string is returned. Else null.
	 */
	public String get(String teststr) {
		for (int i=0; i<args.length; i++) {
			if(args[i].startsWith(teststr)) {
				return args[i].replaceFirst(teststr, "");
			}
		}
		return null;
	}
}
