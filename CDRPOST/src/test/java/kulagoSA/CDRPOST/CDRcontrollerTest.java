package kulagoSA.CDRPOST;

import kulagoSA.CDRPOST.controller.controllerCDR;
import kulagoSA.CDRPOST.model.CDRcall;
import kulagoSA.CDRPOST.repository.InMemoryCDRcallDAO;
import kulagoSA.CDRPOST.service.serviceCDR;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class CDRcontrollerTest {

    private MockMvc mockMvc;

    @Mock
    private serviceCDR service;

    @Mock
    private InMemoryCDRcallDAO cdrRepository; // Мокируем репозиторий

    @InjectMocks
    private controllerCDR controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testFindAllCDRData() throws Exception {
        // Подготовка данных
        CDRcall call1 = new CDRcall();
        call1.setId(1);
        call1.setCall("01");
        call1.setIniciate_Number("1234567890");
        call1.setAccept_Number("0987654321");
        call1.setDateStart("2023-10-01 12:00:00");
        call1.setDateEnd("2023-10-01 12:05:00");

        CDRcall call2 = new CDRcall();
        call2.setId(2);
        call2.setCall("02");
        call2.setIniciate_Number("0987654321");
        call2.setAccept_Number("1234567890");
        call2.setDateStart("2023-10-01 13:00:00");
        call2.setDateEnd("2023-10-01 13:02:00");

        List<CDRcall> calls = Arrays.asList(call1, call2);

        // Мокируем вызов сервиса
        when(service.findAllCDRData()).thenReturn(calls);

        // Выполнение запроса и проверка результата
        MvcResult result = mockMvc.perform(get("/api/v1/CDR"))
                .andExpect(status().isOk())
                .andReturn();

        // Логирование ответа
        String response = result.getResponse().getContentAsString();
        System.out.println("Response: " + response);


        // Проверка результата
        mockMvc.perform(get("/api/v1/CDR"))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].call").value("01"))
                .andExpect(jsonPath("$[0].iniciate_Number").value("1234567890"))
                .andExpect(jsonPath("$[0].accept_Number").value("0987654321"))
                .andExpect(jsonPath("$[0].dateStart").value("2023-10-01 12:00:00"))
                .andExpect(jsonPath("$[0].dateEnd").value("2023-10-01 12:05:00"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].call").value("02"))
                .andExpect(jsonPath("$[1].iniciate_Number").value("0987654321"))
                .andExpect(jsonPath("$[1].accept_Number").value("1234567890"))
                .andExpect(jsonPath("$[1].dateStart").value("2023-10-01 13:00:00"))
                .andExpect(jsonPath("$[1].dateEnd").value("2023-10-01 13:02:00"));
    }

    @Test
    void testSaveCDR() throws Exception {
        // Подготовка данных
        CDRcall call = new CDRcall();
        call.setId(1); // Теперь int
        call.setCall("02"); // 02 - входящие
        call.setIniciate_Number("1234567890");
        call.setAccept_Number("0987654321");
        call.setDateStart("2023-10-01 12:00:00");
        call.setDateEnd("2023-10-01 12:05:00");

        // Мокируем вызов сервиса
        when(service.saveCDR(any(CDRcall.class))).thenReturn(call);

        // Выполнение запроса и проверка результата
        MvcResult result = mockMvc.perform(post("/api/v1/CDR/save_CDR") // Выполняем POST-запрос
                        .contentType(MediaType.APPLICATION_JSON) // Указываем тип содержимого
                        .content("{\"id\": 1, \"call\": \"02\", \"iniciate_Number\": \"1234567890\", \"accept_Number\": \"0987654321\", \"dateStart\": \"2023-10-01 12:00:00\", \"dateEnd\": \"2023-10-01 12:05:00\"}")) // Тело запроса
                .andExpect(status().isOk()) // Ожидаем статус 200
                .andReturn();

        // Логирование ответа
        String response = result.getResponse().getContentAsString();
        System.out.println("Response: " + response);

        // Проверка JSON-ответа
        mockMvc.perform(post("/api/v1/CDR/save_CDR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"call\": \"02\", \"iniciate_Number\": \"1234567890\", \"accept_Number\": \"0987654321\", \"dateStart\": \"2023-10-01 12:00:00\", \"dateEnd\": \"2023-10-01 12:05:00\"}"))
                .andExpect(jsonPath("$.id").value(1)) // Проверяем поле id (теперь int)
                .andExpect(jsonPath("$.call").value("02")) // Проверяем поле call (02 - входящие)
                .andExpect(jsonPath("$.iniciate_Number").value("1234567890")) // Проверяем поле iniciate_Number
                .andExpect(jsonPath("$.accept_Number").value("0987654321")) // Проверяем поле accept_Number
                .andExpect(jsonPath("$.dateStart").value("2023-10-01 12:00:00")) // Проверяем поле dateStart
                .andExpect(jsonPath("$.dateEnd").value("2023-10-01 12:05:00")); // Проверяем поле dateEnd
    }

    @Test
    void testFindByNumber() throws Exception {
        // Подготовка данных
        CDRcall call = new CDRcall();
        call.setId(1); // Теперь int
        call.setCall("01"); // 01 - исходящие
        call.setIniciate_Number("1234567890");
        call.setAccept_Number("0987654321");
        call.setDateStart("2023-10-01 12:00:00");
        call.setDateEnd("2023-10-01 12:05:00");

        // Мокируем вызов сервиса
        when(service.findByNumber("1234567890")).thenReturn(call);

        // Выполнение запроса и проверка результата
        MvcResult result = mockMvc.perform(get("/api/v1/CDR/1234567890") // Выполняем GET-запрос
                        .contentType(MediaType.APPLICATION_JSON)) // Указываем тип содержимого
                .andExpect(status().isOk()) // Ожидаем статус 200
                .andReturn();

        // Логирование ответа
        String response = result.getResponse().getContentAsString();
        System.out.println("Response: " + response);

        // Проверка JSON-ответа
        mockMvc.perform(get("/api/v1/CDR/1234567890"))
                .andExpect(jsonPath("$.id").value(1)) // Проверяем поле id (теперь int)
                .andExpect(jsonPath("$.call").value("01")) // Проверяем поле call (01 - исходящие)
                .andExpect(jsonPath("$.iniciate_Number").value("1234567890")) // Проверяем поле iniciate_Number
                .andExpect(jsonPath("$.accept_Number").value("0987654321")) // Проверяем поле accept_Number
                .andExpect(jsonPath("$.dateStart").value("2023-10-01 12:00:00")) // Проверяем поле dateStart
                .andExpect(jsonPath("$.dateEnd").value("2023-10-01 12:05:00")); // Проверяем поле dateEnd
    }

    @Test
    void testUpdateCDR() throws Exception {
        // Подготовка данных
        CDRcall call = new CDRcall();
        call.setId(1);
        call.setCall("01");
        call.setIniciate_Number("1234567890");
        call.setAccept_Number("0987654321");
        call.setDateStart("2023-10-01 12:00:00");
        call.setDateEnd("2023-10-01 12:05:00");

        // Мокируем вызов сервиса
        when(service.updateNumber(any(CDRcall.class))).thenReturn(call);

        // Выполнение запроса и проверка результата
        MvcResult result = mockMvc.perform(put("/api/v1/CDR/update_CDR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"call\": \"01\", \"iniciate_Number\": \"1234567890\", \"accept_Number\": \"0987654321\", \"dateStart\": \"2023-10-01 12:00:00\", \"dateEnd\": \"2023-10-01 12:05:00\"}"))
                .andExpect(status().isOk())
                .andReturn();

        // Логирование ответа
        String response = result.getResponse().getContentAsString();
        System.out.println("Response: " + response);

        // Проверка результата
        mockMvc.perform(put("/api/v1/CDR/update_CDR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"call\": \"01\", \"iniciate_Number\": \"1234567890\", \"accept_Number\": \"0987654321\", \"dateStart\": \"2023-10-01 12:00:00\", \"dateEnd\": \"2023-10-01 12:05:00\"}"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.call").value("01"))
                .andExpect(jsonPath("$.iniciate_Number").value("1234567890"))
                .andExpect(jsonPath("$.accept_Number").value("0987654321"))
                .andExpect(jsonPath("$.dateStart").value("2023-10-01 12:00:00"))
                .andExpect(jsonPath("$.dateEnd").value("2023-10-01 12:05:00"));
    }

    @Test
    void testDeleteCDR() throws Exception {
        // Мокируем вызов сервиса
        doNothing().when(service).deleteCDR("1234567890");

        // Выполнение запроса и проверка результата
        mockMvc.perform(delete("/api/v1/CDR/delete_CDR/1234567890"))
                .andExpect(status().isOk());

        // Проверка, что метод сервиса был вызван
        verify(service, times(1)).deleteCDR("1234567890");
    }
}