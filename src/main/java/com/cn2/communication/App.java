package com.cn2.communication;

import java.io.*;
import java.net.*;
import javax.sound.sampled.*;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;
import java.lang.Thread;

public class App extends Frame implements WindowListener, ActionListener {

	static TextField inputTextField;		
	static JTextArea textArea;				 
	static JFrame frame;					
	static JButton sendButton;				
	static JTextField messageTextField;		  
	public static Color gray;				
	final static String newline="\n";		
	static JButton callButton;		
	
	public App(String title) {
		
		// setting up the characteristics of the frame
		super(title);									
		gray = new Color(254, 254, 254);		
		setBackground(gray);
		setLayout(new FlowLayout());			
		addWindowListener(this);	
		
		// setting up the TextField and the TextArea
		inputTextField = new TextField();
		inputTextField.setColumns(20);
		
		// setting up the TextArea.
		textArea = new JTextArea(10,40);			
		textArea.setLineWrap(true);				
		textArea.setEditable(false);			
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		// setting up the buttons
		sendButton = new JButton("Send");			
		callButton = new JButton("Call");			
						
		add(scrollPane);								
		add(inputTextField);
		add(sendButton);
		add(callButton);
		
		sendButton.addActionListener(this);			
		callButton.addActionListener(this);	
	}
	
	public static void main(String[] args){
	
		App app = new App("Chat-VoIP");																		  
		app.setSize(500,250);				  
		app.setVisible(true);				  

		// start a thread to listen for incoming messages
        Thread receiveThread = new Thread(() -> {
            DatagramSocket socket = null;
            try {
				// listen to port 12345 for inbound text messages
                socket = new DatagramSocket(12345);
                byte[] buffer = new byte[1024];

				// continuously listen for incoming messages
                while(true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                    textArea.append("Remote: " + receivedMessage + newline);
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            } finally {
                if(socket != null && !socket.isClosed()) {
                    socket.close();
                }
            }
        });
        receiveThread.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == sendButton) {
            handleSendButton();
        } else if (e.getSource() == callButton) {
			// TODO: implement call button functionality
        }
	}

	private void handleSendButton() {
        try {
            // get user input
            String message = inputTextField.getText();
            byte[] buffer  = message.getBytes();
            InetAddress receiverAddress = InetAddress.getByName("RECEIVER_IP"); // replace with actual receiver IP
            int port = 12345; // text message port
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, receiverAddress, port);

            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            socket.close();

            textArea.append("Local: " + message + newline);
            inputTextField.setText("");
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
    }

	/**
	 * These methods have to do with the GUI. You can use them if you wish to define
	 * what the program should do in specific scenarios (e.g., when closing the 
	 * window).
	 */
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		dispose();
        	System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub	
	}
}