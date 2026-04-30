package uk.ac.plymouth.maternityapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.ac.plymouth.maternityapi.client.MaternityApiClient;
import uk.ac.plymouth.maternityapi.model.Admission;
import uk.ac.plymouth.maternityapi.model.Allocation;
import uk.ac.plymouth.maternityapi.model.Employee;
import uk.ac.plymouth.maternityapi.model.Patient;
import uk.ac.plymouth.maternityapi.model.RoomAllocation;
import uk.ac.plymouth.maternityapi.service.MaternityService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class MaternityServiceTest {

    private MaternityApiClient apiClient;
    private MaternityService maternityService;

    @BeforeEach
    void setUp() {
        apiClient = Mockito.mock(MaternityApiClient.class);
        maternityService = new MaternityService(apiClient);
    }

    @Test
    void shouldReturnRoomsUsedByPatient() {
        Admission admission1 = new Admission();
        admission1.setId(1);
        admission1.setPatientId(1);

        Admission admission2 = new Admission();
        admission2.setId(2);
        admission2.setPatientId(2);

        RoomAllocation roomAllocation1 = new RoomAllocation();
        roomAllocation1.setId(1);
        roomAllocation1.setAdmissionId(1);
        roomAllocation1.setRoomNumber("A1");

        RoomAllocation roomAllocation2 = new RoomAllocation();
        roomAllocation2.setId(2);
        roomAllocation2.setAdmissionId(2);
        roomAllocation2.setRoomNumber("B1");

        when(apiClient.getAdmissions()).thenReturn(List.of(admission1, admission2));
        when(apiClient.getRoomAllocations()).thenReturn(List.of(roomAllocation1, roomAllocation2));

        List<String> result = maternityService.getRoomsUsedByPatient(1);

        assertEquals(List.of("A1"), result);
    }

    @Test
    void shouldReturnEmptyWhenNoValidRooms() {
        Admission admission = new Admission();
        admission.setId(1);
        admission.setPatientId(1);

        RoomAllocation roomAllocation = new RoomAllocation();
        roomAllocation.setId(1);
        roomAllocation.setAdmissionId(1);
        roomAllocation.setRoomNumber(null);

        when(apiClient.getAdmissions()).thenReturn(List.of(admission));
        when(apiClient.getRoomAllocations()).thenReturn(List.of(roomAllocation));

        List<String> result = maternityService.getRoomsUsedByPatient(1);

        assertEquals(List.of(), result);
    }

    @Test
    void shouldReturnPatientsForRoomWithinLast7Days() {
        Admission admission = new Admission();
        admission.setId(1);
        admission.setPatientId(1);
        admission.setAdmissionDate(LocalDateTime.now().minusDays(1).toString());

        RoomAllocation roomAllocation = new RoomAllocation();
        roomAllocation.setId(1);
        roomAllocation.setAdmissionId(1);
        roomAllocation.setRoomNumber("A1");

        Patient patient = new Patient();
        patient.setId(1);
        patient.setSurname("Robinson");

        when(apiClient.getAdmissions()).thenReturn(List.of(admission));
        when(apiClient.getRoomAllocations()).thenReturn(List.of(roomAllocation));
        when(apiClient.getPatients()).thenReturn(List.of(patient));

        List<String> result = maternityService.getPatientsInRoomLast7Days("A1");

        assertEquals(List.of("Robinson"), result);
    }

    @Test
    void shouldReturnLeastUsedRooms() {
        RoomAllocation roomAllocation1 = new RoomAllocation();
        roomAllocation1.setId(1);
        roomAllocation1.setAdmissionId(1);
        roomAllocation1.setRoomNumber("A1");

        RoomAllocation roomAllocation2 = new RoomAllocation();
        roomAllocation2.setId(2);
        roomAllocation2.setAdmissionId(2);
        roomAllocation2.setRoomNumber("A1");

        RoomAllocation roomAllocation3 = new RoomAllocation();
        roomAllocation3.setId(3);
        roomAllocation3.setAdmissionId(3);
        roomAllocation3.setRoomNumber("B1");

        when(apiClient.getRoomAllocations()).thenReturn(
                List.of(roomAllocation1, roomAllocation2, roomAllocation3)
        );

        List<String> result = maternityService.getLeastUsedRooms();

        assertEquals(List.of("B1"), result);
    }

    @Test
    void shouldReturnEmptyListWhenNoRoomsExist() {
        when(apiClient.getRoomAllocations()).thenReturn(List.of());

        List<String> result = maternityService.getLeastUsedRooms();

        assertEquals(List.of(), result);
    }

    @Test
    void shouldReturnEmptyWhenNoStaffHaveThreeConcurrentPatients() {
        Allocation allocation1 = new Allocation();
        allocation1.setId(1);
        allocation1.setEmployeeId(1);
        allocation1.setAdmissionId(1);
        allocation1.setStartTime("2024-04-01T10:00:00");
        allocation1.setEndTime("2024-04-01T11:00:00");

        Allocation allocation2 = new Allocation();
        allocation2.setId(2);
        allocation2.setEmployeeId(1);
        allocation2.setAdmissionId(2);
        allocation2.setStartTime("2024-04-01T12:00:00");
        allocation2.setEndTime("2024-04-01T13:00:00");

        Employee employee = new Employee();
        employee.setId(1);
        employee.setForename("Alice");
        employee.setSurname("Smith");

        when(apiClient.getAllocations()).thenReturn(List.of(allocation1, allocation2));
        when(apiClient.getEmployees()).thenReturn(List.of(employee));

        List<String> result = maternityService.getStaffWithThreeOrMoreConcurrentPatients();

        assertEquals(List.of(), result);
    }
}