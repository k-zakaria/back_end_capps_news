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
    private User user;
    private List<Tag> tags;
    private Category category;
}
