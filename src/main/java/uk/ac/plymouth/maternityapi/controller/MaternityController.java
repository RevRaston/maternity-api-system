package uk.ac.plymouth.maternityapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.plymouth.maternityapi.service.MaternityService;

import java.util.List;

@RestController
public class MaternityController {

    private final MaternityService maternityService;

    public MaternityController(MaternityService maternityService) {
        this.maternityService = maternityService;
    }

    @GetMapping("/api/patients/{patientId}/rooms")
    public List<String> getRoomsUsedByPatient(@PathVariable int patientId) {
        return maternityService.getRoomsUsedByPatient(patientId);
    }

    @GetMapping("/api/rooms/{roomNumber}/patients")
    public List<String> getPatientsInRoomLast7Days(@PathVariable String roomNumber) {
        return maternityService.getPatientsInRoomLast7Days(roomNumber);
    }

    @GetMapping("/api/rooms/least-used")
    public List<String> getLeastUsedRooms() {
        return maternityService.getLeastUsedRooms();
    }

    @GetMapping("/api/staff/overloaded")
    public List<String> getStaffWithThreeOrMoreConcurrentPatients() {
        return maternityService.getStaffWithThreeOrMoreConcurrentPatients();
    }
}