package feeling

import static org.jaudiotagger.tag.FieldKey.*
import griffon.effects.Effects

import javax.swing.JFileChooser
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener

import org.jaudiotagger.tag.FieldKey
import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder

import uk.co.caprica.vlcj.player.MediaPlayer
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.MediaPlayerFactory
import uk.co.caprica.vlcj.player.VideoMetaData

/**
 * This controller handles all events from the player so far
 * 
 * @author mgg
 *
 */
class FeelingController {

	static final String TIME_ZERO = "00:00 / 00:00"
	static final String TIME_SEPARATOR = ":"
	static final String MESSAGE_NO_DATA = "No Track Information Available"
	def model
	def view
	def player

 /** 
  * At initialization a {@link MediaPlayer} is created. And all the player lifecycle 
  * is bind to model variables or view components. 
  * 
  * */
	void mvcGroupInit(Map args) {
		MediaPlayerFactory factory = new MediaPlayerFactory();
		MediaPlayer vlcMediaPlayer = factory.newMediaPlayer();
		vlcMediaPlayer.addMediaPlayerEventListener(
			new MediaPlayerEventAdapter() {
				void paused(MediaPlayer currentMediaPlayer){ model.playing = false }
				void playing(MediaPlayer currentMediaPlayer){ model.playing = true }
				void finished(MediaPlayer currentMediaPlayer) { 
					model.with{	
						mediaPlayer.stop()
						timeMonitor = TIME_ZERO
						counter = 0						
						playing = false						
					}					
				}				
				void stopped(MediaPlayer currentMediaPlayer){ finished(currentMediaPlayer)}				
				void error(MediaPlayer currentMediaPlayer) { model.counter = 0 }
				void timeChanged(MediaPlayer currentMediaPlayer, long newTime) {
					doOutside{
						currentMediaPlayer.with{
							def fmt =
								new PeriodFormatterBuilder().
									appendHours().appendSeparator(TIME_SEPARATOR).
									appendMinutes().appendSeparator(TIME_SEPARATOR).
									appendSeconds().
								toFormatter()
							def totalTime = length > 0 ? fmt.print(new Period(length)) : TIME_ZERO
							def currentTime = time > 0 ? fmt.print(new Period(time)) : TIME_ZERO
							def counterRes = (currentMediaPlayer.position * 100).intValue()
							def monitorRes = "$currentTime / $totalTime"
							execAsync{
								model.counter = counterRes
								model.timeMonitor = monitorRes
							}
						}
					}
				}
		});
    /* Avoiding the focus to be located in any particular component */
		view.container.requestFocus()
	 /* this listener should be removed and the behavior should be handled by a model-view binding */
		view.slider.addChangeListener(
			new ChangeListener(){
				public void stateChanged(ChangeEvent e) {
					def source = e.getSource();
					if (!source.valueIsAdjusting && source.isFocusOwner()) {
						execAsync{
							model.mediaPlayer.position = Integer.valueOf(source.value).floatValue() / 100
							view.container.requestFocus()
						}
					}
				}			
			}
		)		
	 /* Initilizing mediaPlayer value */
		model.mediaPlayer = vlcMediaPlayer
	 /* Launching the scroll effect on the welcome message */
		app.eventAsync('scroll')
	}	
	
	def exit = { evt = null ->
		model.mediaPlayer.release()
		app.shutdown()
	}

	def openFile = {
		def fc = new JFileChooser()
		def answer
		execAsync{
			answer = fc.showOpenDialog(view.container)						
		 /* If the user has chosen a file... */
			if (answer == JFileChooser.APPROVE_OPTION){
				model.fileToPlay = fc.selectedFile 		
				model.thereIsAnyAudioToPlay = true
				model.counter = 0
				model.playing = false
				doOutside{
				 /* The file is ready to be played */
					model.with{
						mediaPlayer.stop()
						mediaPlayer.prepareMedia(fileToPlay.absolutePath)						
						def tagData = player.getAudioMetaData(fileToPlay)						
						artistName = tagData ? [TITLE,ARTIST,ALBUM].collect{					
							tagData.getFirst(it)													
						}.join(' - ') : MESSAGE_NO_DATA
					}					
				}
			}
		}
	}
	
	def fwd = {
	 /* Skipping just a 10% forward */
		model.mediaPlayer.skip(0.1)
	}
	
	def stop = {
	 /* Stopping the player */
		model.mediaPlayer.stop()
		execAsync{
			model.timeMonitor = TIME_ZERO
		}
	}
	
	def rew = {
	 /* Skipping just a 10% backward */
		model.mediaPlayer.skip(-0.1)
	}
	
	def playOrPause = {	
	 /* Playing or pausing the current audio depending on whether the audio 
	  * is being played or not  */
		model.mediaPlayer.with{
			"${isPlaying() ? 'pause' : 'play'}"()				
		}
	}
	
 /* This event handler just move the label showing the title from it's initial position 
  * to a point out of the player on the right side */
	def onScroll = {evt->		
		Effects.move([x:400,duration:10000l],view.trackInfo,{a,b->					
			execAsync{
				a.setLocation(0,a.y)
			 /* This effect doesn't work well with mediaplayer events. It blinks all the time like
			  * something is disturbing the EDT. That's why it's commented until this issue will be
			  * fixed. Any help is welcome */
				//app.eventAsync('scroll')
			}			
		})			
	}
}
