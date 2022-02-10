package io.github.mityavasilyev.springreactbarapp.tag;

import io.github.mityavasilyev.springreactbarapp.TestUtils;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TagControllerTest {

    @Mock
    TagService tagService;

    @InjectMocks
    TagController tagController;

    MockMvc mockMvc;

    private Tag mockTag;

    private final String BASE_URL = "/api/tags";



    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tagController).build();

        mockTag = new Tag(1L, "Test");
    }

    @Test
    void getAllTags() throws Exception {
        Mockito.when(tagService.getAll())
                .thenReturn(Arrays.asList(mockTag));
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0]").exists());
        Mockito.verify(tagService, Mockito.times(1))
                .getAll();
    }

    @Test
    void getTagById() throws Exception {
        Mockito.when(tagService.getById(1L))
                .thenReturn(mockTag);
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
        Mockito.verify(tagService, Mockito.times(1))
                .getById(Mockito.any());
    }

    @Test
    void getTagById_InvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/-20"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/0"))
                .andExpect(status().isBadRequest());
        Mockito.verify(tagService, Mockito.times(0))
                .getById(Mockito.any());
    }

    @Test
    void getAllTagsByName() throws Exception {
        Mockito.when(tagService.getAllByName("test"))
                .thenReturn(Arrays.asList(mockTag));
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/name/test")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name")
                        .value(mockTag.getName()));
        Mockito.verify(tagService, Mockito.times(1))
                .getAllByName(Mockito.any());
    }

    @Test
    void addNewTag() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .content(TestUtils.toJson(mockTag))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Mockito.verify(tagService, Mockito.times(1))
                .addNew(Mockito.any());
    }

    @Test
    void deleteTag() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1"))
                .andExpect(status().isOk());
        Mockito.verify(tagService, Mockito.times(1))
                .deleteById(Mockito.any());
    }

    @Test
    void deleteTag_InvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/-20"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/0"))
                .andExpect(status().isBadRequest());
        Mockito.verify(tagService, Mockito.times(0))
                .deleteById(Mockito.any());
    }

    @Test
    void updateTag() throws Exception {
        Mockito.when(tagService.getById(Mockito.any()))
                .thenReturn(mockTag);
        mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL + "/1")
                        .content(TestUtils.toJson(mockTag))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(tagService, Mockito.times(1))
                .updateById(Mockito.any(), Mockito.any());
    }

    @Test
    void updateCocktail_InvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL + "/-20")
                        .content(TestUtils.toJson(mockTag))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mockMvc.perform(MockMvcRequestBuilders.patch(BASE_URL + "/0")
                        .content(TestUtils.toJson(mockTag))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        Mockito.verify(tagService, Mockito.times(0))
                .updateById(Mockito.any(), Mockito.any());
    }
}