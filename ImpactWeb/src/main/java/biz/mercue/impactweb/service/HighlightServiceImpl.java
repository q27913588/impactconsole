//package biz.mercue.impactweb.service;
//
//import biz.mercue.impactweb.dao.HighlightDao;
//import biz.mercue.impactweb.model.Highlight;
//import biz.mercue.impactweb.model.ImageCollection;
//import biz.mercue.impactweb.util.*;
//import org.apache.commons.io.FilenameUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@Transactional
//public class HighlightServiceImpl implements HighlightService {
//    @Autowired
//    private HighlightDao highlightDao;
//
//    @Override
//    public void create(Highlight highlight, MultipartFile[] files) {
//        highlight.setHighlight_id(KeyGeneratorUtils.generateRandomString());
//        multipartFile(highlight, files);
//        highlight.setImageCollectionList(highlight.getImageCollectionList());
//        highlightDao.create(highlight);
//    }
//
//    @Override
//    public Highlight getById(String highlightId) throws CustomException.CanNotFindDataException {
//        Highlight dbHighlight = highlightDao.getById(highlightId);
//
//        if (dbHighlight == null) {
//            throw new CustomException.CanNotFindDataException("highlight_id: " + highlightId);
//        }
//
//        dbHighlight.getImageCollectionList().size();
//        return dbHighlight;
//    }
//
//    @Override
//    public void update(Highlight highlight, MultipartFile[] files) {
//        String highlightId = highlight.getHighlight_id();
//        Highlight dbHighlight = highlightDao.getById(highlightId);
//
//        dbHighlight.setHighlight_title(highlight.getHighlight_title());
//        dbHighlight.setHighlight_subtitle(highlight.getHighlight_subtitle());
//
//        multipartFile(highlight, files);
//
//        // one to many
//        handleImageCollection(dbHighlight, highlight);
//    }
//
//    @Override
//    public void delete(Highlight highlight) {
//        highlightDao.deleteHighlight(highlight);
//    }
//
//    // private method
//    private void multipartFile(Highlight highlight, MultipartFile[] files) {
////        List<ImageCollection> imageCollectionList = new ArrayList<>();
////        for (MultipartFile file : files) {
////            if (file == null || StringUtils.isNULL(file.getOriginalFilename())) continue;
////
////            String imageId = KeyGeneratorUtils.generateRandomString();
////            ImageCollection imageCollection = new ImageCollection();
////            imageCollection.setImage_id(imageId);
////            String imageName = file.getOriginalFilename();
////            String extendName = FilenameUtils.getExtension(imageName);
////            String finalFileName = imageId + "." + extendName;
////            File finalFile = new File(Constants.IMAGE_UPLOAD_PATH + File.separator + finalFileName);
////            if (ImageUtils.writeFile(file, finalFile)) imageCollection.setImage_file(finalFileName);
////
////            imageCollection.setHighlight(highlight);
////            imageCollectionList.add(imageCollection);
////        }
////
////        highlight.setImageCollectionList(imageCollectionList);
//    }
//
//    private void handleImageCollection(Highlight dbHighlight, Highlight editHighlight) {
//        if (editHighlight.getImageCollectionList() != null && !editHighlight.getImageCollectionList().isEmpty()) {
//            List<ImageCollection> imageCollectionList = editHighlight.getImageCollectionList();
//            for (ImageCollection imageCollection : imageCollectionList) {
//                imageCollection.setHighlight(dbHighlight);
//            }
//            highlightDao.deleteImageCollection(dbHighlight.getHighlight_id());
//            dbHighlight.setImageCollectionList(editHighlight.getImageCollectionList());
//        } else {
//            if (dbHighlight.getImageCollectionList() != null) {
//                highlightDao.deleteImageCollection(dbHighlight.getHighlight_id());
//            }
//        }
//    }
//}
