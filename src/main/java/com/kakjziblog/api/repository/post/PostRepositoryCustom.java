package com.kakjziblog.api.repository.post;

import com.kakjziblog.api.domain.Post;
import com.kakjziblog.api.request.post.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
