package com.example.demo;

        import org.slf4j.LoggerFactory;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.scheduling.annotation.Scheduled;
        import org.springframework.stereotype.Component;
        import org.slf4j.Logger;

        import java.text.SimpleDateFormat;
        import java.util.Arrays;
        import java.util.List;
        import java.util.Random;

@Component
public class ScheduledTasks {
    @Autowired
    UserCrudDao userCrudDao;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron = "8 * * * * *")
    public void reportCurrentTime() {
        List<String> statusList = Arrays.asList("UP","DOWN");
        String status = statusList.get(new Random().nextInt(statusList.size()));
        Status st = userCrudDao.updateStatus(new Status(status));
        System.out.println(st.getESIMhealthCheckStatus() + "$$$$$$$$"+st.getHealthCheckID());
    }
}