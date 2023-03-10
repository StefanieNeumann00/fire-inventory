package de.dhbw.plugins.gui.vaadin.routes;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.dhbw.plugins.gui.vaadin.MainLayout;
import de.dhbw.plugins.rest.EquipmentController;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | FireInventory")
public class Dashboard extends VerticalLayout {
    private final EquipmentController controller;

    public Dashboard(EquipmentController controller) {
        this.controller = controller;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
//        add(getEquipmentStats(), getCompaniesChart());
    }

//    private Component getEquipmentStats() {
//       Span stats = new Span(controller.countEquipment() + " equipment");
//        stats.addClassNames("text-xl", "mt-m");
//        return stats;
//    }

//    private Chart getCompaniesChart() {
//        Chart chart = new Chart(ChartType.PIE);

//        DataSeries dataSeries = new DataSeries();
//        controller.getEquipments().forEach(equipment ->
//                dataSeries.add(new DataSeriesItem(equipment.getStatus().getDesignation(), controller.equipmentStatusCount(equipment.getStatus()))));
//        chart.getConfiguration().setSeries(dataSeries);
//        return chart;
//    }
}
