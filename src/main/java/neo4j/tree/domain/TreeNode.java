/**
 * 
 */
package neo4j.tree.domain;

import java.util.Set;

/**
 * Base operations for all nodes in the tree domain structure. Typed with the domain entity that is
 * stored in the data graph.
 * 
 * @author Markus Lamm
 */
public interface TreeNode<T extends PersistableEntity>
{
    /**
     * Returns all children
     * 
     * @return all child elements
     */
    Set<T> getChildren();

    /**
     * Returns the parent TreeNode or null, if the node is the root of the tree structure
     * 
     * @return parent node, or null
     */
    T getParent();

    /**
     * * Returns the name of the node. Mostly for graphical presentation.
     * 
     * @return the name of the TreeNode
     */
    String getName();

    /**
     * False, if the TreeNode contains child elements, true otherwise
     * 
     * @return whether the TreeNode contains child elements
     */
    boolean isLeaf();

    /**
     * False, if a parent TreeNode is set, true if the node is the root element in the tree
     * structure
     * 
     * @return whether the TreeNode is the root element
     */
    boolean isRoot();

    /**
     * Adds given element to the TreeNode
     * 
     * @param node the node to add as child element
     * @return the node with an added element
     */
    T addChild(T node);

    /**
     * Removes an element from the TreeNode
     * 
     * @param node the node to add as child element
     * @return
     */
    T removeChild(T node);
}
