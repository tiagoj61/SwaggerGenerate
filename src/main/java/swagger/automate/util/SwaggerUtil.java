package swagger.automate.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import swagger.automate.annotation.Requerido;
import swagger.automate.swagger.bean.BodyObject;
import swagger.automate.swagger.bean.DocSwagger;
import swagger.automate.swagger.bean.Tag;
import swagger.automate.swagger.bean.TuplaInBody;

public class SwaggerUtil {
	public static int verifyIfTagExists(DocSwagger docSwagger, String string) {
		if (docSwagger.getTags().entrySet().stream().filter(value -> value.getValue().getName().equals(string))
				.findAny().orElse(null) != null) {

			return (docSwagger.getTags().entrySet().stream().filter(value -> value.getValue().getName().equals(string))
					.findAny().get().getKey());
		}
		return -1;
	}

	public static int verifyIfObjectExists(DocSwagger docSwagger, Type type) {
		if (docSwagger.getObjects().entrySet().stream().filter(value -> value.getValue().getType() == type).findAny()
				.orElse(null) != null) {
			return (docSwagger.getObjects().entrySet().stream().filter(value -> value.getValue().getType() == type)
					.findAny().get().getKey());
		}
		return -1;
	}

	public static BodyObject createObject(Class type, DocSwagger docSwaggerAux) throws ClassNotFoundException {
		Field[] fileds = type.getDeclaredFields();

		BodyObject bodyObject = new BodyObject();
		bodyObject.setNome(type.getSimpleName());
		bodyObject.setType(type);

		for (Field field : fileds) {

			TuplaInBody tuplaInBody = ReflectionUtil.tupleFromSomeone(field);
			if (tuplaInBody.getReference() != null) {

				docSwaggerAux.getObjects().put(docSwaggerAux.getObjects().size(),
						createObject(tuplaInBody.getReference(), docSwaggerAux));

			}
			for (Annotation annotationOfField : field.getDeclaredAnnotations()) {
				if (annotationOfField != null) {
					if (annotationOfField instanceof Requerido) {
						tuplaInBody.setRequired(((Requerido) annotationOfField).value());
						continue;
					}
				}
			}
			bodyObject.getTuplaInBodies().add(tuplaInBody);
		}

		return bodyObject;
	}

	public static BodyObject generateBodyObject(Class object, boolean isLista) throws ClassNotFoundException {

		Field[] fileds = object.getDeclaredFields();

		BodyObject bodyObject = new BodyObject();
		bodyObject.setNome(object.getSimpleName());
		bodyObject.setType(object);
		bodyObject.setArray(isLista);
		for (Field field : fileds) {

			TuplaInBody tuplaInBody = ReflectionUtil.tupleFromSomeone(field);

			for (Annotation annotationOfField : field.getDeclaredAnnotations()) {
				if (annotationOfField != null) {
					if (annotationOfField instanceof Requerido) {
						tuplaInBody.setRequired(((Requerido) annotationOfField).value());
						continue;
					}
				}
			}
			bodyObject.getTuplaInBodies().add(tuplaInBody);
		}
		return bodyObject;
	}

	public static Tag generateTag(Annotation annotationOfInterface) {
		Tag tag = new Tag();
		tag.setName(((swagger.automate.annotation.Tag) annotationOfInterface).value());
		tag.setDescription(((swagger.automate.annotation.Tag) annotationOfInterface).description());
		return tag;
	}

	public static void storeAllListObjects(DocSwagger docSwagger, BodyObject bodyObject) {

		bodyObject.getTuplaInBodies().forEach(tuple -> {
			DocSwagger docSwaggerAux = new DocSwagger();
			BodyObject bodyObjectArray = null;
			try {

				if (tuple.getReference() != null) {
					bodyObjectArray = SwaggerUtil.createObject(tuple.getReference(), docSwaggerAux);
					int existsObject = SwaggerUtil.verifyIfObjectExists(docSwagger, bodyObjectArray.getType());
					if (existsObject < 0)
						docSwagger.getObjects().put(docSwagger.getObjects().size(), bodyObjectArray);

				}
				docSwaggerAux.getObjects().forEach((index, value) -> {
					int existsObject = SwaggerUtil.verifyIfObjectExists(docSwagger, value.getType());
					if (existsObject < 0)
						docSwagger.getObjects().put(docSwagger.getObjects().size(), value);
				});
				docSwaggerAux = new DocSwagger();

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		});
	}

}
