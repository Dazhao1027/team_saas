package cn.itcast.domain.vo;

import java.io.Serializable;

public class ShippingVo implements Serializable {
    //现在定义封装值对象的VO对象（Value Object）
    private String shippingOrderId; //主键ID

    private String orderType; //运输方式

    private String shipper; //货主

    private String consignee; //货单抬头

    private String notifyParty; //正本通知人

    private String lcNo; //信用证号

    private String portOfLoading; //装运港

    private String portOfTrans; //转船港

    private String portOfDischar; //卸货港

    private String isBatch; //是否分批

    private String isTrans; //是否转船

    private String remark; //扼要说明

    private Integer state; //状态

    public ShippingVo() {
    }

    public ShippingVo(String shippingOrderId, String orderType, String shipper, String consignee, String notifyParty, String lcNo, String portOfLoading, String portOfTrans, String portOfDischar, String isBatch, String isTrans, String remark, Integer state) {
        this.shippingOrderId = shippingOrderId;
        this.orderType = orderType;
        this.shipper = shipper;
        this.consignee = consignee;
        this.notifyParty = notifyParty;
        this.lcNo = lcNo;
        this.portOfLoading = portOfLoading;
        this.portOfTrans = portOfTrans;
        this.portOfDischar = portOfDischar;
        this.isBatch = isBatch;
        this.isTrans = isTrans;
        this.remark = remark;
        this.state = state;
    }

    /**
     * 获取
     * @return shippingOrderId
     */
    public String getShippingOrderId() {
        return shippingOrderId;
    }

    /**
     * 设置
     * @param shippingOrderId
     */
    public void setShippingOrderId(String shippingOrderId) {
        this.shippingOrderId = shippingOrderId;
    }

    /**
     * 获取
     * @return orderType
     */
    public String getOrderType() {
        return orderType;
    }

    /**
     * 设置
     * @param orderType
     */
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    /**
     * 获取
     * @return shipper
     */
    public String getShipper() {
        return shipper;
    }

    /**
     * 设置
     * @param shipper
     */
    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    /**
     * 获取
     * @return consignee
     */
    public String getConsignee() {
        return consignee;
    }

    /**
     * 设置
     * @param consignee
     */
    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    /**
     * 获取
     * @return notifyParty
     */
    public String getNotifyParty() {
        return notifyParty;
    }

    /**
     * 设置
     * @param notifyParty
     */
    public void setNotifyParty(String notifyParty) {
        this.notifyParty = notifyParty;
    }

    /**
     * 获取
     * @return lcNo
     */
    public String getLcNo() {
        return lcNo;
    }

    /**
     * 设置
     * @param lcNo
     */
    public void setLcNo(String lcNo) {
        this.lcNo = lcNo;
    }

    /**
     * 获取
     * @return portOfLoading
     */
    public String getPortOfLoading() {
        return portOfLoading;
    }

    /**
     * 设置
     * @param portOfLoading
     */
    public void setPortOfLoading(String portOfLoading) {
        this.portOfLoading = portOfLoading;
    }

    /**
     * 获取
     * @return portOfTrans
     */
    public String getPortOfTrans() {
        return portOfTrans;
    }

    /**
     * 设置
     * @param portOfTrans
     */
    public void setPortOfTrans(String portOfTrans) {
        this.portOfTrans = portOfTrans;
    }

    /**
     * 获取
     * @return portOfDischar
     */
    public String getPortOfDischar() {
        return portOfDischar;
    }

    /**
     * 设置
     * @param portOfDischar
     */
    public void setPortOfDischar(String portOfDischar) {
        this.portOfDischar = portOfDischar;
    }

    /**
     * 获取
     * @return isBatch
     */
    public String getIsBatch() {
        return isBatch;
    }

    /**
     * 设置
     * @param isBatch
     */
    public void setIsBatch(String isBatch) {
        this.isBatch = isBatch;
    }

    /**
     * 获取
     * @return isTrans
     */
    public String getIsTrans() {
        return isTrans;
    }

    /**
     * 设置
     * @param isTrans
     */
    public void setIsTrans(String isTrans) {
        this.isTrans = isTrans;
    }

    /**
     * 获取
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取
     * @return state
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置
     * @param state
     */
    public void setState(Integer state) {
        this.state = state;
    }

    public String toString() {
        return "ShippingVo{shippingOrderId = " + shippingOrderId + ", orderType = " + orderType + ", shipper = " + shipper + ", consignee = " + consignee + ", notifyParty = " + notifyParty + ", lcNo = " + lcNo + ", portOfLoading = " + portOfLoading + ", portOfTrans = " + portOfTrans + ", portOfDischar = " + portOfDischar + ", isBatch = " + isBatch + ", isTrans = " + isTrans + ", remark = " + remark + ", state = " + state + "}";
    }
}
