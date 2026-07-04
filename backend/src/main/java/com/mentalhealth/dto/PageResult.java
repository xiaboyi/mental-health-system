package com.mentalhealth.dto;

import java.util.List;

public class PageResult<T> {
    private Long total;
    private Long pages;
    private Long current;
    private Long size;
    private List<T> records;

    public static <T> PageResult<T> of(Long total, Long pages, Long current, Long size, List<T> records) {
        PageResult<T> r = new PageResult<>();
        r.total = total;
        r.pages = pages;
        r.current = current;
        r.size = size;
        r.records = records;
        return r;
    }

    public Long getTotal() { return total; }
    public void setTotal(Long total) { this.total = total; }
    public Long getPages() { return pages; }
    public void setPages(Long pages) { this.pages = pages; }
    public Long getCurrent() { return current; }
    public void setCurrent(Long current) { this.current = current; }
    public Long getSize() { return size; }
    public void setSize(Long size) { this.size = size; }
    public List<T> getRecords() { return records; }
    public void setRecords(List<T> records) { this.records = records; }
}
