package swagger.automate.swagger.bean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BodyObject {
	private boolean array;
	private String nome;
	private Type type;
	private List<TuplaInBody> tuplaInBodies;

	public BodyObject() {
		super();
		this.tuplaInBodies = new ArrayList();
	}

	public boolean isArray() {
		return array;
	}

	public void setArray(boolean array) {
		this.array = array;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<TuplaInBody> getTuplaInBodies() {
		return tuplaInBodies;
	}

	public void setTuplaInBodies(List<TuplaInBody> tuplaInBodies) {
		this.tuplaInBodies = tuplaInBodies;
	}

}
