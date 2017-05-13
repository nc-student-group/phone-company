package com.phonecompany.model.paging;

import java.util.List;

public final class PagingResult<T> {

    private final List<T> pagingResult;
    private final int entityCount;

    public PagingResult(List<T> pagingResult, int entityCount) {
        this.pagingResult = pagingResult;
        this.entityCount = entityCount;
    }

    public List<T> getPagingResult() {
        return pagingResult;
    }

    public int getEntityCount() {
        return entityCount;
    }

    @Override
    public String toString() {
        return "PagingResult{" +
                "pagingResult=" + pagingResult +
                ", entityCount=" + entityCount +
                '}';
    }
}
