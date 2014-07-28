/**
 * 
 */
package proventis.tree.service;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.inject.Inject;

import neo4j.tree.domain.Neo4jNode;
import neo4j.tree.service.TreeService;
import neo4j.tree.web.TreeNodeForm;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import proventis.tree.AbstractIntegrationTest;

import com.google.common.collect.Lists;

/**
 * @author Markus Lamm
 * 
 */
public class TreeNodeServiceIntegrationTest extends AbstractIntegrationTest
{
    @Inject
    private TreeService service;

    @Test
    @Transactional
    public void testFindByUuid() {
        createTestTree();
        final Neo4jNode node = getExistingNodes().get(0);
        final Neo4jNode result = service.findByUuid(node.getUuid());
        assertThat(result, is(node));
    }

    @Test
    @Transactional
    public void testFindAll() {
        createTestTree();
        final List<Neo4jNode> result = service.findAll();
        assertThat(result.size(), is(6));
    }

    @Test
    @Transactional
    public void testCreateTree() {
        final Neo4jNode node = service.createTree("root");
        assertThat(node.getUuid(), is(notNullValue()));
        assertThat(node.getNodeId(), is(notNullValue()));
        assertThat(node.getName(), is("root"));
        assertThat(node.getDepth(), is(0));
        assertThat(node.getChildren(), is(empty()));
    }

    @Test
    @Transactional
    public void testAddNode() {
        Neo4jNode root = createNode("root");
        root = getNeo4jTemplate().save(root);
        final String parentUuid = root.getUuid();
        Neo4jNode node1 = createNode("node1");
        root = service.addNode(node1, parentUuid);
        assertThat(root.getChildren(), hasItem(node1));
    }

    @Test
    @Transactional
    public void testFindByParent() {
        Neo4jNode root = createNode("root");
        Neo4jNode node1 = createNode("node1");
        Neo4jNode node2 = createNode("node2");
        root = root.addChild(node1);
        root = root.addChild(node2);
        root = getNeo4jTemplate().save(root);
        final List<Neo4jNode> result = service.findByParent(root);
        assertThat(result, hasItems(node1, node2));
    }

    @Test
    @Transactional
    public void testUpdateNode() {
        Neo4jNode root = createNode("root");
        root = getNeo4jTemplate().save(root);
        final TreeNodeForm form = new TreeNodeForm();
        form.setName("newRoot");
        root = service.updateNode(form, root.getUuid());
        assertThat(root.getName(), is("newRoot"));
    }

    @Test
    @Transactional
    public void testDeleteNode() {
        Neo4jNode root = createNode("root");
        root = getNeo4jTemplate().save(root);
        final String uuid = root.getUuid();
        int count = getExistingNodes().size();
        service.deleteNode(uuid);
        assertThat(getExistingNodes().size(), is(--count));
    }

    @Test
    @Transactional
    public void testGetRootNode() {
        Neo4jNode root = createNode("root");
        Neo4jNode node1 = createNode("node1");
        Neo4jNode node2 = createNode("node2");
        root = root.addChild(node1);
        root = root.addChild(node2);
        root = getNeo4jTemplate().save(root);
        assertThat(service.getRootNode(), is(root));
    }

    private List<Neo4jNode> getExistingNodes() {
        return Lists.newArrayList(getNeo4jTemplate().findAll(Neo4jNode.class));
    }

    private static Neo4jNode createNode(String title) {
        return new Neo4jNode(title);
    }

    private void createTestTree() {
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
        nodeRoot = getNeo4jTemplate().save(nodeRoot);
    }
}
