package com.example.scheduler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static java.time.LocalTime.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SchedulerServiceTest {

    @Autowired
    SchedulerService schedulerService;

    @Test
    public void testFileExists() {
        File file = new File("src/main/resources/schedule.csv");
        assertTrue(file.exists() && file.isFile() && file.canRead());
    }

    @Test
    public void testFileDataFormat() {
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader("src/main/resources/schedule.csv"));
            String row;
            int lineCounter = 0;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                if (lineCounter == 0) {
                    assertEquals("time", data[0]);
                    assertEquals("bitmask", data[1]);
                } else {
                    assertTrue(isTimeValid(data[0]));
                    assertTrue(isBitmaskValid(data[1]));
                }
                lineCounter++;
            }
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isTimeValid(String time) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime.parse(time, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isBitmaskValid(String bitmask) {
        try {
            int bitmaskInt = Integer.parseInt(bitmask);
            if (bitmaskInt >= 0 && bitmaskInt <= 127) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }


    @Test
    public void testParseTime() {
        LocalTime expected = LocalTime.of(12, 30);
        LocalTime actual = schedulerService.parseTime("12:30");
        assertEquals(expected, actual);
    }


    @Test
    public void testCodeValid() {
        LocalTime actionTime = parse("12:30");
        String bitmask = "2";
        ZonedDateTime now = ZonedDateTime.of(2022, 12, 20, 12, 30, 0, 0, ZoneId.of("Africa/Lagos"));
        DayOfWeek currentDay = now.getDayOfWeek();
        int currentBitmask = (int) Math.pow(2, currentDay.getValue() - 1);

        assertTrue((now.toLocalTime().equals(actionTime) && (currentBitmask & Integer.parseInt(bitmask)) != 0));
    }


}
