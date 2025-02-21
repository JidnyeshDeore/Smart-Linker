package com.tut.scm.scm_tutorial.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.tut.scm.scm_tutorial.helpers.AppConstants;

@Service
public class ImageServiceImpl implements ImageService{

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile contactImage, String fileName) {
       
        
        try {

            //image upload ka code
            byte[] data=new byte[contactImage.getInputStream().available()];
            
            contactImage.getInputStream().read(data);

            cloudinary.uploader().upload(data, ObjectUtils.asMap(
                "public_id",fileName
            ));

            return  this.getUrlFromPublicId(fileName);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public String getUrlFromPublicId(String publicId) {
       

        return cloudinary
        .url()
        .transformation(
            new Transformation<>()
            .width(AppConstants.CONTACT_IMAGE_WIDTH)
            .height(AppConstants.CONTACT_IMAGE_HEIGHT)
            .crop(AppConstants.CONTACT_IMAGE_CROP)
        )
        .generate(publicId);
    }

    

}
