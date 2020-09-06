package cn.itcast.domain.cargo;

import java.io.Serializable;
import java.util.Date;

public class PackingList implements Serializable {
    private String packingListId;

    private String seller;

    private String buyer;

    private String invoiceNo;

    private Date invoiceDate;

    private String marks;

    private String exportIds;

    private String exportNos;

    private Long state;

    private String createBy;

    private String createDept;

    private Date createTime;

    private String companyId;

    private String companyName;

    private Date packingTime;

    private Double totalVolume;

    private Double netWeights;

    private Double grossWeights;

    private String description;

    private Double packingMoney;

    public String getPackingListId() {
        return packingListId;
    }

    public void setPackingListId(String packingListId) {
        this.packingListId = packingListId == null ? null : packingListId.trim();
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller == null ? null : seller.trim();
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer == null ? null : buyer.trim();
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo == null ? null : invoiceNo.trim();
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks == null ? null : marks.trim();
    }

    public String getExportIds() {
        return exportIds;
    }

    public void setExportIds(String exportIds) {
        this.exportIds = exportIds == null ? null : exportIds.trim();
    }

    public String getExportNos() {
        return exportNos;
    }

    public void setExportNos(String exportNos) {
        this.exportNos = exportNos == null ? null : exportNos.trim();
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public String getCreateDept() {
        return createDept;
    }

    public void setCreateDept(String createDept) {
        this.createDept = createDept == null ? null : createDept.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public Date getPackingTime() {
        return packingTime;
    }

    public void setPackingTime(Date packingTime) {
        this.packingTime = packingTime;
    }

    public Double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public Double getNetWeights() {
        return netWeights;
    }

    public void setNetWeights(Double netWeights) {
        this.netWeights = netWeights;
    }

    public Double getGrossWeights() {
        return grossWeights;
    }

    public void setGrossWeights(Double grossWeights) {
        this.grossWeights = grossWeights;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Double getPackingMoney() {
        return packingMoney;
    }

    public void setPackingMoney(Double packingMoney) {
        this.packingMoney = packingMoney;
    }
}