package swagger.automate.util;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

import swagger.automate.swagger.bean.TuplaInBody;

public class ReflectionUtil {
	public static <T> TuplaInBody tupleFromSomeone(T field) {
		TuplaInBody tuplaInBody = new TuplaInBody();
		if (field instanceof Field) {
			tuplaInBody.setName(((Field) field).getName());
			tuplaInBody.setType(((Field) field).getType());
			tuplaInBody.setExample(SwitchUtil.genereteExampleByType(((Field) field).getType()));
		} else if (field instanceof Parameter) {
			tuplaInBody.setName(((Parameter) field).getName());
			tuplaInBody.setType(((Parameter) field).getType());
			tuplaInBody.setExample(SwitchUtil.genereteExampleByType(((Field) field).getType()));

		}

		return tuplaInBody;
	}

}
