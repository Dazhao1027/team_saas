package cn.itcast.domain.cargo;

import java.io.Serializable;

public class MultiExport implements Serializable {
    //多个报运单的时候,数据存在这里
    private String lcno; //信用证号
    private String marks; //唛头

    public MultiExport() {
    }

    public MultiExport(String lcno, String marks) {
        this.lcno = lcno;
        this.marks = marks;
    }

    /**
     * 获取
     * @return lcno
     */
    public String getLcno() {
        return lcno;
    }

    /**
     * 设置
     * @param lcno
     */
    public void setLcno(String lcno) {
        this.lcno = lcno;
    }

    /**
     * 获取
     * @return marks
     */
    public String getMarks() {
        return marks;
    }

    /**
     * 设置
     * @param marks
     */
    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String toString() {
        return "MultiExport{lcno = " + lcno + ", marks = " + marks + "}";
    }
}
