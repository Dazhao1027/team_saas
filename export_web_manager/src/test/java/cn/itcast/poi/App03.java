package cn.itcast.poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class App03 {
    // 导出
    @Test
    public void write() throws Exception {
        //1. 创建工作簿
        Workbook workbook = new HSSFWorkbook();
        //2. 创建工作表
        Sheet sheet = workbook.createSheet();
        //3. 创建行, 第一行
        Row row = sheet.createRow(0);
        //4. 创建单元格
        Cell cell = row.createCell(0);
        //5. 设置单元格内容
        cell.setCellValue("第一行第一列");
        //6. 导出
        workbook.write(new FileOutputStream("e:/tt.xls"));
        workbook.close();
    }

    // 导入
    @Test
    public void read() throws Exception {
        //1. 根据excel文件流，创建工作簿
        Workbook workbook = new HSSFWorkbook(new FileInputStream("e:/tt.xls"));
        //2. 获取工作表
        Sheet sheet = workbook.getSheetAt(0);
        //3. 获取第一行
        Row row = sheet.getRow(0);
        //4. 获取第一行第一列
        Cell cell = row.getCell(0);
        //5. 获取内容
        System.out.println(cell.getStringCellValue());
        System.out.println("总行数：" + sheet.getPhysicalNumberOfRows());
        System.out.println("总列数：" + row.getPhysicalNumberOfCells());
        workbook.close();
    }
}
