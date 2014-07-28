/**
 * 
 */
package neo4j.tree.event;

import java.util.Date;
import java.util.UUID;

import neo4j.tree.domain.AbstractGraphEntity;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.data.neo4j.lifecycle.BeforeSaveEvent;
import org.springframework.stereotype.Component;


/**
 * Observes Spring specific events that occur before saving entities is the graph data store. Will
 * be invoked by the the spring framework.
 * 
 * @author Markus Lamm
 */
@Component
public class BeforeEntitySavedListener implements ApplicationListener<BeforeSaveEvent<AbstractGraphEntity>>
{
    private static final Logger LOG = LoggerFactory.getLogger(BeforeEntitySavedListener.class);

    /**
     * Handles application events before saving an entity to the graph. Responsible for updating
     * audit data and creating an UUID for entities that were created.
     * 
     * @param event
     * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
     */
    @Override
    public void onApplicationEvent(final BeforeSaveEvent<AbstractGraphEntity> event) {
        final AbstractGraphEntity entity = event.getEntity();
        if (event.getEntity() != null) {
            final Date now = new Date();
            if (StringUtils.isBlank(entity.getUuid())) { // new entity?
                entity.setCreatedDate(now);
                final String uuid = generateUuid();
                LOG.debug("UUID created. [{}]", uuid);
                entity.setUuid(uuid);
            }
            entity.setLastModifiedDate(now);
        }
    }

    /**
     * Helper method. Removes '-' from uuid string.
     * 
     * @param uuid raw uuid string
     * @return
     */
    private static String extractValue(final UUID uuid) {
        final String[] parts = StringUtils.split(uuid.toString(), '-');
        final String result = StringUtils.join(parts);
        return result;
    }

    /**
     * Generates final uuid string
     * 
     * @return
     */
    private String generateUuid() {
        final UUID uuid = UUID.randomUUID();
        final String result = extractValue(uuid);
        return result;
    }
}
