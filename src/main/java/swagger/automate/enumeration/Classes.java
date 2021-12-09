package swagger.automate.enumeration;

import java.lang.reflect.Type;

public enum Classes {
	INT(int.class), DOUB(double.class), STRING(String.class), UNKNOWN(null);

	private final Class<?> targetClass;

	Classes(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	public static Classes fromClass(Type type) {
		for (Classes c : values()) {
			if (c.targetClass == type)
				return c;
		}
		return UNKNOWN;
	}
}