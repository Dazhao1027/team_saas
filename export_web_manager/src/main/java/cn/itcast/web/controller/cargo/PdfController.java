package cn.itcast.web.controller.cargo;

import cn.itcast.domain.cargo.Export;
import cn.itcast.domain.cargo.ExportProduct;
import cn.itcast.domain.cargo.ExportProductExample;
import cn.itcast.domain.system.User;
import cn.itcast.service.cargo.ExportProductService;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.web.controller.BaseController;
import cn.itcast.web.util.BeanMapUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.InputStream;
import java.util.*;

@Controller
@RequestMapping("/cargo/export")
public class PdfController extends BaseController {

    /**
     * 报运单列表，点击下载导出PDF， 测试
     * 1.入门案例，展示pdf  + 中文处理
     * http://localhost:8080/cargo/export/exportPdf.do?id=0
     */
    @RequestMapping("/exportPdf1")
    public void exportPdf1(HttpServletRequest request) throws Exception {
        //1. 加载jasper文件流
        InputStream in =
                request.getSession().getServletContext().getResourceAsStream("/jasper/test01.jasper");

        //2. 创建JasperPrint对象，用于往模板中填充数据
        // 参数1：jasper文件流
        // 参数2：通过map集合，往模板中填充数据
        // 参数3：通过数据源，往模板中填充数据
        JasperPrint jasperPrint =
                JasperFillManager.fillReport(in, new HashMap<>(), new JREmptyDataSource());

        //3. 导出PDF: 会把pdf文件流写入到response输出流，直接在页面显示
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }


    /**
     * 3.导出PDF，填充数据（1）map方式填充数据
     * 注意：java中通过map集合填充数据，对应的是模板中的Parameter参数。
     */
    @RequestMapping("/exportPdf3")
    public void exportPdf3(HttpServletRequest request) throws Exception {
        //1. 加载jasper文件流
        InputStream in =
                request.getSession().getServletContext().getResourceAsStream("/jasper/test03_parameter.jasper");

        // 定义map，往模板中填充数据
        Map<String, Object> map = new HashMap<>();
        map.put("userName", "张一天");
        map.put("email", "zyt@export.cn");
        map.put("deptName", "应用开发部");
        map.put("companyName", "字节跳动");

        //2. 创建JasperPrint对象，用于往模板中填充数据
        JasperPrint jasperPrint =
                JasperFillManager.fillReport(in, map, new JREmptyDataSource());

        //3. 导出PDF
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }


    // 注入连接池
    @Autowired
    private DataSource dataSource;

    /**
     * 4.导出PDF，填充数据（2）数据源填充 A jdbc数据源填充
     */
    @RequestMapping("/exportPdf4")
    public void exportPdf4(HttpServletRequest request) throws Exception {
        //1. 加载jasper文件流
        InputStream in =
                request.getSession()
                        .getServletContext().getResourceAsStream("/jasper/test04_jdbc.jasper");

        //2. 创建JasperPrint对象，用于往模板中填充数据
        JasperPrint jasperPrint =
                JasperFillManager.fillReport(in, null, dataSource.getConnection());

        //3. 导出PDF
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }


    /**
     * 5.导出PDF，填充数据（2）数据源填充 A jdbc数据源填充
     */
    @RequestMapping("/exportPdf5")
    public void exportPdf5(HttpServletRequest request) throws Exception {
        //1. 加载jasper文件流
        InputStream in =
                request.getSession()
                        .getServletContext().getResourceAsStream("/jasper/test05_javabean.jasper");

        /**
         * 通过javabean封装数据
         * 集合元素的类型是什么？
         *    1. 对象或者map都可以
         *    2. 举例
         *        List<User> list = new ArrayList<>();
         *        List<Map<String,Object>> list = new ArrayList<>();
         *    3. 无论集合元素是对象还是map，都要确保对象属性或者map的key与Fields名称一致。
         */
        List<Map<String, Object>> list = new ArrayList<>();
        // 初始化10条数据
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("userName", "张一天" + i);
            map.put("email", "zyt@export.cn");
            map.put("deptName", "应用开发部");
            map.put("companyName", "字节跳动");
            list.add(map);
        }


        // 创建数据源对象
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
        //2. 创建JasperPrint对象，用于往模板中填充数据
        JasperPrint jasperPrint =
                JasperFillManager.fillReport(in, null, jrDataSource);

        //3. 导出PDF
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }


    /**
     * 6.分组报表
     */
    @RequestMapping("/exportPdf6")
    public void exportPdf6(HttpServletRequest request) throws Exception {
        //1. 加载jasper文件流
        InputStream in =
                request.getSession()
                        .getServletContext().getResourceAsStream("/jasper/test06_group.jasper");

        List<Map<String, Object>> list = new ArrayList<>();
        // 初始化10条数据
        for (int j = 1; j <= 2; j++) {
            for (int i = 0; i < 5; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("userName", "张一天" + i);
                map.put("email", "zyt@export.cn");
                map.put("deptName", "应用开发部");
                map.put("companyName", "字节跳动" + j);// 根据企业名称分组，相同的作为一组
                list.add(map);
            }
        }

        // 创建数据源对象
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
        //2. 创建JasperPrint对象，用于往模板中填充数据
        JasperPrint jasperPrint =
                JasperFillManager.fillReport(in, null, jrDataSource);

        //3. 导出PDF
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }


    /**
     * 7.图形报表
     */
    @RequestMapping("/exportPdf7")
    public void exportPdf7(HttpServletRequest request) throws Exception {
        //1. 加载jasper文件流
        InputStream in =
                request.getSession()
                        .getServletContext().getResourceAsStream("/jasper/test07_chart.jasper");

        List<Map<String, Object>> list = new ArrayList<>();
        // 初始化10条数据
        for (int i = 0; i < 5; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", "安踏运动鞋"+i);
            map.put("value", new Random().nextInt(1000));
            list.add(map);
        }

        // 创建数据源对象
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
        //2. 创建JasperPrint对象，用于往模板中填充数据
        JasperPrint jasperPrint =
                JasperFillManager.fillReport(in, null, jrDataSource);

        //3. 导出PDF
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }


    @Reference
    private ExportService exportService;
    @Reference
    private ExportProductService exportProductService;

    /**
     * 最后的报表：导出报运单
     * http://localhost:8080/cargo/export/exportPdf.do?id=0
     */
    @RequestMapping("/exportPdf")
    public void exportPdf(HttpServletRequest request,String id) throws Exception {
        //1. 加载jasper文件流
        InputStream in =
                request.getSession()
                        .getServletContext().getResourceAsStream("/jasper/export.jasper");

        //1. 根据报运单id查询报运单
        Export export = exportService.findById(id);
        // 对象转换为map (引入自定义的工具类到项目)
        Map<String, Object> map = BeanMapUtils.beanToMap(export);

        //2. 根据报运单id查询商品集合
        ExportProductExample example = new ExportProductExample();
        example.createCriteria().andExportIdEqualTo(id);
        List<ExportProduct> productList = exportProductService.findAll(example);


        // 创建数据源对象
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(productList);
        //2. 创建JasperPrint对象，用于往模板中填充数据
        JasperPrint jasperPrint =
                JasperFillManager.fillReport(in, map, jrDataSource);

        //3. 导出PDF
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-disposition","attachment;fileName=export.pdf");
        ServletOutputStream outputStream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        outputStream.close();
    }
}
