package io.github.mityavasilyev.springvertxreactbarapp.tag;

import io.github.mityavasilyev.springvertxreactbarapp.cocktail.Cocktail;
import io.github.mityavasilyev.springvertxreactbarapp.exceptions.DataNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class TagService {

    private TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * @return all tags
     */
    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    /**
     * Retrieves tag by id
     *
     * @param id id of the tag
     * @return tag with provided id
     */
    public Tag getById(Long id) {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isPresent()) {
            return tag.get();
        } else {
            throw new ResponseStatusException(NOT_FOUND, "No tag with such id");
        }
    }

    /**
     * Retrieves all tags with name containing provided string
     *
     * @param name string to search for
     * @return matching tags
     */
    public List<Tag> getAllByName(String name) {
        return tagRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Saves provided tag to the repository
     *
     * @param tag to save
     * @return saved tag
     */
    public Tag addNew(Tag tag) {
        return tagRepository.save(tag);
    }

    /**
     * Deletes tag with matching id
     *
     * @param id of tag to delete
     */
    public void deleteById(Long id) {
        tagRepository.deleteById(id);
    }

    /**
     * Updates tag with provided entity
     *
     * @param id          of tag to update
     * @param newTag new entity
     * @return updated entity
     */
    public Tag updateById(Long id, Tag newTag) throws DataNotFoundException {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isPresent()) {
            tagRepository.save(newTag);
            return newTag;
        } else {
            throw new DataNotFoundException("No tag with such id");
        }
    }
}
