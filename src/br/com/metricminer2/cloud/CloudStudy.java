package br.com.metricminer2.cloud;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import br.com.metricminer2.RepositoryMining;
import br.com.metricminer2.Study;
import br.com.metricminer2.metric.ClassLevelMetricCalculator;
import br.com.metricminer2.metric.java8.loc.ClassLevelLinesOfCodeFactory;
import br.com.metricminer2.persistence.PersistenceMechanism;
import br.com.metricminer2.persistence.google.GoogleStorage;
import br.com.metricminer2.scm.GitRepository;
import br.com.metricminer2.scm.commitrange.Commits;

public class CloudStudy implements Study {

	@Override
	public void execute() {
		String projects = System.getenv("PROJECTS");
		String storage = System.getenv("STORAGE");
		ArrayList<String> range = new ArrayList<String>(Arrays.asList(System.getenv("RANGE").split(",")));

		PersistenceMechanism pm = new GoogleStorage(new File("google_storage"), storage);
		
		new RepositoryMining()
			.in(GitRepository.allProjectsIn(projects))
			.through(Commits.range(range))
			.process(new ClassLevelMetricCalculator(new ClassLevelLinesOfCodeFactory()), pm)
			.mine();
	}
}
