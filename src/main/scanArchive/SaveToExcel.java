package main.scanArchive;

import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
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
	        	
	        	index++;
			}
	        
	        try(FileOutputStream fileOutputStream = new FileOutputStream(caminho)){
	        	workbook.write(fileOutputStream);
	        	System.out.println("Arquivo excel criado");
	        }
	        
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
