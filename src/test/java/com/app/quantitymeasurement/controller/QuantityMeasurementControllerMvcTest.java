package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.services.IQuantityMeasurementService;
import com.app.quantitymeasurement.model.QuantityDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MockMvc integration test for QuantityMeasurementController.
 * Uses @WebMvcTest to spin up the web layer only and imports SecurityConfig
 * so that permitAll() is applied to all endpoints.
 */
@WebMvcTest(QuantityMeasurementController.class)
@Import(com.app.quantitymeasurement.config.SecurityConfig.class)
class QuantityMeasurementControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IQuantityMeasurementService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ======================== POST /api/v1/quantities/compare ========================

    @Test
    void compareQuantities_shouldReturn200WithResult() throws Exception {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setThisValue(1.0);
        dto.setThisUnit("FEET");
        dto.setThisMeasurementType("LengthUnit");
        dto.setThatValue(12.0);
        dto.setThatUnit("INCH");
        dto.setThatMeasurementType("LengthUnit");
        dto.setOperation("compare");
        dto.setResultString("true");
        dto.setError(false);

        when(service.compare(any(QuantityDTO.class), any(QuantityDTO.class))).thenReturn(dto);

        String requestBody = """
                {
                  "thisQuantityDTO": {
                    "value": 1.0,
                    "unit": "FEET",
                    "measurementType": "LengthUnit"
                  },
                  "thatQuantityDTO": {
                    "value": 12.0,
                    "unit": "INCH",
                    "measurementType": "LengthUnit"
                  }
                }
                """;

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("compare"))
                .andExpect(jsonPath("$.resultString").value("true"))
                .andExpect(jsonPath("$.error").value(false));
    }

    @Test
    void compareQuantities_withInvalidPayload_shouldReturn400() throws Exception {
        // Missing required fields
        String requestBody = """
                {
                  "thisQuantityDTO": null,
                  "thatQuantityDTO": null
                }
                """;

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    // ======================== POST /api/v1/quantities/convert ========================

    @Test
    void convertQuantity_shouldReturn200WithConvertedResult() throws Exception {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setThisValue(1.0);
        dto.setThisUnit("YARD");
        dto.setThisMeasurementType("LengthUnit");
        dto.setOperation("convert");
        dto.setResultValue(3.0);
        dto.setResultUnit("FEET");
        dto.setResultMeasurementType("LengthUnit");
        dto.setError(false);

        when(service.convert(any(QuantityDTO.class), any())).thenReturn(dto);

        String requestBody = """
                {
                  "thisQuantityDTO": {
                    "value": 1.0,
                    "unit": "YARD",
                    "measurementType": "LengthUnit"
                  },
                  "thatQuantityDTO": {
                    "value": 0.0,
                    "unit": "FEET",
                    "measurementType": "LengthUnit"
                  }
                }
                """;

        mockMvc.perform(post("/api/v1/quantities/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("convert"))
                .andExpect(jsonPath("$.resultValue").value(3.0))
                .andExpect(jsonPath("$.resultUnit").value("FEET"));
    }

    // ======================== POST /api/v1/quantities/add ========================

    @Test
    void addQuantities_shouldReturn200WithSum() throws Exception {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setThisValue(1.0);
        dto.setThisUnit("FEET");
        dto.setThisMeasurementType("LengthUnit");
        dto.setThatValue(12.0);
        dto.setThatUnit("INCH");
        dto.setThatMeasurementType("LengthUnit");
        dto.setOperation("add");
        dto.setResultValue(2.0);
        dto.setResultUnit("FEET");
        dto.setError(false);

        when(service.add(any(QuantityDTO.class), any(QuantityDTO.class), any())).thenReturn(dto);

        String requestBody = """
                {
                  "thisQuantityDTO": {
                    "value": 1.0,
                    "unit": "FEET",
                    "measurementType": "LengthUnit"
                  },
                  "thatQuantityDTO": {
                    "value": 12.0,
                    "unit": "INCH",
                    "measurementType": "LengthUnit"
                  }
                }
                """;

        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("add"))
                .andExpect(jsonPath("$.resultValue").value(2.0))
                .andExpect(jsonPath("$.resultUnit").value("FEET"));
    }

    // ======================== GET /api/v1/quantities/history/operation/{op} ========================

    @Test
    void getOperationHistory_shouldReturnList() throws Exception {
        QuantityMeasurementDTO dto1 = new QuantityMeasurementDTO();
        dto1.setOperation("compare");
        dto1.setResultString("true");
        QuantityMeasurementDTO dto2 = new QuantityMeasurementDTO();
        dto2.setOperation("compare");
        dto2.setResultString("false");
        List<QuantityMeasurementDTO> list = Arrays.asList(dto1, dto2);

        when(service.getHistoryByOperation("compare")).thenReturn(list);

        mockMvc.perform(get("/api/v1/quantities/history/operation/compare"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].operation").value("compare"))
                .andExpect(jsonPath("$[1].operation").value("compare"));
    }

    @Test
    void getOperationHistory_emptyResult_shouldReturnEmptyList() throws Exception {
        when(service.getHistoryByOperation("nonexistent")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/quantities/history/operation/nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ======================== GET /api/v1/quantities/history/type/{type} ========================

    @Test
    void getMeasurementTypeHistory_shouldReturnList() throws Exception {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setThisMeasurementType("LengthUnit");
        dto.setOperation("compare");

        when(service.getHistoryByMeasurementType("LengthUnit")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/quantities/history/type/LengthUnit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].thisMeasurementType").value("LengthUnit"));
    }

    // ======================== GET /api/v1/quantities/count/{operation} ========================

    @Test
    void getOperationCount_shouldReturnCount() throws Exception {
        when(service.getCountByOperation("compare")).thenReturn(5L);

        mockMvc.perform(get("/api/v1/quantities/count/compare"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void getOperationCount_zeroResult_shouldReturnZero() throws Exception {
        when(service.getCountByOperation("unknown")).thenReturn(0L);

        mockMvc.perform(get("/api/v1/quantities/count/unknown"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }
}
