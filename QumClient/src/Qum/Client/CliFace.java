package Qum.Client;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Qum.Mes.Mess;

public class CliFace {

    public static CliFace face;

    public JFrame frmQumChat;

    public static File fb;
    
    private JTextField textEnter;
    public static JTextArea textArea;

    static Date date = new Date();

    public static String MyNick, buffnick = null;

    // private static File prop;
    // private static Properties sett;
    protected FileInputStream in;
    static int win_WIDTH = 110;
    static int win_HEIGHT = 110;

    private CliFace() {
	initialize();
    }

    public static CliFace initCliFace() {
	if (face == null) {
	    EventQueue.invokeLater(new Runnable() {
		public void run() {
		    try {
			face = new CliFace();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
	    });
	}
	return face;
    }

    private void initialize() {
	frmQumChat = new JFrame();
	frmQumChat.setTitle("Hawaii Chat");
	frmQumChat.setResizable(false);
	frmQumChat.setIconImage(Toolkit.getDefaultToolkit().getImage(
		CliFace.class.getResource("/res/ico.png")));
	frmQumChat.setBounds(100, 100, 550, 360);
	frmQumChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	// String root_papka = System.getProperty("user.home");
	// File propdr = new File(root_papka, ".chatik");
	// if (!propdr.exists())
	// propdr.mkdir();
	// prop = new File(propdr, "chat.ini");
	// Properties def_sett = new Properties();
	// def_sett.put("Nick", "");
	// def_sett.put("������ ����", "543");
	// def_sett.put("������ ����", "363");
	//
	// sett = new Properties(def_sett);
	//
	// if (prop.exists()) {
	// try {
	// in = new FileInputStream(prop);
	// sett.load(in);
	// } catch (IOException e) {
	// System.out.println("�������� : ������ � ��������� �������");
	// } finally {
	// try {
	// in.close();
	// } catch (IOException e1) {
	// System.out
	// .println("�������� : ������ � ��������� ����������");
	// e1.printStackTrace();
	// }
	// }
	// }
	// if (sett.getProperty("Nick").isEmpty())
	// greetingQuest();
	// else {
	// MyNick = sett.getProperty("Nick");
	// }
	// win_WIDTH = Integer.parseInt(sett.getProperty("������ ����"));
	// win_HEIGHT = Integer.parseInt(sett.getProperty("������ ����"));
	// if (MyNick.isEmpty())
	// greetingQuest();
	textArea = new JTextArea();
	textArea.setBorder(new LineBorder(new Color(0, 0, 0)));
	textArea.setFocusable(false);
	textArea.setFont(new Font("Arial", Font.PLAIN, 12));
	textArea.setTabSize(9);
	JScrollPane scrollPane = new JScrollPane(textArea);
	scrollPane.setBounds(0, 0, 522, 213);
	textArea.setWrapStyleWord(true);

	textArea.setLineWrap(true);
	textEnter = new JTextField();
	textEnter.setBorder(new EmptyBorder(0, 0, 0, 0));
	textEnter.setFocusCycleRoot(true);
	textEnter
		.setText("\u041F\u0440\u0438\u0432\u0435\u0442 \u043D\u0430\u0440\u043E\u0434!");
	textEnter.setBounds(0, 214, 522, 57);
	textEnter.setFont(new Font("Segoe UI Semibold", Font.BOLD, 13));
	textEnter.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
	textEnter.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		String text;
		if (textEnter.getText().isEmpty()) {
		    JOptionPane
			    .showMessageDialog(
				    null,
				    "<html><i>��������� �� ����� ���� ������!!</i></html>",
				    "��� ������ ������.",
				    JOptionPane.YES_NO_CANCEL_OPTION);
		    return;
		} else {
		    text = textEnter.getText();
		}

		if (!SocketMaster.sendMess(new Mess(SocketMaster.dateFormat
			.format(date) + " " + MyNick, text))) {
		    System.out.println("CliFace : Err in Text Action");
		    textArea.append(("SYS : ���� ����� � ��������.")
			    + SocketMaster.newline);
		} // TO DO ���� ����������� � ������ ������ �������

		textEnter.selectAll();
		textEnter.setText(null);
		textArea.setCaretPosition(textArea.getDocument().getLength());
	    }
	});
	textEnter.setBackground(SystemColor.activeCaption);
	textEnter.setColumns(1);
	textArea.setBackground(SystemColor.inactiveCaption);
	frmQumChat.getContentPane().setLayout(null);
	frmQumChat.getContentPane().add(textEnter);
	frmQumChat.getContentPane().add(scrollPane);

	JMenuBar menuBar = new JMenuBar();
	frmQumChat.setJMenuBar(menuBar);

	JMenu mnNewMenu = new JMenu("����");
	mnNewMenu.setHideActionText(true);
	menuBar.add(mnNewMenu);

	JMenuItem menuItem_1 = new JMenuItem(
		"\u041F\u0435\u0440\u0435\u0441\u043B\u0430\u0442\u044C \u0444\u0430\u0439\u043B");
	menuItem_1.setIcon(new ImageIcon(CliFace.class
		.getResource("/res/menuIcon_5.png")));
	mnNewMenu.add(menuItem_1);

	JMenuItem menuItem = new JMenuItem(
		"\u0421\u043C\u0435\u043D\u0438\u0442\u044C \u043D\u0438\u043A");
	menuItem.setIcon(new ImageIcon(CliFace.class
		.getResource("/res/menuIcon_2.png")));

	menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		buffnick = JOptionPane.showInputDialog("����� ���");
		if (buffnick.equalsIgnoreCase("admin")
			|| buffnick.equalsIgnoreCase("root")
			|| buffnick.equalsIgnoreCase("sys")) {
		    JOptionPane
			    .showMessageDialog(
				    null,
				    "<html><h1><i>��� ����������� ���!!</i></h1><hr>&#822;�&#822;�&#822;�&#822;�&#822;�&#822; &#822;�&#822;�&#822;�&#822;�&#822;�&#822;�&#822;�&#822; </html>",
				    "information",
				    JOptionPane.YES_NO_CANCEL_OPTION);
		    actionPerformed(e);
		} else if (buffnick.isEmpty()) {
		    JOptionPane
			    .showMessageDialog(
				    null,
				    "<html><h1><i>��� �� ����� ���� ������!</i></h1></html>",
				    "information",
				    JOptionPane.YES_NO_CANCEL_OPTION);
		    actionPerformed(e);
		} else
		    MyNick = buffnick;
	    }
	});

	mnNewMenu.add(menuItem);

	JMenuItem menuItem_2 = new JMenuItem(
		"\u0421\u043C\u0435\u043D\u0438\u0442\u044C \u0441\u0435\u0440\u0432\u0435\u0440");
	menuItem_2.setIcon(new ImageIcon(CliFace.class
		.getResource("/res/menuIcon_3.png")));
	mnNewMenu.add(menuItem_2);

	menuItem_2.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(null,
			"� ��� ������ ���� ������, ���� ���", "information",
			JOptionPane.YES_NO_CANCEL_OPTION);
	    }
	});

	menuItem_1.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent event) {

		JFileChooser MisterCho;
		MisterCho = new JFileChooser();
		MisterCho.setCurrentDirectory(new File("."));
		int result = MisterCho.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
		    String name = MisterCho.getSelectedFile().getPath();
		    File fb = new File(name);
		    SocketMaster.sendMess(new Mess(JOptionPane
			    .showInputDialog("���� ?"), fb.getName(), fb
			    .length(), SocketMaster.Sock.getInetAddress()
			    .getHostAddress(), SocketMaster.FILE_REQUEST));
		}
	    }
	});

	frmQumChat.addWindowListener(new WindowAdapter() { // ������ ����� ��
		    // �������� ����
		    public void windowClosing(WindowEvent event) {

			SocketMaster.sendMess(new Mess(" ", "",
				SocketMaster.LOGOUT));

			// sett.put("WIDTH", "" + frame.getWidth());
			// sett.put("HEIGHT", "" + frame.getHeight());
			// sett.put("Nick", MyNick);
			// try {
			// FileOutputStream out = new FileOutputStream(prop);
			// sett.store(out, "prog options");
			// } catch (IOException e) {
			// System.out
			// .println("�������� : ������� ����� � ������ ������");
			// }
		    }
		});
	frmQumChat.setSize(528, 320);
    }

//    public static void greetingQuest() {  ��� �� ������������
//	System.out.println("frame : Gree in");
//	if (MyNick == null) {
//	    System.out.println("frame : MyNick == null");
//	    buffnick = JOptionPane.showInputDialog("�������� ���");
//	    if (buffnick.equals("root")) {
//		System.out.println("frame : buffnick.equals root");
//		JOptionPane
//			.showMessageDialog(
//				null,
//				"<html><h1><i>��� ����������� ���!!</i></h1><hr>&#822;�&#822;�&#822;�&#822;�&#822;�&#822; &#822;�&#822;�&#822;�&#822;�&#822;�&#822;�&#822;�&#822; </html>",
//				"information", JOptionPane.YES_NO_CANCEL_OPTION);
//		greetingQuest();
//	    } else if (buffnick.isEmpty()) {
//		System.out.println("frame : buffnick==null");
//		JOptionPane
//			.showMessageDialog(
//				null,
//				"<html><h1><i>��� �� ����� ���� ������!</i></h1></html>",
//				"information", JOptionPane.YES_NO_CANCEL_OPTION);
//		greetingQuest();
//	    } else {
//		MyNick = buffnick;
//	    }
//	}
//    }
}