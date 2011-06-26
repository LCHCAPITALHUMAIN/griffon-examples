package feeling

import groovy.beans.Bindable
import uk.co.caprica.vlcj.player.MediaPlayer

/**
 * The player model
 * 
 * @author mgg
 *
 */
class FeelingModel {

   @Bindable int counter
   @Bindable boolean playing   
   @Bindable int volume
   @Bindable boolean thereIsAnyAudioToPlay
   
   @Bindable 
   String timeMonitor= "00:00 / 00:00"   // TODO refactoring 00:00 / 00:00
   @Bindable 
   String artistName ="Welcome to Feeling --  A Griffon based tiny mp3 player"

   File fileToPlay
   MediaPlayer mediaPlayer

   /**
    * This method just manipulates the volume from the MediaPlayer instance. I couldn't build 
    * a bean expression in view like:
    * 
    * bean(
	*	model,
	*	mediaPlayer.volume:bind{volumeController.value}	
	*)
    * 
   	* @param volume
   	*/
   public void setVolume(int volume){
   		mediaPlayer.setVolume(volume)
   }
}
