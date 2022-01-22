package io.github.mityavasilyev.springvertxreactbarapp.services;

import io.github.mityavasilyev.springvertxreactbarapp.model.Tag;
import io.github.mityavasilyev.springvertxreactbarapp.repositories.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * @param id id of the tag
     * @return  tag with provided id
     */
    public Tag getById(Long id) {
        return tagRepository.findById(id).get();
    }

    /**
     * Retrieves all tags with name containing provided string
     * @param name string to search for
     * @return matching tags
     */
    public List<Tag> getAllByName(String name) {
        return tagRepository.findByNameContainingIgnoreCase(name);
    }
}
