/**
 *  Scenes
 *
 *  Author: Ryan Nathanson
 */


// Automatically generated. Make future change here.
definition(
    name: "Scenes",
    namespace: "",
    author: "ryan.nathanson@gmail.com",
    description: "Create a scene for any given mode.  Preset the state of switches, dimmers & locks for any mode or tap the app so when you change to that mode or tap the app all switches, dimmers and locks will update to the state you programmed.",
    category: "Convenience",
    iconUrl: "http://images03.olx.com.ph/ui/18/68/16/1331222311_60006216_2-DESIGN-GRAPHICS-PHOTOGRAPHER-VIDEO-STAGE-EVENTS-LIVE-VIDEO-RECORDING-LIVE-FEED-Makati.jpg",
    iconX2Url: "http://images03.olx.com.ph/ui/18/68/16/1331222311_60006216_2-DESIGN-GRAPHICS-PHOTOGRAPHER-VIDEO-STAGE-EVENTS-LIVE-VIDEO-RECORDING-LIVE-FEED-Makati.jpg")

preferences {
		section("When I Change To This Mode") {
		input "newMode", "mode", title: "Mode?"
        
        }
	section("Lock these doors") {
		input "lock", "capability.lock", multiple: true, required: false
	}
section("Unlock these doors") {
			input "unlock", "capability.lock", multiple: true, required: false
	}
	section("Dim These Lights") {
	input "MultilevelSwitch", "capability.switchLevel", multiple: true, required: false
	}
    
    
    section("How Bright?"){
     input "number", "number", title: "Percentage, 0-99", required: false
    }

section("Turn On These Switches"){
input "switcheson", "capability.switch", multiple: true, required: false
}

section("Turn Off These Switches"){
input "switchesoff", "capability.switch", multiple: true, required: false
}
}


def installed() {
subscribe(location)
subscribe(app)

}

def updated() {
unsubscribe()
subscribe(location)
subscribe(app)

}


def uninstalled() {
unsubscribe()
}


def changedLocationMode(evt) {

    switcheson?.on()
    switchesoff?.off()
        settings.MultilevelSwitch?.setLevel(number)
        lock?.lock()
        unlock?.unlock()
		

	       

}

def appTouch(evt) {

    switcheson?.on()
    switchesoff?.off()
        settings.MultilevelSwitch?.setLevel(number)
        lock?.lock()
        unlock?.unlock()
		

	       

}