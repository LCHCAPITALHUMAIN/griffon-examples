package feeling

import org.jaudiotagger.audio.AudioFile
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.Tag

/**
 * This service just brings the meta data from the file
 * 
 * @author mgg
 *
 */
class PlayerService {	
	
	/**
	 * Returns the metadata from the file with information such as
	 * title,album,artist...
	 * 
	 * @param file
	 * @return
	 */
	def getAudioMetaData(file){
		AudioFile f = AudioFileIO.read(file)
		Tag tag = f.getTag()
	}
	
}