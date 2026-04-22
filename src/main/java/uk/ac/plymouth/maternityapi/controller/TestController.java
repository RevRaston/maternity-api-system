package uk.ac.plymouth.maternityapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.plymouth.maternityapi.client.MaternityApiClient;
import uk.ac.plymouth.maternityapi.model.Admission;
import uk.ac.plymouth.maternityapi.model.RoomAllocation;
import uk.ac.plymouth.maternityapi.model.Patient;

import java.util.List;

@RestController
public class TestController {

    private final MaternityApiClient apiClient;

    public TestController(MaternityApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @GetMapping("/test/admissions")
    public List<Admission> testAdmissions() {
        return apiClient.getAdmissions();
    }

    @GetMapping("/test/room-allocations")
    public List<RoomAllocation> testRoomAllocations() {
        return apiClient.getRoomAllocations();
    }

    @GetMapping("/test/patients")
    public List<Patient> testPatients() {
        return apiClient.getPatients();
    }

    @GetMapping("/test/allocations")
    public String testAllocations() {
        return apiClient.getAllocationsRaw();
    }

    @GetMapping("/test/employees")
    public String testEmployees() {
        return apiClient.getEmployeesRaw();
    }

}