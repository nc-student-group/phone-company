package com.phonecompany;

import com.phonecompany.model.Customer;
import com.phonecompany.model.ProductCategory;
import com.phonecompany.model.Service;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.model.enums.UserRole;

import java.util.ArrayList;
import java.util.List;

public class TestUtil {

    public static final String EXAMPLE_EMAIL = "example@gmail.com";
    public static final String EXAMPLE_PASSWORD = "$root$";
    public static final boolean IS_REPRESENTATIVE = true;

    public static Customer getSampleRepresentative() {
        return new Customer(1L, EXAMPLE_EMAIL, EXAMPLE_PASSWORD,
                UserRole.CLIENT, null, null, null,
                null, null, null, null, IS_REPRESENTATIVE, null);
    }

    public static List<Service> getSampleServices() {

        List<Service> sampleServices = new ArrayList<>();

        sampleServices.add(new Service(1L, "Service1", 115,
                ProductStatus.ACTIVATED, 0, null, null, null, null, 0, 0));
        sampleServices.add(new Service(2L, "Service2", 100,
                ProductStatus.ACTIVATED, 0, null, null, null, null, 0, 0));
        sampleServices.add(new Service(3L, "Service3", 260,
                ProductStatus.ACTIVATED, 0, null, null, null, null, 0, 0));
        sampleServices.add(new Service(4L, "Service4", 180,
                ProductStatus.ACTIVATED, 0, null, null, null, null, 0, 0));
        sampleServices.add(new Service(5L, "Service5", 200,
                ProductStatus.ACTIVATED, 0, null, null, null, null, 0, 0));

        return sampleServices;
    }

    public static Service getSampleService() {
        ProductCategory productCategory = new ProductCategory(1L, "Sample category", "Sample units");
        return new Service(200L, "Service under test", 115,
                ProductStatus.ACTIVATED, 0, productCategory, null, null, null, 0, 0);
    }

    public static final int SAMPLE_SERVICES_ENTITY_COUNT = TestUtil.getSampleServices().size();
}
