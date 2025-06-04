package de.greluc.krt.iri.hq.frontend.gui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("User")
@Route(value = "user", layout = MainView.class)
@RolesAllowed("USER")
public class UserView extends HorizontalLayout {
  public UserView() {
    setPadding(true);

    add(new H1("User"));
  }
}