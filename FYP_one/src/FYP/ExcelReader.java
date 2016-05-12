package FYP;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import weka.core.Instances;
public class ExcelReader {
	

	private static final int MY_MINIMUM_COLUMN_COUNT = 7;
	private FileInputStream file;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private ARFF arff;
	//constructor open a excel file
	public ExcelReader(String url) throws Exception{
	try {
		file = new FileInputStream(new File(url));
		workbook = new XSSFWorkbook(file); // workbook
		sheet = workbook.getSheetAt(0); // first sheet
		
		arff = new ARFF();//initialize arff object 
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
	}
	
	public Instances ReadExcel(){
		int lastColumn;
		int rowStart = Math.min(15, sheet.getFirstRowNum());
		int rowEnd = Math.max(1400, sheet.getLastRowNum());

		for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
		   Row r = sheet.getRow(rowNum);
		   arff.createInsatnce();
		   
	       if(r!=null)
	    	  lastColumn = Math.max(r.getLastCellNum(), MY_MINIMUM_COLUMN_COUNT);
	        else
	            continue;
		   for (int cn = 0; cn < lastColumn; cn++) {
		      Cell c = r.getCell(cn, Row.RETURN_NULL_AND_BLANK);
		      if (c == null) {

		         // The spreadsheet is empty in this cell
		      } else {
		         // Do something useful with the cell's contents
		         //CellReference cr = new CellReference(c.toString());
		         //System.out.println("Cell at " + rowNum + "," + cn + " found, that's " + cr.formatAsString());
		         switch(c.getCellType()){
		         case Cell.CELL_TYPE_NUMERIC:
		        	 if(c.getColumnIndex()==3)
		        		 arff.addfeatures("height", c.getNumericCellValue());
		        	 if(c.getColumnIndex()==4)
		        		 arff.addfeatures("width", c.getNumericCellValue());
		             break;
		         case Cell.CELL_TYPE_STRING:
		        	 if(c.getColumnIndex()==0||c.getColumnIndex()==1||c.getColumnIndex()==2)
		        	 {
		        		 for(int i =0; i<arff.strval.length;i++)
		        		 {
		        			 if(c.getStringCellValue().toLowerCase().contains(arff.strval[i]))
		        				 arff.addfeatures(arff.strval[i],arff.strval[i]);
		        			 else
		        				 arff.addfeatures(arff.strval[i],"Non"+arff.strval[i]);
		        		 } 
		        			 
		        	 }
		        	 
		        	 if(c.getColumnIndex()==5)
		        	 {
		        	  if(c.getStringCellValue().equals("iframe"))
		        		  arff.addfeatures("frameOrimage", "iframe");
		        	  else
		        		  arff.addfeatures("frameOrimage", "image");
		        	 }
		        	 
		        	 if(c.getColumnIndex()==6)
		        	 {
		        	  if(c.getStringCellValue().equals("Ad"))
		        		  arff.addfeatures("targetvalue", "AD");
		        	  else
		        		  arff.addfeatures("targetvalue", "NonAD");
		        	 }
		        	 break;
		         case Cell.CELL_TYPE_BLANK:
		        	 if(c.getColumnIndex()==0||c.getColumnIndex()==1||c.getColumnIndex()==2)
		        	 { 
		        	 }
		        	 if(c.getColumnIndex()==3)
		        		 //arff.addfeatures("height", 9999);
		        	 if(c.getColumnIndex()==4)
		        		 //arff.addfeatures("width", 9999);
		        	 
		        	 break;
		        	 
		         }
		         
		      }
		   }
		   arff.AddtoData();
		   arff.delteInstances();
		}
		arff.SetLabel();
		return arff.getInstances();
	}

}
