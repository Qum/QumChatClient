package qum.Client;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import qum.Mes.Mess;

public class SocketMaster extends Thread {

    private static Socket MainChatSocket;

    private static ObjectInputStream Oin;
    private static ObjectOutputStream Oou;
    static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    static Date date = new Date();
    public Mess BuffMesObj;
    private static SocketMaster SMaster;
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
    final static int CHANGE_NAME = 12;
    final static int CHANGE_NAME_SUCCESS = 13;
    final static int CHANGE_NAME_FAIL = 14;

    public static SocketMaster getInstance() {
	if (SMaster == null || SMaster.getState() == Thread.State.TERMINATED) {
	    System.err.println("SMaster = null - " + SMaster);
	    SMaster = new SocketMaster();

	    SMaster.start();

	} else {
	    System.err.println("SMaster != null");
	}

	System.err.println("Current Th - " + SMaster);
	System.err.println("Current Th - Status Alive " + SMaster.isAlive());
	System.err.println("Current Th - State " + SMaster.getState());
	return SMaster;
    }

    private SocketMaster() {

    }

    public static boolean initMainSocket() {
	try {

	    MainChatSocket = getSocket("109.87.60.178", 9090);
	    if (MainChatSocket == null) {
		System.out.println("initMainSocket - null");
		Oin = null;
		Oou = null;
	    } else {
		System.out.println("initMainSocket - !null");
		Oin = new ObjectInputStream(MainChatSocket.getInputStream());
		Oou = new ObjectOutputStream(MainChatSocket.getOutputStream());
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return !(MainChatSocket == null);
    }

    public void run() {
	System.out.println("run");
	retrieveMessages();
	System.out.println("SMaster.isInterrupted - Status "
		+ SMaster.isInterrupted());
	System.out.println("run end - Status Alive " + SMaster.isAlive());
    }

    public boolean sendMess(Mess M) {
	try {
	    Oou.writeObject(M);
	    return true;
	} catch (IOException e) {
	    return false;
	}
    }

    public static Socket getSocket(String addres, int port) {
	Socket Soc = null;
	try {
	    Soc = new Socket(addres, port);
	} catch (IOException e) {
	    e.getStackTrace();
	}
	return Soc;
    }

    private void retrieveMessages() {

	System.out.println("retrieveMessages");
	try {
	    while (true) {
		BuffMesObj = (Mess) Oin.readObject();
		if (BuffMesObj.getServiceCode() > 0) {
		    if (BuffMesObj.getServiceCode() == AUTH_FAIL) {
			System.out.println("AUTH_FAIL");
			StartFrame.infoLable.setIcon(new ImageIcon(
				StartFrame.class.getResource("/res/att.png")));
			StartFrame.infoLable.setForeground(Color.RED);
			StartFrame.infoLable
				.setText("<html><i>Неправильный логин или пароль!!</i></html>");
		    } else if (BuffMesObj.getServiceCode() == SUCCESS_AUTH_SUCCESS_ONLINE) {
			CliFace.getInstance().setMyNick(BuffMesObj.getValue1());
			CliFace.getInstance()
				.getFrame()
				.setTitle(
					"Hawaii Chat      "
						+ BuffMesObj.getValue1());
			StartFrame.frame.setVisible(false);
			CliFace.getInstance().getFrame().setVisible(true);
		    } else if (BuffMesObj.getServiceCode() == SUCCESS_AUTH_ALREADY_ONLINE) {
			StartFrame.infoLable.setIcon(new ImageIcon(
				StartFrame.class.getResource("/res/att.png")));
			StartFrame.infoLable.setForeground(Color.RED);
			StartFrame.infoLable
				.setText("<html><i>Этот пользователь уже в сети!!</i></html>");
		    } else if (BuffMesObj.getServiceCode() == REGISTER_SUCCESS) {
			CliFace.getInstance().setMyNick(BuffMesObj.getValue1());
			CliFace.getInstance().getCurrentUserIs()
				.setText("   " + BuffMesObj.getValue1());
			RegisterFrame.frame.setVisible(false);
			CliFace.getInstance().getFrame().setVisible(true);
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
		}  else {
		    CliFace.getInstance().showText(
			    BuffMesObj.getValue1() + " : "
				    + BuffMesObj.getValue2() + newline);
		}
	    }
	} catch (SocketException e) {
	    System.out.println("retrieveMessages - SocketExcepti0n");
	    CliFace.getInstance()
		    .showText(
			    ("SYS : нету соединения с сервером(возможно он выключен), либо у программы проблемы ")
				    + newline
				    + (" с выходом в итернет.Попробуйте отключить антивирусне программы или фаерволл!!"));
	} catch (IOException e) {
	    e.printStackTrace();
	    CliFace.getInstance()
		    .showText(
			    ("SYS : нету соединения с сервером(возможно он выключен), либо у программы проблемы ")
				    + newline
				    + (" с выходом в итернет.Попробуйте отключить антивирусне программы или фаерволл."));
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	} finally {
	    System.out.println("retrieveMessages-finally");
	    sendMess(new Mess("", "", LOGOUT));
	    if (!(MainChatSocket == null)) {
		System.out.println("retrieveMessages-finally-IF");
		try {
		    MainChatSocket.close();
		} catch (IOException e) {
		    System.out.println("retrieveMessages-finally-IF-ex");
		}
	    }
	    System.out.println("retrieveMessages-finally-after IF");
	    MainChatSocket = null;
	    interrupt();
	}
    }

    public void setTotalNullMdf() {
	System.out.println("setTotalNullMdf");
	MainChatSocket = null;
	SMaster = null;
	Oin = null;
	Oou = null;
    }

    public static Socket getMainChatSocket() {
	return MainChatSocket;
    }
}