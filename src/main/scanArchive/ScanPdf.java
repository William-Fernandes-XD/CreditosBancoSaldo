package main.scanArchive;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import main.entidades.EmpregadoPdf;

public class ScanPdf {

	private String caminho;
	private List<String> matriculas;

	public ScanPdf(String caminho, List<String> matriculas) {

		this.caminho = caminho;
		this.matriculas = matriculas;
	}

	public List<EmpregadoPdf> scanPdf() {

		List<EmpregadoPdf> empregadoPdfs = new ArrayList<>();

		try (PDDocument documento = PDDocument.load(new File(caminho))) {

			PDFTextStripper stripper = new PDFTextStripper();

			int totalPages = documento.getNumberOfPages();

			for (int i = 1; i <= totalPages; i++) {
				
				int percentage = (int) Math.round((i * 100.0) / totalPages);
				
				System.out.print("\rCarregamento: " + percentage + "%");
				System.out.flush();
				
				stripper.setStartPage(i);
				stripper.setEndPage(i);

				if (stripper.getText(documento).toLowerCase().contains("saldo do ciclo")) {
					stripper.setEndPage(i);
				} else {
					stripper.setEndPage(i + 1);
				}

				boolean nomeListado = false;
				
				for (String matricula : this.matriculas) {
					 
					try {
						
						if (stripper.getText(documento).toLowerCase().contains(matricula.replaceAll("^[A-Z]", "").toLowerCase())) {
							nomeListado = true;
						}
					}catch(Exception e) {
						
						if (stripper.getText(documento).toLowerCase().contains(matricula.toLowerCase())) {
							nomeListado = true;
						}
					}
				}

				EmpregadoPdf empregadoPdf = new EmpregadoPdf();

				String matricula = "";
				String nome = "";
				String saldo = "";

				String textoPagina = stripper.getText(documento);

				String normalizado = textoPagina.replaceAll("\\s+", " ");
				String[] linhas = normalizado.split("\\r?\\n");

				for (String linha : linhas) {

					if (nomeListado == true) {
						
						if (linha.contains("Localização:")) {
							Pattern padraoMatricula = Pattern.compile("Localização:\\s*(.+)");
							Matcher matcherMatricula = padraoMatricula.matcher(linha);

							if (matcherMatricula.find()) {
								matricula = matcherMatricula.group(1).split(" ")[0];

								Pattern padraoNome = Pattern.compile("CTPS:\\s*(.+?)\\s+Diretoria");
								Matcher matcherNome = padraoNome.matcher(linha);

								String nomeAux = "";

								if (matcherNome.find()) {
									String[] nomeTextoAux = matcherNome.group(1).split(" ");

									for (int j = 1; j < nomeTextoAux.length; j++) {

										nomeAux += " " + nomeTextoAux[j];
									}

									// Anda retornando por exemplo "Operacoes ATL00856 GUILHERME AUGUSTO DA COSTA
									// ARRUDA"

									Pattern padraoSemNumero = Pattern.compile("\\b\\w*\\d+\\w*\\b\\s*(.+)");
									Matcher matcherSemNumero = padraoSemNumero.matcher(nomeAux);

									if (matcherSemNumero.find()) {
										nome = matcherSemNumero.group(1).trim(); // pega diretamente o nome
									} else {
										nome = nomeAux.trim(); // se não houver código com número
									}
								}
							}

							empregadoPdf.setMatricula(matricula);
							empregadoPdf.setNome(nome);

							if (linha.toLowerCase().contains("saldo do ciclo")) {

								Pattern padraoSaldo = Pattern.compile("Saldo\\sdo\\sCiclo:\\s*(.+?)\\s+Assinatura");
								Matcher matcherSaldo = padraoSaldo.matcher(linha);

								if (matcherSaldo.find()) {

									saldo = matcherSaldo.group(1).trim().split(" ")[4];
									empregadoPdf.setSaldoCiclo(saldo);

								}
							}
						}
					}
					
					if (empregadoPdf.getMatricula() != null && empregadoPdf.getNome() != null
							&& empregadoPdf.getSaldoCiclo() != null) {

						empregadoPdfs.add(empregadoPdf);
					}
				}
			}
			
			return empregadoPdfs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao inicializar o pdf");
		}
	}
}
