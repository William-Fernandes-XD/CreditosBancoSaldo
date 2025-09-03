package main.startup;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import main.entidades.EmpregadoExcel;
import main.entidades.EmpregadoJuncao;
import main.entidades.EmpregadoLideres;
import main.entidades.EmpregadoPdf;
import main.scanArchive.SaveToExcel;
import main.scanArchive.ScanExcel;
import main.scanArchive.ScanExcelLideres;
import main.scanArchive.ScanPdf;

public class startup {

	public static void main(String[] args) {
		
		String caminhoExcelReferenciaNomes = "C:/Users/E21057649/Downloads/Banco de Horas Junho (2).xlsx";
		String caminhoPdf = "C:/Users/E21057649/Downloads/JULHO - EXTRAIDO EM 19 08.pdf";
		String caminhoExcelReferenciaLideres = "C:/Users/E21057649/Downloads/Ponto COI 2025 (1) (3).xlsx";
		
		
		// Tabela excel com os nomes a ser buscado
		
		ScanExcel excel = new ScanExcel(caminhoExcelReferenciaNomes);
		
		List<EmpregadoExcel> empregadosExcel = excel.consultaExcel();
		
		for (EmpregadoExcel empregadoExcel : empregadosExcel) {
			
			String texto = Normalizer.normalize(empregadoExcel.getNome(), Normalizer.Form.NFD)
                    .replaceAll("\\p{M}", "") // remove acentos
                    .replaceAll("[^a-zA-Z0-9 ]", "") // remove caracteres especiais
                    .toLowerCase()
                    .trim();
			
			empregadoExcel.setNome(texto);
		}
		
		// Tabela em pdf dos dados de saldo
		
		List<String> nomes = new ArrayList<>();
		
		for (EmpregadoExcel empregadoExcel : empregadosExcel) {
			nomes.add(empregadoExcel.getNome());
		}
		
		ScanPdf saldos = new ScanPdf(caminhoPdf, nomes);
		
		List<EmpregadoPdf> empregadosPdf = saldos.scanPdf();
		
		// Tabela dos lideres
		
		ScanExcelLideres lideres = new ScanExcelLideres(caminhoExcelReferenciaLideres, empregadosExcel);
		List<EmpregadoLideres> empregadosLideres = lideres.scanExcel();
		
		/***
		 * 
		 * nome 
		 * Matricula
		 * Modelo BH
		 * Lider
		 * Saldo
		 * 
		 */
		
		List<EmpregadoJuncao> empregadoJuncaos = new ArrayList<>();
		
		for (EmpregadoExcel excelEmpregado : empregadosExcel) {
		    EmpregadoJuncao empregadoJuncao = new EmpregadoJuncao();
		    empregadoJuncao.setNome(excelEmpregado.getNome());
		    empregadoJuncao.setModeloBh(excelEmpregado.getModeloBH()); // se houver essa info no Excel

		    // Buscar dados do líder
		    for (EmpregadoLideres empregadoLideres : empregadosLideres) {
		    	
		    	String texto = Normalizer.normalize(empregadoLideres.getNome(), Normalizer.Form.NFD)
	                    .replaceAll("\\p{M}", "") // remove acentos
	                    .replaceAll("[^a-zA-Z0-9 ]", "") // remove caracteres especiais
	                    .toLowerCase()
	                    .trim();
		    	
		    	empregadoLideres.setNome(texto);
		    	
		        if (excelEmpregado.getNome().trim().equalsIgnoreCase(empregadoLideres.getNome().trim())) {
		            empregadoJuncao.setMatricula(empregadoLideres.getMatricula());
		            empregadoJuncao.setLider(empregadoLideres.getLider());
		            break;
		        }
		    }

		    // Buscar dados do PDF
		    for (EmpregadoPdf empregadoPdf : empregadosPdf) {
		        if (excelEmpregado.getNome().trim().equalsIgnoreCase(empregadoPdf.getNome().trim())) {
		            empregadoJuncao.setSaldo(empregadoPdf.getSaldoCiclo());
		            break;
		        }
		    }

		    empregadoJuncaos.add(empregadoJuncao);
		}
		
		// Agora a lista empregadoJuncaos contém todos os dados combinados
		/**for (EmpregadoJuncao ej : empregadoJuncaos) {
		    System.out.println(ej);
		}**/
		
		// removendo o resgate de linha vazia presente no arquivo excel de referencia
		empregadoJuncaos.remove(0);
		
		// criando o arquivo com a lista de empregadosJuncao
		
		String caminhoSalvar = "C:/Users/E21057649/Downloads/tabelaSaldos.xlsx";
		
		SaveToExcel saveToExcel = new SaveToExcel(caminhoSalvar, empregadoJuncaos);
		saveToExcel.dataSave();
	}
}
