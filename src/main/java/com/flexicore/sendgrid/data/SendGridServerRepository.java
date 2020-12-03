package com.flexicore.sendgrid.data;


import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.product.interfaces.IEquipmentRepository;
import com.flexicore.security.SecurityContext;
import com.flexicore.sendgrid.model.SendGridServer;
import com.flexicore.sendgrid.model.SendGridServerFiltering;
import com.flexicore.sendgrid.model.SendGridServer_;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
@Extension
@Component
public class SendGridServerRepository extends AbstractRepositoryPlugin {

	public List<SendGridServer> getAllSendGridServers(SendGridServerFiltering filtering, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SendGridServer> q = cb.createQuery(SendGridServer.class);
		Root<SendGridServer> r = q.from(SendGridServer.class);
		List<Predicate> preds = new ArrayList<>();
		addSendGridServerFiltering(filtering, cb, r, preds);
		QueryInformationHolder<SendGridServer> queryInformationHolder = new QueryInformationHolder<>(filtering, SendGridServer.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);

	}

	public void addSendGridServerFiltering(SendGridServerFiltering filtering, CriteriaBuilder cb, Root<SendGridServer> r, List<Predicate> preds) {
		IEquipmentRepository.addEquipmentFiltering(filtering, cb, r, preds);
		if(filtering.getApiKeys()!=null &&!filtering.getApiKeys().isEmpty()){
			Set<String> ids=filtering.getApiKeys().stream().map(f->f.getId()).collect(Collectors.toSet());
			preds.add(r.get(SendGridServer_.apiKey).in(ids));
		}
	}

	public long countAllSendGridServers(SendGridServerFiltering filtering, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<SendGridServer> r = q.from(SendGridServer.class);
		List<Predicate> preds = new ArrayList<>();
		addSendGridServerFiltering(filtering, cb, r, preds);
		QueryInformationHolder<SendGridServer> queryInformationHolder = new QueryInformationHolder<>(filtering, SendGridServer.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);

	}

}
