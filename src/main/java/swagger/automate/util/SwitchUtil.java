package swagger.automate.util;

import java.lang.reflect.Type;

import swagger.automate.enumeration.Classes;

public class SwitchUtil {
	public static String convertTypeToJson(Type type) {
		switch (Classes.fromClass(type)) {
		case INT:
			return "integer";
		case DOUB:
			return "number";
		case STRING:
			return "string";
		default:
			return "string";
		}
	}
	public static String genereteExampleByType(Type type) {
		switch (Classes.fromClass(type)) {
		case INT:
			return "42";
		case DOUB:
			return "4.2";
		case STRING:
			return "\"Texto de exemplo\"";
		default:
			return "\"Texto\"";
		}
	}
}
