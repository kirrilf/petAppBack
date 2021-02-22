package org.kirrilf.dto;

import java.util.List;

public class CommentPageDto {
    private List<CommentDto> comments;
    private int currentPage;
    private int totalPages;

    public CommentPageDto(List<CommentDto> comments, int currentPage, int totalPages) {
        this.comments = comments;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
