package com.flexicore.sendgrid.data;


import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.sendgrid.model.*;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.BasicRepository;
import com.wizzdi.flexicore.security.data.SecuredBasicRepository;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Extension
@Component
public class SendGridTemplateRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecuredBasicRepository securedBasicRepository;

	public List<SendGridTemplate> getAllSendGridTemplates(SendGridTemplateFiltering filtering, SecurityContextBase securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SendGridTemplate> q = cb.createQuery(SendGridTemplate.class);
		Root<SendGridTemplate> r = q.from(SendGridTemplate.class);
		List<Predicate> preds = new ArrayList<>();
		addSendGridTemplateFiltering(filtering, cb,q, r, preds,securityContext);
		q.select(r).where(preds.toArray(new Predicate[0])).orderBy(cb.asc(r.get(SendGridServer_.name)));
		TypedQuery<SendGridTemplate> query=em.createQuery(q);
		BasicRepository.addPagination(filtering,query);
		return query.getResultList();

	}

	public <T extends SendGridTemplate> void addSendGridTemplateFiltering(SendGridTemplateFiltering filtering, CriteriaBuilder cb,CommonAbstractCriteria q, From<?,T> r, List<Predicate> preds,SecurityContextBase securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(filtering.getBasicPropertiesFilter(), cb,q,r,preds,securityContext);
		if(filtering.getExternalTemplateIds()!=null && !filtering.getExternalTemplateIds().isEmpty()){
			Set<String> ids=filtering.getExternalTemplateIds();
			preds.add(r.get(SendGridTemplate_.externalId).in(ids));
		}
		if(filtering.getSendGridServers()!=null && !filtering.getSendGridServers().isEmpty()){
			Set<String> ids=filtering.getSendGridServers().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<T, SendGridServer> join=r.join(SendGridTemplate_.sendGridServer);
			preds.add(join.get(SendGridServer_.id).in(ids));
		}
	}

	public long countAllSendGridTemplates(SendGridTemplateFiltering filtering, SecurityContextBase securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<SendGridTemplate> r = q.from(SendGridTemplate.class);
		List<Predicate> preds = new ArrayList<>();
		addSendGridTemplateFiltering(filtering, cb,q, r, preds,securityContext);
		q.select(cb.count(r)).where(preds.toArray(new Predicate[0])).orderBy(cb.asc(r.get(SendGridServer_.name)));
		TypedQuery<Long> query=em.createQuery(q);
		BasicRepository.addPagination(filtering,query);
		return query.getSingleResult();

	}


	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return securedBasicRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return securedBasicRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return securedBasicRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return securedBasicRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return securedBasicRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return securedBasicRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return securedBasicRepository.findByIdOrNull(type, id);
	}

	@Transactional
	public void merge(Object base) {
		securedBasicRepository.merge(base);
	}

	@Transactional
	public void massMerge(List<?> toMerge) {
		securedBasicRepository.massMerge(toMerge);
	}
}
