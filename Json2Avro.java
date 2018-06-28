package com.conversion;

import org.apache.avro.AvroTypeException;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.*;

import java.io.*;

public class Json2Avro {
    
    private static final String json = "{" + "\"name\":\"Frank\"," + "\"age\":\"47\"" + "}";

    private static final Schema schema1 = new Schema.Parser().parse("{ \"type\":\"record\", \"namespace\":\"foo\", \"name\":\"Person\", \"fields\":[ { \"name\":\"name\", \"type\":\"string\" }, { \"name\":\"age\", \"type\":\"string\" } ] }");

    private static final Schema schema2 = new Schema.Parser().setValidate(true).parse("{ \"type\":\"record\", \"namespace\":\"foo\", \"name\":\"Person\", \"fields\":[ { \"name\":\"name\", \"type\":\"string\" }, { \"name\":\"age\", \"type\":\"int\" } ] }");

public static void main(String[] args) throws IOException {
        byte[] data = jsonToAvro(json, schema1);
try {
    boolean validate = validateJson(data, schema2);
    System.out.println("validate  "+validate);
    String jsonString = avroToJson(data, schema1);
    System.out.println(jsonString);
}catch(Exception e){
    e.printStackTrace();
}
}


    /**
     * Convert JSON to avro binary array.
     *
     * @param json
     * @param schema
     * @return
     * @throws IOException
     */
    public static byte[] jsonToAvro(String json, Schema schema) throws IOException {
        DatumReader<Object> reader = new GenericDatumReader<>(schema);
        GenericDatumWriter<Object> writer = new GenericDatumWriter<>(schema);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Decoder decoder = DecoderFactory.get().jsonDecoder(schema, json);
        Encoder encoder = EncoderFactory.get().binaryEncoder(output, null);
        Object datum = reader.read(null, decoder);
        writer.write(datum, encoder);
        encoder.flush();
        return output.toByteArray();
    }

    public static boolean validateJson(byte[] data, Schema schema) throws Exception {
        InputStream input = new ByteArrayInputStream(data);
        DataInputStream din = new DataInputStream(input);
        GenericDatumReader<Object> reader = null;
        Object datum = null;

        BinaryDecoder binaryDecoder =
                DecoderFactory.get().binaryDecoder(input, null);
        try {
            //DatumReader reader = new GenericDatumReader(schema);
            reader = new GenericDatumReader<Object>(schema);
            DatumWriter<Object> writer = new GenericDatumWriter<Object>(schema);




            //Decoder decoder = DecoderFactory.get().jsonDecoder(schema,din);
            //DatumWriter<Object> writer = new GenericDatumWriter<>(schema);
            //datum = reader.read(datum, binaryDecoder);
            //writer.write(datum, binaryDecoder);
            Decoder decoder = DecoderFactory.get().binaryDecoder(input, null);

            //reader.read(null, decoder);
            reader.read(din, decoder);
            //reader.read(null, decoder);
            //reader.read(din, decoder);
            return true;
        } catch (AvroTypeException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
        /**
     * Convert Avro binary byte array back to JSON String.
     *
     * @param avro
     * @return
     * @throws IOException
     */
    public static String avroToJsonold(byte[] avro, Schema schema) throws IOException {
        boolean pretty = false;
        GenericDatumReader<Object> reader = null;
        JsonEncoder encoder = null;
        ByteArrayOutputStream output = null;
        try {
            //Schema schema = new Schema.Parser().parse(schemaStr);
            reader = new GenericDatumReader<Object>(schema);
            InputStream input = new ByteArrayInputStream(avro);
            output = new ByteArrayOutputStream();
            DatumWriter<Object> writer = new GenericDatumWriter<Object>(schema);
            encoder = EncoderFactory.get().jsonEncoder(schema, output);
            //encoder = EncoderFactory.get().jsonEncoder(schema, output, pretty);
            Decoder decoder = DecoderFactory.get().binaryDecoder(input, null);
            Object datum;
            while (true) {
                try {
                    datum = reader.read(null, decoder);
                } catch (EOFException eofe) {
                    break;
                }
                writer.write(datum, encoder);
            }
            encoder.flush();
            output.flush();
            return new String(output.toByteArray());
        } finally {

        }
    }

    public static String avroToJson(byte[] avro, Schema schema) throws IOException {
        boolean pretty = false;
        GenericDatumReader<Object> reader = null;
        JsonEncoder encoder = null;
        ByteArrayOutputStream output = null;
        try {
            reader = new GenericDatumReader<Object>(schema);
            InputStream input = new ByteArrayInputStream(avro);
            output = new ByteArrayOutputStream();
            DatumWriter<Object> writer = new GenericDatumWriter<Object>(schema);
            encoder = EncoderFactory.get().jsonEncoder(schema, output);
            Decoder decoder = DecoderFactory.get().binaryDecoder(input, null);
            Object datum;
            while (true) {
                try {
                    datum = reader.read(null, decoder);
                } catch (EOFException eofe) {
                    break;
                }
                writer.write(datum, encoder);
            }
            encoder.flush();
            output.flush();
            return new String(output.toByteArray());
        } finally {

        }
    }

    /**
     * Convert Avro binary byte array back to JSON String.
     *
     * @param avro
     * @param schema
     * @return
     * @throws IOException
     */
   /* public static String avroToJson(byte[] avro, Schema schema) throws IOException {
        boolean pretty = false;
        GenericDatumReader<Object> reader = new GenericDatumReader<>(schema);
        DatumWriter<Object> writer = new GenericDatumWriter<>(schema);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        JsonEncoder encoder = EncoderFactory.get().jsonEncoder(schema, output, pretty);
        Decoder decoder = DecoderFactory.get().binaryDecoder(avro, null);
        Object datum = reader.read(null, decoder);
        writer.write(datum, encoder);
        encoder.flush();
        output.flush();
        return new String(output.toByteArray(), "UTF-8");
    }*/
}
