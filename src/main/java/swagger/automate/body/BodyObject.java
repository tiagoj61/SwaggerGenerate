package swagger.automate.body;

import java.util.ArrayList;
import java.util.List;

public class BodyObject {
	private List<TuplaInBody> tuplaInBodies;

	public BodyObject() {
		super();
		this.tuplaInBodies = new ArrayList();
	}

	public List<TuplaInBody> getTuplaInBodies() {
		return tuplaInBodies;
	}

	public void setTuplaInBodies(List<TuplaInBody> tuplaInBodies) {
		this.tuplaInBodies = tuplaInBodies;
	}

}
