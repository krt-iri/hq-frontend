package de.greluc.krt.iri.hq.frontend.gui.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainView extends AppLayout {

  private final AccessAnnotationChecker accessChecker;
  private H2 viewTitle;

  public MainView(AccessAnnotationChecker accessChecker) {
    this.accessChecker = accessChecker;

    setPrimarySection(Section.DRAWER);
    addHeaderContent();
    addDrawerContent();
  }

  private void addHeaderContent() {
    DrawerToggle toggle = new DrawerToggle();
    toggle.getElement().setAttribute("aria-label", "Menu toggle");

    viewTitle = new H2();
    viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

    addToNavbar(true, toggle, viewTitle);
  }

  private void addDrawerContent() {
    H1 appName = new H1("Iridium HQ");
    appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
    Header header = new Header(appName);

    Scroller scroller = new Scroller(createNavigation());

    addToDrawer(header, scroller, createFooter());
  }

  private @NotNull SideNav createNavigation() {
    SideNav nav = new SideNav();

    if (accessChecker.hasAccess(IndexView.class)) {
      nav.addItem(new SideNavItem("Index", IndexView.class, VaadinIcon.GLOBE.create()));
    }
    if (accessChecker.hasAccess(UserView.class)) {
      nav.addItem(new SideNavItem("User", UserView.class, VaadinIcon.USER.create()));
    }
    if (accessChecker.hasAccess(AdminView.class)) {
      nav.addItem(new SideNavItem("Admin", AdminView.class, VaadinIcon.FILE.create()));
    }

    return nav;
  }

  private @NotNull Footer createFooter() {
    Footer layout = new Footer();

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof OAuth2AuthenticationToken token
        && token.getPrincipal() instanceof DefaultOidcUser oidcUser) {
      Avatar avatar = new Avatar(authentication.getName());
      avatar.setThemeName("xsmall");
      avatar.getElement().setAttribute("tabindex", "-1");

      MenuBar userMenu = new MenuBar();
      userMenu.setThemeName("tertiary-inline contrast");

      MenuItem userName = userMenu.addItem("");
      Div div = new Div();
      div.add(avatar);
      div.add(oidcUser.getFullName());
      div.add(new Icon("lumo", "dropdown"));
      div.getElement().getStyle().set("display", "flex");
      div.getElement().getStyle().set("align-items", "center");
      div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
      userName.add(div);
      userName.getSubMenu().addItem("Sign out", e -> UI.getCurrent().getPage().setLocation("/logout"));

      layout.add(userMenu);
    } else {
      Button login = new Button("Sign in",
          _ -> UI.getCurrent().getPage().setLocation("/user"));
      layout.add(login);
    }

    return layout;
  }

  @Override
  protected void afterNavigation() {
    super.afterNavigation();
    viewTitle.setText(getCurrentPageTitle());
  }

  private String getCurrentPageTitle() {
    PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
    return title == null ? "" : title.value();
  }
}