/**
 * 
 */
package neo4j.tree.config;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring configuration for Neo4j persistence
 * 
 * @author Markus Lamm
 */
@Configuration
@EnableTransactionManagement
@EnableNeo4jRepositories({ "neo4j.tree.repository" })
@PropertySource("classpath:graph.properties")
public class Neo4jConfig extends Neo4jConfiguration
{
    private static final Logger LOG = LoggerFactory.getLogger(Neo4jConfig.class);

    // TODO
    // env is always null here, therefore hardcoded strings here
    // @Inject
    // private Environment env;

    private static final String GRAPH_ENTITY_BASEPACKAGE = "neo4j.tree.domain";
    private static final String EMBEDDED_GRAPH_PATH = "target/graphdb";

    public Neo4jConfig() {
        setBasePackage(GRAPH_ENTITY_BASEPACKAGE);
    }

    /**
     * Creates an neo4j embedded database instance
     * 
     * @return graph database instance
     */
    @Bean(name = "graphDatabaseService", destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        final GraphDatabaseService service = new
                GraphDatabaseFactory().newEmbeddedDatabase(EMBEDDED_GRAPH_PATH);
        LOG.debug("Embedded test graphdatabase created: [{}]", service.toString());
        return service;
        // final GraphDatabaseService service = new
        // SpringRestGraphDatabase("http://localhost:7474/db/data");
        // LOG.debug("Rest stage graphdatabase created: [{}]", service.toString());
        // return service;
    }
}
