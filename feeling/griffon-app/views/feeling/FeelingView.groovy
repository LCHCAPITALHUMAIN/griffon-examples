package feeling

import javax.swing.JLabel
import static java.awt.BorderLayout.*
import static java.awt.Color.*
import java.awt.Font
import java.awt.Dimension
import javax.swing.JLabel

import org.jdesktop.swingx.JXStatusBar.Constraint;
import org.jdesktop.swingx.JXStatusBar.Constraint.ResizeBehavior;

/* --------------------------------------- */
/* ---------------- ACTIONS -------------- */
/* --------------------------------------- */

def openFileAction = action(name:'Open File...',closure:{controller.openFile()})
def exitAction = action(name:'Exit',closure:{controller.exit()})
def iconRoot = "/com/everaldo/crystal/32x32/actions"

container = application(title: "feeling",
  size: [400,200],
  locationByPlatform:true,
  iconImage: imageIcon('/griffon-icon-48x48.png').image,
  iconImages: [imageIcon('/griffon-icon-48x48.png').image,
               imageIcon('/griffon-icon-32x32.png').image,
               imageIcon('/griffon-icon-16x16.png').image]) {		   
		   borderLayout()
		   
		 /* --------------------------------------- */
		 /* -------------- MENU BAR --------------- */
		 /* --------------------------------------- */
			menuBar{
				menu("Application"){
					menuItem(openFileAction)
					separator()
					menuItem(exitAction)
				}
				menu("Help"){
					menuItem('About')
				}
			}
			panel(constraints:CENTER){
		 /* --------------------------------------- */
		 /* --------------- VIEW ------------------- */
		 /* --------------------------------------- */
				gridLayout(cols:1,rows:2)
				panel(border:emptyBorder([4,4,0,4])){
					borderLayout()
					slider(
						id:'slider',
						minorTickSpacing:2,
						paintTicks:true,
						constraints:NORTH,
						value:bind{model.counter},
						enabled:bind{model.thereIsAnyAudioToPlay}
					)
					label(
						id:'trackInfo',
						text:bind{model.artistName},
						constraints:CENTER,
						border:emptyBorder(2)	
					)		
				}			
				panel(border:emptyBorder([0,4,4,4])){
					gridLayout(cols:5,rows:1)
					["stop","rew","pause/play","fwd"].each{
						def methodName = it.toString()
						println methodName
						button(
							id:methodName,								
							icon: methodName == 'pause/play' ? 
								bind{ model.playing ? imageIcon("$iconRoot/player_pause.png"): imageIcon("$iconRoot/player_play.png")} : 
								imageIcon("${iconRoot}/player_${it}.png"),
							actionPerformed: methodName == 'pause/play' ? 
								{controller.playOrPause()} : 
								{controller."${methodName}"()},
							contentAreaFilled:false,
							border:emptyBorder(0),
							focusable:false,
							enabled:bind{model.thereIsAnyAudioToPlay}
						)	
					}		
				}
		   }
			statusBar(
				constraints:SOUTH,
				preferredSize:[0,20] as Dimension){
				label(
					id:'timeProgress',
					text:bind{model.timeMonitor},
					constraints:new Constraint(ResizeBehavior.FILL)
				)
				slider(
					id:'volumeController',
					focusable:false,
					preferredSize:[80,20] as Dimension
				)
				label(
					id:'volumeProgress',
					preferredSize:[40,0] as Dimension,
					text:bind{"${volumeController.value}%"}
				)
				bean(
					model,
					volume:bind{volumeController.value}	
				)
			}
}
