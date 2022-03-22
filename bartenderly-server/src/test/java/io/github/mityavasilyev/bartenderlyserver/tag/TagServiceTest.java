package io.github.mityavasilyev.bartenderlyserver.tag;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@Profile("test")
@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    private Map<Long, Tag> dummyRepository;

    @BeforeEach
    void setUp() {
        dummyRepository = new HashMap<>();
        dummyRepository.put(1l, new Tag(1l, "First"));
        dummyRepository.put(2l, new Tag(2l, "Second"));
    }

    @Test
    void getAll() {
        Mockito.when(tagRepository.findAll())
                .thenReturn(new ArrayList<>(dummyRepository.values()));
        List<Tag> tags = tagService.getAll();
        assertEquals(dummyRepository.size(), tags.size());
    }

    @Test
    void getById() {
        Mockito.when(tagRepository.findById(1l))
                .thenReturn(Optional.ofNullable(dummyRepository.get(1l)));
        Tag tag = tagService.getById(1l);
        assertSame(dummyRepository.get(1l), tag);
        assertEquals(dummyRepository.get(1L).getName(), tag.getName());
    }

    @Test
    void getAllByName() {
        Mockito.when(tagRepository.findByNameContainingIgnoreCase("a"))
                .thenReturn(dummyRepository.values()
                        .stream()
                        .filter(tag -> tag.getName().contains("s"))
                        .toList());
        List<Tag> tags = tagService.getAllByName("a");
        assertEquals(1, tags.size());

        Mockito.when(tagRepository.findByNameContainingIgnoreCase("b"))
                .thenReturn(dummyRepository.values()
                        .stream()
                        .filter(tag -> tag.getName().contains("b"))
                        .toList());
        tags = tagService.getAllByName("b");
        assertEquals(0, tags.size());
    }

    @Test
    void addNew() {
        Tag tag = new Tag(3l, "Third");
        Mockito.when(tagRepository.save(Mockito.any())).thenReturn(tag);
        Tag returnedTag = tagService.addNew(tag);
        assertEquals(tag, returnedTag);
        Mockito.verify(tagRepository).save(Mockito.any());
    }

    @Test
    void deleteById() {
        tagService.deleteById(1l);
        Mockito.verify(tagRepository, Mockito.times(1))
                .deleteById(Mockito.anyLong());
    }

    @Test
    void updateById() {
        Mockito.when(tagRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(new Tag()));
        tagService.updateById(1l, new Tag());
        Mockito.verify(tagRepository, Mockito.times(1))
                .findById(1l);
        Mockito.verify(tagRepository, Mockito.times(1))
                .save(Mockito.any());
    }
}
