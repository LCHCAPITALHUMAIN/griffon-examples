root {
    'groovy.swing.SwingBuilder' {
        controller = ['Threading']
        view = '*'
    }
    'griffon.app.ApplicationBuilder' {
        view = '*'
    }
}
root.'MiglayoutGriffonAddon'.addon=true

root.'LookandfeelGriffonAddon'.addon=true

root.'LookandfeelJtattooGriffonAddon'.addon=true

root.'CrystaliconsGriffonAddon'.addon=true

root.'EffectsGriffonAddon'.addon=true

jx {
    'groovy.swing.SwingXBuilder' {
        view = '*'
    }
}
