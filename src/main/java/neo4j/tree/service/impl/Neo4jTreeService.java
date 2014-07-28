/**
 * 
 */
package neo4j.tree.service.impl;

import java.util.List;

import javax.inject.Inject;

import neo4j.tree.domain.Neo4jNode;
import neo4j.tree.repository.TreeNodeRepository;
import neo4j.tree.service.TreeService;
import neo4j.tree.web.TreeNodeForm;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.google.common.collect.Lists;

/**
 * Service implementation of TreeService interface
 * 
 * @author Markus Lamm
 */
@Service
@Transactional
public class Neo4jTreeService implements TreeService
{
    private static final Logger LOG = LoggerFactory.getLogger(Neo4jTreeService.class);

    private TreeNodeRepository repository;

    /**
     * @see neo4j.tree.service.TreeService#createTree(java.lang.String)
     */
    @Override
    public Neo4jNode createTree(final String rootName) {
        Neo4jNode rootNode = new Neo4jNode(rootName);
        rootNode.setDepth(0);
        rootNode = repository.save(rootNode);
        LOG.debug("Created new Tree. Root node: {}", rootNode.getName());
        return rootNode;
    }

    /**
     * @see neo4j.tree.service.TreeService#addNode(neo4j.tree.domain.Neo4jNode,
     * java.lang.String)
     */
    @Override
    public Neo4jNode addNode(Neo4jNode node, String parentUuid) {
        if (null == node) {
            throw new IllegalArgumentException("node is NULL. Unable to add new node");
        }
        if (StringUtils.isBlank(parentUuid)) {
            final String msg = String.format("parentUuid is [%s]. Unable to add new node", parentUuid);
            throw new IllegalArgumentException(msg);
        }
        Neo4jNode parentNode = repository.findByUuid(parentUuid);
        if (null == parentNode) {
            final String msg = String.format("No node for uuid [%s] found. Unable to add new node", parentUuid);
            throw new IllegalArgumentException(msg);
        }
        parentNode = parentNode.addChild(node);
        parentNode = repository.save(parentNode);
        LOG.debug("new node created: {}");
        return parentNode;
    }

    /**
     * @see neo4j.tree.service.TreeService#findAll()
     */
    @Override
    public List<Neo4jNode> findAll() {
        return Lists.newArrayList(repository.findAll());
    }

    /**
     * @see neo4j.tree.service.TreeService#findByUuid(java.lang.String)
     */
    @Override
    public Neo4jNode findByUuid(final String uuid) {
        return repository.findByUuid(uuid);
    }

    /**
     * @see neo4j.tree.service.TreeService#findByParent(neo4j.tree.domain.Neo4jNode)
     */
    @Override
    public List<Neo4jNode> findByParent(final Neo4jNode parent) {
        return Lists.newArrayList(repository.findByParent(parent));
    }

    /**
     * @see neo4j.tree.service.TreeService#updateNode(neo4j.tree.web.TreeNodeForm,
     * java.lang.String)
     */
    @Override
    public Neo4jNode updateNode(final TreeNodeForm form, final String nodeUuid) {
        if (null == form) {
            throw new IllegalArgumentException("form is NULL. Unable to update node");
        }
        if (StringUtils.isBlank(nodeUuid)) {
            final String msg = String.format("nodeUuid is [{}]. Unable to update node", nodeUuid);
            throw new IllegalArgumentException(msg);
        }
        Neo4jNode node = repository.findByUuid(nodeUuid);
        if (null == node) {
            final String msg = String.format("Invalid nodeUuid %s. No node to update found", nodeUuid);
            throw new IllegalArgumentException(msg);
        }
        node.setName(form.getName());
        node = repository.save(node);
        LOG.debug("Node updated: {}", node);
        return node;
    }

    /**
     * @see neo4j.tree.service.TreeService#deleteNode(java.lang.String)
     */
    @Override
    public void deleteNode(final String nodeUuid) {
        if (StringUtils.isBlank(nodeUuid)) {
            final String msg = String.format("nodeUuid is [{}]. Unable to delete node", nodeUuid);
            throw new IllegalArgumentException(msg);
        }
        final Neo4jNode node = repository.findByUuid(nodeUuid);
        if (null == node) {
            final String msg = String.format("Invalid nodeUuid %s. No node to delete found", nodeUuid);
            throw new IllegalArgumentException(msg);
        }
        if (node.isRoot()) {
            repository.deleteAll();
        }
        else {
            node.getParent().removeChild(node);
            final List<Neo4jNode> subNodes = getPreordered(node);
            repository.delete(subNodes);
        }
        LOG.debug("Node with uuid {} and sub nodes deleted", nodeUuid);
    }

    /**
     * @see neo4j.tree.service.TreeService#getPreordered()
     */
    @Override
    public List<Neo4jNode> getPreordered(final Neo4jNode start) {
        final List<Neo4jNode> nodes = start.preOrder();
        return nodes;
    }

    /**
     * @see neo4j.tree.service.TreeService#getRootNode()
     */
    @Override
    public Neo4jNode getRootNode() {
        final Neo4jNode rootNode = repository.findRootNode();
        return rootNode;
    }

    @Inject
    protected void setRepository(final TreeNodeRepository repository) {
        this.repository = repository;
    }

}
