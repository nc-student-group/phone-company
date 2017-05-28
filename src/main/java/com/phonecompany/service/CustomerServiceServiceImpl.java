package com.phonecompany.service;

import com.phonecompany.annotations.ServiceStereotype;
import com.phonecompany.dao.interfaces.CustomerServiceDao;
import com.phonecompany.exception.ConflictException;
import com.phonecompany.model.*;
import com.phonecompany.model.enums.CustomerProductStatus;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.model.enums.OrderType;
import com.phonecompany.service.interfaces.CustomerServiceService;
import com.phonecompany.service.interfaces.OrderService;
import com.phonecompany.service.interfaces.ServiceService;
import com.phonecompany.util.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@ServiceStereotype
public class CustomerServiceServiceImpl extends CrudServiceImpl<CustomerServiceDto>
        implements CustomerServiceService {

    private CustomerServiceDao customerServiceDao;
    private OrderService orderService;
    private ServiceService serviceService;

    @Autowired
    public CustomerServiceServiceImpl(CustomerServiceDao customerServiceDao,
                                      OrderService orderService,
                                      ServiceService serviceService) {
        this.customerServiceDao = customerServiceDao;
        this.orderService = orderService;
        this.serviceService = serviceService;
    }

    @Override
    public List<CustomerServiceDto> getCurrentCustomerServices(long customerId) {
        return customerServiceDao.getCurrentCustomerServices(customerId);
    }

    @Override
    public List<CustomerServiceDto> getCustomerServicesByCustomerId(long customerId) {
        return customerServiceDao.getCustomerServicesByCustomerId(customerId);
    }

    @Override
    public CustomerServiceDto deactivateCustomerService(CustomerServiceDto customerService) {
        if (CustomerProductStatus.SUSPENDED.equals(customerService.getCustomerProductStatus())) {
            List<Order> resumingOrders = orderService.getResumingOrderByCustomerService(customerService);
            resumingOrders.forEach((Order resumingOrder) -> {
                resumingOrder.setOrderStatus(OrderStatus.CANCELED);
                orderService.update(resumingOrder);
            });
        }
        customerService.setCustomerProductStatus(CustomerProductStatus.DEACTIVATED);
        LocalDate now = LocalDate.now();

        Order deactivationOrder = new Order();
        deactivationOrder.setCustomerService(customerService);
        deactivationOrder.setCreationDate(now);
        deactivationOrder.setExecutionDate(now);
        deactivationOrder.setOrderStatus(OrderStatus.DONE);
        deactivationOrder.setType(OrderType.DEACTIVATION);

        customerServiceDao.update(customerService);
        orderService.save(deactivationOrder);

        return customerService;
    }

    @Override
    public CustomerServiceDto resumeCustomerService(CustomerServiceDto customerService) {
        List<Order> resumingOrders = orderService.getResumingOrderByCustomerService(customerService);
        resumingOrders.forEach((Order resumingOrder) -> {
            resumingOrder.setOrderStatus(OrderStatus.CANCELED);
            orderService.update(resumingOrder);
        });

        customerService.setCustomerProductStatus(CustomerProductStatus.ACTIVE);
        LocalDate now = LocalDate.now();

        Order activationOrder = new Order();
        activationOrder.setCustomerService(customerService);
        activationOrder.setCreationDate(now);
        activationOrder.setExecutionDate(now);
        activationOrder.setOrderStatus(OrderStatus.DONE);
        activationOrder.setType(OrderType.ACTIVATION);

        customerServiceDao.update(customerService);
        orderService.save(activationOrder);

        return customerService;
    }

    @Override
    public void resumeCustomerService(Order order) {
        if(order.getOrderStatus().equals(OrderStatus.CREATED)){
            order.getCustomerService().setCustomerProductStatus(CustomerProductStatus.ACTIVE);
            customerServiceDao.update(order.getCustomerService());
            order.setOrderStatus(OrderStatus.DONE);
            orderService.update(order);
        }
    }

    @Override
    public CustomerServiceDto suspendCustomerService(Map<String, Object> suspensionData) {
        CustomerServiceDto customerService = customerServiceDao.
                getById((new Long((Integer) suspensionData.get("customerServiceId"))));
        Integer daysToExecution = (Integer) suspensionData.get("daysToExecution");

        customerService.setCustomerProductStatus(CustomerProductStatus.SUSPENDED);

        LocalDate now = LocalDate.now();
        LocalDate executionDate = now.plusDays(daysToExecution);

        Order suspensionOrder = new Order(customerService, OrderType.SUSPENSION, OrderStatus.DONE, now, now);

        Order resumingOrder = new Order(customerService, OrderType.RESUMING, OrderStatus.CREATED, now, executionDate);

        customerServiceDao.update(customerService);
        orderService.save(suspensionOrder);
        orderService.save(resumingOrder);
        this.scheduleCustomerServiceResuming(resumingOrder);
        return customerService;
    }

    private void scheduleCustomerServiceResuming(Order resumingOrder) {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        TaskScheduler taskScheduler = new ConcurrentTaskScheduler(scheduledExecutorService);
        taskScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                resumeCustomerService(resumingOrder);
            }
        }, TypeMapper.toUtilDate(resumingOrder.getExecutionDate()));
    }

    @Override
    public CustomerServiceDto activateServiceForCustomer(long serviceId, Customer customer,
                                                         boolean isForCorporateCustomer) {
        boolean isActivated = this.checkIfServiceWasAlreadyActivated(serviceId, customer.getId());
        if (isActivated) {
            if (isForCorporateCustomer) {
                throw new ConflictException("This service was already activated for " + customer.getEmail());
            } else {
                throw new ConflictException("This service was already activated for you");
            }
        }
        Service service = serviceService.getById(serviceId);
        CustomerServiceDto customerService =
                new CustomerServiceDto(customer, service,
                        service.getPrice() - (service.getPrice() * service.getDiscount() / 100),
                        CustomerProductStatus.ACTIVE);
        this.save(customerService);

        return customerService;
    }

    @Override
    public CustomerServiceDto activateMarketingServiceForCustomer(
            MarketingCampaignServices marketingCampaignService, Customer customer) {
        Long serviceId = marketingCampaignService.getService().getId();
        boolean isActivated = this.checkIfServiceWasAlreadyActivated(serviceId, customer.getId());
        if (isActivated) {
            throw new ConflictException("Service " +
                    marketingCampaignService.getService().getServiceName() +
                    " was already activated for you. Please, " +
                    "deactivate it in order to activate marketing campaign!");
        }
        Service service = serviceService.getById(serviceId);
        CustomerServiceDto customerService =
                new CustomerServiceDto(customer, service,
                        marketingCampaignService.getPrice(), CustomerProductStatus.ACTIVE);
        this.save(customerService);

        return customerService;
    }

    private boolean checkIfServiceWasAlreadyActivated(long serviceId, long customerId) {
        return customerServiceDao.isCustomerServiceAlreadyPresent(serviceId, customerId);
    }

    @Override
    public boolean isProductCategoryAvailable(Customer customer, long categoryId, boolean isForCorporateCustomer) {

        /*3 - other services*/
        if (categoryId == 3) {
            return true;
        }

        List<CustomerServiceDto> services = getCurrentCustomerServices(customer.getId());
        for (CustomerServiceDto customerServiceDto : services) {
            if (customerServiceDto.getService().getProductCategory().getId() == categoryId) {
                if (isForCorporateCustomer) {
                    throw new ConflictException("Service in this category have already ordered for "
                            + customer.getEmail());
                } else {
                    throw new ConflictException("Service in this category have already ordered. " +
                            "Please deactivate the service of this type and try again");
                }
            }
        }

        return true;
    }
}

