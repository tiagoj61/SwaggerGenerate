package swagger.automate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import swagger.automate.doc.DocMethods;
import swagger.automate.doc.bean.DocText;
import swagger.automate.swagger.SwaggerMethods;
import swagger.automate.swagger.bean.DocSwagger;

public class ReflectionDemo {

	static DocSwagger docSwagger;
	static DocText docText;

	public static void main(String[] args)
			throws NoSuchFieldException, SecurityException, NoSuchMethodException, IOException, URISyntaxException, ClassNotFoundException {
		docText = new DocText();
		docSwagger = new DocSwagger();
		docSwagger = SwaggerMethods.readClass(docSwagger);
		docText = DocMethods.generateHeader(docSwagger, docText);
		docText.putTextInFile();
//		Reflections reflections = new Reflections("swagger.automate.rest", new SubTypesScanner(false));
//		Set<Class<? extends Object>> packs = reflections.getSubTypesOf(Object.class).stream()
//				.collect(Collectors.toSet());
//		List<Class<? extends Object>> asd = packs.stream().filter(a -> a.getPackageName() == "swagger.automate.rest")
//				.collect(Collectors.toList());
	}
}