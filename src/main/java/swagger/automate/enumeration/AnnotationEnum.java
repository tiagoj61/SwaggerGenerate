package swagger.automate.enumeration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import swagger.automate.annotation.ReturnsCods;
import swagger.automate.annotation.Tag;

public enum AnnotationEnum {
	RETURNCODE(ReturnsCods.class), TAG(Tag.class), PATH(Path.class), CONSUMES(Consumes.class), PRODUCES(Produces.class),
	UNKNOWN(null);

	private final Class<?> targetClass;

	AnnotationEnum(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	public static AnnotationEnum fromClass(Class annotationOfImplemented) {
		for (AnnotationEnum c : values()) {
			if (c.targetClass == annotationOfImplemented)
				return c;
		}
		return UNKNOWN;
	}
}