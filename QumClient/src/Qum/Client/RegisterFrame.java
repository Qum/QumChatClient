package Qum.Client;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import Qum.Mes.Mess;

import javax.swing.UIManager;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

import java.awt.Color;

public class RegisterFrame {
    static JLabel infoLable;
    static RegisterFrame RegFr;
    static JFrame frame;
    private JTextField loginField;
    private JTextField passField;
    private JTextField emailField;

    /**
     * Create the application.
     */
    public RegisterFrame() {
	initialize();
    }

    /**
     * Initialize the contents of the frame.
     * 
     * @return
     */
    public static RegisterFrame initRegisterFrame() {
	if (RegFr == null) {
	    EventQueue.invokeLater(new Runnable() {
		public void run() {
		    try {
			RegFr = new RegisterFrame();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
	    });
	}
	return RegFr;
    }

    private void initialize() {

	ActionListener doRegRequest = new ActionListener() {

	    public void actionPerformed(ActionEvent e) {
		if (loginField.getText().isEmpty()
			|| passField.getText().isEmpty()) {

		    infoLable.setIcon(new ImageIcon(RegisterFrame.class
			    .getResource("/res/att.png")));
		    infoLable.setForeground(Color.RED);
		    infoLable
			    .setText("<html><i>Ћогин и пароль не<br>могут быть пустыми!!</i></html>");

		} else if (loginField.getText().equalsIgnoreCase("admin")
			|| loginField.getText().equalsIgnoreCase("root")
			|| loginField.getText().equalsIgnoreCase("sys")) {
		    infoLable.setIcon(new ImageIcon(RegisterFrame.class
			    .getResource("/res/att.png")));
		    infoLable.setForeground(Color.RED);
		    infoLable
			    .setText("<html><i>Ёто запрещенный ник!!</i></html>");
		} else {

		    if (SocketMaster.Sock == null) {
			if (SocketMaster.initSocket()) {
			    new Thread(new SocketMaster(), "SocketMaster")
				    .start();
			    SocketMaster.sendMess(new Mess(
				    loginField.getText(), passField.getText(),
				    emailField.getText(),
				    SocketMaster.REGISTER_REQUEST));
			}
		    } // если получилось св€затса с сервером,то отправл€ем
		      // запрос на авторизацию
		    else {
			if (!SocketMaster.sendMess(new Mess(loginField
				.getText(), passField.getText(), emailField
				.getText(), SocketMaster.REGISTER_REQUEST))) {
			    infoLable.setIcon(new ImageIcon(RegisterFrame.class
				    .getResource("/res/att.png")));
			    infoLable.setForeground(Color.RED);
			    infoLable
				    .setText("<html><i>Ќету св€зи с сервером!</i></html>");
			    SocketMaster.Sock = null;
			}
		    }

		}
	    }
	};

	frame = new JFrame();
	frame.setTitle("Register");
	frame.setResizable(false);
	frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
		RegisterFrame.class.getResource("/res/ico.png")));
	frame.setBounds(100, 100, 235, 290);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	loginField = new JTextField();
	loginField.setBounds(66, 21, 86, 20);
	loginField.addActionListener(doRegRequest);
	loginField.setColumns(10);

	passField = new JTextField();
	passField.setBounds(66, 64, 86, 20);
	passField.addActionListener(doRegRequest);
	passField.setColumns(10);

	emailField = new JTextField();
	emailField.setBounds(44, 111, 137, 20);
	emailField.addActionListener(doRegRequest);
	emailField.setColumns(10);

	JButton registerButt = new JButton("OK");
	registerButt.setIcon(new ImageIcon(RegisterFrame.class
		.getResource("/res/menuIcon_9.png")));
	registerButt.setBounds(58, 185, 105, 23);
	registerButt.addActionListener(doRegRequest);

	JButton cancelButt = new JButton("Cancel");
	cancelButt.setIcon(new ImageIcon(RegisterFrame.class
		.getResource("/res/menuIcon_10.png")));
	cancelButt.setBounds(58, 220, 105, 23);
	cancelButt.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		frame.setVisible(false);
		StartFrame.frame.setVisible(true);
	    }
	});

	JLabel lblLogin = new JLabel("Login");
	lblLogin.setBounds(66, 6, 46, 14);

	JLabel lblPassword = new JLabel("Password");
	lblPassword.setBounds(66, 52, 74, 14);

	JLabel lblEmail = new JLabel(
		"email (\u043D\u0435 \u043E\u0431\u044F\u0437\u0430\u0442\u0435\u043B\u044C\u043D\u043E)");
	lblEmail.setBounds(44, 89, 137, 26);

	infoLable = new JLabel("");

	infoLable.setHorizontalAlignment(SwingConstants.LEFT);
	infoLable.setBounds(32, 132, 161, 51);
	infoLable.setFont(UIManager.getFont("ToolBar.font"));
	frame.getContentPane().setLayout(null);
	frame.getContentPane().add(registerButt);
	frame.getContentPane().add(cancelButt);
	frame.getContentPane().add(infoLable);
	frame.getContentPane().add(loginField);
	frame.getContentPane().add(lblLogin);
	frame.getContentPane().add(passField);
	frame.getContentPane().add(lblPassword);
	frame.getContentPane().add(emailField);
	frame.getContentPane().add(lblEmail);

	frame.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent event) {
		frame.setVisible(false);
		StartFrame.frame.setVisible(true);
	    }

	});
    }
}
