package swagger.automate.enumeration;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;

public enum Methods {
	POST(POST.class), GET(GET.class), PUT(PUT.class), UNKNOWN(null);

	private final Class<?> targetClass;

	Methods(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	public static Methods fromClass(Class<?> cls) {
		for (Methods c : values()) {
			if (c.targetClass == cls)
				return c;
		}
		return UNKNOWN;
	}
}