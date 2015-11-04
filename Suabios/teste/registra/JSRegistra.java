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
		
		//Exibição de mensagem para orientar o usuário acerca da gravação.
		System.out.println("--- INICIALIZANDO TESTE DE ÁUDIO ---");
		
		//Inicialização do programa através de um try para informar ao usuário caso seja verificado uma exceção.
		try{
			
			//O primeiro passo é especificar uma variável para receber as especificações
			//do formato do áudio a ser lido.
			AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

			//Criação de um objeto para armazenar as informações do áudio a ser lido.
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			//Caso o áudio seja diferente das especificações de formato, será exibida
			//uma mensagem para alertar o usuário.
			if(!AudioSystem.isLineSupported(info)){
				System.err.println(">>> FORMATO NÃO SUPORTADO! <<<");
			}
				
			//Recebe todas as especificações de áudio no objeto "targetLine".
			final TargetDataLine targetLine = (TargetDataLine)AudioSystem.getLine(info);
			//Habilita todos os recursos para inicializar a gravação, embora não começe ainda a gravar.
			targetLine.open();
			
			//Exibição de mensagem para o usuário se preparar para a gravação.
			System.out.println("--- INICIALIZANDO GRAVAÇÃO ---");
			//Início, efetivo, da gravação.
			targetLine.start();
			
			//Criação de uma Thread para que seja possível a criação de uma nova linha de execução do programa.
			Thread thread = new Thread(){
				
				//Método anônimo para início da Thread.
				@Override public void run(){
					
					//Construção de um AudioInputStream que siga as especificações da targetLine.
					AudioInputStream audioStream = new AudioInputStream(targetLine);
					//Criação de um arquivo onde ficará armazenado o áudio gravado.
					File audioFile = new File("teste.wav");
					
					//Método try para certificar-se de que tudo que for capturado pelo microfone será
					//escrito em um arquivo do tipo WAVE, se isso não acontecer, a exceção será impressa
					//para o usuário.
					try {
						AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
					//Mensagem ao usuário informando a finalização da gravação.
					System.out.println("--- GRAVAÇÃO FINALIZADA ---");
					
				}
				
			};
				//Método de chamada da thread alternativa.
				thread.start();
				//Método que coloca a Thread principal para esperar durante 15 segundos, que será o tempo
				//de duração do áudio a ser gravado.
				Thread.sleep(15000);
				//Finalização da gravação.
				targetLine.stop();
				//Fechamento de todos os recursos que habilitam a captura de áudio.
				targetLine.close();
				
		}
		//Captura de exceções para o caso do áudio não estar disponível ou seja interrompido.
		catch(LineUnavailableException lue){ lue.printStackTrace();}
		catch(InterruptedException ie){ ie.printStackTrace();}
		
		
	}
	
}