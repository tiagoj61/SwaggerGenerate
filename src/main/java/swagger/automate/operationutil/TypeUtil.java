package swagger.automate.operationutil;

import java.lang.reflect.Type;

import swagger.automate.enumeration.Classes;

public class TypeUtil {
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
			return "1";
		case DOUB:
			return "1.1";
		case STRING:
			return "\"example\"";
		default:
			return "\"example\"";
		}
	}
}
