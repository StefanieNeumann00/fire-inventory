package de.dhbw.plugins.gui.vaadin.routes;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.dhbw.fireinventory.application.equipment.EquipmentApplicationService;
import de.dhbw.fireinventory.application.status.StatusApplicationService;
import de.dhbw.fireinventory.application.vehicle.VehicleApplicationService;
import de.dhbw.plugins.gui.vaadin.MainLayout;
import de.dhbw.plugins.gui.vaadin.components.AppointmentChart;
import de.dhbw.plugins.gui.vaadin.components.StatsBox;

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
        add(getStatsLayout());
    }

    private HorizontalLayout getStatsLayout() {
        StatsBox equipmentStats = new StatsBox("Equipment", equipmentService.getEquipmentCount(), equipmentService.equipmentStatusCount("Kaputt"), statusService);
        StatsBox vehicleStats = new StatsBox("Vehicle", vehicleService.getVehicleCount(), vehicleService.vehicleStatusCount("Kaputt"), statusService);
        VerticalLayout statsLayout = new VerticalLayout(equipmentStats, vehicleStats);
        statsLayout.setSizeFull();

        VerticalLayout appointmentsChart = new AppointmentChart("Upcoming Appointments");

        HorizontalLayout horizontalLayout = new HorizontalLayout(appointmentsChart, statsLayout);
        horizontalLayout.setSizeFull();

        return horizontalLayout;
    }
}
