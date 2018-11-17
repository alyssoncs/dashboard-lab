package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.component.ApplicationMenuBar;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import lombok.Getter;

@Theme("valo")
@SpringUI(path = "/")
public class IndexUI extends UI {

    @Getter
    private Panel contentPanel;

    private InterSCityService interSCityService;

    public IndexUI(InterSCityService interSCityService) {
        this.interSCityService = interSCityService;
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        contentPanel = new Panel();
        contentPanel.setSizeFull();

        contentPanel.setContent(new IndexView(interSCityService, this));

        ApplicationMenuBar applicationMenuBar = new ApplicationMenuBar(interSCityService, contentPanel,  this);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setMargin(false);
        verticalLayout.setSpacing(false);
        verticalLayout.addComponents(applicationMenuBar, contentPanel);
        setContent(verticalLayout);

    }

}
