package swagger.automate.swagger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import swagger.automate.annotation.ReturnsCods;
import swagger.automate.enumeration.AnnotationEnum;
import swagger.automate.enumeration.MethodsEnum;
import swagger.automate.rest.impl.RestTeste;
import swagger.automate.swagger.bean.BodyObject;
import swagger.automate.swagger.bean.DocSwagger;
import swagger.automate.swagger.bean.PathData;
import swagger.automate.swagger.bean.ResponseAndCode;
import swagger.automate.swagger.bean.Tag;
import swagger.automate.util.ReflectionUtil;
import swagger.automate.util.SwaggerUtil;

public class SwaggerMethods {

	public static <T> DocSwagger readClass(DocSwagger docSwagger, String packAgeName)
			throws NoSuchMethodException, SecurityException, ClassNotFoundException {

		Reflections reflections = new Reflections(packAgeName, new SubTypesScanner(false));
		Set<Class<? extends Object>> packs = reflections.getSubTypesOf(Object.class).stream()
				.collect(Collectors.toSet());
		List<Class<? extends Object>> asd = packs.stream().filter(a -> a.getPackageName() == packAgeName)
				.collect(Collectors.toList());
		List<Class> interfaces = new ArrayList<Class>();
		asd.forEach(a -> {
			interfaces.add(a.getInterfaces()[0]);
		});

		/*
		 * Iterate on interfaces
		 */
		for (int i = 0; i < asd.size(); i++) {
			Method[] privateInterfaceMethods = interfaces.get(i).getMethods();

			/*
			 * Iterate on interface methods
			 */
			for (Method privateInterfaceMethod : privateInterfaceMethods) {

				PathData pathData = new PathData();

				Method implementedMethod = asd.get(i).getDeclaredMethod(privateInterfaceMethod.getName(),
						privateInterfaceMethod.getParameterTypes());
				/*
				 * Read implemented method of interface method
				 */
				readImplementedMethod(implementedMethod, privateInterfaceMethod, docSwagger, pathData);

				/*
				 * Read interface method
				 */
				readInterfaceMethod(privateInterfaceMethod, docSwagger, pathData);
			}
		}
		return docSwagger;
	}

	public static void readImplementedMethod(Method implementedMethod, Method privateInterfaceMethod,
			DocSwagger docSwagger, PathData pathData) throws ClassNotFoundException {

		/*
		 * Iterate in interface annotations
		 */
		for (Annotation annotationOfImplemented : implementedMethod.getDeclaredAnnotations()) {

			if (annotationOfImplemented != null) {
				switch (AnnotationEnum.fromClass(annotationOfImplemented.annotationType())) {
				case PATH:
					pathData.setPath(((Path) annotationOfImplemented).value());
					break;
				case CONSUMES:
					pathData.setConsumes(((Consumes) annotationOfImplemented)
							.value()[((Consumes) annotationOfImplemented).value().length - 1]);
					break;
				case PRODUCES:
					pathData.setProduces(((Produces) annotationOfImplemented)
							.value()[((Produces) annotationOfImplemented).value().length - 1]);
					break;
				default:
					switch (MethodsEnum.fromClass(annotationOfImplemented.annotationType())) {
					case POST:
						pathData.setMethod("POST");
						for (Parameter consume : privateInterfaceMethod.getParameters()) {

							Class clazz;
							boolean isLista = false;

							if (consume.getType().equals(List.class)) {
								clazz = ReflectionUtil.getGenericFromList(consume.getParameterizedType());
								isLista = true;
							} else {
								clazz = consume.getType();
							}
							int existsObject = SwaggerUtil.verifyIfObjectExists(docSwagger, clazz);
							if (existsObject >= 0) {
								pathData.setProducesBodyKey(existsObject);
								continue;
							}
							BodyObject bodyObject = SwaggerUtil.generateBodyObject(clazz, isLista);
							pathData.setConsumesBodyKey(docSwagger.getObjects().size());

							docSwagger.getObjects().put(docSwagger.getObjects().size(), bodyObject);

							SwaggerUtil.storeAllListObjects(docSwagger, bodyObject);

						}
						break;
					case GET:
						pathData.setMethod("GET");
//						for (Parameter parameter : privateInterfaceMethod.getParameters()) {
//							TuplaInBody tuplaInBody = ReflectionUtil.tupleFromSomeone(parameter);
//							List<TuplaInBody> tuples = new ArrayList();
//							tuples.add(tuplaInBody);
//							BodyObject bodyObject = new BodyObject();
//							bodyObject.setTuplaInBodies(tuples);
//							pathData.setConsumesBodyKey(docSwagger.getObjects().size());
//							docSwagger.getObjects().put(docSwagger.getObjects().size(), bodyObject);
//						}
						break;
					case PUT:
						pathData.setMethod("PUT");
						break;
					default:
						break;
					}
					break;
				}
			}
		}
	}

	private static void readInterfaceMethod(Method privateInterfaceMethod, DocSwagger docSwagger, PathData pathData)
			throws ClassNotFoundException {

		for (Annotation annotationOfInterface : privateInterfaceMethod.getDeclaredAnnotations()) {

			if (annotationOfInterface != null) {
				switch (AnnotationEnum.fromClass(annotationOfInterface.annotationType())) {
				case TAG:
 					int existsObject = SwaggerUtil.verifyIfTagExists(docSwagger,
							((swagger.automate.annotation.Tag) annotationOfInterface).value());
					if (existsObject >= 0) {

						pathData.setTagKey(existsObject);
						continue;
					}

					Tag tag = SwaggerUtil.generateTag(annotationOfInterface);
					pathData.setTagKey(docSwagger.getTags().size());
					docSwagger.getTags().put(docSwagger.getTags().size(), tag);
					break;
				case RETURNCODE:

					List<ResponseAndCode> reponses = new ArrayList<>();

					var returnsAndCode = ((ReturnsCods) annotationOfInterface).value();

					/*
					 * Iterate in returns
					 */
					for (int i = 0; i < returnsAndCode.length; i++) {

						var response = new ResponseAndCode();
						var annotationInterfaceClass = ((ReturnsCods) annotationOfInterface).value()[i].object();
						if (annotationInterfaceClass != InternalError.class) {

							existsObject = SwaggerUtil.verifyIfObjectExists(docSwagger, annotationInterfaceClass);
							if (existsObject >= 0) {

								pathData.setProducesBodyKey(existsObject);
								continue;
							} else {

								BodyObject bodyObject = SwaggerUtil.generateBodyObject(annotationInterfaceClass, false);

								response.setProducesBodyKey(docSwagger.getObjects().size());
								response.setArray(returnsAndCode[i].array());
								docSwagger.getObjects().put(docSwagger.getObjects().size(), bodyObject);

								SwaggerUtil.storeAllListObjects(docSwagger, bodyObject);

							}
						} else {

							response.setProducesBodyKey(-1);

						}
						response.setResponseCode(returnsAndCode[i].code());
						response.setArray(returnsAndCode[i].array());
						reponses.add(response);

					}
					pathData.setResponseAndCodes(reponses);
					break;
				default:
					break;
				}

			}

		}
		docSwagger.getPathDatas().add(pathData);

	}

}
