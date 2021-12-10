package swagger.automate.util;

public class TextUtil {
	public static String replicateString(String str, int quantity) {
		String returnString = "";
		for (int i = 0; i < quantity; i++) {
			returnString += str;
		}
		return returnString;
	}
}
