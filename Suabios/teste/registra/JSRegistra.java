package registra;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class JSRegistra{
	

	public static void main(String [] args){
		
		//Exibi��o de mensagem para orientar o usu�rio acerca da grava��o.
		System.out.println("--- INICIALIZANDO TESTE DE �UDIO ---");
		
		//Inicializa��o do programa atrav�s de um try para informar ao usu�rio caso seja verificado uma exce��o.
		try{
			
			//O primeiro passo � especificar uma vari�vel para receber as especifica��es
			//do formato do �udio a ser lido.
			AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

			//Cria��o de um objeto para armazenar as informa��es do �udio a ser lido.
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			//Caso o �udio seja diferente das especifica��es de formato, ser� exibida
			//uma mensagem para alertar o usu�rio.
			if(!AudioSystem.isLineSupported(info)){
				System.err.println(">>> FORMATO N�O SUPORTADO! <<<");
			}
				
			//Recebe todas as especifica��es de �udio no objeto "targetLine".
			final TargetDataLine targetLine = (TargetDataLine)AudioSystem.getLine(info);
			//Habilita todos os recursos para inicializar a grava��o, embora n�o come�e ainda a gravar.
			targetLine.open();
			
			//Exibi��o de mensagem para o usu�rio se preparar para a grava��o.
			System.out.println("--- INICIALIZANDO GRAVA��O ---");
			//In�cio, efetivo, da grava��o.
			targetLine.start();
			
			//Cria��o de uma Thread para que seja poss�vel a cria��o de uma nova linha de execu��o do programa.
			Thread thread = new Thread(){
				
				//M�todo an�nimo para in�cio da Thread.
				@Override public void run(){
					
					//Constru��o de um AudioInputStream que siga as especifica��es da targetLine.
					AudioInputStream audioStream = new AudioInputStream(targetLine);
					//Cria��o de um arquivo onde ficar� armazenado o �udio gravado.
					File audioFile = new File("teste.wav");
					
					//M�todo try para certificar-se de que tudo que for capturado pelo microfone ser�
					//escrito em um arquivo do tipo WAVE, se isso n�o acontecer, a exce��o ser� impressa
					//para o usu�rio.
					try {
						AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
					//Mensagem ao usu�rio informando a finaliza��o da grava��o.
					System.out.println("--- GRAVA��O FINALIZADA ---");
					
				}
				
			};
				//M�todo de chamada da thread alternativa.
				thread.start();
				//M�todo que coloca a Thread principal para esperar durante 15 segundos, que ser� o tempo
				//de dura��o do �udio a ser gravado.
				Thread.sleep(15000);
				//Finaliza��o da grava��o.
				targetLine.stop();
				//Fechamento de todos os recursos que habilitam a captura de �udio.
				targetLine.close();
				
		}
		//Captura de exce��es para o caso do �udio n�o estar dispon�vel ou seja interrompido.
		catch(LineUnavailableException lue){ lue.printStackTrace();}
		catch(InterruptedException ie){ ie.printStackTrace();}
		
		
	}
	
}