package swagger.automate;

import swagger.automate.doc.DocMethods;
import swagger.automate.doc.bean.DocText;
import swagger.automate.swagger.SwaggerMethods;
import swagger.automate.swagger.bean.DocSwagger;

public class ReflectionDemo {

	static DocSwagger docSwagger;
	static DocText docText;

	public static void main(String[] args) throws NoSuchFieldException, SecurityException, NoSuchMethodException {
		System.gc();
		docText = new DocText();
		docSwagger = new DocSwagger();

		docSwagger = SwaggerMethods.readClass(docSwagger);
		docText = DocMethods.generateHeader(docSwagger, docText);
	}

}