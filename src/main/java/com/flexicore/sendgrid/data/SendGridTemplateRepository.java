package com.flexicore.sendgrid.data;


import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.product.interfaces.IEquipmentRepository;
import com.flexicore.security.SecurityContext;
import com.flexicore.sendgrid.model.*;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
@Extension
@Component
public class SendGridTemplateRepository extends AbstractRepositoryPlugin {

	public List<SendGridTemplate> getAllSendGridTemplates(SendGridTemplateFiltering filtering, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SendGridTemplate> q = cb.createQuery(SendGridTemplate.class);
		Root<SendGridTemplate> r = q.from(SendGridTemplate.class);
		List<Predicate> preds = new ArrayList<>();
		addSendGridTemplateFiltering(filtering, cb, r, preds);
		QueryInformationHolder<SendGridTemplate> queryInformationHolder = new QueryInformationHolder<>(filtering, SendGridTemplate.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);

	}

	public void addSendGridTemplateFiltering(SendGridTemplateFiltering filtering, CriteriaBuilder cb, Root<SendGridTemplate> r, List<Predicate> preds) {
		if(filtering.getExternalTemplateIds()!=null && !filtering.getExternalTemplateIds().isEmpty()){
			Set<String> ids=filtering.getExternalTemplateIds();
			preds.add(r.get(SendGridTemplate_.externalId).in(ids));
		}
		if(filtering.getSendGridServers()!=null && !filtering.getSendGridServers().isEmpty()){
			Set<String> ids=filtering.getSendGridServers().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<SendGridTemplate, SendGridServer> join=r.join(SendGridTemplate_.sendGridServer);
			preds.add(join.get(SendGridServer_.id).in(ids));
		}
	}

	public long countAllSendGridTemplates(SendGridTemplateFiltering filtering, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<SendGridTemplate> r = q.from(SendGridTemplate.class);
		List<Predicate> preds = new ArrayList<>();
		addSendGridTemplateFiltering(filtering, cb, r, preds);
		QueryInformationHolder<SendGridTemplate> queryInformationHolder = new QueryInformationHolder<>(filtering, SendGridTemplate.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);

	}

}
