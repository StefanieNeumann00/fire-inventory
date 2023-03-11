package de.dhbw.plugins.gui.vaadin.routes;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.charts.model.style.Color;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.dhbw.fireinventory.application.equipment.EquipmentApplicationService;
import de.dhbw.fireinventory.application.status.StatusApplicationService;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.plugins.gui.vaadin.MainLayout;

import java.util.List;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | FireInventory")
public class Dashboard extends VerticalLayout {
    private final EquipmentApplicationService equipmentService;
    private final StatusApplicationService statusService;

    public Dashboard(EquipmentApplicationService equipmentService, StatusApplicationService statusService) {
        this.equipmentService = equipmentService;
        this.statusService = statusService;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(getCompaniesChart());
    }

    private Chart getCompaniesChart() {
        Chart chart = new Chart(ChartType.PIE);

        Configuration conf = chart.getConfiguration();
        conf.getChart().setStyledMode(true);
        conf.setTitle("Equipment Status Distribution");

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setShowInLegend(true);
        conf.setPlotOptions(plotOptions);

        DataSeries dataSeries = new DataSeries();
        List<Status> statuses = statusService.findAllStatuses();
        for (Status status: statuses) {
            DataSeriesItem seriesItem = new DataSeriesItem(status.getDesignation(), equipmentService.equipmentStatusCount(status.getDesignation()));
            dataSeries.add(seriesItem);
        }
        conf.setSeries(dataSeries);
        chart.setConfiguration(conf);
        return chart;
    }
}
