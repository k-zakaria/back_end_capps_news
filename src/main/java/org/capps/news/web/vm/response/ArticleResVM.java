package org.capps.news.web.vm.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleResVM {
    private UUID id;
    private String title;
    private String description;
    private String content;
    private String image;
    private boolean published;
    private LocalDateTime publicationDate;
    private UserSummaryDTO author;
    private CategorySummaryDTO category;
    private List<TagSummaryDTO> tags;
}

