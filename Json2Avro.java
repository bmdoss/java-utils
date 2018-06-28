package com.conversion;

import org.apache.avro.AvroTypeException;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Json2Avro {

    public static byte[] jsonToAvro(String json, String schemaStr) throws IOException {
        InputStream input = null;
        GenericDatumWriter<GenericRecord> writer = null;
        Encoder encoder = null;
        ByteArrayOutputStream output = null;
        try {
            Schema schema = new Schema.Parser().parse(schemaStr);
            DatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>(schema);
            input = new ByteArrayInputStream(json.getBytes());
            output = new ByteArrayOutputStream();
            DataInputStream din = new DataInputStream(input);
            writer = new GenericDatumWriter<GenericRecord>(schema);
            Decoder decoder = DecoderFactory.get().jsonDecoder(schema, din);
            encoder = EncoderFactory.get().binaryEncoder(output, null);
            GenericRecord datum;
            while (true) {
                try {
                    datum = reader.read(null, decoder);
                } catch (EOFException eofe) {
                    break;
                }
                writer.write(datum, encoder);
            }
            encoder.flush();
            return output.toByteArray();
        } finally {
            try {
                input.close();
            } catch (Exception e) {
            }
        }
    }

    private static String readFileData(String filePath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static String avroToJson(String schemaStr) throws IOException {

        DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
        System.out.println("schemaStr  :"+schemaStr);
        DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(new File(schemaStr), datumReader);
        Schema schema = dataFileReader.getSchema();
        System.out.println(schema);

        return null;
    }


    public static boolean validateJson(String json, Schema schema) throws Exception {
        InputStream input = new ByteArrayInputStream(json.getBytes());
        DataInputStream din = new DataInputStream(input);


        BinaryDecoder binaryDecoder =
                DecoderFactory.get().binaryDecoder(input, null);


        try {
            DatumReader reader = new GenericDatumReader(schema);
            //Decoder decoder = DecoderFactory.get().jsonDecoder(schema,din);
            Object datum = null;
            //DatumWriter<Object> writer = new GenericDatumWriter<>(schema);
            //datum = reader.read(datum, binaryDecoder);
            //writer.write(datum, jsonEncoder);
            Decoder decoder = DecoderFactory.get().binaryDecoder(input, null);
            //reader.read(null, decoder);
            reader.read(din, decoder);
            return true;
        } catch (AvroTypeException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void main(String abc[]) {

        String jsonFileContent = readFileData("src/main/resources/EmployeeActivityDATA.json");
        String schemaFileContent = readFileData("src/main/resources/employee_details.avsc");
        String avroFileContent = readFileData("src/main/resources/EmplyeeDetails_without_schema.avro");
        String avroFilePath = "src/main/resources/EmplyeeDetails_without_schema.avro";

       /* try {
            System.out.println("jsonFileContent  " + jsonFileContent);
            jsonToAvro(jsonFileContent, schemaFileContent);
            System.out.println("schemaFileContent " + schemaFileContent);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        try {
            Schema schema = new Schema.Parser().setValidate(true).parse(schemaFileContent);
            System.out.println("boolean " + validateJson(avroFileContent,schema));
        } catch (Exception e) {
            e.printStackTrace();
        }


            /*try {
                System.out.println("jsonFileContent  " + avroFileContent);
                String data=avroToJson(avroFilePath);
                System.out.println("Json data " + data);
            }catch (Exception e){
                e.printStackTrace();
            }*/

    }
}
