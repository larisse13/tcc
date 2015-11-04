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

	//Cria��o de um objeto para armazenar o mixer.
	public static Mixer mixer;
	//Cria��o de um objeto para referenciar a classe Clip.
	public static Clip clip;
	
	//M�todo principal.
	public static void main (String []args){
		
		//Cria��o de um array para a obten��o de todos os mixers dispon�veis para a reprodu��o.
		Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
		//Sele��o do mixer de �ndice 0.
		mixer = AudioSystem.getMixer(mixerInfo[0]);
		
		//Os dados obtidos ser�o do tipo estabelecido pelo objeto "dataInfo".
		DataLine.Info dataInfo = new DataLine.Info(Clip.class, null);
		
		//Utiliza��o do m�todo try para tratar as exce��es.
		try{
			//O clip ir� receber as defini��es especificadas na vari�vel dataInfo.
			clip = (Clip)mixer.getLine(dataInfo);
		//Caso o objeto possua um formato diferente a exe��o ser� exibida ao usu�rio.
		}catch(LineUnavailableException lue){lue.printStackTrace();}
		
		
		//Utiliza��o do m�todo try para tratar as exce��es.
		try{
			
			//Obten��o do arquivo, j� gravado pela classe JSRegistro.
			File audioFile = new File("C:\\Users\\Larisse\\git\\tcc\\Suabios\\teste.wav");
			//A reprodu��o do �udio referenciar� o arquivo citado anteriormente.
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			//Habilita todos os recursos que possibilitam a reprodu��o do arquivo.
			clip.open(audioStream);

		//Tratamento das poss�veis exce��es a serem obtidas.
		}catch(LineUnavailableException lue){lue.printStackTrace();}
		 catch(IOException ioe){ioe.printStackTrace();}
		 catch(UnsupportedAudioFileException uafe){uafe.printStackTrace();}
		
		//In�cio da reprodu��o, que automaticamente abrir� uma nova thread.
		clip.start();
		
		
		//Cria��o de uma estrutura de repeti��o para lidar com essa thread.
		do{
			
			//M�todo para tratar exce��es.
			try{ 
				//Exigir� que a thread aguarde um tempo espec�fico antes de reproduzir o �udio.
				Thread.sleep(50);
			//Tratamento de exce��es.
			}catch(InterruptedException ie){ie.printStackTrace();}
		//Tudo isso ser� realizado enquanto o clip estiver ativo.
		}while(clip.isActive());
	}
}
