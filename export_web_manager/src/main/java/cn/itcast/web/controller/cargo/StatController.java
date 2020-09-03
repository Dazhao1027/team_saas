package cn.itcast.web.controller.cargo;

import cn.itcast.service.stat.StatService;
import cn.itcast.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/stat")
public class StatController extends BaseController {

    @Reference
    private StatService statService;

    /**
     * 进入三个统计页面
     * http://localhost:8080/stat/toCharts.do?chartsType=factory
     * http://localhost:8080/stat/toCharts.do?chartsType=sell
     * http://localhost:8080/stat/toCharts.do?chartsType=online
     */
    @GetMapping("/toCharts")
    public String toCharts(String chartsType){ // chartsType = factory
        return "stat/stat-" + chartsType;
    }

    /**
     * 需求1：生产厂家销售统计
     * @return
     */
    @RequestMapping("/factorySale")
    @ResponseBody  // 自动把方法返回的对象转json
    public List<Map<String, Object>> factorySale(){
        List<Map<String, Object>> list = statService.factorySale();
        return list;
    }

    /**
     * 需求2：产品销售排行前5
     * @return
     */
    @RequestMapping("/productSale")
    @ResponseBody  // 自动把方法返回的对象转json
    public List<Map<String, Object>> productSale(){
        return statService.productSale();
    }

    /**
     * 需求3：按小时统计访问人数
     * @return
     */
    @RequestMapping("/online")
    @ResponseBody  // 自动把方法返回的对象转json
    public List<Map<String, Object>> online(){
        return statService.online();
    }
}
