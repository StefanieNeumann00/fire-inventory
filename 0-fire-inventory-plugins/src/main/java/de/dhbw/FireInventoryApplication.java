package de.dhbw;

import com.vaadin.flow.component.dependency.NpmPackage;
import java.util.TimeZone;

import com.vaadin.flow.server.PWA;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@PWA(name = "fire-inventory", shortName = "")
@SpringBootApplication
@NpmPackage(value = "lumo-css-framework", version = "^4.0.10")
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class FireInventoryApplication {

    public static void main(String[] args) {
        System.setProperty("user.timezone", "UTC");
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(FireInventoryApplication.class, args);
    }
}
