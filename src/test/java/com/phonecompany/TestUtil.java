package com.phonecompany;

import com.phonecompany.model.*;
import com.phonecompany.model.enums.ProductStatus;
import com.phonecompany.model.enums.Status;
import com.phonecompany.model.enums.UserRole;

import java.util.ArrayList;
import java.util.List;

public class TestUtil {

    public static final long EXISTING_SERVICE_ID = 5L;
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

        sampleServices.add(new Service(1L, "Service1", 50.99,
                ProductStatus.ACTIVATED, 0.0, null, null, null, null, 0, 0));
        sampleServices.add(new Service(2L, "Service2", 100.99,
                ProductStatus.ACTIVATED, 0.0, null, null, null, null, 0, 0));
        sampleServices.add(new Service(3L, "Service3", 260.99,
                ProductStatus.ACTIVATED, 0.0, null, null, null, null, 0, 0));
        sampleServices.add(new Service(4L, "Service4", 180.99,
                ProductStatus.ACTIVATED, 0.0, null, null, null, null, 0, 0));
        sampleServices.add(new Service(5L, "Service5", 200.99,
                ProductStatus.ACTIVATED, 0.0, null, null, null, null, 0, 0));

        return sampleServices;
    }

    public static final int SAMPLE_SERVICES_ENTITY_COUNT = TestUtil.getSampleServices().size();

    public static Service getNonExistentService() {
        ProductCategory productCategory = new ProductCategory(1L, "Sample category", "Sample units");
        return new Service(1000L, "Service under test", 115.99,
                ProductStatus.ACTIVATED, 0.0, productCategory, null, null, null, 0, 0);
    }

    public static Service getExistingService() {
        return new Service(5L, "3G ONLINE 8GB", 129.99,
                ProductStatus.ACTIVATED, 0.0, null,
                "https://s3-us-west-2.amazonaws.com/contentorders/service/218969357/picture.png",
                null, null, 30, 15);
    }

    public static ProductStatus getAnotherProductStatus(ProductStatus productStatus) {
        ProductStatus[] productStatusValues = ProductStatus.values();
        return productStatusValues[(productStatus.ordinal() + 1) % productStatusValues.length];
    }

    public static Customer getSampleCustomer() {
        Region region = new Region(10, "Kyiv Region");
        Address address = new Address(region, "Some locality", "Some street",
                "55B", "12A");
        return new Customer("test_email@gmail.com", "$root$", UserRole.CLIENT,
                Status.INACTIVE, "%r32r2", "f", null, "+380770071245", address,
                null, false, false);
    }
}
