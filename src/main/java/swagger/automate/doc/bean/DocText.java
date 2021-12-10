package swagger.automate.doc.bean;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import swagger.automate.constants.Constants;

public class DocText {
	private StringBuilder header;
	private StringBuilder paths;
	private StringBuilder definitions;
	private File file;

	public DocText() throws IOException {
		file = new File(Constants.FILE_PATH);
		if (!file.exists())
			file.createNewFile();
	}

	public StringBuilder getHeader() {

		return header;
	}

	public void setHeader(StringBuilder header) {
		this.header = header;
	}

	public StringBuilder getPaths() {
		return paths;
	}

	public void setPaths(StringBuilder paths) {
		this.paths = paths;
	}

	public StringBuilder getDefinitions() {
		return definitions;
	}

	public void setDefinitions(StringBuilder definitions) {
		this.definitions = definitions;
	}

	public String getFullText() {
		return header.toString() + paths.toString() + definitions.toString();
	}

	public void putTextInFile() throws IOException {
		 BufferedWriter reader = new BufferedWriter(new FileWriter(file));
		 reader.write(header.toString()+paths.toString()+definitions.toString());
	     reader.close();
	}
}
