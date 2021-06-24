package biz.mercue.impactweb.dao;

import biz.mercue.impactweb.model.Downloads;
import biz.mercue.impactweb.util.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("downloadsDao")
public class DownloadsDaoImpl extends AbstractDao<String, Downloads> implements DownloadsDao {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    public void createDownloads(Downloads bean) {
        persist(bean);
    }

    @Override
    public Downloads getById(String id) {
        return getByKey(id);
    }

    @Override
    public Downloads getByName(String name) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("downloads_name", name));
        return (Downloads) criteria.uniqueResult();
    }

    @Override
    @SuppressWarnings("Duplicates")
    public List<Downloads> getDownloadsList(String categoryId, String text, int page, int pageSize) {
        String hql = "select d from Downloads as d join d.category as dc ";
        if (!StringUtils.isNULL(categoryId)) {
            hql += "where dc.category_id = :categoryId ";
            if (!StringUtils.isNULL(text)) {
                hql += "and d.downloads_title like :text ";
            }
        } else {
            if (!StringUtils.isNULL(text)) {
                hql += "where d.downloads_title like :text ";
            }
        }
        hql += "order by d.downloads_order asc";
        Session session = getSession();
        Query query = session.createQuery(hql);
        if (!StringUtils.isNULL(categoryId)) {
            query.setParameter("categoryId", categoryId);
        }
        if (!StringUtils.isNULL(text)) {
            query.setParameter("text", "%" + text + "%");
        }
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.list();
    }

    @Override
    @SuppressWarnings("Duplicates")
    public int getDownloadsListCount(String categoryId, String text) {
        String hql = "select count(distinct d) from Downloads as d join d.category as dc ";
        if (!StringUtils.isNULL(categoryId)) {
            hql += "where dc.category_id = :categoryId ";
            if (!StringUtils.isNULL(text)) {
                hql += "and d.downloads_title like :text ";
            }
        } else {
            if (!StringUtils.isNULL(text)) {
                hql += "where d.downloads_title like :text ";
            }
        }
        hql += "order by d.downloads_order asc";
        Session session = getSession();
        Query query = session.createQuery(hql);
        if (!StringUtils.isNULL(categoryId)) {
            query.setParameter("categoryId", categoryId);
        }
        if (!StringUtils.isNULL(text)) {
            query.setParameter("text", "%" + text + "%");
        }
        long count = (long) query.uniqueResult();
        return (int) count;
    }

    @Override
    public List<Downloads> getAll() {
        Criteria criteria = createEntityCriteria();
        criteria.addOrder(Order.asc("downloads_order"));
        return criteria.list();
    }

    @Override
    public List<Downloads> getAvailable() {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("available", true));
        criteria.addOrder(Order.asc("downloads_order"));
        return criteria.list();
    }

    @Override
    public int getDownloadsCount() {
        Criteria crit = createEntityCriteria();
        crit.setProjection(Projections.rowCount());
        long count = (long) crit.uniqueResult();
        return (int) count;
    }

    @Override
    public void deleteDownloads(Downloads bean) {
        delete(bean);
    }
}
