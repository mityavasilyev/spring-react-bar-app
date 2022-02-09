package io.github.mityavasilyev.springreactbarapp.cocktail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CocktailControllerTest {

    @Mock
    CocktailService cocktailService;

    @InjectMocks
    CocktailController cocktailController;

    private MockMvc mockMvc;

    private Cocktail mocktail;  // Yeah, I know how stupid that is

    private final String BASE_URL = "/api/cocktails";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cocktailController).build();

        mocktail = Cocktail.builder()
                .id(1L)
                .name("Test")
                .build();
    }

    @Test
    void getAllCocktails() throws Exception {
        Mockito.when(cocktailService.getAll())
                .thenReturn(Arrays.asList(mocktail));
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0]").exists());
        Mockito.verify(cocktailService, Mockito.times(1))
                .getAll();
    }

    @Test
    void getAllCocktailsByName() throws Exception {
        Mockito.when(cocktailService.getAllByName(Mockito.any()))
                .thenReturn(Arrays.asList(mocktail));
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/name/test")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name")
                        .value(mocktail.getName()));
        Mockito.verify(cocktailService, Mockito.times(1))
                .getAllByName(Mockito.any());
    }

    @Test
    void getAllCocktailsByTagId() throws Exception {
        Mockito.when(cocktailService.getAllByTagId(Mockito.any()))
                .thenReturn(Arrays.asList(mocktail));
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/tagId/1")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0]").exists());
        Mockito.verify(cocktailService, Mockito.times(1))
                .getAllByTagId(Mockito.any());
    }

    @Test
    void getCocktailById() throws Exception {
        Mockito.when(cocktailService.getById(1L))
                .thenReturn(mocktail);
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
        Mockito.verify(cocktailService, Mockito.times(1))
                .getById(Mockito.any());
    }

    @Test
    void addNewCocktail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .content(toJson(mocktail))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Mockito.verify(cocktailService, Mockito.times(1))
                .addNew(Mockito.any());
    }

    @Test
    void deleteCocktail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1"))
                .andExpect(status().isOk());
        Mockito.verify(cocktailService, Mockito.times(1))
                .deleteById(Mockito.any());
    }

    @Test
    void updateCocktail() throws Exception {
        Mockito.when(cocktailService.getById(Mockito.any()))
                .thenReturn(mocktail);
        mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL + "/1")
                        .content(toJson(mocktail))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
        Mockito.verify(cocktailService, Mockito.times(1))
                .updateById(Mockito.any(), Mockito.any());
    }

    private String toJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }
}