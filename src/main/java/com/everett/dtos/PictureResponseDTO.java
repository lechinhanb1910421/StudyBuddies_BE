package com.everett.dtos;

import com.everett.models.Picture;

public class PictureResponseDTO {
    private Long postId;
    private Long picId;
    private String picUrl;

    public PictureResponseDTO() {
    }

    public PictureResponseDTO(Picture picture) {
        this.postId = picture.getPost().getPostId();
        this.picId = picture.getPicId();
        this.picUrl = picture.getPicUrl();
    }

    public PictureResponseDTO(Long postId, Long picId, String picUrl) {
        this.postId = postId;
        this.picId = picId;
        this.picUrl = picUrl;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getPicId() {
        return picId;
    }

    public void setPicId(Long picId) {
        this.picId = picId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

}
