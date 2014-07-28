/**
 * 
 */
package neo4j.tree.domain;

import java.util.Date;

/**
 * Base interface for all entities that can be persisted in the data store.
 * 
 * @author Markus Lamm
 */
public interface PersistableEntity
{
    /**
     * Get the unique entity identifier
     * @return the uuid
     */
    String getUuid();

    /**
     * Audit data. get the unique date of creation
     * @return
     */
    Date getCreatedDate();

    /**
     * Audit data. get the unique date of last modification
     * @return
     */
    Date getLastModifiedDate();
}
