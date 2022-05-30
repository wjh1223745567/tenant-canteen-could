package com.iotinall.canteen.protocol.excel.vo;

import lombok.Data;

import java.util.Objects;

@Data
public class TitleValue {
    private String value;
    private String label;
    private Integer isShow;
    private Integer isClick;
    private Integer sort;
    private String key;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TitleValue that = (TitleValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
