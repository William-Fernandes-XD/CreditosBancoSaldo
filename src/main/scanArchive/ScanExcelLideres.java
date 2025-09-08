package main.scanArchive;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import main.entidades.EmpregadoExcel;
import main.entidades.EmpregadoLideres;

public class ScanExcelLideres {

	private String caminho;
	private List<EmpregadoExcel> empregados = new ArrayList<>();
	 
	public ScanExcelLideres(String caminho, List<EmpregadoExcel> empregados) {
		
		this.empregados = empregados;
		this.caminho = caminho;
	}
	
	@SuppressWarnings("static-access")
	public List<EmpregadoLideres> scanExcel() {
		
		
		try(FileInputStream inputStream = new FileInputStream(caminho)){
			
			Workbook workbook = new WorkbookFactory().create(inputStream);
			Sheet planilha = workbook.getSheetAt(0);
			
			List<EmpregadoLideres> list = new ArrayList<>();	
			
			for (Row row : planilha) {
				
			    if(row == null || row.getCell(1) == null || row.getCell(1).getCellType() == Cell.CELL_TYPE_BLANK) {
			        continue;
			    }
			    
			    EmpregadoLideres empregadoLideres = new EmpregadoLideres();
			    
			    for (Cell cell : row) {
			    	
			    	if(cell.getColumnIndex() == 1) {
			    		empregadoLideres.setNome(cell.getStringCellValue());
			    	}
					
		            if(cell.getColumnIndex() == 3) {
		                empregadoLideres.setLider(cell.getStringCellValue());
		            }
		            if(cell.getColumnIndex() == 4) {
		                empregadoLideres.setMatricula(cell.getStringCellValue());
		            }
				}
			    
			    list.add(empregadoLideres);
			}
			
			return list;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao tentar buscar líderes");
		}
	}
}
