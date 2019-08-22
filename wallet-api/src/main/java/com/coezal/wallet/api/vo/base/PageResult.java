package com.coezal.wallet.api.vo.base;


import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;

/**
 *
 * 分页封装类
 * @author yj
 */
public class PageResult<T> implements Serializable {

    private Long count;

    private List<T> list;

    private Boolean isLastPage;

    private int prePage;

    public PageResult(PageInfo pageInfo) {
        this.count = pageInfo.getTotal();
        this.list = pageInfo.getList();
        this.isLastPage = pageInfo.isIsLastPage();
        this.prePage = pageInfo.getPrePage();
    }

    public PageResult(PageInfo pageInfo,List<T> list) {
        this.count = pageInfo.getTotal();
        this.list = list;
        this.isLastPage = pageInfo.isIsLastPage();
        this.prePage = pageInfo.getPrePage();
    }
    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Boolean getLastPage() {
        return isLastPage;
    }

    public void setLastPage(Boolean lastPage) {
        isLastPage = lastPage;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }
}
