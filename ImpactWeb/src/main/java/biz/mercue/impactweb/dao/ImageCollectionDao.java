package biz.mercue.impactweb.dao;

import biz.mercue.impactweb.model.ImageCollection;

public interface ImageCollectionDao {
    ImageCollection getById(String imageId);

    void create(ImageCollection imageCollection);

    void deleteImageCollection(ImageCollection imageCollection);
}
