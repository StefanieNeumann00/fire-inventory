package de.dhbw.plugins.rest;

import de.dhbw.fireinventory.adapter.equipment.EquipmentResource;
import de.dhbw.fireinventory.adapter.equipment.EquipmentToEquipmentResourceMapper;
import de.dhbw.fireinventory.application.equipment.EquipmentApplicationService;
import de.dhbw.fireinventory.domain.equipment.Equipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/equipment")
public class EquipmentController {

    private final EquipmentApplicationService equipmentApplicationService;

    private final EquipmentToEquipmentResourceMapper equipmentToEquipmentResourceMapper;

    @Autowired
    public EquipmentController(final EquipmentApplicationService equipmentApplicationService, final EquipmentToEquipmentResourceMapper equipmentToEquipmentResourceMapper) {
        this.equipmentApplicationService = equipmentApplicationService;
        this.equipmentToEquipmentResourceMapper = equipmentToEquipmentResourceMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<EquipmentResource> getEquipments() {
        return this.equipmentApplicationService.findAllEquipments(null).stream()
                .map(equipmentToEquipmentResourceMapper)
                .collect(Collectors.toList());
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    public void addEquipment(@RequestBody Equipment equipment) {
        this.equipmentApplicationService.saveEquipment(equipment);
    }
}
