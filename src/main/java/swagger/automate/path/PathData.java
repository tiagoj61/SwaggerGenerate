package swagger.automate.path;

import java.util.ArrayList;
import java.util.List;

import swagger.automate.body.BodyObject;
import swagger.automate.response.Responses;
import swagger.automate.tag.Tag;

public class PathData {
	private int tagKey;
	private String path;
	private String method;
	private String consumes;
	private String produces;
	private Responses responses;
	private int consumesBodyKey;
	private int producesBodyKey;

	public int getTagKey() {
		return tagKey;
	}

	public void setTagKey(int tagKey) {
		this.tagKey = tagKey;
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

	public int getConsumesBodyKey() {
		return consumesBodyKey;
	}

	public void setConsumesBodyKey(int consumesBodyKey) {
		this.consumesBodyKey = consumesBodyKey;
	}

	public int getProducesBodyKey() {
		return producesBodyKey;
	}

	public void setProducesBodyKey(int producesBodyKey) {
		this.producesBodyKey = producesBodyKey;
	}

}
