package com.phonecompany.service;

import com.phonecompany.TestUtil;
import com.phonecompany.dao.interfaces.ProductCategoryDao;
import com.phonecompany.dao.interfaces.ServiceDao;
import com.phonecompany.exception.ConflictException;
import com.phonecompany.model.Service;
import com.phonecompany.service.interfaces.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.phonecompany.TestUtil.*;
import static com.phonecompany.service.ServiceServiceImpl.REPRESENTATIVE_DISCOUNT;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceServiceImpl.class)
public class ServiceServiceImplTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @MockBean
    private ServiceDao serviceDao;
    @MockBean
    private ProductCategoryDao productCategoryDao;
    @MockBean
    private FileService fileService;
    @MockBean
    private OrderService orderService;
    @MockBean
    private CustomerService customerService;
    @MockBean
    private CustomerServiceService customerServiceService;
    @MockBean
    private StatisticsService<LocalDate, Long> statisticsService;

    @Captor
    private ArgumentCaptor<Service> serviceCaptor;

    @Autowired
    private ServiceService serviceService;

//    @Test
//    public void shouldApplyDiscountIfCurrentUserIsRepresentative() {
//        given
//        List<Service> sampleServices = getSampleServices();
//        List<Double> initialPrices = this.getInitialPrices(sampleServices);
//
//        when(serviceDao.getPaging(anyInt(), anyInt(), anyInt()))
//                .thenReturn(sampleServices);
//        when(serviceDao.getEntityCount(anyInt()))
//                .thenReturn(SAMPLE_SERVICES_ENTITY_COUNT);
//        when(customerService.getCurrentlyLoggedInUser())
//                .thenReturn(getSampleRepresentative());
//
//        when
//        PagingResult<Service> servicesByProductCategoryId = serviceService
//                .getServicesByProductCategoryId(1, 0, 5, "",
//                        0.0, 0.0, 0, 0, "ASC");
//        List<Service> pagingResult = servicesByProductCategoryId.getPagingResult();
//        int entityCount = servicesByProductCategoryId.getEntityCount();
//
//        then
//        verify(serviceDao, times(1)).getEntityCount(any());
//        verify(customerService, times(1)).getCurrentlyLoggedInUser();
//
//        assertThat(entityCount, is(SAMPLE_SERVICES_ENTITY_COUNT));
//        this.assertThatEachServiceIsCheaperByRepresentativeDiscount(pagingResult, initialPrices);
//    }

    private List<Double> getInitialPrices(List<Service> src) {
        return src.stream()
                .map(Service::getPrice)
                .collect(Collectors.toList());
    }

    private void assertThatEachServiceIsCheaperByRepresentativeDiscount(List<Service> pagingResult,
                                                                        List<Double> initialPrices) {
        for (int i = 0; i < pagingResult.size(); i++) {
            Double priceWithDiscount = pagingResult.get(i).getPrice();
            assertThat(priceWithDiscount, is(not(initialPrices.get(i))));
            assertThat(priceWithDiscount,
                    is((this.applyDiscountToInitialPrice(initialPrices.get(i), REPRESENTATIVE_DISCOUNT))));
        }
    }

    private Double applyDiscountToInitialPrice(Double initialPrice, Double discount) {
        return initialPrice * (1 - discount / 100);
    }

    @Test
    public void shouldApplyDiscountToIndividualServiceIfCurrentUserIsRepresentative() {
        //given
        Service initialService = getNonExistentService();
        Double initialPrice = initialService.getPrice();
        when(serviceDao.getById(anyLong()))
                .thenReturn(initialService);
        when(customerService.getCurrentlyLoggedInUser())
                .thenReturn(getSampleRepresentative());

        //when
        Service serviceFoundById = serviceService.getById(1L);

        //then
        verify(serviceDao, times(1)).getById(any());
        verify(customerService, times(1)).getCurrentlyLoggedInUser();

        Double servicePrice = serviceFoundById.getPrice();
        assertThat(servicePrice, is(not(initialPrice)));
        assertThat(servicePrice,
                is(this.applyDiscountToInitialPrice(initialPrice, REPRESENTATIVE_DISCOUNT)));
    }

    @Test
    public void shouldThrowExceptionOnExistingService() {
        //given
        Service sampleService = TestUtil.getNonExistentService();
        when(serviceDao.isExist(any())).thenReturn(true);

        thrown.expect(ConflictException.class);
        thrown.expectMessage("Service with name " +
                sampleService.getServiceName() + " already exists");

        //when
        serviceService.save(sampleService);

        //then
        verify(serviceDao, never()).save(any());
        verify(productCategoryDao, never()).getByName(any());
    }

    @Test
    public void shouldSaveService() {
        //given
        Service sampleService = TestUtil.getNonExistentService();
        when(serviceDao.isExist(any())).thenReturn(false);

        //when
        serviceService.save(sampleService);

        //then
        verify(serviceDao, times(1)).save(serviceCaptor.capture());
        verify(productCategoryDao, times(1)).getByName(anyString());

        assertThat(serviceCaptor.getValue(), is(sampleService));
    }
}