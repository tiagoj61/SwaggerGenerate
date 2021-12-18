package swagger.automate.swagger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
import swagger.automate.ReflectionHelper2;
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
	private static DocSwagger docSwaggerAux = new DocSwagger();

	public static DocSwagger readClass(DocSwagger docSwagger)
			throws NoSuchMethodException, SecurityException, ClassNotFoundException {
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

							if (docSwagger.getObjects().entrySet().stream().filter(
									value -> value.getValue().getType() == ((Return) annotationOfInterface).value())
									.findAny().orElse(null) != null) {
								pathData.setProducesBodyKey(docSwagger.getObjects().entrySet().stream().filter(
										value -> value.getValue().getType() == ((Return) annotationOfInterface).value())
										.findAny().get().getKey());
								continue;
							}
							Field[] fileds = ((Return) annotationOfInterface).value().getDeclaredFields();
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
								if (tuplaInBody.getReference() != null) {
									BodyObject bo = createObject(tuplaInBody.getReference());

									docSwagger.getObjects().put(docSwagger.getObjects().size(), bo);
								}
								bodyObject.getTuplaInBodies().add(tuplaInBody);
							}
							pathData.setProducesBodyKey(docSwagger.getObjects().size());

							docSwagger.getObjects().put(docSwagger.getObjects().size(), bodyObject);
							continue;
						}
						if (annotationOfInterface instanceof ReturnsCods) {
							// getReturnsAndCode();
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
										response.setArray(returnsAndCode[i].array());

										docSwagger.getObjects().put(docSwagger.getObjects().size(), bodyObject);

										bodyObject.getTuplaInBodies().forEach(q -> {
											System.out.println("-------vaolta----------");
											BodyObject bo = null;
											try {

												if (q.getReference() != null) {
													bo = createObject(q.getReference());
													docSwagger.getObjects().put(docSwagger.getObjects().size(), bo);

												}
												System.out.println(docSwagger.getObjects().get(docSwagger.getObjects().size()-1).getNome());
												System.out.println("tam"+docSwaggerAux.getObjects().size());
												docSwaggerAux.getObjects().forEach((qwe, aux) -> {
													System.out.println(aux.getNome());
													docSwagger.getObjects().put(docSwagger.getObjects().size(), aux);
												});
												docSwaggerAux=new DocSwagger();

											} catch (ClassNotFoundException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										});

									}
								} else {
									response.setProducesBodyKey(-1);
								}
								response.setResponseCode(returnsAndCode[i].code());
								response.setArray(returnsAndCode[i].array());
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

	private static BodyObject createObject(Class type) throws ClassNotFoundException {
		System.out.println("tipo: "+type.getName());
		Field[] fileds = type.getDeclaredFields();

		BodyObject bodyObject = new BodyObject();
		bodyObject.setNome(type.getSimpleName());
		bodyObject.setType(type);

		for (Field field : fileds) {

			TuplaInBody tuplaInBody = ReflectionUtil.tupleFromSomeone(field);
			if (tuplaInBody.getReference() != null) {

				docSwaggerAux.getObjects().put(docSwaggerAux.getObjects().size(),
						createObject(tuplaInBody.getReference()));
				System.out.println("printou : "+ docSwaggerAux.getObjects().get(0).getNome());

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

	public static BodyObject generateBodyObject(Class object) throws ClassNotFoundException {

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

	public Class getGeneric(Field field) {
		Field stringListField = field;
		ParameterizedType stringListType = (ParameterizedType) stringListField.getGenericType();
		Class<?> stringListClass = (Class<?>) stringListType.getActualTypeArguments()[0];
		return stringListClass;
	}
}
