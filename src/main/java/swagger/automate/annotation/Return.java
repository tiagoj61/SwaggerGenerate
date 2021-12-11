package swagger.automate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import swagger.automate.ReflectionHelper;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Return {
	Class value();
}
