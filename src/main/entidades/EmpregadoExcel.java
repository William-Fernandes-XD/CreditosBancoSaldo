package main.entidades;

import java.time.LocalTime;

public class EmpregadoExcel {

	private String nome;
	private String matricula;
	private String modeloBH;
	
	public EmpregadoExcel() {

	}

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getModeloBH() {
		return modeloBH;
	}

	public void setModeloBH(String modeloBH) {
		this.modeloBH = modeloBH;
	}

	@Override
	public String toString() {
		return "EmpregadoExcel [nome=" + nome + ", matricula=" + matricula + ", modeloBH=" + modeloBH + "]";
	}
}
