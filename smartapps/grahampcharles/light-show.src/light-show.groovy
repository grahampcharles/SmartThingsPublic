/**
 *  Light Show
 *
 *  Author: Graham Charles
 */

definition(
    name: "Light Show",
    namespace: "grahampcharles",
    author: "Graham Charles",
    description: "Creates a five-minute gentle light show.",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet@2x.png"
)

preferences {
	section("Switches and Dimmers") {
		input "switches", "capability.switch", multiple: true
	}
}

tiles {
	standardTile("Switch", "state.runThisApp", decoration:"flat") {
    	state "off", label:'${name}'
    }
    main "Switch"
    details(["Switch"])
}

def installed()
{
	state.runThisApp = false
    state.isRunning = false
	subscribe(location, changedLocationMode)
	subscribe(app, appTouch)
}

def updated()
{
	unsubscribe()
	subscribe(location, changedLocationMode)
	subscribe(app, appTouch)
}

def changedLocationMode(evt) {
	log.debug "changedLocationMode: $evt"
	// switches?.on()
}

def appTouch(evt) {
	log.debug "appTouch: $evt"
    def now = new Date()
    
    if(state.runThisApp) {
    	state.runThisApp = false
    } else {
    	state.runThisApp = true
        state.appStarted = now.getTime()
    }
        
    log.debug "Toggled run state to: $state.runThisApp"
    
    if (state.runThisApp) {
    	startTimer(15, randomEvent)
    }
    
}

def startTimer(seconds, function) {
    def now = new Date()
	def runTime = new Date(now.getTime() + (seconds * 1000))
	runOnce(runTime, function) // runIn isn't reliable, use runOnce instead
}


def randomEvent() {
	log.debug "random event firing"

	def r = new Random()
    def now = new Date()
    
    def different = now.getTime() - state.appStarted;
    
    if (different > 300 * 1000) {
    	log.debug "app timing out"
        state.runThisApp = false
    }
    
    
    if (state.runThisApp && !state.isRunning) {
    	
        state.isRunning = true
        def rand = Math.abs(r.nextInt() % 5) 
        def caps
        def hasLevel

        // pick a random element
        def switchnum = r.nextInt(switches.size())
        log.debug "switchnum ${switchnum}"

        //for (theSwitch in switches) {
        def theSwitch = switches[switchnum]

        log.debug "switch ${theSwitch}"

        caps = theSwitch.supportedAttributes
        hasLevel = false

        for ( i in caps ) {
            if ( i as String == "level" ) {
                hasLevel = true
            }
        }


        log.debug "->hasLevel ${hasLevel}"


        if (hasLevel) { 
            // dimmer switch
            log.debug "-->dimmer switch"
            if (rand <= 0) {
                log.debug "---->switches off"
                theSwitch.setLevel(0)
            } else {
                def newLevel = rand * 25
                log.debug "---->switch level ${newLevel}"
                theSwitch.setLevel(newLevel)
            }
        } else {
            log.debug "-->regular switch"
            if (rand < 3) { 
                log.debug "---->switches off"
                theSwitch.off()
            } else {
                log.debug "---->switches on"
                theSwitch.on()
            }
        }
        state.isRunning = false
    }
    // }
    
    if (state.runThisApp) {
    	startTimer(15, randomEvent)
    }
    
}
