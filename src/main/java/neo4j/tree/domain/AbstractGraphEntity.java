/**
 * 
 */
package neo4j.tree.domain;

import java.util.Date;

import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * Base class for all entities stored in the graph database.
 * 
 * @author Markus Lamm
 */
@NodeEntity
public abstract class AbstractGraphEntity implements PersistableEntity
{
    @GraphId
    private Long nodeId;

    @Size(min = 32, max = 32)
    @Indexed(unique = true)
    private String uuid;

    private Date createdDate;
    private Date lastModifiedDate;

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(final Long nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }

    @Override
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(final Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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
        final AbstractGraphEntity other = (AbstractGraphEntity) obj;
        return new EqualsBuilder().append(getNodeId(), other.getNodeId()).append(getUuid(), other.getUuid()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getNodeId()).append(getUuid()).append(getCreatedDate()).append(getLastModifiedDate())
                .toHashCode();
    }

    @Override
    public String toString() {
        final String format = "nodeId=%d, uuid=%s, ";
        return String.format(format, getNodeId(), getUuid());
    }
}
