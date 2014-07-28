/**
 * 
 */
package proventis.tree;

import javax.inject.Inject;

import neo4j.tree.config.ApplicationConfig;

import org.junit.runner.RunWith;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;

import proventis.tree.config.Neo4jTestConfig;

/**
 * @author Markus Lamm
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { ApplicationConfig.class, Neo4jTestConfig.class })
@TransactionConfiguration(transactionManager = "neo4jTransactionManager", defaultRollback = true)
public abstract class AbstractIntegrationTest
{
    @Inject
    private Neo4jTemplate neo4jTemplate;

    protected Neo4jTemplate getNeo4jTemplate() {
        return neo4jTemplate;
    }
}
