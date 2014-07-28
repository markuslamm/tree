/**
 * 
 */
package neo4j.tree.service;

import java.util.List;

import neo4j.tree.domain.Neo4jNode;
import neo4j.tree.web.TreeNodeForm;


/**
 * Service boundary for transactional tree operations
 * 
 * @author Markus Lamm
 */
public interface TreeService
{
    /**
     * Find node by uuid.
     * 
     * @param uuid query parameter
     * @return unique node
     */
    Neo4jNode findByUuid(String uuid);

    /**
     * Find all nodes
     * 
     * @return result list
     */
    List<Neo4jNode> findAll();

    /**
     * Creates a new root element with a given name
     * 
     * @param rootName name of the node
     * @return created element
     */
    Neo4jNode createTree(final String rootName);

    /**
     * Adds a given node to the node with the given parent uuid
     * 
     * @param node child element
     * @param parentUuid identifier of parent node
     * @return the parent element
     */
    Neo4jNode addNode(Neo4jNode node, String parentUuid);

    /**
     * Find child nodes of a given parent node
     * 
     * @param parent query parameter
     * @return result list
     */
    List<Neo4jNode> findByParent(Neo4jNode parent);

    /**
     * Update the node with the given nodeUuid
     * 
     * @param form node form data
     * @param nodeUuid node identifier
     * @return the changed node
     */
    Neo4jNode updateNode(TreeNodeForm form, String nodeUuid);

    /**
     * Removes the node with the given nodeUuid
     * 
     * @param nodeUuid node identifier
     */
    void deleteNode(String nodeUuid);

    /**
     * Get a 'depth-first' ordered list, beginnig from the given start node
     * 
     * @param start traversal start
     * @return result list
     */
    List<Neo4jNode> getPreordered(Neo4jNode start);

    /**
     * Find root node of the tree structure
     * 
     * @return unique node with no parent
     */
    Neo4jNode getRootNode();
}
