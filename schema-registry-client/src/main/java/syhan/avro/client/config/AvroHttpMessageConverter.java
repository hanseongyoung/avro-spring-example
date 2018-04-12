package syhan.avro.client.config;

import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import syhan.avro.client.avro.AvroUtil;
import syhan.avro.client.avro.SchemaManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

public class AvroHttpMessageConverter extends AbstractHttpMessageConverter {
    //
    private static Logger logger = LoggerFactory.getLogger(AvroHttpMessageConverter.class);

    private SchemaManager schemaManager;

    public AvroHttpMessageConverter(SchemaManager schemaManager) {
        //
        super(new MediaType("application", "avro"));
        this.schemaManager = schemaManager;
    }

    @Override
    protected boolean supports(Class aClass) {
        // !! important !! Any type supported
        return true;
    }

    @Override
    protected Object readInternal(Class aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        //
        try {
            return readFromAvroBinary(aClass, httpInputMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        //
        logger.debug("writeAsAvroBinary..................");
        try {
            writeAsAvroBinary(o, httpOutputMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //writeAsAvroDataFile(o, httpOutputMessage);
    }

    private Object readFromAvroBinary(Class aClass, HttpInputMessage httpInputMessage) throws Exception {
        //
        InputStream in = httpInputMessage.getBody();
        //Schema schema = ReflectData.AllowNull.get().getSchema(aClass);
        Schema schema = findSchema(aClass);
        return AvroUtil.decode(in, schema, aClass.newInstance());
    }

    protected void writeAsAvroDataFile(Object o, HttpOutputMessage outputMessage) throws Exception {
        //
        OutputStream os = outputMessage.getBody();
        //Schema schema = ReflectData.AllowNull.get().getSchema(getElementClass(o));
        Schema schema = findSchema(getElementClass(o));

        AvroUtil.encodeDataFile(os, o, schema);
    }

    protected void writeAsAvroBinary(Object o, HttpOutputMessage outputMessage) throws Exception {
        //
        OutputStream os = outputMessage.getBody();
        //Schema schema = ReflectData.AllowNull.get().getSchema(getElementClass(o));
        Schema schema = findSchema(getElementClass(o));

        AvroUtil.encode(os, o, schema);
    }

    private Schema findSchema(Class aClass) throws Exception {
        //
        String schemaString = schemaManager.findSchema(aClass);
        logger.debug("Class : " + aClass.getName());
        logger.debug("finded schema : "+schemaString);
        return new Schema.Parser().parse(schemaString);
    }

    private Class getElementClass(Object o) {
        if (o instanceof Collection) {
            return ((List)o).get(0).getClass();
        }
        return o.getClass();
    }
}
