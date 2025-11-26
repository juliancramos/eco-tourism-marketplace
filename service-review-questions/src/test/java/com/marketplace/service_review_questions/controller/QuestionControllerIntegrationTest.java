package com.marketplace.service_review_questions.controller;
import com.marketplace.service_review_questions.model.Answer;
import com.marketplace.service_review_questions.model.Question;
import com.marketplace.service_review_questions.repository.AnswerRepository;
import com.marketplace.service_review_questions.repository.QuestionRepository;
import com.marketplace.service_review_questions.repository.UserCacheRepository;
import com.marketplace.service_review_questions.repository.ServiceCacheRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class QuestionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @MockBean
    private UserCacheRepository userCacheRepository;

    @MockBean
    private ServiceCacheRepository serviceCacheRepository;

    @BeforeEach
    void setUp() {
        Mockito.when(userCacheRepository.existsById(anyLong()))
               .thenReturn(true);
        Mockito.when(serviceCacheRepository.existsById(anyLong()))
               .thenReturn(true);

        answerRepository.deleteAll();
        questionRepository.deleteAll();
    }

    // 1. CREATE QUESTION
    @Test
    void createQuestion_shouldPersistAndReturnQuestion() throws Exception {
        String json = """
                {
                  "serviceId": 100,
                  "userId": 200,
                  "text": "¿Este tour incluye transporte?"
                }
                """;

        mockMvc.perform(
                        post("/questions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceId").value(100))
                .andExpect(jsonPath("$.userId").value(200))
                .andExpect(jsonPath("$.text").value("¿Este tour incluye transporte?"));

        assertThat(questionRepository.count()).isEqualTo(1);
    }

    // 2. GET QUESTION
    @Test
    void getQuestion_shouldReturnExistingQuestion() throws Exception {
        Question q = Question.builder()
                .serviceId(1L)
                .userId(2L)
                .text("¿Hay alimentación incluida?")
                .creationDate(LocalDateTime.now())
                .build();
        q = questionRepository.save(q);

        mockMvc.perform(get("/questions/{id}", q.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(q.getId()))
                .andExpect(jsonPath("$.text").value("¿Hay alimentación incluida?"));
    }

    // 3. LIST QUESTIONS (filtrando por serviceId)
    @Test
    void listQuestionsByService_shouldReturnPageForService() throws Exception {
        Question q1 = Question.builder()
                .serviceId(10L)
                .userId(1L)
                .text("Pregunta 1")
                .creationDate(LocalDateTime.now())
                .build();
        Question q2 = Question.builder()
                .serviceId(10L)
                .userId(2L)
                .text("Pregunta 2")
                .creationDate(LocalDateTime.now())
                .build();
        Question q3 = Question.builder()
                .serviceId(20L)
                .userId(3L)
                .text("Pregunta 3")
                .creationDate(LocalDateTime.now())
                .build();

        questionRepository.save(q1);
        questionRepository.save(q2);
        questionRepository.save(q3);

        mockMvc.perform(get("/questions")
                        .param("serviceId", "10")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    // 4. REPLY (crear Answer para una Question)
    @Test
    void replyToQuestion_shouldCreateAnswerLinkedToQuestion() throws Exception {
        Question q = Question.builder()
                .serviceId(50L)
                .userId(10L)
                .text("¿La ruta es apta para niños?")
                .creationDate(LocalDateTime.now())
                .build();
        q = questionRepository.save(q);

        String json = """
                {
                  "text": "Sí, es apta para niños mayores de 6 años"
                }
                """;

        mockMvc.perform(
                        post("/questions/{id}/answers", q.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text")
                        .value("Sí, es apta para niños mayores de 6 años"));
        assertThat(answerRepository.count()).isEqualTo(1);
        List<Answer> answers = answerRepository.findAll();
        assertThat(answers).hasSize(1);
        assertThat(answers.get(0).getQuestion().getId()).isEqualTo(q.getId());
    }

    // 5. UPDATE ANSWER    
    @Test
    void updateAnswer_shouldChangeAnswerText() throws Exception {
        Question q = Question.builder()
                .serviceId(1L)
                .userId(1L)
                .text("Pregunta X")
                .creationDate(LocalDateTime.now())
                .build();
        q = questionRepository.save(q);

        Answer a = Answer.builder()
                .question(q)
                .text("Respuesta vieja")
                .creationDate(LocalDateTime.now())
                .build();
        a = answerRepository.save(a);

        String json = """
                {
                  "id": %d,
                  "text": "Respuesta nueva"
                }
                """.formatted(a.getId());

        mockMvc.perform(
                        put("/questions/{questionId}/answers", q.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                // AnswerDTO NO tiene id, sólo text y creationDate
                .andExpect(jsonPath("$.text").value("Respuesta nueva"));

        Answer updated = answerRepository.findById(a.getId()).orElseThrow();
        assertThat(updated.getText()).isEqualTo("Respuesta nueva");
    }

    // 6. DELETE QUESTION
    @Test
    void deleteQuestion_shouldRemoveQuestionAndAnswers() throws Exception {
        Question q = Question.builder()
                .serviceId(50L)
                .userId(10L)
                .text("Pregunta a borrar")
                .creationDate(LocalDateTime.now())
                .build();
        q = questionRepository.save(q);

        Answer a = Answer.builder()
                .question(q)
                .text("Respuesta asociada")
                .creationDate(LocalDateTime.now())
                .build();
        answerRepository.save(a);

        mockMvc.perform(delete("/questions/{id}", q.getId()))
                .andExpect(status().isOk());

        assertThat(questionRepository.count()).isZero();
        assertThat(answerRepository.count()).isZero();
    }
}
