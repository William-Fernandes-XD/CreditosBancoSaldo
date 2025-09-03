package main.entidades;

public class EmpregadoLideres {

	private String nome;
	private String lider;
	private String matricula;
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getLider() {
		return lider;
	}
	public void setLider(String lider) {
		this.lider = lider;
	}
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	@Override
	public String toString() {
		return "EmpregadoLideres [nome=" + nome + ", lider=" + lider + ", matricula=" + matricula + "]";
	}
}
