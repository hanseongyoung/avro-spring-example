package syhan.avro.client.avro;

import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;
import org.junit.Assert;
import org.junit.Test;
import syhan.avro.client.rest.UserSdo;

public class AvroSchemaTest {
    @Test
    public void testSchema() {
        //
        Schema schema = ReflectData.get().getSchema(UserSdo.class);
        String schemaString = schema.toString();

        Schema.Parser parser = new Schema.Parser();
        Schema schemaOther = parser.parse(schemaString);
        String schemaOtherString = schemaOther.toString();

        System.out.println(schemaString);
        System.out.println(schemaOtherString);
        Assert.assertEquals(schemaString, schemaOtherString);
    }
}
