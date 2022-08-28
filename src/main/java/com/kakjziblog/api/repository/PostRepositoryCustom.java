package com.kakjziblog.api.repository;

import com.kakjziblog.api.domain.Post;
import com.kakjziblog.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
