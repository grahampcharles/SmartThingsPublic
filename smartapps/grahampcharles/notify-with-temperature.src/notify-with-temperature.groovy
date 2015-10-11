/**
 *  Notify With Temperature
 *
 *  Copyright 2014 Graham Charles
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Notify With Temperature",
    namespace: "grahampcharles",
    author: "Graham Charles",
    description: "Sends a notification with the temperature",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Using temperature sensor:") {
		input "temperatureSensor1", "capability.temperatureMeasurement", required:true
	}
	
    section("When the mode changes to:") {
    	input "mode1", "mode", required:true, defaultValue:"Home"
    }

	section( "Notify me with the temperature:" ) {
        input "sendPushMessage", "enum", title: "Send a push notification?", options: ["Yes","No"], required:false, defaultValue:"Yes"
        input "phone1", "phone", title: "Send a Text Message?", required: false
    }

}

def installed() {
	subscribe(location, modeChangeHandler)
 //   setNight()
}

def updated() {
	unsubscribe()
	subscribe(location, modeChangeHandler)
    
}

def setNight() {
    setLocationMode("Night")
    log.debug "setNight function ; Current = ${location.mode}"
	runIn(15, setDay)
}
def setDay() {
    setLocationMode("Home")
	log.debug "setDay function ; Current = ${location.mode}"
	runIn(15, setNight)
}

def modeChangeHandler(evt) {
	log.debug "Inside modeChangeHandler. Current mode = ${location.mode}; event value=${evt.value}"
	// session = login(token) //Logs In and gets the token for session

	 if (evt.value == mode1)
        {
        def exTemp = temperatureSensor1.latestValue("temperature")
        
        // send message
		def message = "Hello! The temperature is ${exTemp}."
        
        log.debug message
		if (sendPushMessage) {
			sendPush(message)
		}
		if (phone1) {
			sendSms(phone1, message)
		}
    	
    }

	}
