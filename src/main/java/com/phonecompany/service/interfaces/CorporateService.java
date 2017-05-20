package com.phonecompany.service.interfaces;

import com.phonecompany.model.Corporate;

import java.util.List;

public interface CorporateService extends CrudService<Corporate> {
    List<Corporate> getAllCorporatePaging(int page, int size, String partOfName);

    int getCountCorporates(String partOfName);
}
