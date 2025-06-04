package de.greluc.krt.iri.hq.frontend.gui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Admin")
@Route(value = "admin", layout = MainView.class)
@RolesAllowed("ADMIN")
public class AdminView extends HorizontalLayout {
  public AdminView() {
    setPadding(true);

    add(new H1("Admin"));
  }
}