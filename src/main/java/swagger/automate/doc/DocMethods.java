package swagger.automate.doc;

import java.util.Arrays;
import java.util.stream.Collectors;

import swagger.automate.constants.DocConstants;
import swagger.automate.doc.bean.DocText;
import swagger.automate.swagger.bean.DocSwagger;
import swagger.automate.util.TextUtil;
import swagger.automate.util.SwitchUtil;

public class DocMethods {
	public static DocText generateHeader(DocSwagger docSwagger, DocText docText) {
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

		docSwagger.getTags().forEach((key, value) -> {
			header.append("- name:").append(" \"" + value.getName() + "\"").append("\n");
			header.append(TextUtil.replicateString(DocConstants.SPACE, 1)).append("description: ")
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
			paths.append(TextUtil.replicateString(DocConstants.SPACE, 1)).append("/").append(pathData.getPath() + ":")
					.append("\n");
			paths.append(TextUtil.replicateString(DocConstants.SPACE, 2))
					.append(pathData.getMethod().toLowerCase() + ":").append("\n");

			paths.append(TextUtil.replicateString(DocConstants.SPACE, 3)).append("tags:").append("\n");
			paths.append(TextUtil.replicateString(DocConstants.SPACE, 3))
					.append("- \"" + docSwagger.getTags().get(pathData.getTagKey()).getName() + "\"").append("\n");
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
			paths.append(TextUtil.replicateString(DocConstants.SPACE, 5)).append("$ref: \"#/definitions/"
					+ docSwagger.getObjects().get(pathData.getProducesBodyKey()).getNome() + "\"").append("\n");// TODO:
																												// define
																												// the
																												// name

			paths.append(TextUtil.replicateString(DocConstants.SPACE, 3)).append("responses:").append("\n");

			Arrays.stream(pathData.getResponses().getResponses()).forEach(response -> {
				paths.append(TextUtil.replicateString(DocConstants.SPACE, 4)).append("\"" + response + "\":")
						.append("\n");
				paths.append(TextUtil.replicateString(DocConstants.SPACE, 5)).append("description: \"desc\"")
						.append("\n");// TODO: desc response
			});

		});
		docText.setPaths(paths);
		return generateDefinitions(docSwagger, docText);
	}

	private static DocText generateDefinitions(DocSwagger docSwagger, DocText docText) {
		StringBuilder definitions = new StringBuilder("definitions:").append("\n");
		docSwagger.getObjects().forEach((key, value) -> {
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
						.append("\"" + SwitchUtil.convertTypeToJson(tuple.getType()) + "\"").append("\n");// Fields

				definitions.append(TextUtil.replicateString(DocConstants.SPACE, 4)).append("example: ")
						.append(tuple.getExample()).append("\n");// Fields
			});
		});
		docText.setDefinitions(definitions);
		return docText;
	}
}
