/**
 * 
 */
package proventis.tree.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.inject.Inject;
import javax.validation.ValidationException;

import neo4j.tree.domain.Neo4jNode;
import neo4j.tree.repository.TreeNodeRepository;

import org.junit.Test;
import org.neo4j.graphdb.Transaction;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;

import proventis.tree.AbstractIntegrationTest;

import com.google.common.collect.Lists;

/**
 * @author Markus Lamm
 * 
 */
public class TreeNodeRepositoryIntegrationTest extends AbstractIntegrationTest
{
    @Inject
    private TreeNodeRepository repository;

    @Inject
    private Neo4jTemplate neo4jTemplate;

    private void createTestTree() {
        try (final Transaction tx = neo4jTemplate.getGraphDatabaseService().beginTx()) {
            Neo4jNode nodeRoot = createNode("root");
            Neo4jNode node_1 = createNode("node_1");
            nodeRoot = nodeRoot.addChild(node_1);
            Neo4jNode node_1_1 = createNode("node_1_1");
            Neo4jNode node_1_2 = createNode("node_1_2");
            Neo4jNode node_1_3 = createNode("node_1_3");
            Neo4jNode node_1_4 = createNode("node_1_4");
            node_1 = node_1.addChild(node_1_1);
            node_1 = node_1.addChild(node_1_2);
            node_1 = node_1.addChild(node_1_3);
            node_1 = node_1.addChild(node_1_4);
            nodeRoot = neo4jTemplate.save(nodeRoot);
            tx.success();
        }
    }

    private List<Neo4jNode> getExistingNodes() {
        return Lists.newArrayList(getNeo4jTemplate().findAll(Neo4jNode.class));
    }

    @Test
    @Transactional
    public void testFindByUuid() {
        createTestTree();
        final Neo4jNode node = getExistingNodes().get(0);
        final Neo4jNode result = repository.findByUuid(node.getUuid());
        assertThat(result, is(node));
    }

    @Test
    @Transactional
    public void testFindByUuid_NotFound() {
        createTestTree();
        final String invalidUuid = "67h4c7n9z43432049";
        final Neo4jNode result = repository.findByUuid(invalidUuid);
        assertThat(result, is(nullValue()));
    }

    @Test
    @Transactional
    public void testFindByName() {
        createTestTree();
        final Neo4jNode existing = getExistingNodes().get(0);
        final List<Neo4jNode> result = Lists.newArrayList(repository.findByName(existing.getName()));
        assertThat(result, hasItem(existing));
    }

    @Test
    @Transactional
    public void testFindByName_NotFound() {
        createTestTree();
        final String invalidName = "Unknown name";
        final List<Neo4jNode> result = Lists.newArrayList(repository.findByName(invalidName));
        assertThat(result, is(empty()));
    }

    @Test
    @Transactional
    public void testFindByParent() {
        Neo4jNode root = createNode("root");
        final Neo4jNode node_1 = createNode("node_1");
        final Neo4jNode node_2 = createNode("node_2");
        root = root.addChild(node_1);
        root = root.addChild(node_2);
        root = repository.save(root);
        final List<Neo4jNode> result = Lists.newArrayList(repository.findByParent(root));
        assertThat(result, hasItems(node_1, node_2));
    }

    @Test
    @Transactional
    public void testFindRootNode() {
        Neo4jNode root = createNode("root");
        root.setDepth(0);
        final Neo4jNode node_1 = createNode("node_1");
        final Neo4jNode node_2 = createNode("node_2");
        root = root.addChild(node_1);
        root = root.addChild(node_2);
        root = repository.save(root);
        Neo4jNode result = repository.findRootNode();
        assertThat(result, is(root));
    }

    @Test
    @Transactional
    public void testSave() {
        Neo4jNode node = new Neo4jNode("root");
        node = repository.save(node);
        assertThat(node.getNodeId(), is(notNullValue()));
        assertThat(node.getUuid(), is(notNullValue()));
    }

    @Test
    @Transactional
    public void testDelete() {
        createTestTree();
        List<Neo4jNode> existing = getExistingNodes();
        final Neo4jNode node = existing.get(0);
        int count = existing.size();
        repository.delete(node);
        existing = getExistingNodes();
        assertThat(existing.size(), is(--count));
    }

    @Test
    @Transactional
    public void testEntityIsInResultsAfterSave() {
        Neo4jNode root = createNode("root");
        root = repository.save(root);
        assertThat(getExistingNodes(), hasItem(root));
    }

    private static Neo4jNode createNode(String title) {
        return new Neo4jNode(title);
    }

    @Test(expected = ValidationException.class)
    @Transactional
    public void testNodeNameIsEmpty_Fail() {
        Neo4jNode node = createNode("root");
        node.setName("");
        node = repository.save(node);
    }

    @Test(expected = ValidationException.class)
    @Transactional
    public void testNodeNameIsNull_Fail() {
        Neo4jNode node = createNode("root");
        node.setName("");
        node = repository.save(node);
    }
}
