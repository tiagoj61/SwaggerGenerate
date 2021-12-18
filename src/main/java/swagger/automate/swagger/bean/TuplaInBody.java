package swagger.automate.swagger.bean;

import java.lang.reflect.Type;

public class TuplaInBody {
	private String name;
	private Type type;
	private Class reference;
	private boolean required;
	private String example;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean getRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public Class getReference() {
		return reference;
	}

	public void setReference(Class reference) {
		this.reference = reference;
	}

}
