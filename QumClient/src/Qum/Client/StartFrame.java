package Qum.Client;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import Qum.Mes.Mess;

import java.awt.Font;

public class StartFrame {

    static JLabel infoLable;
    public static JFrame frame;
    private JTextField txtLogin;
    private JPasswordField pwdPass;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		try {
		    StartFrame window = new StartFrame();
		    window.frame.setVisible(true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    /**
     * Create the application.
     */
    public StartFrame() {
	initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
	RegisterFrame.initRegisterFrame();
	CliFace.initCliFace();
	frame = new JFrame();
	frame.setTitle("Hawaii Chat - Login");
	frame.setResizable(false);
	frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
		StartFrame.class.getResource("/res/ico.png")));
	frame.setBounds(100, 100, 235, 290);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	ActionListener doAutgRequest = new ActionListener() {

	    public void actionPerformed(ActionEvent e) {
		if (txtLogin.getText().isEmpty() || pwdPass.getText().isEmpty()) {
		    infoLable.setIcon(new ImageIcon(StartFrame.class
			    .getResource("/res/att.png")));
		    infoLable.setForeground(Color.RED);
		    infoLable
			    .setText("<html><i>Ћогин и пароль не  могут <br> быть пустыми!!</i></html>");
		} else {
		    if (SocketMaster.Sock == null) {
			if (SocketMaster.initSocket()) {
			    new Thread(new SocketMaster(), "SocketMaster")
				    .start();
			    SocketMaster.sendMess(new Mess(txtLogin.getText(),
				    pwdPass.getText(),
				    SocketMaster.AUTH_REQUEST));
			} else {
			    infoLable.setIcon(new ImageIcon(RegisterFrame.class
				    .getResource("/res/att.png")));
			    infoLable.setForeground(Color.RED);
			    infoLable
				    .setText("<html><i>Ќету св€зи с сервером!</i></html>");
			}
		    } // если получилось св€затса с сервером,то отправл€ем
		      // запрос на авторизацию
		    else {
			if (!SocketMaster.sendMess(new Mess(txtLogin.getText(),
				pwdPass.getText(), SocketMaster.AUTH_REQUEST))) {
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

	JButton enterButt = new JButton("\u0412\u0445\u043E\u0434");
	enterButt.setBounds(64, 151, 84, 25);
	enterButt.setIcon(new ImageIcon(StartFrame.class
		.getResource("/res/menuIcon_4.png")));
	enterButt.setVerticalAlignment(SwingConstants.TOP);
	enterButt.addActionListener(doAutgRequest);

	JButton registerButt = new JButton(
		"\u0420\u0435\u0433\u0438\u0441\u0442\u0440\u0430\u0446\u0438\u044F");
	registerButt.setBounds(42, 188, 135, 23);
	registerButt.setIcon(new ImageIcon(StartFrame.class
		.getResource("/res/menuIcon_8.png")));
	registerButt.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		RegisterFrame.frame.setVisible(true);
		frame.setVisible(false);
	    }
	});

	JButton restorPasswordButt = new JButton(
		"\u0412\u043E\u0441\u0441\u0442\u0430\u043D\u043E\u0432\u043B\u0435\u043D\u0438\u0435 \u043F\u0430\u0440\u043E\u043B\u044F");
	restorPasswordButt.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
	    }
	});
	restorPasswordButt.setBounds(16, 218, 197, 23);
	restorPasswordButt.setIcon(new ImageIcon(StartFrame.class
		.getResource("/res/menuIcon_6.png")));

	txtLogin = new JTextField();
	txtLogin.setBounds(42, 24, 140, 22);
	txtLogin.setToolTipText("");
	txtLogin.addActionListener(doAutgRequest);
	txtLogin.setColumns(10);

	pwdPass = new JPasswordField();
	pwdPass.setBounds(43, 80, 139, 22);
	pwdPass.addActionListener(doAutgRequest);

	JLabel lblNewLabel = new JLabel("Login");
	lblNewLabel.setBounds(42, 6, 31, 16);

	JLabel lblNewLabel_1 = new JLabel("Password");
	lblNewLabel_1.setBounds(42, 64, 89, 16);

	infoLable = new JLabel("");

	infoLable.setFont(new Font("SansSerif", Font.PLAIN, 12));
	infoLable.setHorizontalAlignment(SwingConstants.LEFT);
	infoLable.setVerticalAlignment(SwingConstants.TOP);
	infoLable.setBounds(6, 102, 217, 38);
	frame.getContentPane().setLayout(null);
	frame.getContentPane().add(lblNewLabel);
	frame.getContentPane().add(lblNewLabel_1);
	frame.getContentPane().add(txtLogin);
	frame.getContentPane().add(pwdPass);
	frame.getContentPane().add(infoLable);
	frame.getContentPane().add(registerButt);
	frame.getContentPane().add(enterButt);
	frame.getContentPane().add(restorPasswordButt);
	frame.getContentPane().setFocusTraversalPolicy(
		new FocusTraversalOnArray(new Component[] { enterButt,
			restorPasswordButt, registerButt }));
    }
}
