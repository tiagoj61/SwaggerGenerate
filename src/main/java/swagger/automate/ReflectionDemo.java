package swagger.automate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.context.annotation.Description;

import swagger.automate.annotation.Requerido;
import swagger.automate.annotation.Return;
import swagger.automate.annotation.Returns;
import swagger.automate.body.BodyObject;
import swagger.automate.body.TuplaInBody;
import swagger.automate.constants.DocConstants;
import swagger.automate.doc.DocText;
import swagger.automate.operationutil.ReflectionUtil;
import swagger.automate.operationutil.TextUtil;
import swagger.automate.operationutil.TypeUtil;
import swagger.automate.path.PathData;
import swagger.automate.response.Responses;
import swagger.automate.tag.Tag;

public class ReflectionDemo {

	public static void main(String[] args) throws NoSuchFieldException, SecurityException, NoSuchMethodException {

		System.gc();

		/*
		 * Itereate in package rest
		 */
		Class<RestTeste> ReflectionHelperclass = RestTeste.class;

		List<PathData> pathDatas = new ArrayList<>();
		HashMap<Integer, BodyObject> objects = new HashMap<>();
		HashMap<Integer, Tag> tags = new HashMap<>();

		/*
		 * Doc text variables
		 */
		DocText docText = new DocText();

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
								if (objects.entrySet().stream()
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
								pathData.setConsumesBodyKey(objects.size());
								objects.put(objects.size(), bodyObject);
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

								pathData.setConsumesBodyKey(objects.size());

								objects.put(objects.size(), bodyObject);
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
							tag.setName(((swagger.automate.annotation.Tag) annotationOfInterface).value());
							continue;
						}
						if (annotationOfInterface instanceof Description) {
							tag.setDescription(((Description) annotationOfInterface).value());
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
							if (objects.entrySet().stream().filter(
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
							pathData.setProducesBodyKey(objects.size());

							objects.put(objects.size(), bodyObject);
							continue;
						}
					}

				}
				pathData.setTagKey(tags.size());
				tags.put(tags.size(), tag);
				pathDatas.add(pathData);
			}
		}
		StringBuilder header = new StringBuilder("swagger: \"2.0\"").append("\n");

		header.append("info:").append("\n");
		header.append(TextUtil.replicateString(DocConstants.SPACE, 1))
				.append("description: \"Documento referente ao rest do sistema Ponto Security\"").append("\n");// TODO:
																												// define
																												// the
																												// desc
		header.append(TextUtil.replicateString(DocConstants.SPACE, 1)).append("version: \"1.0.0\"").append("\n");// TODO:
																													// define
																													// the
																													// version
		header.append(TextUtil.replicateString(DocConstants.SPACE, 1)).append("title: \"Ponto Security rest\"")
				.append("\n");// TODO: define the title
		header.append("host: \"audax.mobi\"").append("\n");
		header.append("basePath: \"/rest/\"").append("\n");
		header.append("tags:").append("\n");

		tags.forEach((key, value) -> {
			header.append("- name:").append(" \"" + value.getName() + "\"").append("\n");
			header.append(TextUtil.replicateString(DocConstants.SPACE, 1)).append("description: ")
					.append("\"" + value.getDescription() + "\"").append("\n");
		});

		header.append("schemes:").append("\n");
		header.append("- \"http\"").append("\n");
		docText.setHeader(header);
		System.out.println(header);

		StringBuilder paths = new StringBuilder("paths:").append("\n");
		pathDatas.forEach(pathData -> {
			paths.append(TextUtil.replicateString(DocConstants.SPACE, 1)).append("/").append(pathData.getPath() + ":")
					.append("\n");
			paths.append(TextUtil.replicateString(DocConstants.SPACE, 2))
					.append(pathData.getMethod().toLowerCase() + ":").append("\n");

			paths.append(TextUtil.replicateString(DocConstants.SPACE, 3)).append("tags:").append("\n");
			paths.append(TextUtil.replicateString(DocConstants.SPACE, 3))
					.append("- \"" + tags.get(pathData.getTagKey()).getName() + "\"").append("\n");
			paths.append(TextUtil.replicateString(DocConstants.SPACE, 3))
					.append("summary: \"Loggar com o funcionario\"").append("\n");// TODO:
			paths.append(TextUtil.replicateString(DocConstants.SPACE, 3)).append("description: \"Entrar no sistema\"")
					.append("\n");// TODO:
			paths.append(TextUtil.replicateString(DocConstants.SPACE, 3)).append("operationId: \"funclogin\"")
					.append("\n");// TODO:

			paths.append(TextUtil.replicateString(DocConstants.SPACE, 3)).append("consumes:").append("\n");
			paths.append(TextUtil.replicateString(DocConstants.SPACE, 3)).append("- \"" + pathData.getConsumes() + "\"")
					.append("\n");
			paths.append(TextUtil.replicateString(DocConstants.SPACE, 3)).append("produces:").append("\n");
			paths.append(TextUtil.replicateString(DocConstants.SPACE, 3)).append("- \"" + pathData.getProduces() + "\"")
					.append("\n");

			paths.append(TextUtil.replicateString(DocConstants.SPACE, 3)).append("parameters:").append("\n");
			paths.append(TextUtil.replicateString(DocConstants.SPACE, 3))
					.append("- in: \"" + (pathData.getMethod() == "POST" ? "body" : "path") + "\"").append("\n");
			paths.append(TextUtil.replicateString(DocConstants.SPACE, 4))
					.append("name: \"" + (pathData.getMethod() == "POST" ? "body" : "path") + "\"").append("\n");
			paths.append(TextUtil.replicateString(DocConstants.SPACE, 4))
					.append("description: \"Aparelhos bluetooth e senha do funcionario\"").append("\n");
			paths.append(TextUtil.replicateString(DocConstants.SPACE, 4)).append("required: true").append("\n");
			paths.append(TextUtil.replicateString(DocConstants.SPACE, 4)).append("schema:").append("\n");
			// paths.append(TextUtil.replicateString(DocConstants.SPACE, 5)).append("$ref:
			// \"#/definitions/"+objects.get(pathData.getConsumesBodyKey())+"\"").append("\n");//
			// TODO: define the name
			paths.append(TextUtil.replicateString(DocConstants.SPACE, 5))
					.append("$ref: \"#/definitions/" + objects.get(pathData.getProducesBodyKey()).getNome() + "\"")
					.append("\n");// TODO: define the name

			paths.append(TextUtil.replicateString(DocConstants.SPACE, 3)).append("responses:").append("\n");

			Arrays.stream(pathData.getResponses().getResponses()).forEach(response -> {
				paths.append(TextUtil.replicateString(DocConstants.SPACE, 4)).append("\"" + response + "\":")
						.append("\n");
				paths.append(TextUtil.replicateString(DocConstants.SPACE, 5)).append("description: \"desc\"")
						.append("\n");// TODO: desc response
			});

		});
		docText.setPaths(paths);
		System.out.println(paths);

		StringBuilder definitions = new StringBuilder("definitions:").append("\n");
		objects.forEach((key, value) -> {
			definitions.append(TextUtil.replicateString(DocConstants.SPACE, 1)).append(value.getNome()).append(":")
					.append("\n");// TODO:
			// define
			// the
			// name
			definitions.append(TextUtil.replicateString(DocConstants.SPACE, 2)).append("type: \"object\"").append("\n");// type
			definitions.append(TextUtil.replicateString(DocConstants.SPACE, 2)).append("required:").append("\n");// Required

			value.getTuplaInBodies().stream().filter(tuple -> tuple.getRequired() == true).collect(Collectors.toList())
					.forEach(tuple -> {
						definitions.append(TextUtil.replicateString(DocConstants.SPACE, 2))
								.append("- \"" + tuple.getName() + "\"").append("\n");// Required Fields
					});
			definitions.append(TextUtil.replicateString(DocConstants.SPACE, 2)).append("properties:").append("\n");// Properties
			value.getTuplaInBodies().forEach(tuple -> {
				definitions.append(TextUtil.replicateString(DocConstants.SPACE, 3)).append("" + tuple.getName() + ":")
						.append("\n");// Fields
				definitions.append(TextUtil.replicateString(DocConstants.SPACE, 4)).append("type: ")
						.append("\"" + TypeUtil.convertTypeToJson(tuple.getType()) + "\"").append("\n");// Fields

				definitions.append(TextUtil.replicateString(DocConstants.SPACE, 4)).append("example: ")
						.append(tuple.getExample()).append("\n");// Fields
			});
		});
		docText.setDefinitions(definitions);
		System.out.println(definitions);
	}

}