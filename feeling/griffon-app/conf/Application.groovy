application {
    title = 'Feeling'
    startupGroups = ['feeling']

    // Should Griffon exit when no Griffon created frames are showing?
    autoShutdown = true

    // If you want some non-standard application class, apply it here
    //frameClass = 'javax.swing.JFrame'
}
mvcGroups {
    // MVC Group for "feeling"
    'feeling' {
		view       = 'feeling.FeelingView'
        model      = 'feeling.FeelingModel'        
        controller = 'feeling.FeelingController'
    }

}
