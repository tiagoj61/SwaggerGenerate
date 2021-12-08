package swagger.automate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.context.annotation.Description;

import swagger.automate.annotation.Requerido;
import swagger.automate.annotation.Return;
import swagger.automate.body.BodyObject;
import swagger.automate.body.TuplaInBody;
import swagger.automate.path.PathData;
import swagger.automate.tag.Tag;

public class ReflectionDemo {

	public static void main(String[] args) throws NoSuchFieldException, SecurityException, NoSuchMethodException {

		System.gc();

		Class<RestTeste> ReflectionHelperclass = RestTeste.class;

		List<PathData> pathDatas = new ArrayList<>();

		Class[] objInterface = ReflectionHelperclass.getInterfaces();
		for (Class citem : objInterface) {
			Method[] privatefields = citem.getMethods();
			for (Method onefield : privatefields) {
				PathData pathData = new PathData();
				Method implementd = ReflectionHelperclass.getDeclaredMethod(onefield.getName(),
						onefield.getParameterTypes());

				for (Annotation a : implementd.getDeclaredAnnotations()) {
					if (a != null) {
						if (a instanceof Path) {
							System.out.println("-> Path ::: " + ((Path) a).value());
							pathData.setPath(((Path) a).value());
							continue;
						}
						if (a instanceof Consumes) {
							System.out.println(
									"-> Consume ::: " + ((Consumes) a).value()[((Consumes) a).value().length - 1]);
							pathData.setConsumes(((Consumes) a).value()[((Consumes) a).value().length - 1]);
							continue;
						}
						if (a instanceof Produces) {
							System.out.println(
									"-> Produces ::: " + ((Produces) a).value()[((Produces) a).value().length - 1]);
							pathData.setProduces(((Produces) a).value()[((Produces) a).value().length - 1]);
							continue;
						}
						if (a instanceof POST) {
							System.out.println("-> Metodo ::: POST");
							pathData.setMethod("POST");
							List<BodyObject> bodyConsumes = new ArrayList<>();
							for (Parameter q : onefield.getParameters()) {
								BodyObject bodyObject = new BodyObject();
								for (Field q1 : q.getType().getDeclaredFields()) {
									TuplaInBody tuplaInBody = new TuplaInBody();
									tuplaInBody.setName(q1.getName());
									tuplaInBody.setType(q1.getType().getName());
									System.out.println("-> Body Name :::" + q1.getName());
									System.out.println("-> Tipo :::" + q1.getType().getName());
									for (Annotation c : q1.getDeclaredAnnotations()) {
										if (c != null) {
											if (c instanceof Requerido) {
												tuplaInBody.setRequired(((Requerido) c).value());
												System.out.println("-> Requerido ::: " + ((Requerido) c).value());
												continue;
											}
										}
										bodyObject.getTuplaInBodies().add(tuplaInBody);
									}
									bodyConsumes.add(bodyObject);
								}

							}
							pathData.setConsumesBody(bodyConsumes);
							continue;
						} else if (a instanceof GET) {
							pathData.setMethod("GET");
							System.out.println("-> Metodo ::: GET");
							List<BodyObject> bodyConsumes = new ArrayList<>();
							for (Parameter q : onefield.getParameters()) {
								System.out.println("-> Path Nome :::" + q.getName());
								System.out.println("-> Tipo :::" + q.getType().getName());
								TuplaInBody tuplaInBody = new TuplaInBody();
								tuplaInBody.setName(q.getName());
								tuplaInBody.setType(q.getType().getName());
								List<TuplaInBody> t = new ArrayList();
								t.add(tuplaInBody);
								BodyObject b = new BodyObject();
								b.setTuplaInBodies(t);
								bodyConsumes.add(b);
							}
							pathData.setConsumesBody(bodyConsumes);
							continue;
						} else if (a instanceof PUT) {
							pathData.setMethod("PUT");
							System.out.println("-> Metodo ::: PUT");
							continue;
						}
					}

				}
				// PathData pathData = new PathData();
				Tag tag = new Tag();
				for (Annotation a : onefield.getDeclaredAnnotations()) {
					if (a != null) {
						if (a instanceof swagger.automate.annotation.Tag) {
							System.out.println("-> Tag ::: " + ((swagger.automate.annotation.Tag) a).value());
							tag.setName(((swagger.automate.annotation.Tag) a).value());
							continue;
						}
						if (a instanceof Description) {
							System.out.println("-> Description ::: " + ((Description) a).value());
							tag.setDescription(((Description) a).value());
							continue;
						}
						if (a instanceof Return) {
							List<BodyObject> bodyProduces = new ArrayList<>();
							Field[] qq = ((Return) a).value().getDeclaredFields();
							BodyObject bodyObject = new BodyObject();
							for (Field q : qq) {
								TuplaInBody tuplaInBody = new TuplaInBody();
								tuplaInBody.setName(q.getName());
								tuplaInBody.setType(q.getType().getName());
								System.out.println("-> Body Name :::" + q.getName());
								System.out.println("-> Tipo :::" + q.getType().getName());
								for (Annotation c : q.getDeclaredAnnotations()) {
									if (c != null) {
										if (c instanceof Requerido) {
											tuplaInBody.setRequired(((Requerido) c).value());
											System.out.println("-> Requerido ::: " + ((Requerido) c).value());
											continue;
										}
									}
								}
								bodyObject.getTuplaInBodies().add(tuplaInBody);
							}

							bodyProduces.add(bodyObject);
							pathData.setProducesBody(bodyProduces);
							continue;
						}
					}
					
				}
				pathData.setTag(tag);
				pathDatas.add(pathData);
			}
		}
		System.out.println(pathDatas.size());
		System.out.println("swagger: \"2.0\"\r\n" + "info:\r\n"
				+ "  description: \"Documento referente ao rest do sistema Serquipe\"\r\n" + "  version: \"1.0.0\"\r\n"
				+ "  title: \"Sequipe rest\"\r\n" + "host: \"serquip.audax.mobi\"\r\n" + "basePath: \"/rest/\"\r\n"
				+ "tags:");
		pathDatas.forEach(p -> {

			System.out.println("- name: \"" + p.getTag().getName());
			System.out.println("  description: \"ADASD\"");
			System.out.println("schemes:\r\n" + "- \"https\"");
			System.out.println("paths:");
			System.out.println("  " + p.getPath() + ":");
			System.out.println("    " + p.getMethod() + ":");
			System.out.println("      tags:");
			System.out.println("      -\"" + p.getTag() + "\"");
			System.out.println("      description: \"Entrar no sistema\"");
			System.out.println("      operationId: \"Entrar no sistema\"");
			System.out.println("      consumes:");
			System.out.println("      -\"" + p.getConsumes() + "\"");
			System.out.println("      produces:");
			System.out.println("      -\"" + p.getProduces() + "\"");
			System.out.println("      parameters:");
			System.out.println("      - in: \"body\"\r\n"
					+ "        name: \"body\"\r\n"
					+ "        description: \"Dados do usuario\"\r\n"
					+ "        required: true");
			System.out.println("      schema:");
			System.out.println("        $ref: \"#/definitions/Usuario\"");
			System.out.println("definitions:");
			System.out.println("  Usuario:");
			System.out.println("    type: \"object\":");
			System.out.println("     required:\r\n"
					+ "    - \"cpf\"\r\n"
					+ "    - \"senha\"");
			System.out.println("     properties:");
			p.getProducesBody().forEach(q->{
				q.getTuplaInBodies().forEach(q1->{
					System.out.println("      "+q1.getName());
					System.out.println("      "+q1.getType());
				});
				
			});
			


		});
//		Method[] privatefields = ReflectionHelperclass.getDeclaredMethods();
//		for (Method onefield : privatefields) {
//
//			for (Annotation a : onefield.getDeclaredAnnotations()) {
//				if (a != null) {
//					if (a instanceof Path) {
//						System.out.println("-> Path ::: " + ((Path) a).value());
//						continue;
//					}
//					if (a instanceof Consumes) {
//						System.out
//								.println("-> Consume ::: " + ((Consumes) a).value()[((Consumes) a).value().length - 1]);
//						continue;
//					}
//					if (a instanceof Produces) {
//						System.out.println(
//								"-> Produces ::: " + ((Produces) a).value()[((Produces) a).value().length - 1]);
//						continue;
//					}
//					if (a instanceof POST) {
//						System.out.println("-> Metodo ::: POST");
//						for (Parameter q : onefield.getParameters()) {
//							BodyObject bodyObject = new BodyObject();
//							for (Field q1 : q.getType().getDeclaredFields()) {
//								TuplaInBody tuplaInBody = new TuplaInBody();
//								tuplaInBody.setName(q1.getName());
//								tuplaInBody.setType(q1.getType().getName());
//								System.out.println("-> Body Name :::" + q1.getName());
//								System.out.println("-> Tipo :::" + q1.getType().getName());
//								for (Annotation c : q1.getDeclaredAnnotations()) {
//									if (c != null) {
//										if (c instanceof Requerido) {
//											tuplaInBody.setRequired(((Requerido) c).value());
//											System.out.println("-> Requerido ::: " + ((Requerido) c).value());
//											continue;
//										}
//									}
//									bodyObject.getTuplaInBodies().add(tuplaInBody);
//								}
//								bodyConsumes.add(bodyObject);
//							}
//
//						}
//						continue;
//					} else if (a instanceof GET) {
//						System.out.println("-> Metodo ::: GET");
//						for (Parameter q : onefield.getParameters()) {
//							System.out.println("-> Path Nome :::" + q.getName());
//							System.out.println("-> Tipo :::" + q.getType().getName());
//
//						}
//						continue;
//					} else if (a instanceof PUT) {
//						System.out.println("-> Metodo ::: PUT");
//						continue;
//					}
//				}
//
//			}
//
//		}

	}

}