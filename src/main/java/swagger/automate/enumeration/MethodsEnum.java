package swagger.automate.enumeration;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;

public enum MethodsEnum {
	POST(POST.class), GET(GET.class), PUT(PUT.class), UNKNOWN(null);

	private final Class<?> targetClass;

	MethodsEnum(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	public static MethodsEnum fromClass(Class<?> cls) {
		for (MethodsEnum c : values()) {
			if (c.targetClass == cls)
				return c;
		}
		return UNKNOWN;
	}
}