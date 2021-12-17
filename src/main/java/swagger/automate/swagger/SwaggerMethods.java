package swagger.automate.swagger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import swagger.automate.ReflectionHelper;
import swagger.automate.RestTeste;
import swagger.automate.annotation.Requerido;
import swagger.automate.annotation.Return;
import swagger.automate.annotation.Returns;
import swagger.automate.annotation.ReturnsCods;
import swagger.automate.swagger.bean.BodyObject;
import swagger.automate.swagger.bean.DocSwagger;
import swagger.automate.swagger.bean.PathData;
import swagger.automate.swagger.bean.ResponseAndCode;
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

									pathData.setProducesBodyKey(docSwagger.getObjects().entrySet().stream()
											.filter(value -> value.getValue().getType() == consume.getType()).findAny()
											.get().getKey());
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
//							for (Parameter parameter : privateInterfaceMethod.getParameters()) {
//
//								TuplaInBody tuplaInBody = ReflectionUtil.tupleFromSomeone(parameter);
//
//								List<TuplaInBody> tuples = new ArrayList();
//								tuples.add(tuplaInBody);
//
//								BodyObject bodyObject = new BodyObject();
//								bodyObject.setTuplaInBodies(tuples);
//
//								pathData.setConsumesBodyKey(docSwagger.getObjects().size());
//
//								docSwagger.getObjects().put(docSwagger.getObjects().size(), bodyObject);
//							}
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
							if (docSwagger.getTags().entrySet().stream()
									.filter(value -> value.getValue().getName()
											.equals(((swagger.automate.annotation.Tag) annotationOfInterface).value()))
									.findAny().orElse(null) != null) {
								pathData.setTagKey(docSwagger.getTags().entrySet().stream()
										.filter(value -> value.getValue().getName().equals(
												((swagger.automate.annotation.Tag) annotationOfInterface).value()))
										.findAny().get().getKey());
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
								pathData.setProducesBodyKey(docSwagger.getObjects().entrySet().stream().filter(
										value -> value.getValue().getType() == ((Return) annotationOfInterface).value())
										.findAny().get().getKey());
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
						if (annotationOfInterface instanceof ReturnsCods) {
							List<ResponseAndCode> reponses = new ArrayList<>();
							var returnsAndCode = ((ReturnsCods) annotationOfInterface).value();
							for (int i = 0; i < returnsAndCode.length; i++) {
								var response = new ResponseAndCode();
								if (((ReturnsCods) annotationOfInterface).value()[i].object() != InternalError.class) {

									Field[] fileds = ((ReturnsCods) annotationOfInterface).value()[i].object()
											.getDeclaredFields();

//									Field integerListField = Test.class.getDeclaredField("integerList");
//									ParameterizedType integerListType = (ParameterizedType) integerListField
//											.getGenericType();
//									Class<?> integerListClass = (Class<?>) integerListType.getActualTypeArguments()[0];
//									System.out.println(integerListClass); // class java.lang.Integer.

									int a = i;
									if (docSwagger.getObjects().entrySet().stream().filter(value -> value.getValue()
											.getType() == ((ReturnsCods) annotationOfInterface).value()[a].object())
											.findAny().orElse(null) != null) {
										
										pathData.setProducesBodyKey(docSwagger.getObjects().entrySet().stream()
												.filter(value -> value.getValue()
														.getType() == ((ReturnsCods) annotationOfInterface).value()[a]
																.object())
												.findAny().get().getKey());
										
									} else {

										BodyObject bodyObject = generateBodyObject(
												((ReturnsCods) annotationOfInterface).value()[a].object());

										response.setProducesBodyKey(docSwagger.getObjects().size());

										docSwagger.getObjects().put(docSwagger.getObjects().size(), bodyObject);

									}
								} else {
									response.setProducesBodyKey(-1);
								}
								response.setResponseCode(returnsAndCode[i].code());
								reponses.add(response);

							}
							pathData.setResponseAndCodes(reponses);
						}
					}

				}

				docSwagger.getPathDatas().add(pathData);
			}
		}
		return docSwagger;
	}

	public static BodyObject generateBodyObject(Class object) {
		Field[] fileds = object.getDeclaredFields();

		BodyObject bodyObject = new BodyObject();
		bodyObject.setNome(object.getSimpleName());
		bodyObject.setType(object);
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

//	public static <E, T> BodyObject generateBodyObjectList(Class object) {
//		
//		
//		
//		List<ReflectionHelper> list = new ArrayList<ReflectionHelper>();
//		Iterator it = list.iterator();
//		System.out.println("ite");
//		System.out.println(it);
//		// if (it.hasNext()) {
//		System.out.println("----it---");
//		System.out.println(it.next().getClass());
//		// }
//		System.out.println(list.getClass());
//		System.out.println(list.getClass().getGenericSuperclass());
//		System.out.println(((ParameterizedType) list.getClass().getGenericSuperclass()).getActualTypeArguments());
//		System.out.println(((ParameterizedType) list.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
//		System.out.println((ReflectionHelper) (((ParameterizedType) list.getClass().getGenericSuperclass())
//				.getActualTypeArguments()[0]));
//		Field[] fileds = object.getDeclaredFields();
//
//		BodyObject bodyObject = new BodyObject();
//		bodyObject.setNome(object.getSimpleName());
//		bodyObject.setType(object);
//		for (Field field : fileds) {
//
//			TuplaInBody tuplaInBody = ReflectionUtil.tupleFromSomeone(field);
//
//			for (Annotation annotationOfField : field.getDeclaredAnnotations()) {
//				if (annotationOfField != null) {
//					if (annotationOfField instanceof Requerido) {
//						tuplaInBody.setRequired(((Requerido) annotationOfField).value());
//						continue;
//					}
//				}
//			}
//			bodyObject.getTuplaInBodies().add(tuplaInBody);
//		}
//		return bodyObject;
//	}
	public Class getGeneric(Field field) {
		Field stringListField = field;
		ParameterizedType stringListType = (ParameterizedType) stringListField.getGenericType();
		Class<?> stringListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
		System.out.println(stringListClass); // class java.lang.String.
		return stringListClass;
	}
}
