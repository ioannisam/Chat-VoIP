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

    // call variables
    private DatagramSocket sendSocket;
    private DatagramSocket receiveSocket;
    private boolean        callActive = false;      
    
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
            if(callActive) {
                endCall();
                callButton.setText("Call");
            } else {
                handleCallButton();
                callButton.setText("End Call");
            }
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

    private void handleCallButton() {
        callActive = true;
    
        Thread voiceThread = new Thread(() -> {
            final TargetDataLine[] microphone = new TargetDataLine[1];
            final SourceDataLine[] speaker    = new SourceDataLine[1];
            try {
                AudioFormat   format      = new AudioFormat(8000, 8, 1, true, true); // PCM format
                DataLine.Info micInfo     = new DataLine.Info(TargetDataLine.class, format);
                DataLine.Info speakerInfo = new DataLine.Info(SourceDataLine.class, format);
    
                microphone[0] = (TargetDataLine) AudioSystem.getLine(micInfo);
                speaker[0] = (SourceDataLine) AudioSystem.getLine(speakerInfo);
    
                microphone[0].open(format);
                speaker[0].open(format);
    
                microphone[0].start();
                speaker[0].start();
    
                // separate sockets for sending and receiving
                sendSocket = new DatagramSocket();
                receiveSocket = new DatagramSocket(12346);
    
                InetAddress receiverAddress = InetAddress.getByName("RECEIVER_IP"); // Replace with actual receiver IP
                int port = 12346; // VoIP port
                byte[] buffer = new byte[1024];
    
                // thread to capture and send audio
                Thread sendThread = new Thread(() -> {
                    try {
                        while(callActive) {
                            int bytesRead = microphone[0].read(buffer, 0, buffer.length);
                            if(bytesRead > 0) {
                                DatagramPacket packet = new DatagramPacket(buffer, bytesRead, receiverAddress, port);
                                sendSocket.send(packet);
                            }
                        }
                    } catch(Exception ex) {
                        System.err.println("Error in sendThread: " + ex.getMessage());
                        ex.printStackTrace();
                    } finally {
                        if (sendSocket != null && !sendSocket.isClosed()) {
                            sendSocket.close();
                        }
                    }
                });
                sendThread.start();
    
                // continuously receive and play audio
                while(callActive) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    receiveSocket.receive(packet);
                    speaker[0].write(packet.getData(), 0, packet.getLength());
                }
            } catch(Exception ex) {
                System.err.println("Error in voiceThread: " + ex.getMessage());
                ex.printStackTrace();
            } finally {
                if(sendSocket != null && !sendSocket.isClosed()) {
                    sendSocket.close();
                }
                if(receiveSocket != null && !receiveSocket.isClosed()) {
                    receiveSocket.close();
                }
                if(microphone[0] != null) {
                    microphone[0].close();
                }
                if(speaker[0] != null) {
                    speaker[0].close();
                }
            }
        });
        voiceThread.start();
    }
    
    private void endCall() {
        callActive = false;
        if(sendSocket != null && !sendSocket.isClosed()) {
            sendSocket.close();
        }
        if(receiveSocket != null && !receiveSocket.isClosed()) {
            receiveSocket.close();
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