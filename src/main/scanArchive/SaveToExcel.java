package main.scanArchive;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import main.entidades.EmpregadoJuncao;

public class SaveToExcel {

	private List<EmpregadoJuncao> empregadoJuncaos;
	private String caminho;
	
	public SaveToExcel(String caminho, List<EmpregadoJuncao> empregoJuncaos) {
		
		this.caminho = caminho;
		this.empregadoJuncaos = empregoJuncaos;
	}
	
	public void dataSave() {
		
		try {
			
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Funcionários saldos horas");
			
			 // Criar cabeçalho
	        Row header = sheet.createRow(0);
	        header.createCell(0).setCellValue("Nome");
	        header.createCell(1).setCellValue("Matrícula");
	        header.createCell(2).setCellValue("Modelo BH");
	        header.createCell(3).setCellValue("Líder");
	        header.createCell(4).setCellValue("Saldo");
	        
	        /**
		     * 
		     * Percentage Lideres
		     * 
		     */
		    
		    Map<String, List<EmpregadoJuncao>> lideres = new HashMap<>();
	        
	        int index = 1;
	        
	        for (EmpregadoJuncao colaborador : empregadoJuncaos) {
	        	
	        	Row row = sheet.createRow(index);
	        	
	        	row.createCell(0).setCellValue(colaborador.getNome());
	        	row.createCell(1).setCellValue(colaborador.getMatricula());
	        	row.createCell(2).setCellValue(colaborador.getModeloBh());
	        	row.createCell(3).setCellValue(colaborador.getLider());
	        	row.createCell(4).setCellValue(colaborador.getSaldo());
	        	
	        	for (int i = 0; i < 5; i++) {
	                sheet.autoSizeColumn(i);
	            }
	        	
	        	/**
	        	 * Lideres
	        	 */
	        	
	        	if(colaborador.getLider() != null && colaborador.getLider().trim() != "") {
	        		lideres.computeIfAbsent(colaborador.getLider().toUpperCase(), k -> new ArrayList<>()).add(colaborador);
	        	}
	        	
	        	index++;
			}
	        
	        /**
	         * Colocando as porcentagem para cada lider
	         */
	        
	        int rowPosition = 2; 
	        
	        for(Map.Entry<String, List<EmpregadoJuncao>> entry : lideres.entrySet()) {
	        	
	        	List<EmpregadoJuncao> empregadosPerLider = entry.getValue();
	        	
	        	Row row = sheet.getRow(rowPosition);
	        	
	        	float saldoPositivoPercentual = (float) 0.0;
	        	float saldoNegativoPercentual = (float) 0.0;
	        	
	        	for (EmpregadoJuncao empregadoIterable : empregadosPerLider) {
					
	        		System.out.println(empregadoIterable.getSaldo());
	        		
	        		if(empregadoIterable.getSaldo() != null && empregadoIterable.getSaldo().contains("-")) {
	        			saldoNegativoPercentual++;
	        		}else if(empregadoIterable.getSaldo() != null){
	        			saldoPositivoPercentual++;
	        		}
				}
	        	
	        	row.createCell(6).setCellValue(entry.getKey());
	        	row.createCell(7).setCellValue(((saldoPositivoPercentual / empregadosPerLider.size()) * 100) + "%");
	        	row.createCell(8).setCellValue(((saldoNegativoPercentual / empregadosPerLider.size()) * 100) + "%");
	        	
	        	rowPosition++;
	        }
	        
	        try(FileOutputStream fileOutputStream = new FileOutputStream(caminho)){
	        	workbook.write(fileOutputStream);
	        	System.out.println("");
	        	System.out.println("----------------------");
	        	System.out.println("Arquivo excel criado");
	        }
	        
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
