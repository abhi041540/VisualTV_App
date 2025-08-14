package ott_app;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginDialog extends JDialog implements ActionListener
{
	Font fn; JLabel tl,l2,l3;JTextField t2;
	JPasswordField p1;JPanel p;JButton b1;
	JFrame f; Connection con;
     public LoginDialog(JFrame f,String str)
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
    	tl.setBounds(new Rectangle(270,20,getWidth()/2,40));
    	add(tl);
    	 p= new JPanel();
    	 p.setOpaque(false);
    	 p.setBackground(new Color(0,0,0,0));
    	 p.setLayout(new GridLayout(3,2,10,10));
    	 
    	 l2= new JLabel("Enter Email :");
    	 l3= new JLabel("Enter Password :");
    	
    	 t2= new JTextField(20);
    	 p1= new JPasswordField(10);
    	
    	 l2.setFont(fn);
    	 l3.setFont(fn);
    	
    	 t2.setFont(fn);
    	 p1.setFont(fn);
    
    	 l2.setForeground(Color.white);
    	 l3.setForeground(Color.white);
    	 b1=new JButton("Continue");
 		b1.setBackground(new Color(0,20,155));
 		b1.setForeground(Color.white);
 		b1.setBounds(260,260,260,50);
 		b1.setFont(fn);
 		add(b1);
 		b1.setBorderPainted(false);
    	p.add(l2);p.add(t2);p.add(l3);p.add(p1);
    	 p.setBounds(145,150,500,120);
    	 add(p);
    	 b1.addActionListener(this);
    	 setVisible(true);
    	
     }
	@Override
	public void actionPerformed(ActionEvent e) 
	{
	
		String email= t2.getText(),pass= p1.getText();
		
		if(!email.isEmpty() && !pass.isEmpty() && email!=null && pass!=null )
		{
		
		  try 
		   {
			con= DataBase.getConnection();
			CallableStatement cs= con.prepareCall("call getUserData(?)");
			cs.setString(1,email);
		    boolean b=cs.execute();
		    if(b==true)
		    {
		    	ResultSet rs=cs.getResultSet();
		    	if(rs.next())
		    	{
		    	    if(email.equalsIgnoreCase(rs.getString("email")) && pass.equalsIgnoreCase(rs.getString("password")))
		    	    	{
		    	    	
		    	    	 try {
							new HomeWindow("Visual-Tv",f,email);
						    } catch (Exception e1)
		    	    	 {
						    	System.out.println("LoginDialog");
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		    	    	 f.dispose();
						t2.setText(null);
						p1.setText(null);  
		    	    	}
		    	    else
		    	    {
		    	    	JOptionPane.showMessageDialog(this, "Your Password Is Incorrect Try Again");
						t2.setText(null);
						p1.setText(null);
		    	    }
		    	}
		    	else
		    	{
		    		JOptionPane.showMessageDialog(this, "No User Found Please Signup");
					t2.setText(null);
					p1.setText(null);
		    	}
		    }
		    else
		    {
		    	JOptionPane.showMessageDialog(this, "No User Found Please Signup");
				t2.setText(null);
				p1.setText(null);
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
		}
	}
}
