package swagger.automate.rest;

import java.io.IOException;

import swagger.automate.doc.DocMethods;
import swagger.automate.doc.bean.DocText;
import swagger.automate.swagger.SwaggerMethods;
import swagger.automate.swagger.bean.DocSwagger;

public class Main {

	static DocSwagger docSwagger;
	static DocText docText;

	public static void main(String[] args)
			throws IOException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		docText = new DocText();
		docSwagger = new DocSwagger();

		docSwagger = SwaggerMethods.readClass(docSwagger, "swagger.automate.rest.impl");
		docText = DocMethods.generateHeader(docSwagger, docText);
		docText.putTextInFile();

	}

}
