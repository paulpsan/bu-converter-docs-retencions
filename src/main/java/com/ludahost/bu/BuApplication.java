package com.ludahost.bu;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class BuApplication implements CommandLineRunner {

	public static void main(String[] args) {
//		SpringApplication.run(BuApplication.class, args);
		new SpringApplicationBuilder(BuApplication.class).headless(false).run(args);
//		String fileName = "d:\\pdf\\CARTA_AUT_0711.docx";
//
//
	}
	@Override
	public void run(String... args) throws IOException {
//		JFrame.setDefaultLookAndFeelDecorated(true);
//		JDialog.setDefaultLookAndFeelDecorated(true);
//		JFrame frame = new JFrame("JComboBox Test");
//		frame.setLayout(new FlowLayout());
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		JButton button = new JButton("Select File");
//		button.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				JFileChooser fileChooser = new JFileChooser();
//				int returnValue = fileChooser.showOpenDialog(null);
//				if (returnValue == JFileChooser.APPROVE_OPTION) {
//					File selectedFile = fileChooser.getSelectedFile();
//					System.out.println(selectedFile);
//					try {
//						setFile(selectedFile.getAbsolutePath());
//					} catch (IOException ex) {
//						throw new RuntimeException(ex);
//					}
//				}
//			}
//		});
////		frame.add(text, BorderLayout.CENTER);
//		frame.add(button, BorderLayout.CENTER);
//		frame.pack();
//		frame.setVisible(true);

		String fileName = "d:\\pdf\\CARTA_AUT_0711.docx";
		setFile(fileName);
	}

	public void setFile(String fileName) throws IOException {
		try (XWPFDocument doc = new XWPFDocument(
			Files.newInputStream(Paths.get(fileName)))) {
			//using XWPFWordExtractor Class
			XWPFWordExtractor we = new XWPFWordExtractor(doc);
			String content = we.getText();
			List<String> arrayContent =Arrays.asList(content.split("\n"));
			List<PageBuDto> pages = new ArrayList<>();
			PageBuDto pageBu = new PageBuDto();
			for (int i = 0; i < arrayContent.size(); i++)
			{
				System.out.println(arrayContent.get(i)+ " -->");
				if(arrayContent.get(i).startsWith("RPC/")){
					pageBu.setNota(arrayContent.get(i));
					pageBu.setNota1(arrayContent.get(++i));
					pageBu.setNota2(arrayContent.get(++i));
					pageBu.setNota3(arrayContent.get(++i));
					pageBu.setNota4(arrayContent.get(++i));
					pageBu.setNota5(arrayContent.get(++i));
					pageBu.setNota6(arrayContent.get(++i));
				}

				if(arrayContent.get(i).startsWith("La Paz,"))
					pageBu.setFecha(arrayContent.get(i));
				if(arrayContent.get(i).startsWith("CITE:"))
					pageBu.setCite(arrayContent.get(i));
				if(arrayContent.get(i).startsWith("Señor")){
					pageBu.setSr(arrayContent.get(i));
					pageBu.setNombre(arrayContent.get(++i));
					pageBu.setCargo(arrayContent.get(++i));
					pageBu.setCiudad(arrayContent.get(++i));
				}
				if(arrayContent.get(i).startsWith("Ref."))
					pageBu.setRef(arrayContent.get(i));
				if(arrayContent.get(i).startsWith("De nuestra")){
					pageBu.setSaludo(arrayContent.get(i));
					i++;
					pageBu.setContenido(arrayContent.get(++i));
				}
				//TODO tabla detalle
				if(arrayContent.get(i).startsWith("Sin otro"))
					pageBu.setDespedida(arrayContent.get(i));

				if(arrayContent.get(i).startsWith("Al respecto,")){
					pageBu.setTitleDetalle(arrayContent.get(i)+arrayContent.get(++i));
				}
				if(pageBu.getTitleDetalle()!=null){
					pages.add(pageBu);
					pageBu = new PageBuDto();
				}
			}
			System.out.println(pages);
		}
	}
}
