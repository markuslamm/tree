/**
 * 
 */
package neo4j.tree.domain;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.NotEmpty;
import org.neo4j.graphdb.Direction;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Domain class, represents a TreeNode that is stored in the graph database.
 * 
 * @author Markus Lamm
 */
@TypeAlias("TreeNode")
public class Neo4jNode extends AbstractGraphEntity implements TreeNode<Neo4jNode>
{
    @Indexed
    @NotEmpty(message = "{TreeNode.name.NotEmpty}")
    private String name;

    @Fetch
    @RelatedTo(type = "IS_PARENT_OF", direction = Direction.INCOMING)
    private Neo4jNode parent;

    @Fetch
    @RelatedTo(type = "IS_PARENT_OF")
    private Set<Neo4jNode> children = null;

    private int depth = -1;

    @Transient
    private String depthString = "";

    public Neo4jNode() {
    }

    public Neo4jNode(final String name) {
        this.name = name;
    }

    /**
     * Recursively traverses the tree and adds subnodes in depth-first order.
     * 
     * @param node starting point of traversal
     * @param list result container
     */
    private void traverse(Neo4jNode node, List<Neo4jNode> list) {
        list.add(node);
        for (Neo4jNode data : node.getChildren()) {
            traverse(data, list);
        }
    }

    /**
     * Get subnotes in in depth-first order.
     * 
     * @return result container
     */
    public List<Neo4jNode> preOrder() {
        List<Neo4jNode> list = Lists.newArrayList();
        traverse(this, list);
        return list;
    }

    /**
     * For layouting purposes, actually unneccessary
     * 
     * @return layout string
     */
    public String getDepthString() {
        StringBuilder result = new StringBuilder("");
        if (getDepth() > -1) {
            for (int i = 0; i < getDepth() + 1; i++) {
                result = result.append("*");
            }
        }
        return result.toString();
    }

    /**
     * @see neo4j.tree.domain.TreeNode#getChildren()
     */
    @Override
    public Set<Neo4jNode> getChildren() {
        if (null == children) {
            return Sets.newHashSet();
        }
        return children;
    }

    /**
     * @see neo4j.tree.domain.TreeNode#getParent()
     */
    @Override
    public Neo4jNode getParent() {
        return parent;
    }

    /**
     * @see neo4j.tree.domain.TreeNode#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @see neo4j.tree.domain.TreeNode#isLeaf()
     */
    @Override
    public boolean isLeaf() {
        final boolean result = getChildren().size() < 1;
        return result;
    }

    /**
     * @see neo4j.tree.domain.TreeNode#isRoot()
     */
    @Override
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * @see neo4j.tree.domain.TreeNode#addChild(neo4j.tree.domain.TreeNode)
     */
    @Override
    public Neo4jNode addChild(final Neo4jNode node) {
        if (null == children) {
            children = Sets.newHashSet();
        }
        node.setParent(this);
        node.setDepth(getDepth() + 1);
        children.add(node);
        return this;
    }

    /**
     * @see neo4j.tree.domain.TreeNode#removeChild(neo4j.tree.domain.TreeNode)
     */
    @Override
    public Neo4jNode removeChild(final Neo4jNode node) {
        if (children != null && children.contains(node)) {
            children.remove(node);
        }
        return this;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setParent(final Neo4jNode parent) {
        this.parent = parent;
    }

    public void setChildren(final Set<Neo4jNode> children) {
        this.children = children;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(final int depth) {
        this.depth = depth;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Neo4jNode other = (Neo4jNode) obj;
        return new EqualsBuilder().appendSuper(super.equals(other)).append(getName(), other.getName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(getName()).toHashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TreeNode[");
        sb.append(super.toString()).append("name=").append(getName()).append(", parent=");
        String parentName = "null";
        if (null != getParent()) {
            parentName = getParent().getName();
        }
        sb.append(parentName).append(", children=").append(getChildren().size()).append("]");
        return sb.toString();
    }
}
