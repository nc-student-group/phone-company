package com.phonecompany.service;

import com.phonecompany.dao.interfaces.TariffDao;
import com.phonecompany.model.Tariff;
import com.phonecompany.model.TariffRegion;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.service.interfaces.FileService;
import com.phonecompany.service.interfaces.TariffRegionService;
import com.phonecompany.service.interfaces.TariffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TariffServiceImpl extends CrudServiceImpl<Tariff> implements TariffService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TariffServiceImpl.class);

    private TariffDao tariffDao;
    private TariffRegionService tariffRegionService;

    private FileService fileService;

    @Autowired
    public TariffServiceImpl(TariffDao tariffDao, TariffRegionService tariffRegionService, FileService fileService) {
        super(tariffDao);
        this.tariffDao = tariffDao;
        this.tariffRegionService = tariffRegionService;
        this.fileService = fileService;
    }

    @Override
    public List<Tariff> getByRegionIdAndPaging(long regionId, int page, int size) {
        return tariffDao.getByRegionIdAndPaging(regionId, page, size);
    }

    @Override
    public List<Tariff> getByRegionIdAndClient(Long regionId, Boolean isRepresentative) {
        return tariffDao.getByRegionId(regionId).stream()
                .filter(t -> (t.getProductStatus().equals(ProductStatus.ACTIVATED) &&
                        isRepresentative.equals(t.isCorporate())))
                .collect(Collectors.toList());
    }

    @Override
    public Integer getCountByRegionIdAndPaging(long regionId) {
        return tariffDao.getCountByRegionIdAndPaging(regionId);
    }

    @Override
    public Map<String, Object> getTariffsTable(long regionId, int page, int size) {
        Map<String, Object> response = new HashMap<>();
        List<Tariff> tariffs = this.getByRegionIdAndPaging(regionId, page, size);
        List<Object> rows = new ArrayList<>();
        tariffs.forEach((Tariff tariff) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("tariff", tariff);
            row.put("regions", tariffRegionService.getAllByTariffId(tariff.getId()));
            rows.add(row);
        });
        response.put("tariffs", rows);
        response.put("tariffsSelected", this.getCountByRegionIdAndPaging(regionId));
        return response;
    }

    @Override
    public void updateTariffStatus(long tariffId, ProductStatus productStatus) {
        this.tariffDao.updateTariffStatus(tariffId, productStatus);
    }

    @Override
    public Tariff findByTariffName(String tariffName) {
        return this.tariffDao.findByTariffName(tariffName);
    }

    @Override
    public ResponseEntity<?> updateTariffAndRegions(List<TariffRegion> tariffRegions) {
        if (tariffRegions.size() > 0) {
            Tariff tariff = tariffRegions.get(0).getTariff();
            Tariff temp = this.findByTariffName(tariff.getTariffName());
            if (temp != null && temp.getId() != tariff.getId()) {
                return new ResponseEntity<>(new Error("Tariff with name \"" + tariff.getTariffName() + "\" already exist!"), HttpStatus.BAD_REQUEST);
            }
            tariff.setPictureUrl(fileService.stringToFile(tariff.getPictureUrl(), "tariff/" + tariff.getCreationDate().getTime()));
            Tariff savedTariff = this.update(tariff);
            LOGGER.debug("Tariff added {}", tariff);
            tariffRegionService.deleteByTariffId(savedTariff.getId());
            tariffRegions.forEach((TariffRegion tariffRegion) -> {
                if (tariffRegion.getPrice() > 0 && tariffRegion.getRegion() != null) {
                    tariffRegion.setTariff(savedTariff);
                    tariffRegionService.save(tariffRegion);
                    LOGGER.debug("Tariff-region added {}", tariffRegion);
                }
            });
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public List<Tariff> getTariffsAvailableForCustomer(long regionId, int page, int size){
        return tariffDao.getTariffsAvailableForCustomer(regionId, page, size);
    }

    public Integer getCountTariffsAvailableForCustomer(long regionId){
        return tariffDao.getCountTariffsAvailableForCustomer(regionId);
    }

}
