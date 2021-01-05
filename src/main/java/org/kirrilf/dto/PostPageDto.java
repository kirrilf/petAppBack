package org.kirrilf.dto;

import java.util.List;

public class PostPageDto {
    List<PostDto> posts;
    private int currentPage;
    private int totalPage;

    public PostPageDto(List<PostDto> posts, int currentPage, int totalPage) {
        this.posts = posts;
        this.currentPage = currentPage;
        this.totalPage = totalPage;
    }

    public List<PostDto> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDto> posts) {
        this.posts = posts;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
