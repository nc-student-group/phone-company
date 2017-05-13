package com.phonecompany.service.interfaces;

import com.phonecompany.model.*;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.service.xssfHelper.SheetDataSet;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TariffService extends CrudService<Tariff> {
    List<Tariff> getByRegionIdAndPaging(long regionId, int page, int size);

    public Integer getCountByRegionId(long regionId);

    public void updateTariffStatus(long tariffId, ProductStatus productStatus);

    public Tariff findByTariffName(String tariffName);

    public Map<String, Object> getTariffsAvailableForCustomer(Customer customer, int page, int size);

    public List<Tariff> getTariffsAvailableForCustomer(Customer customer);

    public List<Tariff> getTariffsAvailableForCustomer(long regionId, int page, int size);

    public Integer getCountTariffsAvailableForCustomer(long regionId);

    public List<Tariff> getTariffsAvailableForCorporate(int page, int size);

    public List<Tariff> getTariffsAvailableForCorporate();

    public Integer getCountTariffsAvailableForCorporate();

    public Tariff getByIdForSingleCustomer(long id, long regionId);

    public void deactivateSingleTariff(CustomerTariff customerTariff);

    public void deactivateCorporateTariff(CustomerTariff customerTariff);

    public void activateSingleTariff(Customer customer, TariffRegion tariffRegion);

    public void activateCorporateTariff(Corporate corporate, Tariff tariff);

    public void activateTariff(long tariffId, Customer customer);

    public Tariff addNewTariff(List<TariffRegion> tariffRegions);

    public Tariff addNewTariff(Tariff tariff);

    public Tariff updateTariff(List<TariffRegion> tariffRegions);

    public Tariff updateTariff(Tariff tariff);

    public Tariff getTariffForCustomer(long tariffId, Customer customer);

    public Map<String, Object> getTariffsTable(int page, int size, String name, int status,
                                               int type, String from, String to, int orderBy, int orderByType);

    public void activateTariffForSingleCustomer(long tariffId, Customer customer);

    public void activateTariffForCorporateCustomer(long tariffId, Corporate corporate);

    SheetDataSet prepareTariffReportDataSet(long regionId,
                                            LocalDate startDate,
                                            LocalDate endDate);
}
