import java.net.*;
import java.io.*;

public class chatclient {
public static void main(String args[]) throws Exception {

Socket sk = new Socket("127.0.0.1", 2000);

BufferedReader sin = new BufferedReader(
new InputStreamReader(sk.getInputStream()));

PrintStream sout = new PrintStream(sk.getOutputStream());

BufferedReader stdin = new BufferedReader(
new InputStreamReader(System.in));

String s;

while (true) {
System.out.print("Client : ");
s = stdin.readLine();
sout.println(s);

//  Read from server
s = sin.readLine();
System.out.println("Server : " + s);

if (s.equalsIgnoreCase("BYE"))
break;
}

sk.close();
sin.close();
sout.close();
stdin.close();
}
}
