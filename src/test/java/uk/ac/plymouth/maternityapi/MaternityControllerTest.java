package uk.ac.plymouth.maternityapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.plymouth.maternityapi.controller.MaternityController;
import uk.ac.plymouth.maternityapi.service.MaternityService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MaternityController.class)
public class MaternityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MaternityService maternityService;

    @Test
    void shouldReturnRoomsForPatient() throws Exception {
        when(maternityService.getRoomsUsedByPatient(1))
                .thenReturn(List.of("A1", "B1"));

        mockMvc.perform(get("/api/patients/1/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("A1"))
                .andExpect(jsonPath("$[1]").value("B1"));
    }

    @Test
    void shouldReturnPatientsForRoom() throws Exception {
        when(maternityService.getPatientsInRoomLast7Days("A1"))
                .thenReturn(List.of("Smith"));

        mockMvc.perform(get("/api/rooms/A1/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Smith"));
    }

    @Test
    void shouldReturnLeastUsedRooms() throws Exception {
        when(maternityService.getLeastUsedRooms())
                .thenReturn(List.of("B1"));

        mockMvc.perform(get("/api/rooms/least-used"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("B1"));
    }

    @Test
    void shouldReturnOverloadedStaff() throws Exception {
        when(maternityService.getStaffWithThreeOrMoreConcurrentPatients())
                .thenReturn(List.of("Sarah Jones"));

        mockMvc.perform(get("/api/staff/overloaded"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Sarah Jones"));
    }
}