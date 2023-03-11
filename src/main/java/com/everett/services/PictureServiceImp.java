package com.everett.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.everett.daos.PictureDAO;
import com.everett.daos.PostDAO;
import com.everett.dtos.PictureResponseDTO;
import com.everett.exceptions.checkedExceptions.EmptyEntityException;
import com.everett.models.Picture;
import com.everett.models.Post;

@Stateless
public class PictureServiceImp implements PictureService {

    @Inject
    PictureDAO pictureDAO;

    @Inject
    PostDAO postDAO;

    @Override
    public PictureResponseDTO getPictureById(Long id) throws EmptyEntityException {
        return new PictureResponseDTO(pictureDAO.getPictureById(id));
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<PictureResponseDTO> getAllPictures() {
        List<Picture> pictures = pictureDAO.getAllPictures();
        List<PictureResponseDTO> results = new ArrayList<PictureResponseDTO>();
        pictures.forEach(pic -> {
            results.add(new PictureResponseDTO(pic));
        });
        return results;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addPostPicture(Long postId, String picUrl) throws EmptyEntityException {
        Post post = postDAO.getPostById(postId);
        Picture picture = new Picture(post, picUrl);
        pictureDAO.addPicture(picture);
    }

}
