package de.dhbw.plugins.gui.vaadin.routes;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.dhbw.fireinventory.application.equipment.EquipmentApplicationService;
import de.dhbw.fireinventory.application.status.StatusApplicationService;
import de.dhbw.fireinventory.application.vehicle.VehicleApplicationService;
import de.dhbw.fireinventory.domain.status.Status;
import de.dhbw.plugins.gui.vaadin.MainLayout;
import de.dhbw.plugins.gui.vaadin.components.AppointmentChart;
import de.dhbw.plugins.gui.vaadin.components.StatsBox;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.Rect;

import java.util.List;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | FireInventory")
public class Dashboard extends VerticalLayout {
    private final EquipmentApplicationService equipmentService;
    private final StatusApplicationService statusService;
    private final VehicleApplicationService vehicleService;

    public Dashboard(EquipmentApplicationService equipmentService, StatusApplicationService statusService, VehicleApplicationService vehicleService) {
        this.equipmentService = equipmentService;
        this.statusService = statusService;
        this.vehicleService = vehicleService;
        addClassName("dashboard-view");
        addClassName("full-height-layout");
        setId("charts-layout");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        HorizontalLayout vehiclePlaceLayout = new HorizontalLayout(getVehicleStats(), getPlaceStats());
        add(getEquipmentStats(), vehiclePlaceLayout);
    }

    private HorizontalLayout getEquipmentStats() {
        Chart equipmentPieChart = getEquipmentPieChart();

        StatsBox equipmentStats = new StatsBox("Equipment", equipmentService.getEquipmentCount(), equipmentService.equipmentStatusCount("Kaputt"));
        StatsBox vehicleStats = new StatsBox("Vehicle", vehicleService.getVehicleCount(), vehicleService.vehicleStatusCount("Kaputt"));
        HorizontalLayout statsLayout = new HorizontalLayout(equipmentStats, vehicleStats);
        statsLayout.setSizeFull();

        VerticalLayout appointmentsChart = new AppointmentChart("Upcoming Appointments");
        VerticalLayout statsAppointmentLayout = new VerticalLayout(statsLayout, appointmentsChart);
        statsAppointmentLayout.addClassName("full-height-layout");

        HorizontalLayout horizontalLayout = new HorizontalLayout(equipmentPieChart, statsAppointmentLayout);
        horizontalLayout.setSizeFull();

        return horizontalLayout;
    }

    private Chart getEquipmentPieChart() {
        Chart chart = new Chart(ChartType.PIE);

        Configuration conf = chart.getConfiguration();
        conf.getChart().setStyledMode(true);

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

    private VerticalLayout getVehicleStats() {
        return new VerticalLayout();
    }

    private VerticalLayout getPlaceStats() {
        return new VerticalLayout();
    }
}
