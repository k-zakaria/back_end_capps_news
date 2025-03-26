package org.capps.news.service.interfaces;

import org.capps.news.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagServiceInterface {

    List<Tag> getAllTags();

    Optional<Tag> getTagById(Long id);

    Tag createTag(Tag tag);

    Tag updateTag(Long id, Tag tagDetails);

    void deleteTag(Long id);
}
