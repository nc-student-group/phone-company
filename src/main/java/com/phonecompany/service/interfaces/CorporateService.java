package com.phonecompany.service.interfaces;

import com.phonecompany.model.Corporate;

import java.util.List;
import java.util.Map;

public interface CorporateService extends CrudService<Corporate> {
    Map<String, Object> getAllCorporatePaging(int page, int size, String partOfName);
}
