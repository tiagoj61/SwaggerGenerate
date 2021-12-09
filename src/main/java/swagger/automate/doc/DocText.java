package swagger.automate.doc;

public class DocText {
	private StringBuilder header;
	private StringBuilder paths;
	private StringBuilder definitions;

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

}
