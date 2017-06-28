package cn.itcast.tom.stormlog.orderMonitor.domain;

/**
 * Describe: 所需要判断的规则字段
 * Author:   maoxiangyi
 * Domain:   www.itcast.cn
 * Data:     2015/11/23.
 */
public class RuleField {
    private int id;
    private String field;
    private String desc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "RuleField{" +
                "id=" + id +
                ", field='" + field + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
