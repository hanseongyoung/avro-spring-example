package syhan.avro.client.avro;

import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.schema.SchemaReference;
import org.springframework.cloud.stream.schema.SchemaRegistrationResponse;
import org.springframework.cloud.stream.schema.client.SchemaRegistryClient;
import org.springframework.web.client.HttpClientErrorException;

public class SchemaManager {
    //
    private static final String SCHEMA_FORMAT = "avro";
    private Logger logger = LoggerFactory.getLogger(getClass());

    private SchemaRegistryClient schemaRegistryClient;

    public SchemaManager(SchemaRegistryClient schemaRegistryClient) {
        //
        this.schemaRegistryClient = schemaRegistryClient;
    }

    public String findSchema(Class clazz) throws Exception {
        //
        return findSchema(clazz.getName());
    }

    public String findSchema(String className) throws Exception {
        //
        String schema = find(className);
        if (schema == null) {
            register(className);
            schema = find(className);
        }
        return schema;
    }

    private String find(String className) {
        //
        SchemaReference schemaReference = new SchemaReference(className, 1, SCHEMA_FORMAT);
        logger.debug("find --> "+schemaReference);
        String schema = fetch(schemaReference);
        logger.debug("schema -> "+schema);
        return schema;
    }

    private String fetch(SchemaReference schemaReference) {
        try {
            return schemaRegistryClient.fetch(schemaReference);
        } catch (HttpClientErrorException e) {
            logger.error("status:"+e.getStatusCode());
            if (!e.getStatusCode().equals("404"))
                throw new RuntimeException(e);
        }
        return null;
    }

    private void register(String className) throws Exception {
        //
        Class aClass = Class.forName(className);
        Schema schema = ReflectData.AllowNull.get().getSchema(aClass);
        logger.debug("register... " + schema.toString());
        SchemaRegistrationResponse response = schemaRegistryClient.register(className, SCHEMA_FORMAT, schema.toString());

        logger.debug("  id:"+response.getId());
        logger.debug("  version:"+response.getSchemaReference().getVersion());
        logger.debug("  subject:"+response.getSchemaReference().getSubject());
        logger.debug("  format:"+response.getSchemaReference().getFormat());
    }
}
