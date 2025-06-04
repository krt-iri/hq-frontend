package de.greluc.krt.iri.hq.frontend.gui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Start")
@Route(value = "start", layout = MainLayout.class)
@RolesAllowed({"GUEST", "USER"})
public class StartView extends HorizontalLayout {
  public StartView() {
    setPadding(true);

    add(new H1("Start"));
  }
}