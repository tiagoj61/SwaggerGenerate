package swagger.automate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CodeAndReturn {
	public int code();

	public Class object() default InternalError.class;
	public boolean array() default false;
}
