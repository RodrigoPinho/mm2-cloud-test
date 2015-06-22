package br.com.metricminer2.cloud;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import br.com.metricminer2.MetricMiner2;
import br.com.metricminer2.RepositoryMining;
import br.com.metricminer2.Study;
import br.com.metricminer2.metric.ClassLevelMetricCalculator;
import br.com.metricminer2.metric.java8.loc.ClassLevelLinesOfCodeFactory;
import br.com.metricminer2.persistence.PersistenceMechanism;
import br.com.metricminer2.persistence.google.GoogleStorage;
import br.com.metricminer2.scm.GitRepository;
import br.com.metricminer2.scm.commitrange.Commits;

public class CloudStudy implements Study {

	public static void main(String[] args) {
		new MetricMiner2().start(new CloudStudy());
	} 

	@Override
	public void execute() {
		String applicationName = System.getenv("APPLICATION_NAME");
		String bucketName = System.getenv("BUCKET_NAME");
		String clientSecretFileName = System.getenv("CLIENTE_SECRET_FILE_NAME");
		String fileName = System.getenv("FILE_NAME");
		File dataStoreDir = new File(System.getenv("DATA_STORE_DIR"));
		String project = System.getenv("PROJECT");
		ArrayList<String> range = new ArrayList<String>(Arrays.asList(System.getenv("RANGE").split(",")));
		

		PersistenceMechanism pm = new GoogleStorage(applicationName,
				bucketName, clientSecretFileName, true,
				fileName, dataStoreDir);

		new RepositoryMining()
				.in(GitRepository.singleProject(project))
				.through(Commits.range(range))
				.process(
						new ClassLevelMetricCalculator(
								new ClassLevelLinesOfCodeFactory()), pm).mine();
	}
}

