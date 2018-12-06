package br.ufma.lsdi.dashboardlab.dashboardlab.component;

import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import br.ufma.lsdi.dashboardlab.dashboardlab.view.*;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import lombok.val;

public class ApplicationMenuBar extends MenuBar {

    public ApplicationMenuBar(InterSCityService interSCityService, Panel contentPanel, IndexUI indexUI) {

        val resourcesMenuItem = addItem("View", null, null);
        resourcesMenuItem.addItem("Home", null, c -> {
            contentPanel.setContent(new IndexView(interSCityService, indexUI));
        });
        resourcesMenuItem.addItem("Stats", null, c -> {
            contentPanel.setContent(new CommonQueriesView(interSCityService, indexUI));
        });
        resourcesMenuItem.addItem("Map", null, c -> {
            contentPanel.setContent(new MapView(interSCityService, indexUI));
        });
        resourcesMenuItem.addItem("Chart", null, c -> {
            contentPanel.setContent(new ChartView(interSCityService, indexUI));
        });

        resourcesMenuItem.addSeparator();
        resourcesMenuItem.addItem("Exit", null, null);

        setSizeFull();
    }

}
