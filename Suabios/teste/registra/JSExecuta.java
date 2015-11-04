package registra;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;

public class JSExecuta {

	//Criação de um objeto para armazenar o mixer.
	public static Mixer mixer;
	//Criação de um objeto para referenciar a classe Clip.
	public static Clip clip;
	
	//Método principal.
	public static void main (String []args){
		
		//Criação de um array para a obtenção de todos os mixers disponíveis para a reprodução.
		Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
		//Seleção do mixer de índice 0.
		mixer = AudioSystem.getMixer(mixerInfo[0]);
		
		//Os dados obtidos serão do tipo estabelecido pelo objeto "dataInfo".
		DataLine.Info dataInfo = new DataLine.Info(Clip.class, null);
		
		//Utilização do método try para tratar as exceções.
		try{
			//O clip irá receber as definições especificadas na variável dataInfo.
			clip = (Clip)mixer.getLine(dataInfo);
		//Caso o objeto possua um formato diferente a exeção será exibida ao usuário.
		}catch(LineUnavailableException lue){lue.printStackTrace();}
		
		
		//Utilização do método try para tratar as exceções.
		try{
			
			//Obtenção do arquivo, já gravado pela classe JSRegistro.
			File audioFile = new File("C:\\Users\\Larisse\\git\\tcc\\Suabios\\teste.wav");
			//A reprodução do áudio referenciará o arquivo citado anteriormente.
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			//Habilita todos os recursos que possibilitam a reprodução do arquivo.
			clip.open(audioStream);

		//Tratamento das possíveis exceções a serem obtidas.
		}catch(LineUnavailableException lue){lue.printStackTrace();}
		 catch(IOException ioe){ioe.printStackTrace();}
		 catch(UnsupportedAudioFileException uafe){uafe.printStackTrace();}
		
		//Início da reprodução, que automaticamente abrirá uma nova thread.
		clip.start();
		
		
		//Criação de uma estrutura de repetição para lidar com essa thread.
		do{
			
			//Método para tratar exceções.
			try{ 
				//Exigirá que a thread aguarde um tempo específico antes de reproduzir o áudio.
				Thread.sleep(50);
			//Tratamento de exceções.
			}catch(InterruptedException ie){ie.printStackTrace();}
		//Tudo isso será realizado enquanto o clip estiver ativo.
		}while(clip.isActive());
	}
}
