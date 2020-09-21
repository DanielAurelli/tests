package https;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class LeitorPorURL {
    public static void main(String[] args) {

        final JFrame janela = new JFrame("Leitor usando openStream() de URL");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font fonte = new Font("Arial", Font.PLAIN, 12);
        JLabel pedeURL = new JLabel("Informe URL:");
        pedeURL.setFont(fonte);
        final JTextField inputURL = new JTextField(
                "http://www.consist.com.br/index.html", 40);
        inputURL.setFont(fonte);
        JButton mostrarRecursos = new JButton("Mostrar recurso");
        mostrarRecursos.setFont(fonte);
        final JTextArea mostradorRecursos = new JTextArea(10, 60);
        mostradorRecursos.setFont(fonte);

        Box entrada = Box.createHorizontalBox();
        entrada.add(Box.createHorizontalStrut(20));
        entrada.add(pedeURL);
        entrada.add(Box.createHorizontalStrut(20));
        entrada.add(inputURL);
        entrada.add(Box.createHorizontalStrut(20));

        Box entradaMaisBotao = Box.createVerticalBox();
        entradaMaisBotao.add(Box.createVerticalStrut(20));
        entradaMaisBotao.add(entrada);
        entradaMaisBotao.add(Box.createVerticalStrut(20));
        entradaMaisBotao.add(mostrarRecursos);
        entradaMaisBotao.add(Box.createVerticalStrut(20));

        JScrollPane visorComponentes = new JScrollPane(mostradorRecursos,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        Container suporte = janela.getContentPane();
        suporte.add(entradaMaisBotao, BorderLayout.NORTH);
        suporte.add(visorComponentes, BorderLayout.CENTER);

        mostrarRecursos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    URL recurso = new URL(inputURL.getText());
                    BufferedReader entradaRecurso = new BufferedReader(
                            new InputStreamReader(recurso.openStream()));
                    String linhaEntrada;
                    mostradorRecursos.setText(null);
                    while ((linhaEntrada = entradaRecurso.readLine()) != null) {
                        mostradorRecursos.append("\n" + linhaEntrada);
                    }
                    entradaRecurso.close();
                } catch (MalformedURLException excecao) {
                    JOptionPane.showMessageDialog(janela, "URL mal formada",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    excecao.printStackTrace();
                } catch (IOException excecao) {
                    JOptionPane.showMessageDialog(janela, "Falha de E/S",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    excecao.printStackTrace();
                }
            }
        });

        janela.setBounds(10, 10, 640, 480);
        janela.setVisible(true);
    }
}
