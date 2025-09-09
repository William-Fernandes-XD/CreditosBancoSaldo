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
		
		String caminhoExcelReferenciaNomes = "C:/Users/E21057649/Downloads/BancoHoras.xlsx";
		String caminhoPdf = "C:/Users/E21057649/Downloads/EspelhoPonto.pdf";
		String caminhoExcelReferenciaLideres = "C:/Users/E21057649/Downloads/PontoCoi.xlsx";
		
		
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
		
		List<EmpregadoExcel> matriculas = new ArrayList<>();
		
		for (EmpregadoExcel empregadoExcel : empregadosExcel) {
			matriculas.add(empregadoExcel);
		}
		
		ScanPdf saldos = new ScanPdf(caminhoPdf, matriculas);
		List<EmpregadoPdf> empregadosPdf = saldos.scanPdf();
		
		// Tabela dos lideres
		
		ScanExcelLideres lideres = new ScanExcelLideres(caminhoExcelReferenciaLideres, empregadosExcel);
		List<EmpregadoLideres> empregadosLideres = lideres.scanExcel();
		
		/***
		 * 
		 * Junção de todos os dados
		 * 
		 */
		
		List<EmpregadoJuncao> empregadoJuncaos = new ArrayList<>();
		
		for (EmpregadoExcel excelEmpregado : empregadosExcel) {
			
		    EmpregadoJuncao empregadoJuncao = new EmpregadoJuncao();
		    empregadoJuncao.setModeloBh(excelEmpregado.getModeloBH()); 

		    // Buscar dados do líder
		    for (EmpregadoLideres empregadoLideres : empregadosLideres) {
		    	
		    	String texto = Normalizer.normalize(empregadoLideres.getNome(), Normalizer.Form.NFD)
	                    .replaceAll("\\p{M}", "") // remove acentos
	                    .replaceAll("[^a-zA-Z0-9 ]", "") // remove caracteres especiais
	                    .trim();
		    	
		    	empregadoLideres.setNome(texto);
		    	
		        if (excelEmpregado.getMatricula().replaceAll("^[A-Z]", "").trim()
		        		.equals(empregadoLideres.getMatricula().replaceAll("^[A-Z]", "").trim())) {
		        	empregadoJuncao.setNome(empregadoLideres.getNome());
		            empregadoJuncao.setMatricula(excelEmpregado.getMatricula());
		            empregadoJuncao.setLider(empregadoLideres.getLider());
		            break;
		        }
		    }

		    // Buscar dados do PDF
		    for (EmpregadoPdf empregadoPdf : empregadosPdf) {
		    	
		        if (excelEmpregado.getMatricula().replaceAll("^[A-Z]", "").trim().equals(
		        		empregadoPdf.getMatricula().replaceAll("^[A-Z]", "")
		        		.substring(1))) {
		        	
		            empregadoJuncao.setSaldo(empregadoPdf.getSaldoCiclo());
		            break;
		        }
		    }

		    if(empregadoJuncao.getSaldo() != null && empregadoJuncao.getSaldo().trim() != "") {
		    	if(empregadoJuncao.getNome() == null || empregadoJuncao.getNome().trim() == "") {
		    		
		    		empregadoJuncao.setNome(excelEmpregado.getNome());
		    	}
		    }
		    
		    empregadoJuncaos.add(empregadoJuncao);
		}
		
		// Agora a lista empregadoJuncaos contém todos os dados combinados
		/**for (EmpregadoJuncao ej : empregadoJuncaos) {
		    System.out.println(ej);
		}**/
		
		// criando o arquivo com a lista de empregadosJuncao
		
		String caminhoSalvar = "C:/Users/E21057649/Downloads/tabelaSaldos.xlsx";
		
		SaveToExcel saveToExcel = new SaveToExcel(caminhoSalvar, empregadoJuncaos);
		saveToExcel.dataSave();
	}
}
