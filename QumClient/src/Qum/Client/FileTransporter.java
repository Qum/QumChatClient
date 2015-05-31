package Qum.Client;

import java.io.IOException;
import java.util.Formatter;

import javax.swing.JOptionPane;

import Qum.Mes.Mess;

public class FileTransporter {

    private static String sendFileName;
    private static String reciver;
    private static long fileSize;

    public static void reqestForSending(String reciverNick, String fileName,
	    long size) {

	SocketMaster.sendMess(new Mess(reciverNick, fileName, size,
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
		    SocketMaster.sendMess(new Mess(whoSendReqest, "",
			    SocketMaster.FILE_REQUEST_SUCCESS));
		    doRecive(fileSenderIp);
		} else if (response == 1 || response == -1) { // нет, не
							      // принимать
		    SocketMaster.sendMess(new Mess(whoSendReqest, "",
			    SocketMaster.FILE_REQUEST_FAIL));
		}
	    }
	}
	new AskObj(senderNick, senderIp);
    }

    public static void doRecive(String Ip) { // не готов

	JOptionPane.showMessageDialog(null, "<html><i>Принимаю.</i></html>",
		CliFace.MyNick, JOptionPane.YES_NO_CANCEL_OPTION);
	// try {

	// ServerSocket SS = new ServerSocket(9091, 5);
	// Socket S = SS.accept();
	//
	//
	//
	// File fb = new File("C:/Users/ZM/Desktop/reciv/" + sendFileName);
	// InputStream bin = new BufferedInputStream(S.getInputStream(),
	// 131072);
	// OutputStream Ous = new BufferedOutputStream(new FileOutputStream(
	// CliFace.fb), 131072);
	// byte[] buffArr = null;
	// while(bin.read(buffArr) != -1){
	//
	// }
	//
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
    }

    public static void doSend(String Ip) throws IOException { // не готов

	JOptionPane.showMessageDialog(null, "<html><i>Передаю.</i></html>",
		CliFace.MyNick, JOptionPane.YES_NO_CANCEL_OPTION);

	// Socket S = new Socket(Ip, 9091);
    }
}
