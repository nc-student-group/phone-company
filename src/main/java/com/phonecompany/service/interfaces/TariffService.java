package com.phonecompany.service.interfaces;

import com.phonecompany.model.*;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.service.xssfHelper.SheetDataSet;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TariffService extends CrudService<Tariff>,
        ExtendedStatisticsGenerating<LocalDate, Long> {

    void updateTariffStatus(long tariffId, ProductStatus productStatus);

    Tariff findByTariffName(String tariffName);

    Map<String, Object> getTariffsAvailableForCustomer(Customer customer, int page, int size);

    List<Tariff> getTariffsAvailableForCustomer(Customer customer);

    List<Tariff> getTariffsAvailableForCustomer(long regionId, int page, int size);

    Integer getCountTariffsAvailableForCustomer(long regionId);

    List<Tariff> getTariffsAvailableForCorporate(int page, int size);

    List<Tariff> getTariffsAvailableForCorporate();

    Map<String, Object> getTariffsAvailableForCorporatePaged(int page, int size);

    Integer getCountTariffsAvailableForCorporate();

    Tariff getByIdForSingleCustomer(long id, long regionId);

    void deactivateSingleTariff(CustomerTariff customerTariff);

    void deactivateCorporateTariff(CustomerTariff customerTariff);

    void activateSingleTariff(Customer customer, TariffRegion tariffRegion);

    void activateCorporateTariff(Corporate corporate, Tariff tariff);

    void activateTariff(long tariffId, Customer customer);

    Tariff addNewTariff(List<TariffRegion> tariffRegions);

    Tariff addNewTariff(Tariff tariff);

    Tariff updateTariff(List<TariffRegion> tariffRegions);

    Tariff updateTariff(Tariff tariff);

    Tariff getTariffForCustomer(long tariffId, Customer customer);

    Map<String, Object> getTariffsTable(int page, int size, String name, int status,
                                        int type, String from, String to, int orderBy, String orderByType);

    Map<String, Object> getTariffsTable(int page, int size, long regionId);

    void activateTariffForSingleCustomer(long tariffId, Customer customer);

    void activateTariffForCorporateCustomer(long tariffId, Corporate corporate);

    Map<String, Object> getAllTariffsSearch(int page, int size, String name, String status, String category);

}
