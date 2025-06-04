package de.greluc.krt.iri.hq.frontend.gui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Index")
@Route(value = "", layout = MainView.class)
@AnonymousAllowed()
public class IndexView extends HorizontalLayout {
  public IndexView() {
    setPadding(true);

    add(new H1("Index"));
  }
}