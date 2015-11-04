package captura;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class JSCaptura{
	
	
	//M�todo principal da classe.
	public static void main(String []args){
		
		//Especifica��es do formato de �udio a ser lido.
		AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
		
		//Cria��o do m�todo try para tratar as poss�veis exce��es.
		try {
			
			//Defini��o de um objeto respons�vel por armazenar as especifica��es do �udio.
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			//Obten��o das mesmas informa��es atrav�s da classe SourceDataLine.
			final SourceDataLine sourceLine = (SourceDataLine)AudioSystem.getLine(info);
			//Habilita os recursos necess�rios para reprodu��o do �udio.
			sourceLine.open();

			//Defini��o de uma classe TargetDataLine com as mesmas especifica��es mencionadas anteriormente.
			info = new DataLine.Info(TargetDataLine.class, format);
			//Obten��o das especifica��es de �udio atrav�s da classe TargetDataLine dessa vez.
			final TargetDataLine targetLine = (TargetDataLine)AudioSystem.getLine(info);
			//Habilita os recursos necess�rios para a captura do �udio.
			targetLine.open();

			//Objeto criado para armazenar a entrada de �udio e reproduz�-lo, sem a necessidade da cria��o de um arquivo.
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			//Cria��o de uma thread para reprodu��o do �udio.
			Thread sourceThread = new Thread(){
				//M�todo p�blico e an�nimo de inicializa��o da mesma.
				@Override public void run(){
					//Inicializa��o da reprodu��o.
					sourceLine.start();
					//Enquanto o �udio estiver em reprodu��o o array ser� verificado e reproduzido at� o seu tamanho.
					while(true){
						sourceLine.write(out.toByteArray(), 0, out.size());
					}
				}
			};
			
			//Crian��o de uma thread para a grava��o do �udio.
			Thread targetThread = new Thread(){
				//M�todo p�blico e an�nimo de inicializa��o da mesma.
				@Override public void run(){
					//Inicializa��o da grava��o.
					targetLine.start();
					//Cria��o de um byte array para coletar os dados provenientes da grava��o, diminuindo o seu tamanho para otimizar a reprodu��o.
					byte[] data = new byte[targetLine.getBufferSize()/5];
					int readBytes;
					
					while(true){
						//Objeto para retornar o tamanho do buffer.
						readBytes = targetLine.read(data, 0, data.length);
						//Escrita de dados no objeto de respons�vel pela reprodu��o do �udio, at� o tamanho verificado anteriormente.
						out.write(data, 0, readBytes);
					}
				}
			};
		
			//In�cio da thread respons�vel pela grava��o do �udio.
			targetThread.start();
			//Exibi��o de mensagens para orientar o usu�rio quanto ao prosseguimento do programa.
			System.out.println(">>>>> INICIALIZANDO TESTE DE �UDIO <<<<<");
			System.out.println("--- INICIALIZANDO GRAVA��O ---");
			//Thread principal ir� "dormir" por 5 segundos, tempo em que ocorrer� a captura do �udio.
			Thread.sleep(5000);
			//Finaliza��o da captura.
			targetLine.stop();
			//Fechamento dos recursos que habilitam a captura de �udio.
			targetLine.close();
			System.out.println("--- GRAVA��O FINALIZADA ---");
			
			System.out.println("--- REPRODU��O DO �UDIO CAPTURADO ---");
			
			//In�cio da thread de reprodu��o do �udio.
			sourceThread.start();
			//Thread principal ir� "dormir" por 5 segundos, tempo em que ocorrer� a reprodu��o do �udio.
			Thread.sleep(5000);
			//Finaliza��o da reprodu��o.
			sourceLine.stop();
			//Fechamento dos recursos que habilitam a reprodu��o de �udio.
			sourceLine.close();
			
			System.out.println("--- REPRODU��O FINALIZADA ---");
			
		//Captura de exce��es para o caso do �udio n�o estar dispon�vel ou seja interrompido.
		} catch (LineUnavailableException e) {e.printStackTrace();}
		  catch (InterruptedException ie) {ie.printStackTrace();}

	}

}