package ott_app;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;



public class SignupDialog  extends JDialog implements ActionListener
{
	Font fn; JLabel tl,l1,l2,l3;JTextField t1,t2;
	JFrame f; Connection con;
	JPasswordField p1;JPanel p;JButton b1;
     public SignupDialog(JFrame f,String str)
     {
    	 super(f,str);
    	 this.f=f;
    	 
    	 fn= new Font("Arial",Font.BOLD,20);
    	 setResizable(false);
    	 setModal(true);
    	 setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    	 setSize(800, 500);
    	 setIconImage(Image_Box.LOGO);
    	 setLocationRelativeTo(f);
    	 getContentPane().setBackground(new Color(0,0,13));
    	 setLayout(null);
    	 tl= new JLabel(str);
    	 tl.setForeground(Color.white);
    	 tl.setFont(new Font("Arial",Font.BOLD,30));
    	addMouseListener(new MouseAdapter()
    	{
    		 @Override
    		 public void mouseClicked(MouseEvent me)
    		 {
    			 System.out.println(me.getLocationOnScreen().x+","+me.getLocationOnScreen().y);
    		 }
    	});
    	tl.setBounds(new Rectangle(260,20,362,40));
    	add(tl);
    	 p= new JPanel();
    	 p.setOpaque(false);
    	 p.setBackground(new Color(0,0,0,0));
    	 p.setLayout(new GridLayout(3,2,10,10));
    	 l1= new JLabel("Enter Name :");
    	 l2= new JLabel("Enter Email :");
    	 l3= new JLabel("Enter Password :");
    	 t1= new JTextField(10);
    	 t2= new JTextField(20);
    	 p1= new JPasswordField(10);
    	 l1.setFont(fn);
    	 l2.setFont(fn);
    	 l3.setFont(fn);
    	 t1.setFont(fn);
    	 t2.setFont(fn);
    	 p1.setFont(fn);
    	 l1.setForeground(Color.white);
    	 l2.setForeground(Color.white);
    	 l3.setForeground(Color.white);
    	 b1=new JButton("Continue");
 		b1.setBackground(new Color(0,20,155));
 		b1.setForeground(Color.white);
 		b1.setBounds(260,290,260,50);
 		b1.setFont(fn);
 		add(b1);
 		b1.setBorderPainted(false);
    	 p.add(l1);p.add(t1);p.add(l2);p.add(t2);p.add(l3);p.add(p1);
    	 p.setBounds(145,120,500,120);
    	 add(p);
    	 b1.addActionListener(this);
    	 setVisible(true);
     }
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String email= t2.getText(),pass=p1.getText(),name=t1.getText();
		
		if(!email.isEmpty() && !pass.isEmpty() && email!=null && pass!=null && !name.isEmpty() && name!=null)
		{
	  try 
	   {
		con= DataBase.getConnection();
		con.setAutoCommit(false);
		java.sql.CallableStatement cs= con.prepareCall("call getUserData(?)");
		cs.setString(1,email);
	    boolean b=cs.execute();
	    if(b==true)
	    {
	    	ResultSet rs=cs.getResultSet();
	    	if(rs.next())
	    	{
	    	    	JOptionPane.showMessageDialog(this, "This Email Already In Use");
					t2.setText(null);
					p1.setText(null);
	    	}  
	    	else
	    	{
	    		String rand=getRandom();
	    		Runnable rn= ()->
	    		 {
	    			emailSend(rand,email,name);
	    			return;
	    		 };
	    		Thread th= new Thread(rn,"emailThread");
	    		th.start();
	    		String otp=JOptionPane.showInputDialog(this, "Enter The Email Verification OTP","Verification",JOptionPane.QUESTION_MESSAGE);
	    		if(otp!=null && !otp.isEmpty())
	    		{
	    			if(otp.equalsIgnoreCase(rand))
	    			{
	    				PreparedStatement ps=con.prepareStatement("insert into userdata(name,email,password) values(?,?,?)");
	    				ps.setString(1, name);
	    				ps.setString(2, email);
	    				ps.setString(3, pass);
	    				ps.executeUpdate();
	    				con.commit();
	    				ps.close();
	    				JOptionPane.showMessageDialog(this, "Signup Successfully");
	    				t2.setText(null);
	    				p1.setText(null);
	    				t1.setText(null);
	    			}
	    			else
	    			{
	    				JOptionPane.showMessageDialog(this, "Incorrect OTP");
	    				t2.setText(null);
	    				p1.setText(null);
	    				t1.setText(null);
	    			}
	    		}
	    		
	    	}
	    }
		con.close();
		cs.close();
	   }
	  catch (SQLException e1) 
	  {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	  }
	}
		else
		{

			JOptionPane.showMessageDialog(this, "Enter Required Data To Continue");
			t2.setText(null);
			p1.setText(null);
			t1.setText(null);
		}
	}
	public String getRandom()
	{
		String rand="";
		for(int i=1;i<=4;i++)
		{
			rand =rand+ (int)(Math.random()*10);
		}
		return rand;
	}
	public void emailSend(String otp,String email,String name)
	{
		String from = "tellyking15@gmail.com";
        String host = "smtp.gmail.com";
        final String username = "tellyking15@gmail.com";
        final String password = "mnwbweyfpbojwhdt";
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth","true");
        Session session= Session.getInstance(properties, new Authenticator()
        {
        	 protected PasswordAuthentication getPasswordAuthentication()
             {
                 return (new PasswordAuthentication(username, password));
             }
		});
        try
        {
        	 MimeMessage message = new MimeMessage(session);

             message.setFrom(new InternetAddress(from));

             message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

             message.setSubject("Your Visual TV Verification Code");
           
             message.setText("Dear "+name+",\r\n"
             		+ "\r\n"
             		+ "Welcome to Visual TV!\nTo complete your registration and verify your email address, please use the following verification code:\n"+ "\r\n"+"=====>>>>["+otp+"]<<<<======\n"
             		+ "\r\n"
             		+ "Please enter this code on the Visual TV app or website to activate your account.\r\n"
             		+ "\r\n"
             		+ "If you did not request this verification code, please ignore this email or contact our support team for assistance.\r\n"
             		+ "\r\n"
             		+ "Thank you for choosing Visual TV. Enjoy your streaming experience!\r\n"
             		+ "\r\n"
             		+ "Best regards,\r\n"
             		+ "The Visual TV Team ("+new SimpleDateFormat("yyyy").format(new Date())+")!");

             Transport.send(message);
             System.out.println("Sent message successfully....");
        }
        catch(Exception e)
        {
//        	e.printStackTrace();
        }
	}
	}

