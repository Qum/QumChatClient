package Qum.Client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Formatter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Qum.Mes.Mess;

public class FileTransporter {

    private static String sendFileName;
    private static long fileSize;
    private static Socket FileSocket;

    /**
     * @return the fileSize
     */
    public static long getFileSize() {
	return fileSize;
    }

    /**
     * @param fileSize
     *            the fileSize to set
     */
    public static void setFileSize(long fileSize) {
	FileTransporter.fileSize = fileSize;
    }

    public static void reqestForSending(String reciverNick, String fileName,
	    long size) {
	SocketMaster.getInstance()
		.sendMess(
			new Mess(reciverNick, fileName, size,
				SocketMaster.FILE_REQUEST));
    }

    public static void showReciveRequest(String senderNick, String fileName,
	    long size, String senderIp) throws IOException {
	sendFileName = fileName;
	fileSize = size;

	System.out.println(CliFace.MyNick + "FILE_REQUEST");
	class AskObj {
	    String whoSendReqest, fileSenderIp;

	    AskObj(String Name, String Ip) throws IOException {
		whoSendReqest = Name;
		fileSenderIp = Ip;
		init();
	    }

	    void init() throws IOException {
		System.err.println(CliFace.MyNick
			+ "Создался AskObj - защел в init");

		Formatter f = new Formatter();
		f.format("%.2f", (float) (size / 1024) / 1024);
		String messForDialog = "Do you want to receive file ' "
			+ fileName + " ' (size = " + f + " Mb)" + "\n"
			+ " from ' " + senderNick + " '";
		int response = JOptionPane
			.showConfirmDialog(null, messForDialog,
				"Confirm File receive ",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (response == 0) { // Да,принять файл
		    SocketMaster.getInstance().sendMess(
			    new Mess(whoSendReqest, "",
				    SocketMaster.FILE_REQUEST_SUCCESS));
		    doRecive(fileSenderIp);
		} else if (response == 1 || response == -1) { // нет, не
							      // принимать
		    SocketMaster.getInstance().sendMess(
			    new Mess(whoSendReqest, "",
				    SocketMaster.FILE_REQUEST_FAIL));
		}
	    }
	}
	new AskObj(senderNick, senderIp);
    }

    public static void doRecive(String Ip) { // не готов
	System.out.println("Прием ");
	new Thread(new Runnable() {

	    @Override
	    public void run() {
		if (FileSocket == null) {
		    try {
			FileSocket = new ServerSocket(9091).accept();
			FileSocket.setReceiveBufferSize(1048576);
		    } catch (IOException e2) {
			e2.printStackTrace();
		    }
		} else if ((FileSocket.getInetAddress().getHostAddress()
			.equals(Ip) && FileSocket.isClosed())
			|| FileSocket.isClosed()) {
		    try {
			System.out.println("else if ");
			System.out.println("getRemoteSocketAddress"
				+ FileSocket.getRemoteSocketAddress());
			System.out.println("getInetAddress"
				+ FileSocket.getInetAddress());
			System.out.println("Ip"+Ip);
			FileSocket = new ServerSocket(9091).accept();
			FileSocket.setReceiveBufferSize(1048576);
		    } catch (IOException e2) {
			e2.printStackTrace();
		    }
		}

		File RecivedFile = null;
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("."));
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setDialogType(JFileChooser.SAVE_DIALOG);
		if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
		    RecivedFile = new File(fc.getSelectedFile() + sendFileName);
		} else {
		    return;
		}

		if (FileSocket == null) {
		    JOptionPane
			    .showMessageDialog(
				    null,
				    "<html><i>Не получилось соеденится с отправителем!!</i></html>",
				    CliFace.MyNick,
				    JOptionPane.YES_NO_CANCEL_OPTION);
		}

		try (BufferedInputStream bin = new BufferedInputStream(
			FileSocket.getInputStream(), 209715200);
			BufferedOutputStream Ous = new BufferedOutputStream(
				new FileOutputStream(RecivedFile), 209715200);) {
		    System.out.println("getRemoteSocketAddress"
			    + FileSocket.getRemoteSocketAddress());
		    System.out.println("getInetAddress"
			    + FileSocket.getInetAddress());
		    System.out.println("Прием - дал сокет ОК");
		    byte[] buffArr = new byte[209715200];
		    long readed = 0;
		    int recivd = 0;
		    while ((recivd = bin.read(buffArr)) != -1
			    && fileSize >= readed) {
			System.out.println("Прием - WHILE");
			readed += recivd;
			Ous.write(buffArr, 0, recivd);
			Ous.flush();
		    }
		} catch (IOException e) {
		    System.out.println("IO Ex Reciv ОК");
		    try {
			if (FileSocket != null && FileSocket.isConnected()) {
			    FileSocket.close();
			}
		    } catch (IOException e1) {
			e1.printStackTrace();
		    }
		    e.printStackTrace();
		} finally {
		    System.out.println("finall re");
		    try {
			if (FileSocket != null && FileSocket.isConnected()) {
			    System.out.println("finall re if");
			    FileSocket.close();
			}
		    } catch (IOException e) {
			System.out.println("finall re if ex");
			e.printStackTrace();
		    }
		}
		System.out.println("Прием ОК");

	    }
	}).start();
    }

    public static void doSend(String Ip) throws IOException { // не готов
	System.out.println("Передача");

	new Thread(new Runnable() {

	    @Override
	    public void run() {

		if (FileSocket == null) {
		    try {
			FileSocket = SocketMaster.getSocket(Ip, 9091);
			FileSocket.setReceiveBufferSize(1048576);
		    } catch (IOException e2) {
			e2.printStackTrace();
		    }
		} else if ((FileSocket.getInetAddress().getHostAddress()
			.equals(Ip) && FileSocket.isClosed())
			|| FileSocket.isClosed()) {
		    try {
			FileSocket = SocketMaster.getSocket(Ip, 9091);
			FileSocket.setReceiveBufferSize(1048576);
		    } catch (IOException e2) {
			e2.printStackTrace();
		    }
		}

		// try {
		// if (FileSocket != null && FileSocket.isConnected()) {
		// System.out.println("в doSend не было закрыто");
		// FileSocket.close();
		// }
		// } catch (IOException e1) {
		//
		// e1.printStackTrace();
		// }

		if (FileSocket == null) {
		    JOptionPane
			    .showMessageDialog(
				    null,
				    "<html><i>Не получилось соеденится с получателем!!</i></html>",
				    CliFace.MyNick,
				    JOptionPane.YES_NO_CANCEL_OPTION);
		}
		try (OutputStream Ous = new BufferedOutputStream(FileSocket
			.getOutputStream(), 209715200);
			InputStream Ins = new BufferedInputStream(
				new FileInputStream(CliFace.getFb()), 209715200);) {
		    System.out.println("getRemoteSocketAddress"
			    + FileSocket.getRemoteSocketAddress());
		    System.out.println("getInetAddress"
			    + FileSocket.getInetAddress());
		    byte[] buffArr = new byte[209715200];
		    long sended = 0;
		    int recivd = 0;
		    while ((recivd = Ins.read(buffArr)) != -1
			    && fileSize >= sended) {
			System.out.println("передача WHILE");
			sended += recivd;
			Ous.write(buffArr, 0, recivd);
			Ous.flush();
		    }
		} catch (IOException e) {
		    System.out.println("IO Ex Send ОК");
		    try {
			if (FileSocket != null && FileSocket.isConnected()) {
			    FileSocket.close();
			}
		    } catch (IOException e1) {
			System.out.println("IO Ex-ex Send ОК");
			e1.printStackTrace();
		    }
		    e.printStackTrace();
		} finally {
		    System.out.println("finall se");
		    try {
			if (FileSocket != null && FileSocket.isConnected()) {
			    System.out.println("finall se if");
			    FileSocket.close();
			}
		    } catch (IOException e) {
			System.out.println("finall se if ex");
			e.printStackTrace();
		    }
		}
		System.out.println("Передача ОК");
	    }
	}).start();
    }
}
