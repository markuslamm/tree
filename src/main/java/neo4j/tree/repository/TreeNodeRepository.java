/**
 * 
 */
package neo4j.tree.repository;

import neo4j.tree.domain.Neo4jNode;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;


/**
 * Main data access interface. Provides basic, spring generated CRUD operations, as well as self
 * defined queries
 * 
 * @author Markus Lamm
 */
public interface TreeNodeRepository extends GraphRepository<Neo4jNode>
{
    /**
     * Find node by uuid.
     * 
     * @param uuid query parameter
     * @return unique node
     */
    @Query("MATCH (n:TreeNode) WHERE n.uuid = {0} RETURN n")
    Neo4jNode findByUuid(String uuid);

    /**
     * Find root node of the tree structure
     * 
     * @return unique node with no parent
     */
    @Query("MATCH n WHERE NOT ()-[:IS_PARENT_OF]->(n:TreeNode) RETURN n")
    Neo4jNode findRootNode();

    /**
     * Find nodes by name
     * 
     * @param name query parameter
     * @return result list
     */
    @Query("MATCH (n:TreeNode) WHERE n.name = {0} RETURN n")
    Iterable<Neo4jNode> findByName(String name);

    /**
     * Find child nodes of a given parent node
     * 
     * @param parent query parameter
     * @return result list
     */
    @Query("MATCH (p:TreeNode)-[:IS_PARENT_OF]->(n:TreeNode) WHERE id(p) = {0} RETURN n")
    Iterable<Neo4jNode> findByParent(Neo4jNode parent);
}
