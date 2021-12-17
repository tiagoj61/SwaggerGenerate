package swagger.automate.swagger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
										
										BodyObject bodyObject = generateBodyObject(((ReturnsCods) annotationOfInterface).value()[a].object());
											
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
		List<ReflectionHelper> a = new ArrayList<ReflectionHelper>();
		ReflectionHelper w= new ReflectionHelper();
		w.setAge(123);
		w.setDeptName("aqui");
		w.setName("adfasd");
		a.add(w);
		generateBodyObjectList(a.getClass());
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
	static <E> Class<E> getClassE(List<E> list) {
	    Class<?> listClass = list.getClass();

	    Type gSuper = listClass.getGenericSuperclass();
	    if(!(gSuper instanceof ParameterizedType))
	        throw new IllegalArgumentException();

	    ParameterizedType pType = (ParameterizedType)gSuper;

	    Type tArg = pType.getActualTypeArguments()[0];
	    if(!(tArg instanceof Class<?>))
	        throw new IllegalArgumentException();

	    @SuppressWarnings("unchecked")
	    final Class<E> classE = (Class<E>)tArg;
	    System.out.println(classE);
	    return classE;
	}
	public static <E> BodyObject generateBodyObjectList(Class object) {
		System.out.println("aDASD");
		System.out.println(object.getGenericSuperclass());
		getClassE((List<E>) object.getClass());
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
}
