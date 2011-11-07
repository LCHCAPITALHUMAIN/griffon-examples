

def eventClosure1 = binding.variables.containsKey('eventSetClasspath') ? eventSetClasspath : {cl->}
eventSetClasspath = { cl ->
    eventClosure1(cl)
    if(compilingPlugin('solrj')) return
    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-solrj-plugin', dirs: "${solrjPluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('solrj', [
        conf: 'compile',
        name: 'griffon-solrj-addon',
        group: 'org.codehaus.griffon.plugins',
        version: solrjPluginVersion
    ])
}
