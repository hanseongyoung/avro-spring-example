package syhan.avro.client.avro;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class GenericRecordMapper {
    //
    public static GenericData.Record mapObjectToRecord(Object object, final Schema recordTypeSchema) {
        //
        Assert.notNull(object, "object must not be null");
        Assert.notNull(recordTypeSchema, "schema must not be null");

        final GenericData.Record record = new GenericData.Record(recordTypeSchema);
        for (Schema.Field field : recordTypeSchema.getFields()) {
            ConfigurablePropertyAccessor accessor = PropertyAccessorFactory.forDirectFieldAccess(object);
            Object obj = accessor.getPropertyValue(field.name());

            System.out.println("---------------");
            System.out.println("field:"+field);
            System.out.println("obj:"+obj);
            System.out.println("filed.schema:"+field.schema());
            System.out.println("filed.schema.type:"+field.schema().getType());
            if (field.schema().getType() == Schema.Type.UNION) {
                System.out.println("filed.schema.types:"+field.schema().getTypes());
                System.out.println("filed.schema.types.0:"+field.schema().getTypes().get(0));
                System.out.println("filed.schema.types.0.type:"+field.schema().getTypes().get(0).getType());
                System.out.println("filed.schema.types.1:"+field.schema().getTypes().get(1));
                System.out.println("filed.schema.types.1.type:"+field.schema().getTypes().get(1).getType());
            }

            System.out.println("** field schema ? "+getFieldSchema(field));
            System.out.println("** field type ? "+getFieldType(field));

            Schema fieldSchma = getFieldSchema(field);
            Schema.Type fieldType = getFieldType(field);

            if (fieldType == Schema.Type.ARRAY) {
                GenericData.Array array = mapObjectToArray(obj, fieldSchma);
                record.put(field.name(), array);
            } else if(fieldType == Schema.Type.RECORD) {
                GenericData.Record subRecord = mapObjectToRecord(obj, fieldSchma);
                record.put(field.name(), subRecord);
            } else if (fieldType == Schema.Type.ENUM) {
                // TODO : not allow enum null... in this line.
                GenericData.EnumSymbol enumSymbol = new GenericData.EnumSymbol(fieldSchma, obj);
                record.put(field.name(), enumSymbol);
            } else {
                record.put(field.name(), obj);
            }
        }

        return record;
    }

    private static Schema.Type getFieldType(Schema.Field field) {
        //
        Schema fieldSchema = getFieldSchema(field);
        if (fieldSchema != null) {
            return fieldSchema.getType();
        }
        return null;
    }

    private static Schema getFieldSchema(Schema.Field field) {
        //
        Schema fieldSchema = field.schema();
        Schema.Type fieldSchemaType = fieldSchema.getType();
        if (fieldSchemaType != Schema.Type.UNION) {
            return fieldSchema;
        }

        // UNION
        List<Schema> unionSchemas = fieldSchema.getTypes();
        for (Schema schema : unionSchemas) {
            Schema.Type schemaType = schema.getType();
            if (schemaType != Schema.Type.NULL) {
                return schema;
            }
        }
        return null;
    }

    private static GenericData.Array mapObjectToArray(Object obj, Schema arrayTypeSchema) {
        //
        //Schema arraySchema = field.schema().getTypes().get(1);
        GenericData.Array array = new GenericData.Array(1, arrayTypeSchema);

        for (Object ele : (List)obj) {
            array.add(mapObjectToRecord(ele, arrayTypeSchema.getElementType()));
        }
        return array;
    }

    public static <T> T mapRecordToObject(GenericData.Record record, T object, final Schema schema) {
        //
        Assert.notNull(record, "record must not be null");
        Assert.notNull(object, "object must not be null");
        Assert.notNull(schema, "schema must not be null");
        Assert.isTrue(schema.getFields().equals(record.getSchema().getFields()), "Schema fields didn't match");

        List<Schema.Field> fields = record.getSchema().getFields();
        for (Schema.Field field : fields) {
            ConfigurablePropertyAccessor accessor = PropertyAccessorFactory.forDirectFieldAccess(object);
            Object ele = record.get(field.name());

            if (ele != null) {
                if (ele.getClass() == GenericData.Array.class) {
                    GenericData.Array array = (GenericData.Array) ele;
                    if (!array.isEmpty()) {
                        ArrayList arrayList = mapArrayToObjectList(array);
                        accessor.setPropertyValue(field.name(), arrayList);
                    }
                } else if (ele.getClass() == GenericData.Record.class) {
                    GenericData.Record subRecord = (GenericData.Record) ele;
                    Object subObject = createObjectFromRecord(subRecord);
                    accessor.setPropertyValue(field.name(), subObject);
                } else if (ele.getClass() == GenericData.EnumSymbol.class) {
                    GenericData.EnumSymbol enumSymbol = (GenericData.EnumSymbol) ele;
                    //
                    Object enumType = createEnumObjectFromEnumSymbol(enumSymbol);
                    accessor.setPropertyValue(field.name(), enumType);
                } else {
                    accessor.setPropertyValue(field.name(), ele.toString());
                }
            }
        }
        return object;
    }



    private static ArrayList mapArrayToObjectList(GenericData.Array array) {
        //
        ArrayList arrayList = new ArrayList();
        for (Iterator<GenericData.Record> iter = array.iterator(); iter.hasNext(); ) {
            // Record
            GenericData.Record eleRecord = iter.next();
            Object createdObj = createObjectFromRecord(eleRecord);
            if (createdObj != null) {
                arrayList.add(createdObj);
            }
        }
        return arrayList;
    }

    private static Object createObjectFromRecord(GenericData.Record record) {
        //
        Schema eleSchema = record.getSchema();
        Object createdObj = null;
        try {
            String accessClassName = eleSchema.getNamespace() + "." + eleSchema.getName();
            createdObj = mapRecordToObject(record, Class.forName(accessClassName).newInstance(), eleSchema);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return createdObj;
    }

    private static Object createEnumObjectFromEnumSymbol(GenericData.EnumSymbol enumSymbol) {
        //
        Schema schema = enumSymbol.getSchema();
        Object createdEnumType = null;
        try {
            String enumTypeName = schema.getNamespace() + "." + schema.getName();
            Class aClass = Class.forName(enumTypeName);
            for (Object enumEle : aClass.getEnumConstants()) {
                if (enumEle.toString().equals(enumSymbol.toString())) {
                    createdEnumType = enumEle;
                    break;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return createdEnumType;
    }
}