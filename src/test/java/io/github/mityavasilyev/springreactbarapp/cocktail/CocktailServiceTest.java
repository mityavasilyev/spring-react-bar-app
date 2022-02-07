package io.github.mityavasilyev.springreactbarapp.cocktail;

import io.github.mityavasilyev.springreactbarapp.tag.Tag;
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
class CocktailServiceTest {

    private final Tag testTag = new Tag(1l, "Tag");
    @Mock
    private CocktailRepository cocktailRepository;
    @InjectMocks
    private CocktailService cocktailService;
    private Map<Long, Cocktail> dummyRepository;

    @BeforeEach
    void setUp() {
        dummyRepository = new HashMap<>();
        dummyRepository.put(1l, Cocktail.builder()
                .id(1l)
                .name("Margarita")
                .tags(new HashSet<>(Arrays.asList(testTag)))
                .build());
    }

    @Test
    void getAll() {
        Mockito.when(cocktailRepository.findAll())
                .thenReturn(new ArrayList<>(dummyRepository.values()));
        List<Cocktail> cocktails = cocktailService.getAll();
        assertEquals(dummyRepository.values().size(), cocktails.size());
    }

    @Test
    void getById() {
        Mockito.when(cocktailRepository.findById(1l))
                .thenReturn(Optional.ofNullable(dummyRepository.get(1l)));
        Cocktail cocktail = cocktailService.getById(1l);
        assertSame(dummyRepository.get(1l), cocktail);
        assertEquals(dummyRepository.get(1l).getName(), cocktail.getName());
    }

    @Test
    void getAllByTagId() {
        Mockito.when(cocktailRepository.findCocktailsByTagId(Mockito.anyLong()))
                .thenReturn(dummyRepository.values().stream()
                        .filter(cocktail -> cocktail.getTags().contains(testTag))
                        .toList());
        List<Cocktail> cocktails = cocktailService.getAllByTagId(testTag.getId());
        assertEquals(1, cocktails.size());
    }

    @Test
    void getAllByName() {
        Mockito.when(cocktailRepository.findByNameContainingIgnoreCase("a"))
                .thenReturn(dummyRepository.values()
                        .stream()
                        .filter(cocktail -> cocktail.getName().contains("a"))
                        .toList());
        List<Cocktail> cocktails = cocktailService.getAllByName("a");
        assertEquals(dummyRepository.size(), cocktails.size());

        Mockito.when(cocktailRepository.findByNameContainingIgnoreCase("b"))
                .thenReturn(dummyRepository.values()
                        .stream()
                        .filter(cocktail -> cocktail.getName().contains("b"))
                        .toList());
        cocktails = cocktailService.getAllByName("b");
        assertEquals(0, cocktails.size());
    }

    @Test
    void addNew() {
        Cocktail cocktail = Cocktail.builder()
                .id(2l)
                .name("Test")
                .build();
        Mockito.when(cocktailRepository.save(Mockito.any())).thenReturn(cocktail);
        Cocktail returnedCocktail = cocktailService.addNew(cocktail);
        assertEquals(cocktail, returnedCocktail);
        Mockito.verify(cocktailRepository, Mockito.times(1))
                .save(Mockito.any());
    }

    @Test
    void deleteById() {
        cocktailService.deleteById(1l);
        Mockito.verify(cocktailRepository, Mockito.times(1))
                .deleteById(Mockito.any());
    }

    @Test
    void updateById() {
        Mockito.when(cocktailRepository.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(Cocktail.builder().build()));
        cocktailService.updateById(1l, Cocktail.builder().build());
        Mockito.verify(cocktailRepository, Mockito.times(1))
                .findById(1l);
        Mockito.verify(cocktailRepository, Mockito.times(1))
                .save(Mockito.any());
    }
}
