package main.scanArchive;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import main.entidades.EmpregadoExcel;

@SuppressWarnings("static-access")
public class ScanExcel {

	private String caminho;
	
	public ScanExcel(String caminho) {
		this.caminho = caminho;
	}
	
	public List<EmpregadoExcel> consultaExcel() {
		
		List<EmpregadoExcel> empregados = new ArrayList<>();
		
		try(FileInputStream fileInputStream = new FileInputStream(caminho)){
				
			Workbook workbook = new WorkbookFactory().create(fileInputStream);
			
			Sheet sheet = workbook.getSheetAt(0);
			
			for(Row row : sheet) {
				
				if(row == null || row.getCell(0) == null || row.getCell(0).getCellType() == Cell.CELL_TYPE_BLANK) {
					continue;
				}
				
				EmpregadoExcel empregadoExcel = new EmpregadoExcel();
				
				for (Cell cell : row) {
					
					if(cell.getColumnIndex() == 0) {
						empregadoExcel.setNome(cell.getStringCellValue());
						
					}else if(cell.getColumnIndex() == 1) {
						empregadoExcel.setMatricula(cell.getStringCellValue());
						
					}else if(cell.getColumnIndex() == 2) {
						empregadoExcel.setModeloBH(cell.getStringCellValue());
						
					}
				}
				
				empregados.add(empregadoExcel);
			}
			
			return empregados;
			
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Um erro ao tentar buscar no excel de funcionários");
		}
	}
}
