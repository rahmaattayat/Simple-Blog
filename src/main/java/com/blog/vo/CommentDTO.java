package com.blog.vo;

public class CommentDTO {
    private Long postId;
    private String user;
    private String comment;

    public CommentDTO() {
    }

    public CommentDTO(Long postId, String user, String comment) {
        this.postId = postId;
        this.user = user;
        this.comment = comment;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
