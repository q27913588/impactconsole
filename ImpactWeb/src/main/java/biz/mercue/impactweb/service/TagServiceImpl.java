package biz.mercue.impactweb.service;

import biz.mercue.impactweb.dao.AbstractDao;
import biz.mercue.impactweb.model.Tag;
import org.hibernate.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("tagService")
@Transactional
public class TagServiceImpl extends AbstractDao<String, Tag> implements TagService {
	@Override
	public List<Tag> getTagList() {
		Criteria criteria = createEntityCriteria();
		return criteria.list();
	}
}
