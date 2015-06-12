package qum.chatClient;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import qum.messageClass.Mess;

public class RegisterFrame {
    static JLabel infoLable;
    static RegisterFrame RegFr;
    static JFrame frame;
    private JTextField loginField;
    private JPasswordField passField;
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
			|| String.valueOf(passField.getPassword()).isEmpty()) {

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

		    if (SocketMaster.getMainChatSocket() == null) {
			if (SocketMaster.initMainSocket()) {
			    SocketMaster.getInstance().sendMess(new Mess(
				    loginField.getText(), String.valueOf(passField.getPassword()),
				    emailField.getText(),
				    SocketMaster.REGISTER_REQUEST));
			    System.out.println("REG_REQUEST - send");
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
			if (!SocketMaster.getInstance().sendMess(new Mess(loginField
				.getText(), passField.getText(), emailField
				.getText(), SocketMaster.REGISTER_REQUEST))) {
			    infoLable.setIcon(new ImageIcon(RegisterFrame.class
				    .getResource("/res/att.png")));
			    infoLable.setForeground(Color.RED);
			    infoLable
				    .setText("<html><i>Ќету св€зи с сервером!</i></html>");
			    SocketMaster.getInstance().setTotalNullMdf();
			    System.gc();
			} else {
			    System.out.println("REG_REQUEST - send");
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

	loginField = new JTextField(20);
	loginField.setToolTipText("\u043C\u0430\u043A\u0441\u0438\u043C\u0443\u043C 20 \u0441\u0438\u043C\u0432\u043E\u043B\u043E\u0432");
	loginField.setBounds(44, 21, 137, 20);
	loginField.addActionListener(doRegRequest);
	loginField.setColumns(10);

	passField = new JPasswordField(20);
	passField.setToolTipText("\u043C\u0430\u043A\u0441\u0438\u043C\u0443\u043C 20 \u0441\u0438\u043C\u0432\u043E\u043B\u043E\u0432");
	passField.setBounds(44, 64, 137, 20);
	passField.addActionListener(doRegRequest);
	passField.setColumns(10);

	emailField = new JTextField();
	emailField.setBounds(44, 106, 137, 20);
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
	lblLogin.setBounds(44, 6, 46, 14);

	JLabel lblPassword = new JLabel("Password");
	lblPassword.setBounds(44, 53, 74, 14);

	JLabel lblEmail = new JLabel(
		"Email (\u043D\u0435 \u043E\u0431\u044F\u0437\u0430\u0442\u0435\u043B\u044C\u043D\u043E)");
	lblEmail.setBounds(44, 85, 137, 35);

	infoLable = new JLabel("");

	infoLable.setHorizontalAlignment(SwingConstants.LEFT);
	infoLable.setBounds(16, 132, 207, 51);
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
