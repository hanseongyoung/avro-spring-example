package syhan.avro.client.avro;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AvroUtil {
    //
    public static void encode(OutputStream out, Object object, Schema schema) throws IOException {
        //
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
        if (object instanceof Collection) {
            for (Object ele : (List)object) {
                datumWriter.write(GenericRecordMapper.mapObjectToRecord(ele, schema), encoder);
            }
        } else {
            datumWriter.write(GenericRecordMapper.mapObjectToRecord(object, schema), encoder);
        }
        encoder.flush();
    }

    public static void encodeDataFile(OutputStream out, Object object, Schema schema) throws IOException {
        //
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(datumWriter);
        dataFileWriter.create(schema, out);

        if (object instanceof Collection) {
            for (Object ele : (List)object) {
                dataFileWriter.append(GenericRecordMapper.mapObjectToRecord(ele, schema));
            }
        } else {
            dataFileWriter.append(GenericRecordMapper.mapObjectToRecord(object, schema));
        }
        dataFileWriter.close();
    }

    public static <T> List<T> decodeList(InputStream in, Schema schema, AvroTypeAdapter<T> adapter) throws IOException {
        //
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
        Decoder decoder = DecoderFactory.get().binaryDecoder(in, null);

        List<T> list = new ArrayList<>();
        try {
            while (true) {
                GenericRecord record = datumReader.read(null, decoder);
                T object = GenericRecordMapper.mapRecordToObject((GenericData.Record) record, adapter.instance(), schema);
                list.add(object);
            }
        } catch (EOFException e) {
            //
        } finally {
            in.close();
        }
        return list;
    }

    public static <T> T decode(InputStream in, Schema schema, T objectInstance) throws IOException {
        //
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
        Decoder decoder = DecoderFactory.get().binaryDecoder(in, null);

        try {
            GenericRecord record = datumReader.read(null, decoder);
            T object = GenericRecordMapper.mapRecordToObject((GenericData.Record) record, objectInstance, schema);
            return object;
        } catch (EOFException e) {
            //
        } finally {
            in.close();
        }
        return null;
    }
}
