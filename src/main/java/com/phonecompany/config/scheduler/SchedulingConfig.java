package com.phonecompany.config.scheduler;

import com.phonecompany.model.Order;
import com.phonecompany.service.interfaces.OrderService;
import com.phonecompany.util.TypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
public class SchedulingConfig implements SchedulingConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulingConfig.class);

    @Bean(destroyMethod = "shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(100);
    }

    @Autowired
    private OrderResumeTask orderResumeTask;
    @Autowired
    private OrderService orderService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(taskExecutor());
        scheduledTaskRegistrar.addTriggerTask(
                new Runnable() {
                    @Override
                    public void run() {
                        orderResumeTask.resumeNextOrder();
                    }
                },
                new Trigger() {
                    @Override
                    public Date nextExecutionTime(TriggerContext triggerContext) {
                        Order nextResumingOrder = orderService.getNextResumingOrder();
                        if (nextResumingOrder != null) {
                            LOGGER.debug("Next task execution time: {}", nextResumingOrder.getExecutionDate());
                            return TypeMapper.toUtilDate(nextResumingOrder.getExecutionDate());
                        } else {
                            LOGGER.debug("No next order, sleep 1 hour");
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.MILLISECOND, 60 * 60 * 1000);
                            return calendar.getTime();
                        }
                    }
                }
        );
    }
}
