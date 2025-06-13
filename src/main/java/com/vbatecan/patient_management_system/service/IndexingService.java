package com.vbatecan.patient_management_system.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;

@Service
public class IndexingService {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void reindexAll() {
		SearchSession searchSession = Search.session(entityManager);
		searchSession.massIndexer().start();
	}
}