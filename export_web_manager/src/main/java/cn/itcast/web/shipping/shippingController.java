package cn.itcast.web.shipping;

import cn.itcast.domain.cargo.Export;
import cn.itcast.domain.cargo.MultiExport;
import cn.itcast.domain.cargo.PackingList;
import cn.itcast.domain.shipping.Shipping;
import cn.itcast.domain.shipping.ShippingExample;
import cn.itcast.domain.system.User;
import cn.itcast.service.cargo.ExportService;
import cn.itcast.service.cargo.PackingListService;
import cn.itcast.service.cargo.ShippingService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/cargo/shipping")
public class shippingController extends BaseController {
    @Reference
    private PackingListService packingListService;
    @Reference
    private ExportService exportService;
    @Reference
    private ShippingService shippingService;
    //到达添加页面
    @RequestMapping("/toAdd")
    public String add(String packingListId){
        //点击之后会传输装箱ID过来,
        // 然后得到装箱对象,
        PackingList packing = packingListService.findById(packingListId);
        // 得到装箱对象之后就根据里面的export_ids查询到export,然后装进请求域中
        String exportIds = packing.getExportIds();
        //如果是多个报运单的话,他们的ID是用逗号分隔开的,需要切割再转换成数组
        String[] split = exportIds.split(",");
        String lcno = "";
        String marks = "";
        //遍历得到所有的报运单
        for (String exportid : split) {
            Export export = exportService.findById(exportid);
            String exportLcno = export.getLcno();
            lcno+= exportLcno+",";
            String exportMarks = export.getMarks();
            marks+=exportMarks+",";
            request.setAttribute("export",export);
        }
        //加入
        lcno = lcno.substring(0,lcno.length()-1);
        marks = marks.substring(0,marks.length()-1);
        MultiExport multiExport = new MultiExport();
        multiExport.setLcno(lcno);
        multiExport.setMarks(marks);
        //放进请求域
        request.setAttribute("multiExport",multiExport);
        //将装箱单ID放进请求域中
        request.setAttribute("packing",packing);
        return "shipping/shipping-add";
    }
    //保存
    @RequestMapping("/edit")
    public String save(Shipping shipping,String id){
        //页面数据提交之后会自动封装到一个shipping对象中,id是装箱单ID
        //添加建表人
        shipping.setCreateBy(getLoginUser().getId());
        //添加部门名称
        shipping.setCreateDept(getLoginUser().getDeptId());
        //添加企业ID
        shipping.setCompanyId(getLoginCompanyId());
        //添加企业名称
        shipping.setCompanyName(getLoginCompanyName());
        //调用添加方法
        shippingService.save(shipping,id);
        //跳转到列表页面
        return "redirect:/cargo/shipping/list.do";
    }

    //到达列表页面
    @RequestMapping("/list.do")
    public String list(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize){
        //查询委托页面对象,细粒化权限查询
        //构造查询条件
        ShippingExample example = new ShippingExample();
        // 排序：create_time 创建时间降序排序
        example.setOrderByClause("create_time desc");
        ShippingExample.Criteria criteria = example.createCriteria();
        criteria.andCompanyIdEqualTo(getLoginCompanyId());
        /**
         * 细粒度权限控制：不同用户查看购销合同列表看到的数据不一样
         * 1. 登陆用户等级degree=4,普通用户，只能查看自己创建的购销合同
         * 2. 登陆用户等级degree=3,部门经理，可以查看当前部门的所有购销合同
         * 3. 登陆用户等级degree=2,大部门经理，可以查看当前部门的购销合同，以及所有子部门
         */
        User loginUser = getLoginUser();
        PageInfo<Shipping> pageInfo = null;
        if (loginUser.getDegree() == 4){
            // SELECT * FROM co_contract WHERE create_by='登陆用户的id'
            criteria.andCreateByEqualTo(loginUser.getId());
            pageInfo = shippingService.findByPage(example,pageNum,pageSize);
        }
        else if (loginUser.getDegree() == 3){
            // SELECT * FROM co_contract WHERE create_dept='登陆用户的部门id'
            criteria.andCreateDeptEqualTo(loginUser.getDeptId());
            pageInfo = shippingService.findByPage(example,pageNum,pageSize);
        }
        else if (loginUser.getDegree() == 2) {
            // SELECT * FROM co_contract WHERE FIND_IN_SET(create_dept,getDeptChild('100'))
            pageInfo = shippingService.findByDeptId(loginUser.getDeptId(),pageNum,pageSize);
        }
        //保存结果
        request.setAttribute("pageInfo", pageInfo);
        return "shipping/shipping-list";
    }

    //删除,需要判断委托单的状态是否是草稿
    @RequestMapping("/delete")
    @ResponseBody
    public String cancelDraft(String id){
        //删除状态为草稿的委托单,id为选中的委托订单
        //调用查询方法查询委托单
        Shipping shipping = shippingService.findById(id);
        //0为草稿,1为已开票
        Integer state = shipping.getState();
        //判断
        if (state!=null&&state==0){
            //为0可以删除,返回true
            shippingService.delete(id);
            return "1";
        }else {
            //否则返回false
            return "0";
        }

    }

    /**
     * 根据ID实现模板导出委托单Excel
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("/excel")
        public void printExcel(String id, HttpServletRequest request) throws IOException {
        //传输ID到控制器,该ID可以同时查询装箱单和报运单
        Shipping shipping = shippingService.findById(id);
        PackingList packing = packingListService.findById(id);
        //获取项目资源
        InputStream in = request.getSession().getServletContext().getResourceAsStream("make/xlsprint/tSHIPPINGORDER.xlsx");
        //模板导出,传入文件流模板
        XSSFWorkbook workbook = new XSSFWorkbook(in);
        //获取工作表
        XSSFSheet sheet = workbook.getSheetAt(0);
        //获取第四行,货主shipper,索引为3
        XSSFRow row = sheet.getRow(3);
        //获取第四行第一列
        XSSFCell cell = row.getCell(0);
        //设置货主内容
        String shipper = shipping.getShipper();
        cell.setCellValue(shipper);
        //获取第四行第六列,企业名称
        if (row.getCell(5)!=null){
            cell = row.getCell(5);
            cell.setCellValue(getLoginCompanyName());
        }
        //获取第六行
         row = sheet.getRow(5);
         //获取第六行第六列,电话:TEL
        cell = row.getCell(5);
        User loginUser = getLoginUser();
        //将登陆用户的电话传进去
        cell.setCellValue("TEL:"+loginUser.getTelephone());
        //获得第九行第一列,提单抬头
        XSSFRow Consigneerow = sheet.getRow(8);
        XSSFCell Consigneecell = Consigneerow.getCell(0);
        //将提单抬头放进去
        Consigneecell.setCellValue(shipping.getConsignee());
        //获得第十六行第一列,正本通知人
        XSSFRow Notifypartyrow = sheet.getRow(15);
        XSSFCell notifypartyrowCell = Notifypartyrow.getCell(0);
        //将正本通知人放进去
        notifypartyrowCell.setCellValue(shipping.getNotifyParty());
        //获得第二十行第一列和第二列,进行合并,然后放入发票号
        // 合并单元格
       sheet.addMergedRegion(new CellRangeAddress(19,19,0,1));
        //获得第二十行
        XSSFRow invoicerow = sheet.getRow(19);
        //获得第二十行第一列
        String invoiceNo = packing.getInvoiceNo();
        XSSFCell cell2 = invoicerow.getCell(0);
        if (invoicerow.getCell(0)==null){
             cell2 = invoicerow.createCell(0);
        }
        if (invoiceNo!=null){
               invoicerow.getCell(0).setCellValue(invoiceNo);
           }else {
               invoicerow.getCell(0).setCellValue("待定");
           }

        //合并单元格:时间
        sheet.addMergedRegion(new CellRangeAddress(19,19,3,4));
        //获得第二十行第四列,建表时间
//        if ( invoicerow.getCell(3)!=null){
////            invoicerow.getCell(3).setCellValue(shipping.getCreateTime());
////        }
        XSSFCell timecell = invoicerow.getCell(3);
        if (invoicerow.getCell(3)==null){
            timecell = invoicerow.createCell(3);
        }
        if (shipping.getCreateTime()!=null){
            String format = new SimpleDateFormat("yyyy-MM-dd").format(shipping.getCreateTime());
            invoicerow.getCell(3).setCellValue(format);
        }else {
            invoicerow.getCell(3).setCellValue("待定");
        }
        //合并单元格,信用证
        sheet.addMergedRegion(new CellRangeAddress(19,19,6,7));
        //获得第二十行第7列,信用证号
//        if (invoicerow.getCell(6)!=null){
//            invoicerow.getCell(6).setCellValue(shipping.getLcNo());
//        }
        XSSFCell lcnorow = invoicerow.getCell(6);
        if (invoicerow.getCell(6)==null){
            lcnorow = invoicerow.createCell(6);
        }
        if (shipping.getLcNo()!=null){
            invoicerow.getCell(6).setCellValue(shipping.getLcNo());
        }else {
            invoicerow.getCell(6).setCellValue("待定");
        }

        //合并单元格:装船港
        sheet.addMergedRegion(new CellRangeAddress(23,23,0,1));
        //合并单元格:转船港
        sheet.addMergedRegion(new CellRangeAddress(23,23,3,4));
        //合并单元格:卸货港
        sheet.addMergedRegion(new CellRangeAddress(23,23,6,7));
        //获得第24行
        XSSFRow portrow = sheet.getRow(23);
        //24行第一列:装船港
        XSSFCell loadcell = portrow.getCell(0);
        if (portrow.getCell(0)==null){
           loadcell =  portrow.createCell(0);
        }
        if (shipping.getPortOfLoading()!=null){
            portrow.getCell(0).setCellValue(shipping.getPortOfLoading());
        }else {
            portrow.getCell(0).setCellValue("待定");
        }
//        if (portrow.getCell(0)!=null){
////            portrow.getCell(0).setCellValue(shipping.getPortOfLoading());
////        }
        //24行第4列:转船港
        XSSFCell trancell = portrow.getCell(3);
        if (portrow.getCell(3)==null){
            trancell = portrow.createCell(3);
        }
        if (shipping.getPortOfTrans()!=null){
            portrow.getCell(3).setCellValue(shipping.getPortOfTrans());
        }else {
            portrow.getCell(3).setCellValue("待定");
        }
//        if ( portrow.getCell(3)!=null){
//            portrow.getCell(3).setCellValue(shipping.getPortOfTrans());
//        }
        //24行第7列:卸货港
        XSSFCell discharcell = portrow.getCell(6);
        if (portrow.getCell(6)==null){
            discharcell = portrow.createCell(6);
        }
        if (shipping.getPortOfDischar()!=null){
            portrow.getCell(6).setCellValue(shipping.getPortOfDischar());
        }else {
            portrow.getCell(6).setCellValue("待定");
        }
//        if (portrow.getCell(6)!=null){
//            portrow.getCell(6).setCellValue(shipping.getPortOfDischar());
//        }
        //合并28行单元格:装期
        sheet.addMergedRegion(new CellRangeAddress(27,27,0,1));
        //创建28行
        XSSFRow arow = sheet.getRow(27);
        XSSFCell cell1 = arow.getCell(0);
        if (arow.getCell(0)==null){
            cell1 = arow.createCell(0);
        }
        //创建28行第1列:装期
        Date loadingDate = shipping.getLoadingDate();
        if (loadingDate!=null){
            cell1.setCellValue(loadingDate);
        }else {
            cell1.setCellValue("待定");
        }
//        if ( arow.getCell(0)!=null){
//            if (loadingDate!=null){
//                arow.getCell(0).setCellValue(loadingDate);
//            }
//            else {
//                arow.getCell(0).setCellValue("待定");
//            }
//        }

        //创建28行第3列:效期
        XSSFCell cell3 = arow.getCell(2);
        if (arow.getCell(2)==null){
            cell3 = arow.createCell(2);
        }
        if (shipping.getLimitDate()!=null){
            arow.getCell(2).setCellValue(shipping.getLimitDate());
        }else {
            arow.getCell(2).setCellValue("待定");
        }
//        if ( arow.getCell(2)!=null){
//            Date limitDate = shipping.getLimitDate();
//            if (limitDate!=null&&!limitDate.equals("")){
//                arow.getCell(2).setCellValue(limitDate);
//            }
//            else {
//                arow.getCell(2).setCellValue("待定");
//            }
//        }

        if ( arow.getCell(3)!=null){
            //创建28行第4列:是否分批
            String isBatch = shipping.getIsBatch();
            if (isBatch.equals("1")){
                //等于1表示是
                arow.getCell(3).setCellValue("是");
            }
            else {
                arow.getCell(3).setCellValue("否");
            }
        }
        //创建28行第6列:是否转船
        if (arow.getCell(5)!=null){
            String isTrans = shipping.getIsTrans();
            if (isTrans.equals("1")){
                arow.getCell(5).setCellValue("是");
            }else {
                arow.getCell(5).setCellValue("否");
            }
        }
        XSSFCell cell4 = arow.getCell(7);
        if (arow.getCell(7)==null){
            cell4 = arow.createCell(7);
        }
        if (shipping.getCopyNum()!=null){
            arow.getCell(7).setCellValue(shipping.getCopyNum());
        }else {
            arow.getCell(7).setCellValue("待定");
        }

//        if (arow.getCell(7)!=null){
//            //创建28行第8列
//            String copyNum = shipping.getCopyNum();
//            if (copyNum!=null&&!copyNum.equals("")){
//                arow.getCell(7).setCellValue(copyNum);
//            } else {
//                arow.getCell(7).setCellValue("待定");
//            }
//        }

        //创建第32行第一列:唛头
        XSSFRow brow = sheet.getRow(31);
        if ( brow.getCell(0)!=null){
            brow.getCell(0).setCellValue(shipping.getRemark());
        }
        if ( brow.getCell(3)!=null){
            //创建第32行第四列:描述Descriptions
            brow.getCell(3).setCellValue(packing.getDescription());
        }
        if ( brow.getCell(5)!=null){
            //创建第32行第六列:Quantity
            brow.getCell(5).setCellValue(packing.getTotalVolume());
        }
        if ( brow.getCell(6)!=null){
            //创建第32行第七列:总毛重
            brow.getCell(6).setCellValue(packing.getGrossWeights());
        }
        if ( brow.getCell(8)!=null){
            //创建第32行第九列:Measurement
            brow.getCell(8).setCellValue("待定");
        }


        //创建第38行第二列:运输要求
        XSSFRow conditionrow = sheet.getRow(37);
        if ( conditionrow.getCell(0)!=null){
            conditionrow.getCell(0).setCellValue(shipping.getSpecialCondition());
        }


        //创建第44行第八列:复核
        XSSFRow crow = sheet.getRow(43);
        if ( crow.getCell(7)!=null){
            String checkBy = shipping.getCheckBy();
            if (checkBy!=null&&!checkBy.equals("")){
                crow.getCell(7).setCellValue(checkBy);
            }
            else {
                crow.getCell(7).setCellValue("待定");
            }
        }

        /* 4. 导出下载*/
        //4.1 设置编码
        response.setCharacterEncoding("UTF-8");
        //4.2 设置下载响应头
        response.setHeader("Content-Disposition","attachment;fileName=shippingorder.xlsx");
        //4.3 获取response输出流
        ServletOutputStream out = response.getOutputStream();
        //4.4 把excel文件流，写入到response输出流
        workbook.write(out);
        //4.5 关闭
        workbook.close();
        out.close();
}

}
