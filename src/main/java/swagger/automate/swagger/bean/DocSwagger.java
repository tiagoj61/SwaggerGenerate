package swagger.automate.swagger.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DocSwagger {
	private List<PathData> pathDatas;
	private HashMap<Integer, BodyObject> objects;
	private HashMap<Integer, Tag> tags;

	public DocSwagger() {
		super();
		this.pathDatas = new ArrayList<>();
		this.objects = new HashMap<>();
		this.tags = new HashMap<>();
	}

	public List<PathData> getPathDatas() {
		return pathDatas;
	}

	public void setPathDatas(List<PathData> pathDatas) {
		this.pathDatas = pathDatas;
	}

	public HashMap<Integer, BodyObject> getObjects() {
		return objects;
	}

	public void setObjects(HashMap<Integer, BodyObject> objects) {
		this.objects = objects;
	}

	public HashMap<Integer, Tag> getTags() {
		return tags;
	}

	public void setTags(HashMap<Integer, Tag> tags) {
		this.tags = tags;
	}

}
