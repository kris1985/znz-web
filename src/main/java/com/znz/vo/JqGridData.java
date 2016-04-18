package com.znz.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by admin on 2016/4/16.
 */
@Data
@NoArgsConstructor
public class JqGridData<T> {

    /** Total number of pages */
    private int total;
    /** The current page number */
    private int page;
    /** Total number of records */
    private int records;
    /** The actual data */
    private List<T> rows;

    public JqGridData(int total, int page, int records, List<T> rows) {
        this.total = total;
        this.page = page;
        this.records = records;
        this.rows = rows;
    }
}
