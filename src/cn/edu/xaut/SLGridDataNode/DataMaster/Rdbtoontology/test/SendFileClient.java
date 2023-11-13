package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class SendFileClient {

private static final int Server_PORT = 6000;


public static void main(String[] args) throws IOException {

    System.out.println("This is client");

    byte[] buf = new byte[100];
    byte[] name = new byte[100];

    String ipStr = "127.0.0.1";


    try {
        // 创建一个Socket
        Socket s = new Socket(ipStr, Server_PORT);
        //s.connect ( new InetSocketAddress (ipStr , Server_PORT ), Client_PORT );
        OutputStream os = s.getOutputStream();// 输出流
        InputStream is = s.getInputStream();// 输入流

        //向服务器传送文件
//			int len = is.read( buf );// 从输入流中读取数据到buf
//			String revStr = new String( buf, 0, len );
        os.write("file:lytserver.txt".getBytes());//表示将要传递文件
        int len = is.read(buf);// 从输入流中读取数据到buf
        String revStr = new String(buf, 0, len);
        if (revStr.equals("well"))//表示服务器端准备好了接受文件
        {
            FileInputStream fins = new FileInputStream("server.txt");
            //byte[] fielBuf = new byte[100];
            int data;
            while (-1 != (data = fins.read()))//从文件中读取数据，每次读取1字节
            {
                os.write(data); //将读取到的数据写到网络数据流中发送给客户段
            }
        }

        os.close();
        is.close();
        s.close();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

}

/*

			System.out.println( new String( buf, 0, len ) );
			// 向输出流中写入数据,请求传输一个文件
			os.write( "get:server.txt".getBytes( ) );
			len = is.read( buf );// 从输入流中读取数据到buf
			String tempStr = new String(buf,0,len);
			if ( tempStr.startsWith( "Please input your name and password" ) )
			{
				System.out.println("Please input your name and password, ");
				System.out.println("Using the format:name@password:");
				do
				{
					System.in.read( name );
				} while ( name.length<5 );
				os.write( name );
			}

			//开始读取文件数据并把它写到一个名为"clientread.txt"的文件中
			FileOutputStream fos = new FileOutputStream( "clientread.txt" );

			while ( -1 != ( data = is.read( ) ) )
			{
				fos.write( data );
			}

			System.out.println("\nFile has been recerved successfully.");


 */
