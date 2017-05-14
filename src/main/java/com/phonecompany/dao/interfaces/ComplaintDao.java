package com.phonecompany.dao.interfaces;

import com.phonecompany.model.Complaint;
import com.phonecompany.util.Query;

import java.util.List;

public interface ComplaintDao extends CrudDao<Complaint>,
        AbstractPageableDao<Complaint> {
    public List<Complaint> getAllComplaintsSearch(Query query);
}
