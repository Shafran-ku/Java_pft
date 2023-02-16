package ru.stqa.pft.soap;

import com.lavasoft.GeoIPService;
import org.testng.annotations.Test;

public class GeoIpServiceTests {

    @Test
    public void testMyIp() {
        //обращаемся через удаленный программный интерфейс к сервису
        //(getGeoIPServiceSoap12:  12-версия релизации)
        //(85.174.206.19 - мой публичный ip адрес)
        String ipLocation = new GeoIPService().getGeoIPServiceSoap12().getIpLocation("85.174.206.19");
        //assertEquals();
        System.out.println(ipLocation);
    }
}
