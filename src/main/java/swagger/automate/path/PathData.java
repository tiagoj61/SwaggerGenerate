package swagger.automate.path;

import java.util.List;

import swagger.automate.body.BodyObject;
import swagger.automate.response.Responses;
import swagger.automate.tag.Tag;

public class PathData {
	private Tag tag;
	private String path;
	private String method;
	private String consumes;
	private String produces;
	private Responses responses;
	private List<BodyObject> consumesBody;
	private List<BodyObject> producesBody;

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getConsumes() {
		return consumes;
	}

	public void setConsumes(String consumes) {
		this.consumes = consumes;
	}

	public String getProduces() {
		return produces;
	}

	public void setProduces(String produces) {
		this.produces = produces;
	}

	public Responses getResponses() {
		return responses;
	}

	public void setResponses(Responses responses) {
		this.responses = responses;
	}

	public List<BodyObject> getConsumesBody() {
		return consumesBody;
	}

	public void setConsumesBody(List<BodyObject> consumesBody) {
		this.consumesBody = consumesBody;
	}

	public List<BodyObject> getProducesBody() {
		return producesBody;
	}

	public void setProducesBody(List<BodyObject> producesBody) {
		this.producesBody = producesBody;
	}

}
