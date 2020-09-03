package cn.itcast.dao.stat;

import java.util.List;
import java.util.Map;

public interface StatDao {
    /**
     * 需求1：统计出各个生产厂家的销售额
     */
    List<Map<String,Object>> factorySale();

    /**
     * 需求2：商品销售统计排行
     */
    List<Map<String,Object>> productSale();

    /**
     * 需求3：按小时统计访问人数
     */
    List<Map<String,Object>> online();
}
