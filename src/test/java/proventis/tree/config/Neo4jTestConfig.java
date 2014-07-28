/**
 * 
 */
package proventis.tree.config;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Spring test configuration for Neo4j persistence
 * 
 * @author Markus Lamm
 */
@Configuration
@EnableTransactionManagement
@ComponentScan({ "neo4j.tree.event" })
@EnableNeo4jRepositories({ "neo4j.tree.repository" })
@PropertySource("classpath:test-graph.properties")
public class Neo4jTestConfig extends Neo4jConfiguration
{
    private static final Logger LOG = LoggerFactory.getLogger(Neo4jTestConfig.class);

    // TODO
    // env is always null here, therefore hardcoded strings here
    // @Inject
    // private Environment env;

    private static final String GRAPH_ENTITY_BASEPACKAGE = "neo4j.tree.domain";

    public Neo4jTestConfig() {
        setBasePackage(GRAPH_ENTITY_BASEPACKAGE);
    }

    /**
     * Creates an impermanent neo4j test database instance
     * 
     * @return
     */
    @Bean(name = "graphDatabaseService", destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        final GraphDatabaseService graphDB = new
                TestGraphDatabaseFactory().newImpermanentDatabase();
        LOG.debug("Impermanent test graphdatabase created: [{}]", graphDB.toString());
        return graphDB;
    }

    /**
     * Creates Validator instance
     * 
     * @return
     */
    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }
}
