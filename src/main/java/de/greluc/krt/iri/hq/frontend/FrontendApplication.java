package de.greluc.krt.iri.hq.frontend;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme(value = "krt", variant = Lumo.DARK)
@Generated
public class FrontendApplication implements AppShellConfigurator {

  public static void main(String[] args) {
    SpringApplication.run(FrontendApplication.class, args);
  }
}
