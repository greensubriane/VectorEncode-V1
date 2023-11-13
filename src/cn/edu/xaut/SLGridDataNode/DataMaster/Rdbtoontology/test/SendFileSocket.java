package cn.edu.xaut.SLGridDataNode.DataMaster.Rdbtoontology.test;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 服务器程序,用于传输文件
 */
public class SendFileSocket extends Thread {
public static JFrame parent = null;  //用于返回父窗体
private JFrame frame = null;
private JPanel pane = null;
private JLabel label1 = new JLabel("集成服务状态：未启动");
private MenuBar mb = new MenuBar();
private Menu m1 = new Menu("服务");
private MenuItem start = new MenuItem("开始"), stop = new MenuItem("停止"), exit = new MenuItem("退出");
private ServerSocket ss;

private volatile boolean running;  //服务器正在运行的标志

public static void main(String[] args) {
    server();//启动服务器程序
}

private static final int PORT = 6000;
private Socket s;
private static String owlfileA = null, owlfileB = null;


public SendFileSocket() {
    try {
        frame = new JFrame();
        pane = new JPanel();
        label1.setSize(new Dimension(100, 100));
        label1.setBounds(new Rectangle(50, 50, 300, 80));
        label1.setFont(label1.getFont().deriveFont(25f));


        pane.setLayout(null);
        pane.add(label1);

        mb.add(m1);
        m1.add(start);
        m1.add(stop);
        m1.add(exit);

        ss = new ServerSocket(PORT);
        //ss.setSoTimeout(2000);

        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    startServer();
                    label1.setText("集成服务状态：已启动");
                    pane.add(label1);
                    frame.setVisible(true);
                    System.out.println("This is server");
                    int count = 0;
                    while (running) {
                        // 创建一个Socket等待客户端连接
                        //
                        try {
                            Socket s = ss.accept();
                            count++;
                            System.out.println("This is the " + count + "'st client connetion!");
                            new SendFileSocket(s).start();// 启动一个线程为这个客户端服务
                        } catch (Exception ex) {
                            this.wait(10);
                            break;
                        }

                    }

                    //server( );//启动服务器程序

                } catch (Exception ex) {
                    //ex.printStackTrace();
                }
            }
        });

        stop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    startServer();
                    ss.close();
                    label1.setText("集成服务状态：已停止");
                    pane.add(label1);
                    frame.setVisible(true);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    parent.setVisible(true);
                    frame.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        frame.getContentPane().add(pane);
        frame.setMenuBar(mb);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocation(50, 150);
        frame.setTitle("异构系统本体集成平台——服务");
        frame.setResizable(false);
        frame.setVisible(true);
    } catch (Exception e) {
        e.printStackTrace();
    }

}

public SendFileSocket(Socket s) {
    this.s = s;
}


public void run() {
    try {
        OutputStream os = s.getOutputStream();
        InputStream is = s.getInputStream();
        FileInputStream fins;
        int data;
        String clientName;

        Convert convert = new Convert(true);

        //os.write( "Hello,welcome you!".getBytes( ) );
        byte[] buf = new byte[100];
        while (true) {
            int len = is.read(buf);
            String revStr = new String(buf, 0, len);
            System.out.println("This client wants to " + revStr);

            if (revStr.startsWith("Login"))//表明客户端请求传输一个文件
            {
                clientName = getFileName(revStr);
                owlfileA = getowlfilename(clientName);//获取本体文件名

                convert.creatallnameXML(clientName);
                //传递所有参与协作系统名
                fins = new FileInputStream("allname.xml");
                //byte[] fielBuf = new byte[100];

                while (-1 != (data = fins.read()))//从文件中读取数据，每次读取1字节
                {
                    os.write(data); //将读取到的数据写到网络数据流中发送给客户段
                }

                fins = null;

                break;

            }

            //	传递目标系统名

            if (revStr.startsWith("Object")) {
                String[] name = getFileName(revStr).split(":");
                String nameA = name[0];
                String nameB = name[1];

                //convert.getfilename(); //获取文件名前缀result.txt中
                convert.creatclsxml(nameA, nameB);
                fins = new FileInputStream("objectclass.xml");
                //byte[] fielBuf = new byte[100];

                while (-1 != (data = fins.read()))//从文件中读取数据，每次读取1字节
                {
                    os.write(data); //将读取到的数据写到网络数据流中发送给客户段
                }

                fins = null;

                break;
            }


            //	传递目标系统名

            if (revStr.startsWith("Convert"))//表明客户端请求传输一个文件
            {
                String[] name = getFileName(revStr).split(":");
                String nameA = name[0];
                String nameB = name[1];
                String nameC = name[2];
                convert.creatclspropertyxml(nameA, nameB, nameC);

                fins = new FileInputStream("convertname.xml");
                //byte[] fielBuf = new byte[100];

                while (-1 != (data = fins.read()))//从文件中读取数据，每次读取1字节
                {
                    os.write(data); //将读取到的数据写到网络数据流中发送给客户段
                }
                fins = null;
                break;
            }


            //	客户传递请求转换文件

            if (revStr.startsWith("File"))//表明客户端请求传输一个文件
            {
                //fileName = getFileName(revStr );
                os.write("well".getBytes());

                byte[] filedata = new byte[0];

                len = is.read(buf);

                while (len != -1) {

                    if (len > 0) {
                        String str = new String(buf, "gb2312");
                        byte[] temp = new byte[filedata.length + len];
                        if (len == 100)  //前面部分完整数据
                        {
                            for (int i = 0; i < filedata.length; i++) {
                                temp[i] = filedata[i];
                            }
                            for (int i = 0; i < buf.length; i++) {
                                temp[filedata.length + i] = buf[i];
                            }
                        } else    //前面部分完整数据
                        {
                            for (int i = 0; i < filedata.length; i++) {
                                temp[i] = filedata[i];
                            }
                            for (int i = 0; i < len; i++) {
                                temp[filedata.length + i] = buf[i];
                            }
                        }

                        filedata = temp;
                    }
                    try {
                        if (is.available() > 0) {
                            len = is.read(buf);
                        } else {
                            len = -1;
                        }
                    } catch (Exception e) {
                        len = -1;
                        e.printStackTrace();
                    }
                }

                try {
                    String s = new String(filedata, "gb2312");
                    BufferedWriter bw = new BufferedWriter(new FileWriter("convert.xml"));

                    if (s != null) {
                        bw.write(s);
                    }
                    bw.close();


                    System.out.println("\nFile has been recerved successfully.");

                } catch (Exception e) {
                    System.out.println("\nfile save error.");
                    e.printStackTrace();
                }

                //转换后传送给客户端
                convert.convertontoXML();
                fins = new FileInputStream("convertresult.xml");
                while (-1 != (data = fins.read()))//从文件中读取数据，每次读取1字节
                {
                    os.write(data); //将读取到的数据写到网络数据流中发送给客户段
                }

                fins = null;

                break;
            }

            if (revStr.startsWith("query"))//表明客户端请求传输一个文件
            {
                //fileName = getFileName(revStr );
                os.write("well".getBytes());
                byte[] filedata = new byte[0];
                len = is.read(buf);
                while (len != -1) {
                    if (len > 0) {
                        String str = new String(buf, "gb2312");
                        byte[] temp = new byte[filedata.length + len];
                        if (len == 100)  //前面部分完整数据
                        {
                            for (int i = 0; i < filedata.length; i++) {
                                temp[i] = filedata[i];
                            }
                            for (int i = 0; i < buf.length; i++) {
                                temp[filedata.length + i] = buf[i];
                            }
                        } else    //前面部分完整数据
                        {
                            for (int i = 0; i < filedata.length; i++) {
                                temp[i] = filedata[i];
                            }
                            for (int i = 0; i < len; i++) {
                                temp[filedata.length + i] = buf[i];
                            }
                        }
                        filedata = temp;
                    }
                    try {
                        if (is.available() > 0) {
                            len = is.read(buf);
                        } else {
                            len = -1;
                        }
                    } catch (Exception e) {
                        len = -1;
                        e.printStackTrace();
                    }
                }
                try {
                    String s = new String(filedata, "gb2312");
                    BufferedWriter bw = new BufferedWriter(new FileWriter("query.xml"));

                    if (s != null) {
                        bw.write(s);
                    }
                    bw.close();
                    System.out.println("\nFile has been recerved successfully.");
                } catch (Exception e) {
                    System.out.println("\nfile save error.");
                    e.printStackTrace();
                }

                Query query = new Query();
                //传递查询结果文件
                fins = new FileInputStream("queryresult.xml");
                while (-1 != (data = fins.read()))//从文件中读取数据，每次读取1字节
                {
                    os.write(data); //将读取到的数据写到网络数据流中发送给客户段
                }

                fins = null;
                break;
            }


            if (revStr.startsWith("update"))//表明客户端请求传输一个文件
            {
                //fileName = getFileName(revStr );
                os.write("well".getBytes());
                byte[] filedata = new byte[0];
                len = is.read(buf);
                while (len != -1) {
                    if (len > 0) {
                        String str = new String(buf, "gb2312");
                        byte[] temp = new byte[filedata.length + len];
                        if (len == 100)  //前面部分完整数据
                        {
                            for (int i = 0; i < filedata.length; i++) {
                                temp[i] = filedata[i];
                            }
                            for (int i = 0; i < buf.length; i++) {
                                temp[filedata.length + i] = buf[i];
                            }
                        } else    //前面部分完整数据
                        {
                            for (int i = 0; i < filedata.length; i++) {
                                temp[i] = filedata[i];
                            }
                            for (int i = 0; i < len; i++) {
                                temp[filedata.length + i] = buf[i];
                            }
                        }
                        filedata = temp;
                    }
                    try {
                        if (is.available() > 0) {
                            len = is.read(buf);
                        } else {
                            len = -1;
                        }
                    } catch (Exception e) {
                        len = -1;
                        e.printStackTrace();
                    }
                }
                try {
                    String s = new String(filedata, "gb2312");
                    BufferedWriter bw = new BufferedWriter(new FileWriter("update.xml"));

                    if (s != null) {
                        bw.write(s);
                    }
                    bw.close();
                    System.out.println("\nFile has been recerved successfully.");
                } catch (Exception e) {
                    System.out.println("\nfile save error.");
                    e.printStackTrace();
                    os.write("update fail".getBytes());
                }

                new update();
                //转换后传送给客户端
                os.write("update success".getBytes());

                break;
            }


            if (revStr.startsWith("sim"))//表明客户端请求传输一个文件
            {
                //fileName = getFileName(revStr );
                os.write("well".getBytes());
                byte[] filedata = new byte[0];
                len = is.read(buf);
                while (len != -1) {
                    if (len > 0) {
                        String str = new String(buf, "gb2312");
                        byte[] temp = new byte[filedata.length + len];
                        if (len == 100)  //前面部分完整数据
                        {
                            for (int i = 0; i < filedata.length; i++) {
                                temp[i] = filedata[i];
                            }
                            for (int i = 0; i < buf.length; i++) {
                                temp[filedata.length + i] = buf[i];
                            }
                        } else    //前面部分完整数据
                        {
                            for (int i = 0; i < filedata.length; i++) {
                                temp[i] = filedata[i];
                            }
                            for (int i = 0; i < len; i++) {
                                temp[filedata.length + i] = buf[i];
                            }
                        }
                        filedata = temp;
                    }
                    try {
                        if (is.available() > 0) {
                            len = is.read(buf);
                        } else {
                            len = -1;
                        }
                    } catch (Exception e) {
                        len = -1;
                        e.printStackTrace();
                    }
                }
                try {
                    String s = new String(filedata, "gb2312");
                    BufferedWriter bw = new BufferedWriter(new FileWriter("sim.xml"));

                    if (s != null) {
                        bw.write(s);
                    }
                    bw.close();
                    System.out.println("\nFile has been recerved successfully.");
                } catch (Exception e) {
                    System.out.println("\nfile save error.");
                    e.printStackTrace();
                    os.write("update fail".getBytes());
                }

                updatesim sim = new updatesim();
                //转换后传送给客户端
                os.write("update success".getBytes());

                break;
            }

        }

        os.close();
        is.close();
        s.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private String getFileName(String revStr) {
    String fileName;
    int index = revStr.indexOf(":") + 1;
    fileName = revStr.substring(index);
    return fileName;
}

public static void server() {
    System.out.println("This is server");

    try {
        ServerSocket ss = new ServerSocket(PORT);
        int count = 0;
        while (true) {
            // 创建一个Socket等待客户端连接
            Socket s = ss.accept();
            count++;
            System.out.println("This is the " + count + "'st client connetion!");
            new SendFileSocket(s).start();// 启动一个线程为这个客户端服务
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

public String getowlfilename(String clientname) {
    String line = null; // 用来保存每行读取的内容
    String owlfilename = null;
    try {
        InputStream is = new FileInputStream("ontoclient.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        line = reader.readLine(); // 读取第一行

        while (line != null) {
            String[] str = line.split("#");
            if (clientname.equals(str[0])) {
                owlfilename = str[1];
            }
            line = reader.readLine(); // 读取下一行
        }

    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return owlfilename;
}

public synchronized void startServer() //启动服务器
{
    this.running = true;
    //start();  //启动服务器线程
}

public synchronized void stopServer() {
    this.running = false;
    try {
        this.ss.close();
    } catch (IOException ex) {
        ex.printStackTrace();
    }
    //用socket虚假连接，跳过阻塞，结束进程
    Socket socket = null;
    try {
        socket = new Socket("127.0.0.1", PORT);      //!!!!!!!!!!!
    } catch (UnknownHostException ex) {
        ex.printStackTrace();
    } catch (IOException ex) {
        ex.printStackTrace();
    }
    try {
        socket.getOutputStream().write(1);
    } catch (IOException ex) {
        ex.printStackTrace();
    }
    try {
        socket.close();
    } catch (IOException ex) {
        ex.printStackTrace();
    }

}

}

/*
class PrinterServer extends Thread
{




    public static final int DEFFAULT_PORT = 8888;

    private ServerSocket serverSocket;
    private int port;

    private volatile boolean running;  //服务器正在运行的标志

    public PrinterServer()
    {
        this(DEFFAULT_PORT);
    }
    public PrinterServer(int port)
    {

        try
        {
            if(port <= 0 || port > 0xFFFF)
            {
                this.port = port;
            }
            serverSocket  = new ServerSocket(port);
            //设置超时值如果 accept 方法调用超过超时值将引发 java.net.SocketTimeoutException
  //          this.serverSocket.setSoTimeout(3000);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    public synchronized void startServer() {  //启动服务器
        this.running = true;
        start();  //启动服务器线程
    }
    public synchronized void stopServer()
    {
        this.running = false;
        try
        {
            this.serverSocket.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        //用socket虚假连接，跳过阻塞，结束进程
        Socket socket = null;
        try
        {
            socket = new Socket("127.0.0.1",8888);      //!!!!!!!!!!!
        }
        catch (UnknownHostException ex)
        {
            ex.printStackTrace();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        try
        {
            socket.getOutputStream().write(1);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        try
        {
            socket.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

    }


    public void run()
    {
        try
        {
            while(this.running)
            {
                Socket socket = null;
                try
                {
                    socket = serverSocket.accept();
                }
                catch (SocketTimeoutException e)
                {
                    //e.printStackTrace();
                }
                if(socket!=null)
                {
                      UserThread userThread = new UserThread(socket);
                      userThread.start();
                }
            }
        }
        catch (IOException ex)
        {
                    System.out.println("The serverSocket stopping");
        }
    }
}
*/


