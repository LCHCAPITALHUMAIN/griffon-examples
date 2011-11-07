
import org.apache.solr.client.solrj.impl.*
import griffon.plugins.solrj.SolrjBuilder

class AlaGormGriffonAddon {
    def server
    def events = [
        BootstrapEnd:{
            def configurationClass = app.class.classLoader.loadClass('Solr')            
            def serverConfig = new ConfigSlurper(Environment.current.name).parse(configurationClass)

            server = new CommonsHttpSolrServer(serverConfig.host) 
            server.setParser(new XMLResponseParser())
        },
        NewInstance: { klass, type, instance ->
            def types =  ['controller']
            if(!types.contains(type)) return
            def mc = app.artifactManager.findGriffonClass(klass).metaClass
            mc.withSolr = new SolrjBuilder(server)
        }
    ]
}
