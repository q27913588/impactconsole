//package biz.mercue.impactweb.dao;
//
//import biz.mercue.impactweb.model.Highlight;
//import org.hibernate.Session;
//import org.hibernate.query.Query;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class HighlightDaoImpl extends AbstractDao<String, Highlight> implements HighlightDao {
//
//    @Override
//    public Highlight getById(String highlightId) {
//        return getByKey(highlightId);
//    }
//
//    @Override
//    public void create(Highlight highlight) {
//        persist(highlight);
//    }
//
//    @Override
//    public void deleteHighlight(Highlight highlight) {
//        delete(highlight);
//    }
//
//    @Override
//    public void deleteImageCollection(String highlightId) {
//        String hql = "Delete From ImageCollection image where image.highlight.highlight_id = :highlightId";
//        Session session = getSession();
//        Query query = session.createQuery(hql);
//        query.setParameter("highlightId", highlightId);
//        query.executeUpdate();
//    }
//}
