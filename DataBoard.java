package mydashBoard;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.awt.event.ActionEvent;
import java.util.*;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class DataBoard extends JFrame {

	private JPanel contentPane;
	int point = -1;
	String title = "untitiled";
	JLabel chart;
	JTextPane desc;
	JTextPane ltitle;
	private JButton btnPrevious;
	private JLabel lblTitile;
	private JSeparator separator;
	private JSeparator separator_1;
	private JLabel lblLogo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DataBoard frame = new DataBoard();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public ImageIcon ScaleMyImage(String url, JLabel label){
		BufferedImage img = null;
		try{
			img = ImageIO.read(new File(url));
		}catch(Exception e){}
		Image dimg = img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon icon = new ImageIcon(dimg);
		return icon;
	}
	public JLabel slideMaker(String url,JLabel label){
		
		ImageIcon Img = ScaleMyImage(url,label);
		label.setIcon(Img);
		return label;
	}

	/**
	 * Create the frame.
	 */
	public DataBoard() {
		setTitle("Dashboard");
		//String des;
		//String path;
		List<String> pathList = new ArrayList<String>();
		List<StringBuilder> desList = new ArrayList<StringBuilder>();
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 833, 506);
		contentPane = new JPanel();
		contentPane.setForeground(Color.WHITE);
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		chart = new JLabel("Chart");
		chart.setHorizontalAlignment(SwingConstants.CENTER);
		chart.setForeground(Color.WHITE);
		chart.setBackground(Color.GREEN);
		chart.setBounds(193, 11, 614, 344);
		contentPane.add(chart);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(193, 359, 614, 97);
		contentPane.add(scrollPane);
		
		desc = new JTextPane();
		scrollPane.setViewportView(desc);
		
		desc.setForeground(Color.WHITE);
		desc.setEditable(false);
		desc.setBackground(Color.BLACK);
		desc.setToolTipText("");
		//scrollPane.setViewportView(desc);
		
		JButton btnNewButton = new JButton("Search File");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc;
				fc = new JFileChooser();
				int retVal = fc.showOpenDialog(null);
				if(retVal != fc.APPROVE_OPTION) return;
				File myFile = fc.getSelectedFile();
				String fileloc = myFile.getAbsolutePath();
				try {
					FileReader fr = new FileReader(fileloc);
					BufferedReader br = new BufferedReader(fr);
					String fileType = br.readLine();
					System.out.println(fileType);
					if(!fileType.equals("dbf")){
						JOptionPane.showMessageDialog(null,"Only dbf files allowed");
					}
					else{
						System.out.println("success!");
						String str;
						String tag_str = "notInititalized";
						while((str = br.readLine())!=null){
							if(str.equals("path")){
								tag_str = str;
								str = br.readLine();
								pathList.add(str);
							}
							else if(str.equals("desc")){
								tag_str = str;
								str = br.readLine();
								StringBuilder sb = new StringBuilder(str);
								desList.add(sb);
							}
							else if(str.equals("title")){
								str = br.readLine();
								title = str;
							}
							else if(tag_str.equals("path")){
								pathList.add(str);
							}
							else if(tag_str.equals("desc")){
								desList.get(desList.size()-1).append("\n" + str);
							}
							
						}
						ltitle.setText(title);
						/*
						for(int i=0;i<pathList.size();i++){
							System.out.println(pathList.get(i));
							System.out.println(desList.get(i));
						}
						*/
					}
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		btnNewButton.setBounds(10, 11, 125, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(point+1<pathList.size()){
				point += 1;
				String filelocation = pathList.get(point);
				String description = desList.get(point).toString();
				chart = slideMaker(filelocation,chart);
				desc.setText(description);
				}
			}
		});
		btnNext.setBounds(10, 45, 125, 23);
		contentPane.add(btnNext);
		
		btnPrevious = new JButton("Previous");
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(point-1>=0){
				point -= 1;
				String filelocation = pathList.get(point);
				String description = desList.get(point).toString();
				chart = slideMaker(filelocation,chart);
				desc.setText(description);
				}
			}
		});
		btnPrevious.setBounds(10, 79, 125, 23);
		contentPane.add(btnPrevious);
		
		lblTitile = new JLabel("Titile");
		lblTitile.setForeground(Color.WHITE);
		lblTitile.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitile.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTitile.setBounds(10, 123, 125, 35);
		contentPane.add(lblTitile);
		
		separator = new JSeparator();
		separator.setBounds(10, 156, 161, 2);
		contentPane.add(separator);
		
		separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(175, 11, 2, 445);
		contentPane.add(separator_1);
		
		lblLogo = new JLabel("By Sharon");
		lblLogo.setForeground(Color.WHITE);
		lblLogo.setBounds(10, 342, 155, 14);
		contentPane.add(lblLogo);
		
		ltitle = new JTextPane();
		ltitle.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ltitle.setForeground(Color.WHITE);
		ltitle.setBackground(Color.DARK_GRAY);
		ltitle.setEditable(false);
		ltitle.setText("Untitled Data Board");
		ltitle.setBounds(10, 161, 161, 87);
		contentPane.add(ltitle);
		
	}
}
