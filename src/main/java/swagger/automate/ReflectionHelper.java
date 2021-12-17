package swagger.automate;

import java.util.List;

import swagger.automate.annotation.Requerido;

public class ReflectionHelper {
	@Requerido(true)
	private int age;
	private String name;
	public String deptName;
	public int empID;
	public List<Double> teste;

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

	public List<Double> getTeste() {
		return teste;
	}

	public void setTeste(List<Double> teste) {
		this.teste = teste;
	}

}