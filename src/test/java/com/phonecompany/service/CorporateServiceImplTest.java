package com.phonecompany.service;

import com.phonecompany.dao.interfaces.CorporateDao;
import com.phonecompany.model.Corporate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CorporateServiceImplTest {

    @Mock
    private CorporateDao corporateDao;

    @InjectMocks
    private CorporateServiceImpl corporateService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnCorporateById() {
        long id = 1L;
        Corporate corporate = createCorporate(id);

        when(corporateDao.getById(id)).thenReturn(corporate);

        corporate = corporateService.getById(id);

        assertThat(id, equalTo(corporate.getId()));
    }

    @Test
    public void shouldUpdateCorporate() {
        long id = 1L;
        Corporate corporate = createCorporate(id);

        corporateService.update(corporate);

        verify(corporateDao, atLeastOnce()).update(corporate);
    }

    @Test
    public void shouldSaveCorporate() {
        long id = 1L;
        Corporate corporate = createCorporate(id);

        corporateService.save(corporate);

        verify(corporateDao, atLeastOnce()).save(corporate);
    }

    @Test
    public void shouldGetAllCorporates() {
        long id1 = 1L;
        Corporate corporate1 = createCorporate(id1);

        long id2 = 2L;
        Corporate corporate2 = createCorporate(id2);

        when(corporateDao.getAll()).thenReturn(Arrays.asList(corporate1, corporate2));

        corporateService.getAll();

        assertThat(id1, equalTo(corporate1.getId()));

        assertThat(id2, equalTo(corporate2.getId()));
    }


    private Corporate createCorporate(Long id) {
        Corporate corporate = new Corporate();
        corporate.setId(id);
        corporate.setCorporateName("Corporation");
        return corporate;
    }

}