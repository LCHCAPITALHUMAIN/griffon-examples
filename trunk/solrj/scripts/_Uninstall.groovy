new File("${basedir}/griffon-app/conf/Solr.groovy").delete()
// check to see if we already have a AlaGormGriffonAddon
boolean addonIsSet1
builderConfig = configSlurper.parse(builderConfigFile.text)
builderConfig.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'AlaGormGriffonAddon' == builder
    }
}

if (addonIsSet1) {
    println 'Removing AlaGormGriffonAddon from Builder.groovy'
    builderConfigFile.text = builderConfigFile.text - "root.'AlaGormGriffonAddon'.addon=true\n"
}
