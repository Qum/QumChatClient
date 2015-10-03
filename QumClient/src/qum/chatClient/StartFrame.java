package qum.chatClient;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import qum.messageClass.Mess;

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
     * @wbp.parser.entryPoint
     */
    public StartFrame() {
	initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
	RegisterFrame.initRegisterFrame();
	CliFace.getInstance();
	frame = new JFrame();
	frame.setTitle("Hawaii Chat - Login");
	frame.setResizable(false);
	frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
		StartFrame.class.getResource("/res/ico.png")));
	frame.setBounds(100, 100, 235, 290);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//Enter Button
	ActionListener doAuthRequest = new ActionListener() {

	    public void actionPerformed(ActionEvent e) {
		if (txtLogin.getText().isEmpty() || String.valueOf(pwdPass.getPassword()).isEmpty()) {
		    infoLable.setIcon(new ImageIcon(StartFrame.class
			    .getResource("/res/att.png")));
		    infoLable.setForeground(Color.RED);  
		    infoLable
			    .setText("<html><i>Ћогин и пароль не  могут <br> быть пустыми!!</i></html>");
		} else {
		    if (SocketMaster.getMainChatSocket() == null) {
			if (SocketMaster.initMainSocket()) {
			    SocketMaster.getInstance().sendMess(
				    new Mess(txtLogin.getText(), String.valueOf(pwdPass.getPassword()),
					    SocketMaster.AUTH_REQUEST));
			    System.out.println("AUTH_REQUEST - send");
			} else {
			    System.out.println("else");
			    infoLable.setIcon(new ImageIcon(RegisterFrame.class
				    .getResource("/res/att.png")));
			    infoLable.setForeground(Color.RED);
			    infoLable
				    .setText("<html><i>Ќету св€зи с сервером!</i></html>");
			}
		    } // если получилось св€затса с сервером,то отправл€ем
		      // запрос на авторизацию
		    else {
			if (!SocketMaster.getInstance().sendMess(
				new Mess(txtLogin.getText(), String.valueOf(pwdPass.getPassword()),
					SocketMaster.AUTH_REQUEST))) {
			    System.out.println("ELSE - ! sended");
			    infoLable.setIcon(new ImageIcon(RegisterFrame.class
				    .getResource("/res/att.png")));
			    infoLable.setForeground(Color.RED);
			    infoLable
				    .setText("<html><i>Ќету св€зи с сервером!</i></html>");
			    SocketMaster.getInstance().setTotalNullMdf();
			    System.gc(); //plz ))
			} else
			    System.out.println("ELSE - sended");
		    }
		}
	    }
	};

	JButton enterButt = new JButton("\u0412\u0445\u043E\u0434");
	enterButt.setBounds(68, 157, 84, 23);
	enterButt.setIcon(new ImageIcon(StartFrame.class
		.getResource("/res/menuIcon_4.png")));
	enterButt.setVerticalAlignment(SwingConstants.TOP);
	enterButt.addActionListener(doAuthRequest);

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

	restorPasswordButt.setBounds(16, 218, 197, 23);
	restorPasswordButt.setIcon(new ImageIcon(StartFrame.class
		.getResource("/res/menuIcon_6.png")));

	txtLogin = new JTextField(20);
	txtLogin.setBounds(42, 24, 140, 22);
	txtLogin.setToolTipText("");
	txtLogin.addActionListener(doAuthRequest);
	txtLogin.setColumns(10);

	pwdPass = new JPasswordField(20);
	pwdPass.setToolTipText("");
	pwdPass.setBounds(43, 80, 139, 22);
	pwdPass.addActionListener(doAuthRequest);

	JLabel lblNewLabel = new JLabel("Login");
	lblNewLabel.setBounds(42, 6, 31, 16);

	JLabel lblNewLabel_1 = new JLabel("Password");
	lblNewLabel_1.setBounds(42, 64, 89, 16);

	infoLable = new JLabel("");

	infoLable.setFont(UIManager.getFont("ToolBar.font"));
	infoLable.setHorizontalAlignment(SwingConstants.LEFT);
	infoLable.setVerticalAlignment(SwingConstants.TOP);
	infoLable.setBounds(16, 108, 207, 42);
	frame.getContentPane().setLayout(null);
	frame.getContentPane().add(lblNewLabel);
	frame.getContentPane().add(lblNewLabel_1);
	frame.getContentPane().add(txtLogin);
	frame.getContentPane().add(pwdPass);
	frame.getContentPane().add(infoLable);
	frame.getContentPane().add(registerButt);
	frame.getContentPane().add(enterButt);
	frame.getContentPane().add(restorPasswordButt);

	JButton btnNewButton = new JButton("User 2");
	btnNewButton.addActionListener(new ActionListener() {
	
	    public void actionPerformed(ActionEvent e) {
		txtLogin.setText("Test_II");
		pwdPass.setText("pass");
		doAuthRequest.actionPerformed(e);
	    }
	});
	
	btnNewButton.setBounds(115, 45, 97, 23);
	frame.getContentPane().add(btnNewButton);

	JButton btnTestUser = new JButton("User 1");
	btnTestUser.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		txtLogin.setText("Test_I");
		pwdPass.setText("pass");
		doAuthRequest.actionPerformed(arg0);
	    }
	});
	btnTestUser.setBounds(6, 45, 97, 23);
	frame.getContentPane().add(btnTestUser);
	frame.getContentPane().setFocusTraversalPolicy(
		new FocusTraversalOnArray(new Component[] { enterButt,
			restorPasswordButt, registerButt }));
    }
}
