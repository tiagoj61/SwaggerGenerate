package swagger.automate.rest;

import java.util.List;

import swagger.automate.annotation.Requerido;

public class ReflectionHelper4 {
	@Requerido(true)
	private int age;
	private String name;
	public String deptName;
	public int empID;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public int getEmpID() {
		return empID;
	}

	public void setEmpID(int empID) {
		this.empID = empID;
	}

	
}