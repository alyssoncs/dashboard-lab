package br.ufma.lsdi.dashboardlab.dashboardlab.view;

import br.ufma.lsdi.dashboardlab.dashboardlab.chart.PieChart;
import br.ufma.lsdi.dashboardlab.dashboardlab.chart.VerticalBarChart;
import br.ufma.lsdi.dashboardlab.dashboardlab.model.SearchResourcesRequest;
import br.ufma.lsdi.dashboardlab.dashboardlab.service.InterSCityService;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import io.vavr.control.Option;
import lombok.val;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CommonQueriesView extends VerticalLayout {

    private final InterSCityService interSCityService;

    VerticalLayout generalInfo;
    VerticalLayout capPerTypeLayout;
    VerticalLayout resPerCapLayout;
    VerticalLayout resPerCapTypeLayout;
    ComboBox<String> queries;
    LMap map;

    double zoomLevel = 11.49;

    public CommonQueriesView(InterSCityService interSCityService, IndexUI indexUI) {

        this.interSCityService = interSCityService;

        generalInfo = new VerticalLayout();
        generalInfo.setSpacing(false);
        generalInfo.setMargin(false);
        generalInfo.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        capPerTypeLayout = new VerticalLayout();
        capPerTypeLayout.setSpacing(false);
        capPerTypeLayout.setMargin(false);
        capPerTypeLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        resPerCapLayout = new VerticalLayout();
        resPerCapLayout.setSpacing(false);
        resPerCapLayout.setMargin(false);
        resPerCapLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        resPerCapTypeLayout = new VerticalLayout();
        resPerCapTypeLayout.setSpacing(false);
        resPerCapTypeLayout.setMargin(false);
        //resPerCapTypeLayout.setSizeFull();
        resPerCapTypeLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);


        VerticalLayout formLayout = createFormPanel();


        GridLayout statsGrid = new GridLayout(2,2);
        statsGrid.setSizeFull();
        statsGrid.setSpacing(true);
        statsGrid.addComponent(generalInfo, 0, 0);
        statsGrid.addComponent(capPerTypeLayout, 0, 1);
        statsGrid.addComponent(resPerCapLayout, 1, 0);
        //statsGrid.addComponent(formLayout, 1, 1);
        //statsGrid.addComponent(resPerCapTypeLayout);

        map = new LMap();
        map.setCenter(-2.590479, -44.224759);
        map.setZoomLevel(zoomLevel);
        map.setSizeFull();
        LOpenStreetMapLayer layer = new LOpenStreetMapLayer();
        map.addBaseLayer(layer, "OSM");


        GridLayout grid = new GridLayout(2,1);
        grid.setSizeFull();
        grid.setColumnExpandRatio(0, 1);
        grid.setColumnExpandRatio(0, 0.55f);
        grid.setColumnExpandRatio(1, 0.45f);
        grid.addComponent(statsGrid);
        grid.addComponent(map);




        initCharts();

        addComponent(grid);

        setWidth("100%");
        setHeight("100%");


    }

    private void initCharts() {
        initText();

        plotCapabilitiesPerTypeChart();

        val capabilities = interSCityService.getAllCapabilities(Option.none()).stream()
                .map(cap -> cap.getName())
                .sorted();
        String[] capabilitiesArr = capabilities.toArray(size -> new String[size]);

        Set<String> capabilitiesSet = new HashSet<String>();
        Random rand = new Random();
        int capNum = rand.nextInt(3) + 2;
        for (int i = 0; i < capNum; i++) {
            capabilitiesSet.add(capabilitiesArr[rand.nextInt(capabilitiesArr.length)]);
        }

        plotResourcesPerCapabilityChart(capabilitiesSet);
    }

    private VerticalLayout createFormPanel() {

        val vl = new VerticalLayout();
        vl.setSpacing(false);
        vl.setMargin(false);

        val form = new VerticalLayout();
        form.setSizeFull();
        form.setMargin(false);
        form.setMargin(new MarginInfo(true,false,false,false));
        form.setSpacing(false);

        /*val queriesList = Arrays.asList(
                "Capabilities per type",
                "Resources per capability",
                "Resources per capability type"
        );*/
        val queriesList = Arrays.asList(
                "Resources per capability"
        );

        queries = new ComboBox<>("Queries");
        queries.setItems(queriesList);
        queries.setSizeFull();
        queries.addValueChangeListener(e -> {
            form.removeAllComponents();
            form.addComponent(createFormQuery(queries.getValue()));
        });

        vl.addComponent(queries);
        vl.addComponent(form);

        return vl;
    }

    private VerticalLayout createFormQuery(String query) {

        if (query.equals("Capabilities per type")) {
            return createCapabilitiesPerTypeForm();
        }
        else if (query.equals("Resources per capability")) {
            return createResourcesPerCapabilityForm();
        }
        else if (query.equals("Resources per capability type")) {
            return createResourcesPerCapabilityTypeForm();
        }

        return null;
    }

    private VerticalLayout createCapabilitiesPerTypeForm() {
        val vl = new VerticalLayout();
        vl.setMargin(false);
        Button b = new Button("Plot Chart");
        b.addClickListener(e -> plotCapabilitiesPerTypeChart());
        vl.addComponent(b);
        return vl;
    }

    private VerticalLayout createResourcesPerCapabilityTypeForm() {
        val vl = new VerticalLayout();
        vl.setMargin(false);
        Button b = new Button("Plot Chart");
        b.addClickListener(e -> plotResourcesPerCapabilityTypeChart());
        vl.addComponent(b);
        return vl;
    }

    private VerticalLayout createResourcesPerCapabilityForm() {
        val vl = new VerticalLayout();
        //vl.setSpacing(false);
        vl.setMargin(false);

        val capabilities = interSCityService.getAllCapabilities(Option.none()).stream()
                .map(cap -> cap.getName())
                .sorted();

        ListSelect<String> select = new ListSelect<>("Capabilities");
        select.setSizeFull();
        select.setItems(capabilities);
        vl.addComponent(select);
        Button b = new Button("Plot Chart");
        b.addClickListener(e -> plotResourcesPerCapabilityChart(select.getSelectedItems()));
        vl.addComponent(b);
        return vl;
    }

    private void initText() { ;
        VerticalLayout city = new VerticalLayout();
        city.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        city.addComponent(new Label("<div align=\"left\"> <font size=\"7\"> <b> Cidade de São Luís </b> </font> </div>", ContentMode.HTML));


        DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dtfTime = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();

        Label date1 = new Label("<div align=\"center\"> <font size=\"5\"> <b>" + dtfDate.format(now) + "</b> </font> </div>", ContentMode.HTML);
        Label date2 = new Label("<div align=\"center\"> <font size=\"5\"> <b>" + dtfTime.format(now) + "</b> </font></div>", ContentMode.HTML);

        HorizontalLayout date = new HorizontalLayout();
        date.addComponentsAndExpand(date1, date2);


        Label resourceLabel = new Label(
                "<div align=\"center\"> <font size=\"20\"> <b>" +
                            "266" +
                            //interSCityService.getAllResources().size() +
                        "</b> </font> </div>" +
                        "<div align=\"center\"> <font size=\"6\"> <b>" +
                            "Resources" +
                        "</b> </font> </div>"
                , ContentMode.HTML
        );

        Label capabilitiesLabel = new Label(
                "<div align=\"center\"> <font size=\"40\"> <b>" +
                            "5355" +
                            //interSCityService.getAllCapabilities(Option.none()).size() +
                        "</b> </font> </div>" +
                        "<div align=\"center\"> <font size=\"6\"> <b>" +
                            "Capabilities" +
                        "</b> </font> </div>"
                , ContentMode.HTML
        );


        HorizontalLayout resourcesAndCapabilitiesLabel = new HorizontalLayout();
        resourcesAndCapabilitiesLabel.addComponentsAndExpand(resourceLabel, capabilitiesLabel);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(city);
        verticalLayout.addComponent(date);
        verticalLayout.addComponent(resourcesAndCapabilitiesLabel);

        Panel panel = new Panel("General information".toUpperCase());
        panel.setContent(verticalLayout);
        panel.setHeight("100%");

        generalInfo.addComponent(panel);
    }

    private void plotResourcesPerCapabilityChart(Set<String> capabilities) {

        ProgressBar spinner = new ProgressBar();
        spinner.setIndeterminate(true);
        resPerCapLayout.addComponent(spinner);

        val chart = new VerticalBarChart();
        //chart.setTitle("Resources per capability".toUpperCase());

        capabilities.stream().forEach(cap -> {
            val request = new SearchResourcesRequest();
            request.setCapability(cap);
            val resources = interSCityService.searchResources(request);
            resources.stream().forEach(res -> chart.addData("Resources", cap, (double) resources.size()));
        });

        Panel panel = new Panel("Resources per capability".toUpperCase());
        panel.setContent(chart.getChart());
        panel.setHeight("100%");

        resPerCapLayout.removeAllComponents();
        resPerCapLayout.addComponent(panel);
    }

    private void plotResourcesPerCapabilityTypeChart() {

        ProgressBar spinner = new ProgressBar();
        spinner.setIndeterminate(true);
        resPerCapTypeLayout.addComponent(spinner);

        int sensors = interSCityService.getAllResourcesWithSensorCapabilities().size();
        int actuators = interSCityService.getAllResourcesWithActuatorCapabilities().size();

        PieChart pieChart = new PieChart();
        pieChart.setTitle(queries.getValue().toUpperCase());
        pieChart.addData("Sensors", (double) sensors);
        pieChart.addData("Actuators", (double) actuators);

        resPerCapTypeLayout.removeAllComponents();
        resPerCapTypeLayout.addComponent(pieChart.getChart());
    }

    private void plotCapabilitiesPerTypeChart() {

        ProgressBar spinner = new ProgressBar();
        spinner.setIndeterminate(true);
        capPerTypeLayout.addComponent(spinner);

        int sensors = interSCityService.getAllCapabilities(Option.of("sensor")).size();
        int actuators = interSCityService.getAllCapabilities(Option.of("actuator")).size();

        PieChart pieChart = new PieChart();
        //pieChart.setTitle("Capabilities per type".toUpperCase());
        pieChart.addData("Sensors", (double) sensors);
        pieChart.addData("Actuators", (double) actuators);

        Panel panel = new Panel("Capabilities per type".toUpperCase());
        //panel.setSizeUndefined();
        panel.setContent(pieChart.getChart());
        panel.setHeight("100%");


        capPerTypeLayout.removeAllComponents();
        capPerTypeLayout.addComponent(panel);

    }


}

