package biz.mercue.impactweb.dao;

import biz.mercue.impactweb.model.ImageCollection;
import org.springframework.stereotype.Repository;

@Repository
public class ImageCollectionDaoImpl extends AbstractDao<String, ImageCollection> implements ImageCollectionDao {
    @Override
    public ImageCollection getById(String imageId) {
        return getByKey(imageId);
    }

    @Override
    public void create(ImageCollection imageCollection) {
        persist(imageCollection);
    }

    @Override
    public void deleteImageCollection(ImageCollection imageCollection) {
        delete(imageCollection);
    }


}
