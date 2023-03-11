package com.everett.services;

import java.util.List;

import com.everett.dtos.PictureResponseDTO;
import com.everett.exceptions.checkedExceptions.EmptyEntityException;

public interface PictureService {
    public PictureResponseDTO getPictureById(Long id) throws EmptyEntityException;

    public List<PictureResponseDTO> getAllPictures();

    public void addPostPicture(Long postId, String picUrl) throws EmptyEntityException;
}
