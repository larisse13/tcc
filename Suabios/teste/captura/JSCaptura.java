package captura;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class JSCaptura{
	
	
	//Método principal da classe.
	public static void main(String []args){
		
		//Especificações do formato de áudio a ser lido.
		AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
		
		//Criação do método try para tratar as possíveis exceções.
		try {
			
			//Definição de um objeto responsável por armazenar as especificações do áudio.
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			//Obtenção das mesmas informações através da classe SourceDataLine.
			final SourceDataLine sourceLine = (SourceDataLine)AudioSystem.getLine(info);
			//Habilita os recursos necessários para reprodução do áudio.
			sourceLine.open();

			//Definição de uma classe TargetDataLine com as mesmas especificações mencionadas anteriormente.
			info = new DataLine.Info(TargetDataLine.class, format);
			//Obtenção das especificações de áudio através da classe TargetDataLine dessa vez.
			final TargetDataLine targetLine = (TargetDataLine)AudioSystem.getLine(info);
			//Habilita os recursos necessários para a captura do áudio.
			targetLine.open();

			//Objeto criado para armazenar a entrada de áudio e reproduzí-lo, sem a necessidade da criação de um arquivo.
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			//Criação de uma thread para reprodução do áudio.
			Thread sourceThread = new Thread(){
				//Método público e anônimo de inicialização da mesma.
				@Override public void run(){
					//Inicialização da reprodução.
					sourceLine.start();
					//Enquanto o áudio estiver em reprodução o array será verificado e reproduzido até o seu tamanho.
					while(true){
						sourceLine.write(out.toByteArray(), 0, out.size());
					}
				}
			};
			
			//Crianção de uma thread para a gravação do áudio.
			Thread targetThread = new Thread(){
				//Método público e anônimo de inicialização da mesma.
				@Override public void run(){
					//Inicialização da gravação.
					targetLine.start();
					//Criação de um byte array para coletar os dados provenientes da gravação, diminuindo o seu tamanho para otimizar a reprodução.
					byte[] data = new byte[targetLine.getBufferSize()/5];
					int readBytes;
					
					while(true){
						//Objeto para retornar o tamanho do buffer.
						readBytes = targetLine.read(data, 0, data.length);
						//Escrita de dados no objeto de responsável pela reprodução do áudio, até o tamanho verificado anteriormente.
						out.write(data, 0, readBytes);
					}
				}
			};
		
			//Início da thread responsável pela gravação do áudio.
			targetThread.start();
			//Exibição de mensagens para orientar o usuário quanto ao prosseguimento do programa.
			System.out.println(">>>>> INICIALIZANDO TESTE DE ÁUDIO <<<<<");
			System.out.println("--- INICIALIZANDO GRAVAÇÃO ---");
			//Thread principal irá "dormir" por 5 segundos, tempo em que ocorrerá a captura do áudio.
			Thread.sleep(5000);
			//Finalização da captura.
			targetLine.stop();
			//Fechamento dos recursos que habilitam a captura de áudio.
			targetLine.close();
			System.out.println("--- GRAVAÇÃO FINALIZADA ---");
			
			System.out.println("--- REPRODUÇÃO DO ÁUDIO CAPTURADO ---");
			
			//Início da thread de reprodução do áudio.
			sourceThread.start();
			//Thread principal irá "dormir" por 5 segundos, tempo em que ocorrerá a reprodução do áudio.
			Thread.sleep(5000);
			//Finalização da reprodução.
			sourceLine.stop();
			//Fechamento dos recursos que habilitam a reprodução de áudio.
			sourceLine.close();
			
			System.out.println("--- REPRODUÇÃO FINALIZADA ---");
			
		//Captura de exceções para o caso do áudio não estar disponível ou seja interrompido.
		} catch (LineUnavailableException e) {e.printStackTrace();}
		  catch (InterruptedException ie) {ie.printStackTrace();}

	}

}