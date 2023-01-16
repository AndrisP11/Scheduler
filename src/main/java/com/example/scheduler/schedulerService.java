package com.example.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class schedulerService {

    @Scheduled(fixedRate = 60000)
    public void executeAction() throws IOException {
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader("dto/schedule.csv"));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                String time = data[0];
                String bitmask = data[1];

                LocalTime actionTime = LocalTime.parse(time);
                ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Africa/Lagos"));

                DayOfWeek currentDay = now.getDayOfWeek();
                int currentBitmask = (int) Math.pow(2, currentDay.getValue() - 1);

                if (now.toLocalTime().isAfter(actionTime) && (currentBitmask & Integer.parseInt(bitmask)) != 0) {
                    // your code here to execute action
                }
            }
            csvReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
