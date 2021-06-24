package biz.mercue.impactweb.dao;

import biz.mercue.impactweb.model.News;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository("newsDao")
public class NewsDaoImpl extends AbstractDao<String, News> implements NewsDao {

    private Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    public News getById(String id) {

        return getByKey(id);
    }

    @Override
    public void createNews(News bean) {
        persist(bean);
    }

    @Override
    public void deleteNews(News bean) {
        delete(bean);
    }

    @Override
    public List<News> getNewsList(int page, int pageSize) {
        Criteria criteria = createEntityCriteria();
        criteria.addOrder(Order.desc("published_date"));
        criteria.setFirstResult((page - 1) * pageSize);
        criteria.setMaxResults(pageSize);
        return criteria.list();
    }

    @Override
    public int getCountNewsList() {
        Criteria criteria = createEntityCriteria();
        criteria.setProjection(Projections.rowCount());
        long count = (long) criteria.uniqueResult();
        return (int) count;
    }

    @Override
    public List<News> getNewsListByTag(String tagId, int page, int pageSize) {
        String hql = "select n from News n join n.listTag as nt where nt.tag_id = :id order by n.published_date desc";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setParameter("id", tagId);
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.list();
    }

    @Override
    public int getCountNewsListByTag(String tagId) {
        String hql = "select count(distinct n) from News n join n.listTag as nt where nt.tag_id = :id";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setParameter("id", tagId);
        long count = (long) query.uniqueResult();
        return (int) count;
    }

    @Override
    public List<News> getNewsListFrontEnd(int page, int pageSize) {
        Criteria criteria = createEntityCriteria();

        criteria.add(Restrictions.eq("available", true));
        criteria.add(Restrictions.le("published_date", new Date()));
        criteria.addOrder(Order.desc("published_date"));
        criteria.setFirstResult((page - 1) * pageSize);
        criteria.setMaxResults(pageSize);
        return criteria.list();
    }

    @Override
    public int getCountNewsListFrontEnd() {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("available", true));
        criteria.add(Restrictions.le("published_date", new Date()));
        criteria.addOrder(Order.desc("published_date"));
        criteria.setProjection(Projections.rowCount());
        long count = (long) criteria.uniqueResult();
        return (int) count;
    }

    @Override
    public List<News> getNewsListBackEnd(int page, int pageSize) {
        Criteria criteria = createEntityCriteria();
        criteria.addOrder(Order.desc("published_date"));
        criteria.setFirstResult((page - 1) * pageSize);
        criteria.setMaxResults(pageSize);
        return criteria.list();
    }

    @Override
    public int getCountNewsListBackEnd() {
        Criteria criteria = createEntityCriteria();
        criteria.setProjection(Projections.rowCount());
        long count = (long) criteria.uniqueResult();
        return (int) count;
    }

    @Override
    public List<News> getAll() {
        Criteria criteria = createEntityCriteria();
        criteria.addOrder(Order.desc("news_order"));
        return criteria.list();
    }

    @Override
    public List<News> getAvailable() {
        Criteria criteria = createEntityCriteria();
        criteria.addOrder(Order.desc("published_date"));
        criteria.add(Restrictions.eq("available", true));
        return criteria.list();
    }

    @Override
    public int getNewsCount() {
        Criteria criteria = createEntityCriteria();
        criteria.setProjection(Projections.rowCount());
        long count = (long) criteria.uniqueResult();
        return (int) count;
    }

    @Override
    public void deleteAttachment(String newsId) {
        String hql = "Delete From Attachment a where a.news.news_id = :newsId";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setParameter("newsId", newsId);
        query.executeUpdate();
    }

    @Override
    public void deleteImageCollection(String newsId) {
        String hql = "Delete From ImageCollection ic where ic.news.news_id = :newsId";
        Session session = getSession();
        Query query = session.createQuery(hql);
        query.setParameter("newsId", newsId);
        query.executeUpdate();
    }
}
