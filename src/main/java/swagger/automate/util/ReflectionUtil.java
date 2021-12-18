package swagger.automate.util;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import swagger.automate.swagger.bean.TuplaInBody;

public class ReflectionUtil {
	public static <T> TuplaInBody tupleFromSomeone(T field) throws ClassNotFoundException {
		TuplaInBody tuplaInBody = new TuplaInBody();
		if (field instanceof Field) {
			// field).getType().getName()).isInstance(List.class));
			// field).getType().getName()).isInstance(List.class));
//			if(((Field) field).getType() instanceof List) {

			// }
			if (!((Field) field).getType().equals(List.class)) {
				tuplaInBody.setName(((Field) field).getName());
				tuplaInBody.setType(((Field) field).getType());
				tuplaInBody.setExample(SwitchUtil.genereteExampleByType(((Field) field).getType()));

			} else {
				tuplaInBody.setName(((Field) field).getName());
				tuplaInBody.setType(((Field) field).getType());

				tuplaInBody.setReference(getGeneric(((Field) field)));
			}
		} else if (field instanceof Parameter) {
			tuplaInBody.setName(((Parameter) field).getName());
			tuplaInBody.setType(((Parameter) field).getType());
			tuplaInBody.setExample(SwitchUtil.genereteExampleByType(((Parameter) field).getType()));
		}
		return tuplaInBody;
	}

	public static Class getGeneric(Field field) {
		Field stringListField = field;
		ParameterizedType stringListType = (ParameterizedType) stringListField.getGenericType();
		Class<?> stringListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
		return stringListClass;
	}
}
