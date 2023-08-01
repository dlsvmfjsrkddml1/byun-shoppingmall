package com.byunmall.repository;

import com.byunmall.domain.Post;
import com.byunmall.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {
    /**
     * 인자 값을 postSearch를 받는 메서드를 구현해보자.
     */
    List<Post> getList(PostSearch postSearch);
}
