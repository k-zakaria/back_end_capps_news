package org.capps.news.web.vm.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.capps.news.model.Category;
import org.capps.news.model.Tag;
import org.capps.news.model.User;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleReqVM {
    private String image;
    private String title;
    private String description;
    private String content;
    private List<Long> tagIds;  // Changez de List<Tag> à List<Long>
    private Long categoryId;    // Changez de Category à Long
}
