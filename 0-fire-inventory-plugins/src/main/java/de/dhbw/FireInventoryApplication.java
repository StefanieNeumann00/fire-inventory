package de.dhbw;

import java.util.TimeZone;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@PWA(name = "fire-inventory", shortName = "fireIn")
@SpringBootApplication()
@Theme(value = "fire-theme")
public class FireInventoryApplication implements AppShellConfigurator{

    public static void main(String[] args) {
        System.setProperty("user.timezone", "UTC");
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(FireInventoryApplication.class, args);
    }
}
