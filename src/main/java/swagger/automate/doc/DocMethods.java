package swagger.automate.doc;

import java.util.stream.Collectors;

import swagger.automate.constants.Constants;
import swagger.automate.doc.bean.DocText;
import swagger.automate.enumeration.MethodsEnum;
import swagger.automate.swagger.bean.DocSwagger;
import swagger.automate.util.SwitchUtil;
import swagger.automate.util.TextUtil;

public class DocMethods {
	public static DocText generateHeader(DocSwagger docSwagger, DocText docText) {
		StringBuilder header = new StringBuilder("swagger: \"2.0\"").append("\n");

		header.append("info:").append("\n");
		header.append(TextUtil.replicateString(Constants.SPACE, 1))
				.append("description: \"Documento referente ao rest do sistema Ponto Security\"").append("\n");// TODO:
																												// define
																												// the
																												// desc
		header.append(TextUtil.replicateString(Constants.SPACE, 1)).append("version: \"1.0.0\"").append("\n");// TODO:
																												// define
																												// the
																												// version
		header.append(TextUtil.replicateString(Constants.SPACE, 1)).append("title: \"Ponto Security rest\"")
				.append("\n");// TODO: define the title
		header.append("host: \"audax.mobi\"").append("\n");
		header.append("basePath: \"/rest/\"").append("\n");
		header.append("tags:").append("\n");

		docSwagger.getTags().forEach((key, value) -> {
			header.append("- name:").append(" \"" + value.getName() + "\"").append("\n");
			header.append(TextUtil.replicateString(Constants.SPACE, 1)).append("description: ")
					.append("\"" + value.getDescription() + "\"").append("\n");
		});

		header.append("schemes:").append("\n");
		header.append("- \"http\"").append("\n");
		docText.setHeader(header);
		return generatePath(docSwagger, docText);
	}

	private static DocText generatePath(DocSwagger docSwagger, DocText docText) {
		StringBuilder paths = new StringBuilder("paths:").append("\n");
		docSwagger.getPathDatas().forEach(pathData -> {
			paths.append(TextUtil.replicateString(Constants.SPACE, 1)).append("/").append(pathData.getPath() + ":")
					.append("\n");
			paths.append(TextUtil.replicateString(Constants.SPACE, 2)).append(pathData.getMethod().toLowerCase() + ":")
					.append("\n");

			paths.append(TextUtil.replicateString(Constants.SPACE, 3)).append("tags:").append("\n");
			paths.append(TextUtil.replicateString(Constants.SPACE, 3))
					.append("- \"" + docSwagger.getTags().get(pathData.getTagKey()).getName() + "\"").append("\n");
			paths.append(TextUtil.replicateString(Constants.SPACE, 3)).append("summary: \"Loggar com o funcionario\"")
					.append("\n");// TODO:
			paths.append(TextUtil.replicateString(Constants.SPACE, 3)).append("description: \"Entrar no sistema\"")
					.append("\n");// TODO:

			if (pathData.getMethod() == MethodsEnum.POST.name()) {
				paths.append(TextUtil.replicateString(Constants.SPACE, 3)).append("operationId: \""
						+ docSwagger.getTags().get(pathData.getTagKey()).getName() + pathData.getPath() + "\"")
						.append("\n");
				paths.append(TextUtil.replicateString(Constants.SPACE, 3)).append("consumes:").append("\n");
				paths.append(TextUtil.replicateString(Constants.SPACE, 3))
						.append("- \"" + pathData.getConsumes() + "\"").append("\n");
				paths.append(TextUtil.replicateString(Constants.SPACE, 3)).append("produces:").append("\n");
				paths.append(TextUtil.replicateString(Constants.SPACE, 3))
						.append("- \"" + pathData.getProduces() + "\"").append("\n");

				paths.append(TextUtil.replicateString(Constants.SPACE, 3)).append("parameters:").append("\n");
				paths.append(TextUtil.replicateString(Constants.SPACE, 3))
						.append("- in: \"" + (pathData.getMethod() == "POST" ? "body" : "path") + "\"").append("\n");
				paths.append(TextUtil.replicateString(Constants.SPACE, 4))
						.append("name: \"" + (pathData.getMethod() == "POST" ? "body" : "path") + "\"").append("\n");
				paths.append(TextUtil.replicateString(Constants.SPACE, 4))
						.append("description: \"Aparelhos bluetooth e senha do funcionario\"").append("\n");
				paths.append(TextUtil.replicateString(Constants.SPACE, 4)).append("required: true").append("\n");
				paths.append(TextUtil.replicateString(Constants.SPACE, 4)).append("schema:").append("\n");
				if (docSwagger.getObjects().get(pathData.getConsumesBodyKey()).isArray()) {
					paths.append(TextUtil.replicateString(Constants.SPACE, 5)).append("type: \"array\"").append("\n");
					paths.append(TextUtil.replicateString(Constants.SPACE, 5)).append("items:").append("\n");
					paths.append(TextUtil.replicateString(Constants.SPACE, 6))
							.append("$ref: \"#/definitions/"
									+ docSwagger.getObjects().get(pathData.getConsumesBodyKey()).getNome() + "\"")
							.append("\n");// TODO:
				} else {
					paths.append(TextUtil.replicateString(Constants.SPACE, 5))
							.append("$ref: \"#/definitions/"
									+ docSwagger.getObjects().get(pathData.getProducesBodyKey()).getNome() + "\"")
							.append("\n");// TODO:
				}
			} else {
				paths.append(TextUtil.replicateString(Constants.SPACE, 3))
						.append("operationId: \"" + pathData.getPath() + "\"").append("\n");
				paths.append(TextUtil.replicateString(Constants.SPACE, 3)).append("produces:").append("\n");
				paths.append(TextUtil.replicateString(Constants.SPACE, 4)).append("- \"application/json\"")
						.append("\n");

				if (pathData.getPath() != null && pathData.getPath().toString().contains("{")) {
					paths.append(TextUtil.replicateString(Constants.SPACE, 3)).append("parameters:").append("\n");
					paths.append(TextUtil.replicateString(Constants.SPACE, 3))
							.append("- in: " + (pathData.getMethod() == "POST" ? "body" : "path") + "").append("\n");
					paths.append(TextUtil.replicateString(Constants.SPACE, 4))
							.append("name: " + pathData.getPath().toString().substring(
									pathData.getPath().toString().indexOf("{") + 1,
									pathData.getPath().toString().indexOf("}")))
							.append("\n");
//					paths.append(TextUtil.replicateString(Constants.SPACE, 4))
//					.append("name: " + pathData.getPath().toString()).append("\n");
					paths.append(TextUtil.replicateString(Constants.SPACE, 4)).append("required: true").append("\n");
					paths.append(TextUtil.replicateString(Constants.SPACE, 4)).append("type: integer").append("\n");
				} else {
					paths.append(TextUtil.replicateString(Constants.SPACE, 3)).append("parameters: []").append("\n");

				}

			}
			paths.append(TextUtil.replicateString(Constants.SPACE, 3)).append("responses:").append("\n");

			pathData.getResponseAndCodes().forEach(response -> {
				paths.append(TextUtil.replicateString(Constants.SPACE, 4))
						.append("\"" + response.getResponseCode() + "\":").append("\n");
				paths.append(TextUtil.replicateString(Constants.SPACE, 5)).append("description: \"desc\"").append("\n");// TODO:
																														// response
				if (response.getProducesBodyKey() != -1) {
					paths.append(TextUtil.replicateString(Constants.SPACE, 5)).append("schema:").append("\n");
					if (response.isArray()) {
						paths.append(TextUtil.replicateString(Constants.SPACE, 6)).append("type: \"array\"")
								.append("\n");
						paths.append(TextUtil.replicateString(Constants.SPACE, 6)).append("items:").append("\n");

						paths.append(TextUtil.replicateString(Constants.SPACE, 7))
								.append("$ref: \"#/definitions/"
										+ docSwagger.getObjects().get(response.getProducesBodyKey()).getNome() + "\"")
								.append("\n");
					} else {
						paths.append(TextUtil.replicateString(Constants.SPACE, 6))
								.append("$ref: \"#/definitions/"
										+ docSwagger.getObjects().get(response.getProducesBodyKey()).getNome() + "\"")
								.append("\n");
					}

				}
			});

		});
		docText.setPaths(paths);
		return generateDefinitions(docSwagger, docText);
	}

	private static DocText generateDefinitions(DocSwagger docSwagger, DocText docText) {
		StringBuilder definitions = new StringBuilder("definitions:").append("\n");
		docSwagger.getObjects().forEach((key, value) -> {
			definitions.append(TextUtil.replicateString(Constants.SPACE, 1)).append(value.getNome()).append(":")
					.append("\n");// TODO:
			// define
			// the
			// name
			definitions.append(TextUtil.replicateString(Constants.SPACE, 2)).append("type: \"object\"").append("\n");// type
			if (value.getTuplaInBodies().stream().filter(tuple -> tuple.getRequired() == true).findAny()
					.orElse(null) != null) {
				definitions.append(TextUtil.replicateString(Constants.SPACE, 2)).append("required:").append("\n");// Required

				value.getTuplaInBodies().stream().filter(tuple -> tuple.getRequired() == true)
						.collect(Collectors.toList()).forEach(tuple -> {
							definitions.append(TextUtil.replicateString(Constants.SPACE, 2))
									.append("- \"" + tuple.getName() + "\"").append("\n");// Required Fields
						});
			}
			definitions.append(TextUtil.replicateString(Constants.SPACE, 2)).append("properties:").append("\n");// Properties
			value.getTuplaInBodies().forEach(tuple -> {
				definitions.append(TextUtil.replicateString(Constants.SPACE, 3)).append("" + tuple.getName() + ":")
						.append("\n");// Fields
				definitions.append(TextUtil.replicateString(Constants.SPACE, 4)).append("type: ")
						.append("\"" + SwitchUtil.convertTypeToJson(tuple.getType()) + "\"").append("\n");// Fields
				if (tuple.getReference() != null) {
					definitions.append(TextUtil.replicateString(Constants.SPACE, 4)).append("items: ").append("\n");// Fields
					definitions.append(TextUtil.replicateString(Constants.SPACE, 5))
							.append("$ref: \"#/definitions/" + tuple.getReference().getSimpleName() + "\"")
							.append("\n");// Fields

				} else {
					definitions.append(TextUtil.replicateString(Constants.SPACE, 4)).append("example: ")
							.append(tuple.getExample()).append("\n");// Fields
				}
			});
		});
		docText.setDefinitions(definitions);
		return docText;
	}
}
