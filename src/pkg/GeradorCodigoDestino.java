package pkg;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import jcc.*;
public class GeradorCodigoDestino {
      public static void geraCodigoAssembler(LinkedList<Comando> listaComandos) {
            BufferedWriter arqSaida;
            try {
                  arqSaida = new BufferedWriter(new FileWriter("prog_destino.j"));
                  arqSaida.write(".source prog_destino.java\r\n");
                  arqSaida.write(".class public prog_destino\r\n");
                  arqSaida.write(".super java/lang/Object\r\n");
                  arqSaida.write(".method public <init>()V\r\n");
                  arqSaida.write(".limit stack 1\r\n");
                  arqSaida.write(".limit locals 1\r\n");
                  arqSaida.write("aload_0\r\n");
                  arqSaida.write("invokespecial java/lang/Object/<init>()V\r\n");
                  arqSaida.write("return\r\n");
                  arqSaida.write(".end method\r\n");
                  arqSaida.write(".method public static main([Ljava/lang/String;)V\r\n");
                  arqSaida.write(".limit stack 4\r\n");  
                  arqSaida.write(".limit locals 100\r\n"); // m�ximo de vari�veis locais (deve ser calculado)
                  arqSaida.write(processaListaComandos(listaComandos));
                  arqSaida.write("return\r\n");
                  arqSaida.write(".end method\r\n");
                  arqSaida.close();
            }
            catch(IOException e) {
                  System.out.println("Problemas no arquivo 'prog_destino.j'");
            }
            catch(Exception e) {
                  System.out.println(e.getMessage());
            }
      }
      static String processaListaComandos(LinkedList<Comando> listaComandos) {
            String saida = "";
            Comando com;
            String nomeDaVariavel, codigoExpressao;
            int referenciaDaVariavel;
            for(int i = 0; i < listaComandos.size(); i++) {
                  com = (Comando)listaComandos.get(i);
                  nomeDaVariavel = (String)com.getRef1();
                  referenciaDaVariavel = gl.tabela.consultaReferencia(nomeDaVariavel);
                  // se � uma atribui��o
                  if(com.getTipo() == 'A') {
                        codigoExpressao = geraCodigoExpressao((LinkedList<Item>)(com.getRef2()));
                        saida += codigoExpressao;
                        saida += "dstore " + referenciaDaVariavel + "\r\n";
                  }
                  // se � um comando de exibi��o
                  if(com.getTipo() == 'E') {
                        saida += "getstatic java/lang/System/out Ljava/io/PrintStream;\r\n";
                        saida += "dload " + referenciaDaVariavel + "\r\n";
                        saida += "invokevirtual java/io/PrintStream/println(D)V\r\n";
                  }
            }
            return saida;
      }
      static String geraCodigoExpressao(LinkedList<Item> listaExp) {
            String saida = "";
            Item item;
            for(int i = 0; i < listaExp.size(); i++) {
                  item = listaExp.get(i);
                  // se � um n�mero
                  if(item.getTipo() == 'n')
                        saida += "ldc2_w " + item.getvalor() + ".0\r\n";
                  // se � um operador de adi��o
                  if(item.getTipo() == 'o')
                        saida += "dadd\r\n";
                  // se � uma vari�vel
                  if(item.getTipo() == 'v') {
                        String nomeDaVariavel = item.getvalor();
                        int referenciaDaVariavel = gl.tabela.consultaReferencia(nomeDaVariavel);
                        saida += "dload " + referenciaDaVariavel + "\r\n";
                  }
            }
            return saida;
      }
}