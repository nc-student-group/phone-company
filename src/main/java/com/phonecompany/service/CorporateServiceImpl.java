package com.phonecompany.service;

import com.phonecompany.annotations.ServiceStereotype;
import com.phonecompany.dao.interfaces.CorporateDao;
import com.phonecompany.model.Corporate;
import com.phonecompany.service.interfaces.CorporateService;
import com.phonecompany.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ServiceStereotype
public class CorporateServiceImpl extends CrudServiceImpl<Corporate> implements CorporateService {

    private CorporateDao corporateDao;

    @Autowired
    public CorporateServiceImpl(CorporateDao corporateDao) {
        this.corporateDao = corporateDao;
    }

    @Override
    public Map<String, Object> getAllCorporatePaging(int page, int size, String partOfName) {


        Query.Builder builder = new Query.Builder("corporate");
        builder.where().addLikeCondition("corporate_name", partOfName).addPaging(page, size);
        Query query = builder.build();
        Map<String, Object> response = new HashMap<>();
        response.put("corporates", corporateDao.executeForList(query.getQuery(), query.getPreparedStatementParams().toArray()));
        response.put("corporatesSelected", corporateDao.executeForInt(query.getCountQuery(), query.getCountParams().toArray()));
        return response;
    }

}
