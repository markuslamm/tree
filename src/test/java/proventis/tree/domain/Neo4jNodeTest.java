/**
 * 
 */
package proventis.tree.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import neo4j.tree.domain.Neo4jNode;

import org.junit.Test;

/**
 * @author Markus Lamm
 * 
 */
public class Neo4jNodeTest
{
    private static final Long NODE_ID = Long.valueOf(123L);
    private static final String UUID = "046b6c7f-0b8a-43b9-b35d-6489e6daee91";
    private static final String NAME = "nodeXYZ";

    private static Neo4jNode createNode(String name) {
        final Neo4jNode node = new Neo4jNode();
        node.setName(name);
        return node;
    }

    @Test
    public void testInititializing() {
        final Neo4jNode root = createNode("root");
        assertThat(root.getParent(), is(nullValue()));
        assertThat(root.getChildren(), is(empty()));
    }

    @Test
    public void testAddChild() {
        Neo4jNode root = createNode("root");
        Neo4jNode node_1 = createNode("Node_1");
        Neo4jNode node_2 = createNode("Node_2");
        root = root.addChild(node_1);
        root = root.addChild(node_2);
        assertThat(root.getChildren(), not(empty()));
        assertThat(root.getChildren().size(), is(2));
    }

    @Test
    public void testRemoveChild() {
        Neo4jNode root = createNode("root");
        Neo4jNode node_1 = createNode("Node_1");
        Neo4jNode node_2 = createNode("Node_2");
        root = root.addChild(node_1);
        root = root.addChild(node_2);
        root = root.removeChild(node_1);
        assertThat(root.getChildren(), not(hasItem(node_1)));
        assertThat(root.getChildren(), hasItem(node_2));
        root = root.removeChild(node_2);
        assertThat(root.getChildren(), not(hasItem(node_2)));
    }

    @Test
    public void testGetParent() {
        Neo4jNode root = createNode("root");
        Neo4jNode node_1 = createNode("Node_1");
        root = root.addChild(node_1);
        assertThat(root, is(node_1.getParent()));
        Neo4jNode node_1_1 = createNode("Node_1_1");
        node_1 = node_1.addChild(node_1_1);
        assertThat(node_1, is(node_1_1.getParent()));
    }

    @Test
    public void testGetChildren() {
        Neo4jNode root = createNode("root");
        Neo4jNode node_1 = createNode("Node_1");
        root = root.addChild(node_1);
        assertThat(root.getChildren(), hasItem(node_1));
        assertThat(node_1.getChildren(), is(empty()));
    }

    @Test
    public void testIsRoot() {
        Neo4jNode root = createNode("root");
        assertThat(root.isRoot(), is(true));
        Neo4jNode node_1 = createNode("Node_1");
        root = root.addChild(node_1);
        assertThat(node_1.isRoot(), is(false));
    }

    @Test
    public void testIsLeaf() {
        Neo4jNode root = createNode("root");
        assertThat(root.isLeaf(), is(true));
        Neo4jNode node_1 = createNode("Node_1");
        root = root.addChild(node_1);
        assertThat(root.isLeaf(), is(false));
        assertThat(node_1.isLeaf(), is(true));
    }

    @Test
    public void testEquals() {
        final Neo4jNode node1 = new Neo4jNode("node111");
        final Neo4jNode node2 = new Neo4jNode("node111");
        assertThat(Boolean.valueOf(node1.equals(node2)), is(Boolean.TRUE));

        node1.setNodeId(NODE_ID);
        assertThat(Boolean.valueOf(node1.equals(node2)), is(Boolean.FALSE));
        node2.setNodeId(NODE_ID);
        assertThat(Boolean.valueOf(node1.equals(node2)), is(Boolean.TRUE));

        node1.setUuid(UUID);
        assertThat(Boolean.valueOf(node1.equals(node2)), is(Boolean.FALSE));
        node2.setUuid(UUID);
        assertThat(Boolean.valueOf(node1.equals(node2)), is(Boolean.TRUE));

        node1.setName(NAME);
        assertThat(Boolean.valueOf(node1.equals(node2)), is(Boolean.FALSE));
        node2.setName(NAME);
        assertThat(Boolean.valueOf(node1.equals(node2)), is(Boolean.TRUE));
    }

    @Test
    public void testHashCode() {
        final Neo4jNode node1 = new Neo4jNode("node111");
        final Neo4jNode node2 = new Neo4jNode("node111");
        assertThat(Integer.valueOf(node1.hashCode()), is(Integer.valueOf(node2.hashCode())));

        node1.setNodeId(NODE_ID);
        assertThat(Integer.valueOf(node1.hashCode()), not(Integer.valueOf(node2.hashCode())));
        node2.setNodeId(NODE_ID);
        assertThat(Integer.valueOf(node1.hashCode()), is(Integer.valueOf(node2.hashCode())));

        node1.setUuid(UUID);
        assertThat(Integer.valueOf(node1.hashCode()), not(Integer.valueOf(node2.hashCode())));
        node2.setUuid(UUID);
        assertThat(Integer.valueOf(node1.hashCode()), is(Integer.valueOf(node2.hashCode())));

        node1.setName(NAME);
        assertThat(Integer.valueOf(node1.hashCode()), not(Integer.valueOf(node2.hashCode())));
        node2.setName(NAME);
        assertThat(Integer.valueOf(node1.hashCode()), is(Integer.valueOf(node2.hashCode())));
    }
}
