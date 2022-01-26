package swagger.automate.rest;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import swagger.automate.doc.DocMethods;
import swagger.automate.doc.bean.DocText;
import swagger.automate.rest.impl.RestTeste;
import swagger.automate.swagger.SwaggerMethods;
import swagger.automate.swagger.bean.DocSwagger;

public class ReflectionDemo {

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