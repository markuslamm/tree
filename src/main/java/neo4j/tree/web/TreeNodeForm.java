/**
 * 
 */
package neo4j.tree.web;

import java.util.Set;

import neo4j.tree.domain.Neo4jNode;

import org.hibernate.validator.constraints.NotEmpty;


import com.google.common.collect.Sets;

/**
 * @author Markus Lamm
 * 
 */
public class TreeNodeForm
{

    private Long nodeId;
    private String uuid;
    private String createdDate;
    private String lastModifiedDate;

    @NotEmpty(message = "TreeNode.name.NotEmpty")
    private String name;

    private Set<Neo4jNode> children = Sets.newHashSet();
    private Neo4jNode parent = null;

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Set<Neo4jNode> getChildren() {
        return children;
    }

    public void setChildren(final Set<Neo4jNode> children) {
        this.children = children;
    }

    public Neo4jNode getParent() {
        return parent;
    }

    public void setParent(final Neo4jNode parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TreeNodeForm[");
        sb.append(super.toString()).append("name=").append(getName()).append("]");
        return sb.toString();
    }
}
