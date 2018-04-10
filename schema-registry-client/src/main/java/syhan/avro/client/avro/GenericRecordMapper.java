package syhan.avro.client.avro;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.Assert;

public class GenericRecordMapper {
    //
    public static GenericData.Record mapObjectToRecord(Object object, final Schema schema) {
        //
        Assert.notNull(object, "object must not be null");
        Assert.notNull(schema, "schema must not be null");

        final GenericData.Record record = new GenericData.Record(schema);
        schema.getFields().forEach(r -> record.put(r.name(), PropertyAccessorFactory.forDirectFieldAccess(object).getPropertyValue(r.name())));
        return record;
    }

    public static <T> T mapRecordToObject(GenericData.Record record, T object, final Schema schema) {
        //
        Assert.notNull(record, "record must not be null");
        Assert.notNull(object, "object must not be null");
        Assert.notNull(schema, "schema must not be null");

        Assert.isTrue(schema.getFields().equals(record.getSchema().getFields()), "Schema fields didn't match");
        record.getSchema().getFields().forEach(d -> PropertyAccessorFactory
                .forDirectFieldAccess(object)
                .setPropertyValue(d.name(), record.get(d.name()) == null ? record.get(d.name()) : record.get(d.name()).toString()));
        return object;
    }
}
