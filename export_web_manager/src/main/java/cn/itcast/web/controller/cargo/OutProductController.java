package cn.itcast.web.controller.cargo;

import cn.itcast.domain.vo.ContractProductVo;
import cn.itcast.service.cargo.ContractProductService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping("/cargo/contract")
public class OutProductController extends BaseController {

    @Reference
    private ContractProductService contractProductService;

    /**
     * 出货表导出: contract-print.jsp进入导出页面
     */
    @RequestMapping("/print")
    public String print(){
        return "cargo/print/contract-print";
    }

    /**
     * 出货表导出（1）XSSF普通的实现
     * @param inputDate
     */
    @RequestMapping("/printExcel_01")
    public void printExcel_01(String inputDate, HttpServletResponse response) throws IOException { //2019-03
        /* 1. 导出第一行*/
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0,0,1,8));
        // 设置列的宽度
        sheet.setColumnWidth(0,256*5);
        sheet.setColumnWidth(1,256*26);
        sheet.setColumnWidth(2,256*11);
        sheet.setColumnWidth(3,256*29);
        sheet.setColumnWidth(4,256*12);
        sheet.setColumnWidth(5,256*15);
        sheet.setColumnWidth(6,256*10);
        sheet.setColumnWidth(7,256*10);
        sheet.setColumnWidth(8,256*10);


        // 1.2 创建第一行：标题行，需要合并单元格
        Row row = sheet.createRow(0);
        // 设置行高
        row.setHeightInPoints(36);
        // 1.3 创建第一行第二列
        Cell cell = row.createCell(1);
        // 1.3.1 设置内容： 2019-03--->2012年8月份出货表
        String bigTitle = inputDate.replace("-0","-").replace("-","年")+"月份出货表";
        cell.setCellValue(bigTitle);
        // 1.3.2 设置单元格样式
        cell.setCellStyle(this.bigTitle(workbook));

        /* 2. 导出第二行*/
        row = sheet.createRow(1);
        row.setHeightInPoints(26);
        String[] titles = {"客户","订单号","货号","数量","工厂","工厂交期","船期","贸易条款"};
        for (int i = 0; i < titles.length; i++) {
            cell = row.createCell(i+1);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(this.title(workbook));
        }

        /* 3. 导出数据行*/
        List<ContractProductVo> list =
                contractProductService.findByShipTime(getLoginCompanyId(), inputDate + "%");
        if (list != null && list.size()>0){
            int index = 2;
            for (ContractProductVo vo : list) {
                // 创建行，从第三行开始
                row = sheet.createRow(index++);
                row.setHeightInPoints(24);
                cell = row.createCell(1);
                cell.setCellValue(vo.getCustomName());
                cell.setCellStyle(this.text(workbook));

                cell = row.createCell(2);
                cell.setCellValue(vo.getContractNo());
                cell.setCellStyle(this.text(workbook));

                cell = row.createCell(3);
                cell.setCellValue(vo.getProductNo());
                cell.setCellStyle(this.text(workbook));

                cell = row.createCell(4);
                // 注意：ApachePOI往单元格写内容不能为NULL。
                if (vo.getCnumber() != null) {
                    cell.setCellValue(vo.getCnumber());
                }
                cell.setCellStyle(this.text(workbook));

                cell = row.createCell(5);
                cell.setCellValue(vo.getFactoryName());
                cell.setCellStyle(this.text(workbook));

                cell = row.createCell(6);
                cell.setCellValue(vo.getDeliveryPeriod());
                cell.setCellStyle(this.text(workbook));

                cell = row.createCell(7);
                cell.setCellValue(vo.getShipTime());
                cell.setCellStyle(this.text(workbook));

                cell = row.createCell(8);
                cell.setCellValue(vo.getTradeTerms());
                cell.setCellStyle(this.text(workbook));
            }
        }

        /* 4. 导出下载*/
        //4.1 设置编码
        response.setCharacterEncoding("UTF-8");
        //4.2 设置下载响应头
        response.setHeader("Content-Disposition","attachment;fileName=export.xlsx");
        //4.3 获取response输出流
        ServletOutputStream out = response.getOutputStream();
        //4.4 把excel文件流，写入到response输出流
        workbook.write(out);
        //4.5 关闭
        workbook.close();
        out.close();
    }



    /**
     * 出货表导出（2）XSSF实现模板导出
     * @param inputDate
     */
    @RequestMapping("/printExcel_02")
    public void printExcel_02(String inputDate, HttpServletRequest request, HttpServletResponse response) throws IOException { //2019-03
        // 【获取项目资源】
        InputStream in = request.getSession()
                .getServletContext().getResourceAsStream("/make/xlsprint/tOUTPRODUCT.xlsx");

        /* 【1. 模板导出， 传入文件流模板】*/
        Workbook workbook = new XSSFWorkbook(in);
        //【获取工作表】
        Sheet sheet = workbook.getSheetAt(0);



        // 1.2 【获取第一行】
        Row row = sheet.getRow(0);
        // 1.3 【获取第一行第二列】
        Cell cell = row.getCell(1);
        // 1.3.1 设置内容： 2019-03--->2012年8月份出货表
        String bigTitle = inputDate.replace("-0","-").replace("-","年")+"月份出货表";
        cell.setCellValue(bigTitle);


        /* 2. 【获取第三行，拿到第三行的样式】*/
        row = sheet.getRow(2);
        CellStyle[] cellStyles = new CellStyle[8];
        for (int i = 0; i < cellStyles.length; i++) {
            cellStyles[i] = row.getCell(i+1).getCellStyle();
        }

        /* 3. 导出数据行*/
        List<ContractProductVo> list =
                contractProductService.findByShipTime(getLoginCompanyId(), inputDate + "%");
        if (list != null && list.size()>0){
            int index = 2;
            for (ContractProductVo vo : list) {
                // 创建行，从第三行开始
                row = sheet.createRow(index++);
                row.setHeightInPoints(24);
                cell = row.createCell(1);
                cell.setCellValue(vo.getCustomName());
                cell.setCellStyle(cellStyles[0]);

                cell = row.createCell(2);
                cell.setCellValue(vo.getContractNo());
                cell.setCellStyle(cellStyles[1]);

                cell = row.createCell(3);
                cell.setCellValue(vo.getProductNo());
                cell.setCellStyle(cellStyles[2]);

                cell = row.createCell(4);
                // 注意：ApachePOI往单元格写内容不能为NULL。
                if (vo.getCnumber() != null) {
                    cell.setCellValue(vo.getCnumber());
                }
                cell.setCellStyle(cellStyles[3]);

                cell = row.createCell(5);
                cell.setCellValue(vo.getFactoryName());
                cell.setCellStyle(cellStyles[4]);

                cell = row.createCell(6);
                cell.setCellValue(vo.getDeliveryPeriod());
                cell.setCellStyle(cellStyles[5]);

                cell = row.createCell(7);
                cell.setCellValue(vo.getShipTime());
                cell.setCellStyle(cellStyles[6]);

                cell = row.createCell(8);
                cell.setCellValue(vo.getTradeTerms());
                cell.setCellStyle(cellStyles[7]);
            }
        }

        /* 4. 导出下载*/
        //4.1 设置编码
        response.setCharacterEncoding("UTF-8");
        //4.2 设置下载响应头
        response.setHeader("Content-Disposition","attachment;fileName=export.xlsx");
        //4.3 获取response输出流
        ServletOutputStream out = response.getOutputStream();
        //4.4 把excel文件流，写入到response输出流
        workbook.write(out);
        //4.5 关闭
        workbook.close();
        out.close();
    }



    /**
     * 出货表导出（3）SXSSF  支持导出百万数据，有效避免内存溢出问题。
     * 原理：内存中默认只存储1000条数据，超出部分写入磁盘缓存目录。
     */
    @RequestMapping("/printExcel")
    public void printExcel(String inputDate, HttpServletResponse response) throws IOException { //2019-03
        /* 1. 导出第一行*/
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0,0,1,8));
        // 设置列的宽度
        sheet.setColumnWidth(0,256*5);
        sheet.setColumnWidth(1,256*26);
        sheet.setColumnWidth(2,256*11);
        sheet.setColumnWidth(3,256*29);
        sheet.setColumnWidth(4,256*12);
        sheet.setColumnWidth(5,256*15);
        sheet.setColumnWidth(6,256*10);
        sheet.setColumnWidth(7,256*10);
        sheet.setColumnWidth(8,256*10);


        // 1.2 创建第一行：标题行，需要合并单元格
        Row row = sheet.createRow(0);
        // 设置行高
        row.setHeightInPoints(36);
        // 1.3 创建第一行第二列
        Cell cell = row.createCell(1);
        // 1.3.1 设置内容： 2019-03--->2012年8月份出货表
        String bigTitle = inputDate.replace("-0","-").replace("-","年")+"月份出货表";
        cell.setCellValue(bigTitle);
        // 1.3.2 设置单元格样式
        cell.setCellStyle(this.bigTitle(workbook));

        /* 2. 导出第二行*/
        row = sheet.createRow(1);
        row.setHeightInPoints(26);
        String[] titles = {"客户","订单号","货号","数量","工厂","工厂交期","船期","贸易条款"};
        for (int i = 0; i < titles.length; i++) {
            cell = row.createCell(i+1);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(this.title(workbook));
        }

        /* 3. 导出数据行*/
        List<ContractProductVo> list =
                contractProductService.findByShipTime(getLoginCompanyId(), inputDate + "%");
        if (list != null && list.size()>0){
            int index = 2;
            for (ContractProductVo vo : list) {
                // 模拟导出海量数据 60万
                for (int i = 1; i < 30000; i++) {
                    // 创建行，从第三行开始
                    row = sheet.createRow(index++);
                    row.setHeightInPoints(24);
                    cell = row.createCell(1);
                    cell.setCellValue(vo.getCustomName());

                    cell = row.createCell(2);
                    cell.setCellValue(vo.getContractNo());

                    cell = row.createCell(3);
                    cell.setCellValue(vo.getProductNo());

                    cell = row.createCell(4);
                    // 注意：ApachePOI往单元格写内容不能为NULL。
                    if (vo.getCnumber() != null) {
                        cell.setCellValue(vo.getCnumber());
                    }

                    cell = row.createCell(5);
                    cell.setCellValue(vo.getFactoryName());

                    cell = row.createCell(6);
                    cell.setCellValue(vo.getDeliveryPeriod());

                    cell = row.createCell(7);
                    cell.setCellValue(vo.getShipTime());

                    cell = row.createCell(8);
                    cell.setCellValue(vo.getTradeTerms());
                }
            }
        }

        /* 4. 导出下载*/
        //4.1 设置编码
        response.setCharacterEncoding("UTF-8");
        //4.2 设置下载响应头
        response.setHeader("Content-Disposition","attachment;fileName=export.xlsx");
        //4.3 获取response输出流
        ServletOutputStream out = response.getOutputStream();
        //4.4 把excel文件流，写入到response输出流
        workbook.write(out);
        //4.5 关闭
        workbook.close();
        out.close();
    }






    //大标题的样式
    public CellStyle bigTitle(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)16);
        font.setBold(true);//字体加粗
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);				//横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        return style;
    }

    //小标题的样式
    public CellStyle title(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short)12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);				//横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        style.setBorderTop(BorderStyle.THIN);						//上细线
        style.setBorderBottom(BorderStyle.THIN);					//下细线
        style.setBorderLeft(BorderStyle.THIN);						//左细线
        style.setBorderRight(BorderStyle.THIN);						//右细线
        return style;
    }

    //文字样式
    public CellStyle text(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short)10);

        style.setFont(font);

        style.setAlignment(HorizontalAlignment.LEFT);				//横向居左
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        style.setBorderTop(BorderStyle.THIN);						//上细线
        style.setBorderBottom(BorderStyle.THIN);					//下细线
        style.setBorderLeft(BorderStyle.THIN);						//左细线
        style.setBorderRight(BorderStyle.THIN);						//右细线

        return style;
    }
}

















