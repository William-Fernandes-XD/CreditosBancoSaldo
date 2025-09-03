package main.entidades;

public class EmpregadoJuncao {

	/***
	 * 
	 * nome 
	 * Matricula
	 * Modelo BH
	 * Lider
	 * Saldo
	 * 
	 */
	
	private String nome;
	private String matricula;
	private String modeloBh;
	private String lider;
	private String saldo;
	
	public EmpregadoJuncao() {

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

	public String getModeloBh() {
		return modeloBh;
	}

	public void setModeloBh(String modeloBh) {
		this.modeloBh = modeloBh;
	}

	public String getLider() {
		return lider;
	}

	public void setLider(String lider) {
		this.lider = lider;
	}

	public String getSaldo() {
		return saldo;
	}

	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}

	@Override
	public String toString() {
		return "EmpregadoJuncao [nome=" + nome + ", matricula=" + matricula + ", modeloBh=" + modeloBh + ", lider="
				+ lider + ", saldo=" + saldo + "]";
	}
}
