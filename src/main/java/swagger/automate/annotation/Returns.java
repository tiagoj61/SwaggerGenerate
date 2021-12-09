package swagger.automate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import swagger.automate.constants.ReturnConstants;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Returns {
	public int[] value() default {200,400,500};
}
