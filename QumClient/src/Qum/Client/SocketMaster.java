package Qum.Client;

import Qum.Client.CliFace;
import Qum.Mes.Mess;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class SocketMaster implements Runnable {

    public static Socket Sock;
    public static ObjectInputStream Oin;
    public static ObjectOutputStream Oou;
    static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    static Date date = new Date();
    public Mess BuffMesObj;
    static int posled, posledOtpr = 0;
    public final static String newline = "\n";

    final static int AUTH_REQUEST = 1;
    final static int REGISTER_REQUEST = 2;
    final static int AUTH_FAIL = 3;
    final static int SUCCESS_AUTH_ALREADY_ONLINE = 4;
    final static int SUCCESS_AUTH_SUCCESS_ONLINE = 5;
    final static int REGISTER_SUCCESS = 6;
    final static int REGISTER_FAIL = 7;
    final static int FILE_REQUEST = 8;
    final static int FILE_REQUEST_SUCCESS = 9;
    final static int FILE_REQUEST_FAIL = 10;
    final static int LOGOUT = 11;

    // SocketMaster(){
    //
    // }
    //
    // SocketMaster(Mess Mesage){
    // sendMess(new Mess(loginField
    // .getText(), passField.getText(), emailField
    // .getText(), SocketMaster.REGISTER_REQUEST));
    // }

    public void run() {
	// log
	retrieveMessages();
    }

    public static boolean sendMess(Mess M) {
	try {
	    Oou.writeObject(M);
	    return true;
	} catch (IOException e) {
	    e.printStackTrace();
	    return false;
	}
    }

    public static boolean initSocket() {

	try {
		InetAddress local = InetAddress.getLocalHost(); 
	    Sock = new Socket(InetAddress.getByName(local.getHostAddress()), 9090);//тестово здесь вставляется НАШ  IP т к сервер на этой же машине,а так тут должен быть IP сервера!
	    Oin = new ObjectInputStream(Sock.getInputStream());
	    Oou = new ObjectOutputStream(Sock.getOutputStream());

	} catch (IOException e) {
	    e.printStackTrace();
	}

	return !(Sock == null);
    }

    private void retrieveMessages() {
	try {
	    while (true) {
		BuffMesObj = (Mess) Oin.readObject();
		if (BuffMesObj.getServiceCode() > 0) {
		    
		    if (BuffMesObj.getServiceCode() == AUTH_FAIL) {
			
			StartFrame.infoLable.setIcon(new ImageIcon(
				StartFrame.class.getResource("/res/att.png")));
			StartFrame.infoLable.setForeground(Color.RED);
			StartFrame.infoLable
				.setText("<html><i>Неправильный логин или пароль!!</i></html>");

		    } else if (BuffMesObj.getServiceCode() == SUCCESS_AUTH_SUCCESS_ONLINE) {
			
			CliFace.MyNick = BuffMesObj.getValue1();
			StartFrame.frame.setVisible(false);
			CliFace.face.frmQumChat.setVisible(true);

		    } else if (BuffMesObj.getServiceCode() == SUCCESS_AUTH_ALREADY_ONLINE) {
			
			StartFrame.infoLable.setIcon(new ImageIcon(
				StartFrame.class.getResource("/res/att.png")));
			StartFrame.infoLable.setForeground(Color.RED);
			StartFrame.infoLable
				.setText("<html><i>Этот пользователь уже в сети!!</i></html>");

		    } else if (BuffMesObj.getServiceCode() == REGISTER_SUCCESS) {

			CliFace.MyNick = BuffMesObj.getValue1();
			RegisterFrame.frame.setVisible(false);
			CliFace.face.frmQumChat.setVisible(true);

		    } else if (BuffMesObj.getServiceCode() == REGISTER_FAIL) {

			RegisterFrame.infoLable
				.setIcon(new ImageIcon(RegisterFrame.class
					.getResource("/res/att.png")));
			RegisterFrame.infoLable.setForeground(Color.RED);
			RegisterFrame.infoLable
				.setText("<html><i>Такой пользователь<br>уже зарегистрирован!!</i></html>");

		    } else if (BuffMesObj.getServiceCode() == FILE_REQUEST) {

			FileTransporter.showReciveRequest(
				BuffMesObj.getValue1(), BuffMesObj.getValue2(),
				BuffMesObj.getFileSize(),
				BuffMesObj.getValue3());

		    } else if (BuffMesObj.getServiceCode() == FILE_REQUEST_SUCCESS) {

			FileTransporter.doSend(BuffMesObj.getValue2());

		    } else if (BuffMesObj.getServiceCode() == FILE_REQUEST_FAIL) {

			JOptionPane.showMessageDialog(null,
				"Пользователь отказался принимать файл!",
				"Error!", JOptionPane.ERROR_MESSAGE);

		    }
		} else {

		    CliFace.textArea.append(BuffMesObj.getValue1() + " : "
			    + BuffMesObj.getValue2() + newline);
		}
	    }

	} catch (IOException e) {
	    CliFace.textArea
		    .append(("SYS : нету соединения с сервером(возможно он выключен), либо у программы проблемы ")
			    + newline
			    + (" с выходом в итернет.Попробуйте отключить антивирусне программы или фаерволл."));
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	} finally {
	    try {
		sendMess(new Mess(" ", "", LOGOUT));
		if (Sock != null)
		    Sock.close();
	    } catch (IOException e) {
		// log
	    }
	}
    }

}