package main.entidades;

public class EmpregadoPdf {

	private String nome;
	private String matricula;
	private String saldoCiclo;
	
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
	public String getSaldoCiclo() {
		return saldoCiclo;
	}
	public void setSaldoCiclo(String saldoCiclo) {
		this.saldoCiclo = saldoCiclo;
	}
	@Override
	public String toString() {
		return "EmpregadoPdf [nome=" + nome + ", matricula=" + matricula + ", saldoCiclo=" + saldoCiclo + "]";
	}
}
