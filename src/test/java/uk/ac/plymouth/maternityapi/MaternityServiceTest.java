package uk.ac.plymouth.maternityapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uk.ac.plymouth.maternityapi.client.MaternityApiClient;
import uk.ac.plymouth.maternityapi.model.Admission;
import uk.ac.plymouth.maternityapi.model.RoomAllocation;
import uk.ac.plymouth.maternityapi.service.MaternityService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import uk.ac.plymouth.maternityapi.model.Allocation;
import uk.ac.plymouth.maternityapi.model.Employee;

import java.util.List;

public class MaternityServiceTest {

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

        RoomAllocation room1 = new RoomAllocation();
        room1.setId(1);
        room1.setAdmissionId(1);
        room1.setRoomNumber("Room A");

        RoomAllocation room2 = new RoomAllocation();
        room2.setId(2);
        room2.setAdmissionId(1);
        room2.setRoomNumber("Room B");

        RoomAllocation room3 = new RoomAllocation();
        room3.setId(3);
        room3.setAdmissionId(2);
        room3.setRoomNumber("Room C");

        when(apiClient.getAdmissions()).thenReturn(List.of(admission1, admission2));
        when(apiClient.getRoomAllocations()).thenReturn(List.of(room1, room2, room3));

        List<String> result = maternityService.getRoomsUsedByPatient(1);

        assertEquals(List.of("Room A", "Room B"), result);
    }

    @Test
    void shouldReturnEmptyListWhenNoRoomsExist() {
        Admission admission = new Admission();
        admission.setId(1);
        admission.setPatientId(1);

        RoomAllocation room = new RoomAllocation();
        room.setId(1);
        room.setAdmissionId(1);
        room.setRoomNumber(null);

        when(apiClient.getAdmissions()).thenReturn(List.of(admission));
        when(apiClient.getRoomAllocations()).thenReturn(List.of(room));

        List<String> result = maternityService.getRoomsUsedByPatient(1);

        assertEquals(List.of(), result);
    }

    @Test
    void shouldReturnLeastUsedRooms() {
        RoomAllocation r1 = new RoomAllocation();
        r1.setRoomNumber("A1");

        RoomAllocation r2 = new RoomAllocation();
        r2.setRoomNumber("A1");

        RoomAllocation r3 = new RoomAllocation();
        r3.setRoomNumber("B1");

        when(apiClient.getRoomAllocations()).thenReturn(List.of(r1, r2, r3));

        List<String> result = maternityService.getLeastUsedRooms();

        assertEquals(List.of("B1"), result);
    }

    @Test
    void shouldReturnEmptyWhenNoValidRooms() {
        RoomAllocation r1 = new RoomAllocation();
        r1.setRoomNumber(null);

        when(apiClient.getRoomAllocations()).thenReturn(List.of(r1));

        List<String> result = maternityService.getLeastUsedRooms();

        assertEquals(List.of(), result);
    }

    @Test
    void shouldReturnStaffWithThreeOrMoreConcurrentPatients() {
        Allocation a1 = new Allocation();
        a1.setEmployeeId(4);
        a1.setStartTime("2026-04-01T10:00:00");
        a1.setEndTime("2026-04-01T12:00:00");

        Allocation a2 = new Allocation();
        a2.setEmployeeId(4);
        a2.setStartTime("2026-04-01T10:30:00");
        a2.setEndTime("2026-04-01T12:30:00");

        Allocation a3 = new Allocation();
        a3.setEmployeeId(4);
        a3.setStartTime("2026-04-01T11:00:00");
        a3.setEndTime("2026-04-01T13:00:00");

        Allocation a4 = new Allocation();
        a4.setEmployeeId(3);
        a4.setStartTime("2026-04-01T09:00:00");
        a4.setEndTime("2026-04-01T10:00:00");

        Employee e1 = new Employee();
        e1.setId(4);
        e1.setForename("Sarah");
        e1.setSurname("Jones");

        Employee e2 = new Employee();
        e2.setId(3);
        e2.setForename("Alice");
        e2.setSurname("Allen");

        when(apiClient.getAllocations()).thenReturn(List.of(a1, a2, a3, a4));
        when(apiClient.getEmployees()).thenReturn(List.of(e1, e2));

        List<String> result = maternityService.getStaffWithThreeOrMoreConcurrentPatients();

        assertEquals(List.of("Sarah Jones"), result);
    }

    @Test
    void shouldReturnEmptyWhenNoStaffHaveThreeConcurrentPatients() {
        Allocation a1 = new Allocation();
        a1.setEmployeeId(4);
        a1.setStartTime("2026-04-01T10:00:00");
        a1.setEndTime("2026-04-01T11:00:00");

        Allocation a2 = new Allocation();
        a2.setEmployeeId(4);
        a2.setStartTime("2026-04-01T11:00:00");
        a2.setEndTime("2026-04-01T12:00:00");

        Employee e1 = new Employee();
        e1.setId(4);
        e1.setForename("Sarah");
        e1.setSurname("Jones");

        when(apiClient.getAllocations()).thenReturn(List.of(a1, a2));
        when(apiClient.getEmployees()).thenReturn(List.of(e1));

        List<String> result = maternityService.getStaffWithThreeOrMoreConcurrentPatients();

        assertEquals(List.of(), result);
    }

}