new File("${basedir}/griffon-app/conf/Solr.groovy").append("""
environments{
    development{host = 'http://localhost:8983/solr'}
    test{host = 'http://localhost:8983/solr'}
    production{host = 'http://localhost:8983/solr'}
}
""")
// check to see if we already have a AlaGormGriffonAddon
boolean addonIsSet1
builderConfig = configSlurper.parse(builderConfigFile.text)
builderConfig.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'AlaGormGriffonAddon' == builder
    }
}

if (!addonIsSet1) {
    println 'Adding AlaGormGriffonAddon to Builder.groovy'
    builderConfigFile.append('''
root.'AlaGormGriffonAddon'.addon=true
''')
}