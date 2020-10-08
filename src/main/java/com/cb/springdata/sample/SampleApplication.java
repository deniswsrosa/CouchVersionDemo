package com.cb.springdata.sample;

import com.cb.springdata.sample.config.CouchbaseConfig;
import com.github.couchversion.CouchVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
public class SampleApplication {

	@Autowired
	private CouchbaseConfig couchbaseConfig;

    @Autowired
	private ApplicationContext context;

	@Bean
	public CouchVersion couchVersions(){
		CouchVersion runner = new CouchVersion(couchbaseConfig.getConnectionString(),
				couchbaseConfig.getBucketName(), couchbaseConfig.getUserName(), couchbaseConfig.getPassword());
		//if you don't set the application context, your migrations can't be autowired
		runner.setApplicationContext(context);
		runner.setChangeLogsScanPackage(
				"com.cb.springdata.sample.migration"); // the package to be scanned for changesets
		return runner;
	}


	public static void main(String[] args) throws IOException {
		SpringApplication.run(SampleApplication.class, args);
	}

}
