package swagger.automate.swagger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.springframework.context.annotation.Description;

import swagger.automate.RestTeste;
import swagger.automate.annotation.Requerido;
import swagger.automate.annotation.Return;
import swagger.automate.annotation.Returns;
import swagger.automate.doc.bean.DocText;
import swagger.automate.swagger.bean.BodyObject;
import swagger.automate.swagger.bean.DocSwagger;
import swagger.automate.swagger.bean.PathData;
import swagger.automate.swagger.bean.Responses;
import swagger.automate.swagger.bean.Tag;
import swagger.automate.swagger.bean.TuplaInBody;
import swagger.automate.util.ReflectionUtil;

public class SwaggerMethods {
	public static DocSwagger readClass(DocSwagger docSwagger) throws NoSuchMethodException, SecurityException {
		Class<RestTeste> ReflectionHelperclass = RestTeste.class;

//		Reflections reflections = new Reflections("swagger.automate.rest", new SubTypesScanner(false));
//		Set<Class<? extends Object>> packs = reflections.getSubTypesOf(Object.class).stream()
//				.collect(Collectors.toSet());
//		List<Class<? extends Object>> asd = packs.stream().filter(a -> a.getPackageName() == "swagger.automate.rest")
//				.collect(Collectors.toList());
		Class[] interfaces = ReflectionHelperclass.getInterfaces();
		for (Class declaredInterface : interfaces) {
			Method[] privateInterfaceMethods = declaredInterface.getMethods();
			for (Method privateInterfaceMethod : privateInterfaceMethods) {
				PathData pathData = new PathData();
				Method implementedMethod = ReflectionHelperclass.getDeclaredMethod(privateInterfaceMethod.getName(),
						privateInterfaceMethod.getParameterTypes());

				for (Annotation annotationOfImplemented : implementedMethod.getDeclaredAnnotations()) {
					if (annotationOfImplemented != null) {
						if (annotationOfImplemented instanceof Path) {
							pathData.setPath(((Path) annotationOfImplemented).value());
							continue;
						}
						if (annotationOfImplemented instanceof Consumes) {
							pathData.setConsumes(((Consumes) annotationOfImplemented)
									.value()[((Consumes) annotationOfImplemented).value().length - 1]);
							continue;
						}
						if (annotationOfImplemented instanceof Produces) {
							pathData.setProduces(((Produces) annotationOfImplemented)
									.value()[((Produces) annotationOfImplemented).value().length - 1]);
							continue;
						}
						/*
						 * Catch the method and parameters
						 */
						if (annotationOfImplemented instanceof POST) {
							pathData.setMethod("POST");
							for (Parameter consume : privateInterfaceMethod.getParameters()) {
								if (docSwagger.getObjects().entrySet().stream()
										.filter(value -> value.getValue().getType() == consume.getType()).findAny()
										.orElse(null) != null) {
									continue;
								}
								BodyObject bodyObject = new BodyObject();
								bodyObject.setNome(consume.getType().getSimpleName());
								bodyObject.setType(consume.getType());
								for (Field field : consume.getType().getDeclaredFields()) {

									TuplaInBody tuplaInBody = ReflectionUtil.tupleFromSomeone(field);
									for (Annotation annotationFiled : field.getDeclaredAnnotations()) {
										if (annotationFiled != null) {
											if (annotationFiled instanceof Requerido) {
												tuplaInBody.setRequired(((Requerido) annotationFiled).value());
												continue;
											}
										}

									}
									bodyObject.getTuplaInBodies().add(tuplaInBody);
								}
								pathData.setConsumesBodyKey(docSwagger.getObjects().size());
								docSwagger.getObjects().put(docSwagger.getObjects().size(), bodyObject);
							}
							continue;
						} else if (annotationOfImplemented instanceof GET) {
							pathData.setMethod("GET");
							for (Parameter parameter : privateInterfaceMethod.getParameters()) {

								TuplaInBody tuplaInBody = ReflectionUtil.tupleFromSomeone(parameter);

								List<TuplaInBody> tuples = new ArrayList();
								tuples.add(tuplaInBody);

								BodyObject bodyObject = new BodyObject();
								bodyObject.setTuplaInBodies(tuples);

								pathData.setConsumesBodyKey(docSwagger.getObjects().size());

								docSwagger.getObjects().put(docSwagger.getObjects().size(), bodyObject);
							}
							continue;
						} else if (annotationOfImplemented instanceof PUT) {
							pathData.setMethod("PUT");
							continue;
						}
					}

				}
				Tag tag = new Tag();
				for (Annotation annotationOfInterface : privateInterfaceMethod.getDeclaredAnnotations()) {
					if (annotationOfInterface != null) {
						if (annotationOfInterface instanceof swagger.automate.annotation.Tag) {
							if (docSwagger.getTags().entrySet().stream().filter(value -> value.getValue()
									.getName().equals(((swagger.automate.annotation.Tag) annotationOfInterface).value()))
									.findAny().orElse(null) != null) {
								continue;
							}
							tag.setName(((swagger.automate.annotation.Tag) annotationOfInterface).value());
							tag.setDescription(((swagger.automate.annotation.Tag) annotationOfInterface).description());
							pathData.setTagKey(docSwagger.getTags().size());
							docSwagger.getTags().put(docSwagger.getTags().size(), tag);
							continue;
						}
						if (annotationOfInterface instanceof Returns) {

							Responses response = new Responses();
							response.setResponses(((Returns) annotationOfInterface).value());

							pathData.setResponses(response);
							continue;
						}
						/*
						 * Catch the return
						 */
						if (annotationOfInterface instanceof Return) {
							Field[] fileds = ((Return) annotationOfInterface).value().getDeclaredFields();
							if (docSwagger.getObjects().entrySet().stream().filter(
									value -> value.getValue().getType() == ((Return) annotationOfInterface).value())
									.findAny().orElse(null) != null) {
								continue;
							}
							BodyObject bodyObject = new BodyObject();
							bodyObject.setNome(((Return) annotationOfInterface).value().getSimpleName());
							bodyObject.setType(((Return) annotationOfInterface).value());
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
							pathData.setProducesBodyKey(docSwagger.getObjects().size());

							docSwagger.getObjects().put(docSwagger.getObjects().size(), bodyObject);
							continue;
						}
					}

				}

				docSwagger.getPathDatas().add(pathData);
			}
		}
		return docSwagger;
	}
}
