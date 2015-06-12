package qum.Client;

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

import qum.Mes.Mess;

public class FileTransporter {

    private static String sendFileName;
    private static long fileSize;
//    private static Socket FileSocket;
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
	CliFace.getInstance().showText(CliFace.getInstance().getMyNick() + "FILE_REQUEST");
	class AskObj {

	    void init() throws IOException {
		System.err.println(CliFace.getInstance().getMyNick()
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
			    new Mess(senderNick, "",
				    SocketMaster.FILE_REQUEST_SUCCESS));
		    doRecive(senderIp);
		} else if (response == 1 || response == -1) { // нет, не
							      // принимать
		    SocketMaster.getInstance().sendMess(
			    new Mess(senderNick, "",
				    SocketMaster.FILE_REQUEST_FAIL));
		}
	    }
	}
	new AskObj().init();
    }

    public static void doRecive(String Ip) throws IOException { 
	CliFace.getInstance().showText("Прием ");
	new Thread(new Runnable() {

	    @Override
	    public void run() {
		 Socket FileSocket = null;
//		if (FileSocket == null) {
		    try {
			FileSocket = new ServerSocket(9091).accept();
			FileSocket.setReceiveBufferSize(1048576);
		    } catch (IOException e2) {
			e2.printStackTrace();
		    }
////		} else if ((FileSocket.getInetAddress().getHostAddress()
//			.equals(Ip) && FileSocket.isClosed())
//			|| FileSocket.isClosed()) {
//		    try {
//			CliFace.getInstance().showText("else if ");
//			CliFace.getInstance().showText("getRemoteSocketAddress"
//				+ FileSocket.getRemoteSocketAddress());
//			CliFace.getInstance().showText("getInetAddress"
//				+ FileSocket.getInetAddress());
//			CliFace.getInstance().showText("Ip"+Ip);
//			FileSocket = SS.accept();
//			FileSocket.setReceiveBufferSize(1048576);
//		    } catch (IOException e2) {
//			e2.printStackTrace();
//		    }
//		}

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
				    CliFace.getInstance().getMyNick(),
				    JOptionPane.YES_NO_CANCEL_OPTION);
		}

		try (BufferedInputStream bin = new BufferedInputStream(
			FileSocket.getInputStream(), 20971520);
			BufferedOutputStream Ous = new BufferedOutputStream(
				new FileOutputStream(RecivedFile), 20971520);) {
		    CliFace.getInstance().showText("getRemoteSocketAddress"
			    + FileSocket.getRemoteSocketAddress());
		    CliFace.getInstance().showText("getInetAddress"
			    + FileSocket.getInetAddress());
		    CliFace.getInstance().showText("Прием - дал сокет ОК");
		    byte[] buffArr = new byte[20971520];
		    long readed = 0;
		    int recivd = 0;
		    while ((recivd = bin.read(buffArr)) != -1
			    && fileSize >= readed) {
			CliFace.getInstance().showText("Прием - WHILE");
			readed += recivd;
			Ous.write(buffArr, 0, recivd);
			Ous.flush();
		    }
		} catch (IOException e) {
		    CliFace.getInstance().showText("IO Ex Reciv ОК");
		    try {
			if (FileSocket != null && FileSocket.isConnected()) {
			    FileSocket.close();
			}
		    } catch (IOException e1) {
			e1.printStackTrace();
		    }
		    e.printStackTrace();
		} finally {
		    CliFace.getInstance().showText("finall re");
		    try {
			if (FileSocket != null && FileSocket.isConnected()) {
			    CliFace.getInstance().showText("finall re if");
			    FileSocket.close();
			}
		    } catch (IOException e) {
			CliFace.getInstance().showText("finall re if ex");
			e.printStackTrace();
		    }
		}
		CliFace.getInstance().showText("Прием ОК");

	    }
	}).start();
    }

    public static void doSend(String Ip) throws IOException { // не готов
	CliFace.getInstance().showText("Передача");
	new Thread(new Runnable() {

	    @Override
	    public void run() {
		 Socket FileSocket = null;
		if (FileSocket == null) {
		    try {
			FileSocket = new Socket(Ip,9091) ;
//				SocketMaster.getSocket(Ip, 9091);
			FileSocket.setReceiveBufferSize(1048576);
		    } catch (IOException e2) {
			e2.printStackTrace();
		    }
		} else if ((FileSocket.getInetAddress().getHostAddress()
			.equals(Ip) && FileSocket.isClosed())
			|| FileSocket.isClosed()) {
		    try {
			FileSocket = new Socket(Ip,9091) ;
//				SocketMaster.getSocket(Ip, 9091);
			FileSocket.setReceiveBufferSize(1048576);
		    } catch (IOException e2) {
			e2.printStackTrace();
		    }
		}

		// try {
		// if (FileSocket != null && FileSocket.isConnected()) {
		// CliFace.getInstance().showText("в doSend не было закрыто");
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
				    CliFace.getInstance().getMyNick(),
				    JOptionPane.YES_NO_CANCEL_OPTION);
		}
		try (OutputStream Ous = new BufferedOutputStream(FileSocket
			.getOutputStream(), 20971520);
			InputStream Ins = new BufferedInputStream(
				new FileInputStream(CliFace.getFb()), 20971520);) {
		    CliFace.getInstance().showText("getRemoteSocketAddress"
			    + FileSocket.getRemoteSocketAddress());
		    CliFace.getInstance().showText("getInetAddress"
			    + FileSocket.getInetAddress());
		    byte[] buffArr = new byte[20971520];
		    long sended = 0;
		    int recivd = 0;
		    while ((recivd = Ins.read(buffArr)) != -1
			    && fileSize >= sended) {
			CliFace.getInstance().showText("передача WHILE");
			sended += recivd;
			Ous.write(buffArr, 0, recivd);
			Ous.flush();
		    }
		} catch (IOException e) {
		    CliFace.getInstance().showText("IO Ex Send ОК");
		    try {
			if (FileSocket != null && FileSocket.isConnected()) {
			    FileSocket.close();
			}
		    } catch (IOException e1) {
			CliFace.getInstance().showText("IO Ex-ex Send ОК");
			e1.printStackTrace();
		    }
		    e.printStackTrace();
		} finally {
		    CliFace.getInstance().showText("finall se");
		    try {
			if (FileSocket != null && FileSocket.isConnected()) {
			    CliFace.getInstance().showText("finall se if");
			    FileSocket.close();
			}
		    } catch (IOException e) {
			CliFace.getInstance().showText("finall se if ex");
			e.printStackTrace();
		    }
		}
		CliFace.getInstance().showText("Передача ОК");
	    }
	}).start();
    }
}
