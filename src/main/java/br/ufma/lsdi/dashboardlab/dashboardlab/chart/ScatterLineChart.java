package br.ufma.lsdi.dashboardlab.dashboardlab.chart;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.ScatterChartConfig;
import com.byteowls.vaadin.chartjs.data.Dataset;
import com.byteowls.vaadin.chartjs.data.ScatterDataset;
import com.byteowls.vaadin.chartjs.options.InteractionMode;
import com.byteowls.vaadin.chartjs.options.Position;
import com.byteowls.vaadin.chartjs.options.scale.Axis;
import com.byteowls.vaadin.chartjs.options.scale.LinearScale;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;

public class ScatterLineChart {

    private static final long serialVersionUID = -4668420742225695694L;

    public Component getChart() {
        ScatterChartConfig config = new ScatterChartConfig();
        config
                .data()
                .addDataset(new ScatterDataset().label("My First dataset").xAxisID("x-axis-1").yAxisID("y-axis-1"))
                .addDataset(new ScatterDataset().label("My Second  dataset").xAxisID("x-axis-1").yAxisID("y-axis-2"))
                .and();
        config.
                options()
                .responsive(true)
                .hover()
                .mode(InteractionMode.NEAREST)
                .intersect(false)
                .and()
                .title()
                .display(true)
                .text("Chart.js Scatter Chart - Multi Axis")
                .and()
                .scales()
                .add(Axis.X, new LinearScale().position(Position.BOTTOM).gridLines().zeroLineColor("rgba(0,0,0,1)").and())
                .add(Axis.Y, new LinearScale().display(true).position(Position.LEFT).id("y-axis-1"))
                .add(Axis.Y, new LinearScale().display(true).position(Position.RIGHT).id("y-axis-2").ticks().reverse(true).and().gridLines().drawOnChartArea(false).and())
                .and()
                .done();

        for (Dataset<?, ?> ds : config.data().getDatasets()) {
            ScatterDataset lds = (ScatterDataset) ds;
            lds.borderColor(ColorUtils.randomColor(.4));
            lds.backgroundColor(ColorUtils.randomColor(.1));
            lds.pointBorderColor(ColorUtils.randomColor(.7));
            lds.pointBackgroundColor(ColorUtils.randomColor(.5));
            lds.pointBorderWidth(1);
            for (int i = 0; i < 7; i++) {
                lds.addData(ChartUtils.randomScalingFactor(), ChartUtils.randomScalingFactor());
            }
        }

        ChartJs chart = new ChartJs(config);
        chart.setJsLoggingEnabled(true);
        chart.addClickListener((a,b) ->
                ChartUtils.notification(a, b, config.data().getDatasets().get(a)));

        chart.setSizeFull();
        return chart;
    }

}