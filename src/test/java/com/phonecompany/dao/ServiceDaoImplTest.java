package com.phonecompany.dao;

import com.phonecompany.TestUtil;
import com.phonecompany.dao.interfaces.ServiceDao;
import com.phonecompany.model.ProductCategory;
import com.phonecompany.model.Service;
import com.phonecompany.model.enums.ProductStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static com.phonecompany.TestUtil.EXISTING_SERVICE_ID;
import static com.phonecompany.model.enums.ProductStatus.ACTIVATED;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ServiceDaoImplTest {

    @Autowired
    private ServiceDao serviceDao;

    @Test
    public void shouldSaveService() {
        //given
        Service sampleService = TestUtil.getNonExistentService();

        //when
        serviceDao.save(sampleService);

        //then
        Service serviceFoundById = serviceDao.getById(sampleService.getId());

        assertThat(serviceFoundById.getServiceName(), is(sampleService.getServiceName()));
        assertThat(serviceFoundById.getPrice(), is(sampleService.getPrice()));
        assertThat(serviceFoundById.getProductStatus(), is(sampleService.getProductStatus()));
        assertThat(serviceFoundById.getDiscount(), is(sampleService.getDiscount()));
    }

    @Test
    public void shouldReturnTrueOnExistingService() {
        //given
        Service nonExistentService = TestUtil.getNonExistentService();

        //when
        boolean isExist = serviceDao.isExist(nonExistentService);

        //then
        assertThat(isExist, is(false));
    }

    @Test
    public void shouldReturnFalseIfServiceDoesNotExist() {
        //given
        Service existingService = TestUtil.getExistingService();

        //when
        boolean isExist = serviceDao.isExist(existingService);

        //then
        assertThat(isExist, is(true));
    }

    @Test
    public void shouldUpdateServiceStatus() {
        //given
        Service initialService = serviceDao.getById(EXISTING_SERVICE_ID);
        ProductStatus initialStatus = initialService.getProductStatus();
        ProductStatus expectedStatus = TestUtil.getAnotherProductStatus(initialStatus);

        //when
        serviceDao.updateServiceStatus(initialService.getId(), expectedStatus);

        //then
        Service updatedService = serviceDao.getById(EXISTING_SERVICE_ID);
        assertThat(updatedService.getProductStatus(), is(expectedStatus));
    }

    @Test
    public void shouldGetServiceById() {
        //when
        Service serviceById = serviceDao.getById(EXISTING_SERVICE_ID);

        //then
        assertThat(serviceById.getProductCategory().getCategoryName(),
                is("Internet package"));
        assertThat(serviceById.getServiceName(), is("3G ONLINE 8GB"));
        assertThat(serviceById.getPrice(), is(129.99));
        assertThat(serviceById.getProductStatus(), is(ACTIVATED));
        assertThat(serviceById.getDiscount(), is(0.0));
        assertThat(serviceById.getPictureUrl(),
                is("https://s3-us-west-2.amazonaws.com/contentorders/service/218969357/picture.png"));
        assertThat(serviceById.getDurationInDays(), is(30));
        assertThat(serviceById.getAmount(), is(15));
    }
}
