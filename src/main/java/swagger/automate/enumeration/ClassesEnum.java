package swagger.automate.enumeration;

import java.lang.reflect.Type;
import java.util.List;

public enum ClassesEnum {
	INT(int.class), DOUB(double.class), STRING(String.class), LIST(List.class), UNKNOWN(null);

	private final Class<?> targetClass;

	ClassesEnum(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	public static ClassesEnum fromClass(Type type) {
		for (ClassesEnum c : values()) {
			if (c.targetClass == type)
				return c;
		}
		return UNKNOWN;
	}
}